$ErrorActionPreference = "Stop"

function Write-HookJson {
    param(
        [string]$Message = $null
    )

    $payload = @{
        continue = $true
    }

    if ($Message) {
        $payload.systemMessage = $Message
    }

    $payload | ConvertTo-Json -Compress
}

function Add-LogLine {
    param(
        [string]$Path,
        [string]$Message
    )

    if ($Path) {
        $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
        Add-Content -LiteralPath $Path -Value "[$timestamp] $Message"
    }
}

function Invoke-Git {
    param(
        [string[]]$Arguments
    )

    $output = & git @Arguments 2>&1
    $exitCode = $LASTEXITCODE
    return @{
        ExitCode = $exitCode
        Output = ($output -join [Environment]::NewLine)
    }
}

$stdin = [Console]::In.ReadToEnd()
$hookInput = $null
if ($stdin.Trim()) {
    try {
        $hookInput = $stdin | ConvertFrom-Json
    }
    catch {
        $hookInput = $null
    }
}

$startDirectory = if ($hookInput -and $hookInput.cwd) { [string]$hookInput.cwd } else { (Get-Location).Path }
$repoRoot = $null
$logPath = $null

try {
    Set-Location -LiteralPath $startDirectory

    $rootResult = Invoke-Git -Arguments @("rev-parse", "--show-toplevel")
    if ($rootResult.ExitCode -eq 0 -and $rootResult.Output.Trim()) {
        $repoRoot = $rootResult.Output.Trim()
    }
    else {
        $repoRoot = $startDirectory
        & git init | Out-Null
        if ($LASTEXITCODE -ne 0) {
            throw "git init failed in $repoRoot"
        }
        & git branch -M main | Out-Null
    }

    Set-Location -LiteralPath $repoRoot
    $gitDir = (& git rev-parse --git-dir 2>$null).Trim()
    if (-not $gitDir) {
        $gitDir = Join-Path $repoRoot ".git"
    }
    if (-not [System.IO.Path]::IsPathRooted($gitDir)) {
        $gitDir = Join-Path $repoRoot $gitDir
    }
    $logPath = Join-Path $gitDir "codex-auto-push.log"
    Add-LogLine -Path $logPath -Message "Stop hook started in $repoRoot"

    $userName = (& git config user.name 2>$null).Trim()
    if (-not $userName) {
        & git config user.name "Codex Auto Push" | Out-Null
    }
    $userEmail = (& git config user.email 2>$null).Trim()
    if (-not $userEmail) {
        & git config user.email "codex-auto-push@users.noreply.github.com" | Out-Null
    }

    $remoteResult = Invoke-Git -Arguments @("remote", "get-url", "origin")
    if ($remoteResult.ExitCode -ne 0 -or -not $remoteResult.Output.Trim()) {
        $ghCommand = Get-Command gh -ErrorAction SilentlyContinue
        if (-not $ghCommand) {
            throw "No git remote named origin exists, and GitHub CLI (gh) is not installed."
        }

        $repoName = Split-Path -Leaf $repoRoot
        Add-LogLine -Path $logPath -Message "No origin remote found. Creating GitHub repository $repoName."
        & gh repo create $repoName --private --source $repoRoot --remote origin 2>&1 | ForEach-Object {
            Add-LogLine -Path $logPath -Message $_
        }
        if ($LASTEXITCODE -ne 0) {
            throw "gh repo create failed for $repoName. Run 'gh auth status' for details."
        }
    }

    & git add -A 2>&1 | ForEach-Object {
        Add-LogLine -Path $logPath -Message $_
    }
    if ($LASTEXITCODE -ne 0) {
        throw "git add failed."
    }

    $porcelain = (& git status --porcelain)
    if (-not $porcelain) {
        Add-LogLine -Path $logPath -Message "No changes to commit."
    }
    else {
        $commitMessage = "chore: codex auto-push $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')"
        & git commit -m $commitMessage 2>&1 | ForEach-Object {
            Add-LogLine -Path $logPath -Message $_
        }
        if ($LASTEXITCODE -ne 0) {
            throw "git commit failed."
        }
    }

    $branch = (& git branch --show-current 2>$null).Trim()
    if (-not $branch) {
        $branch = "main"
        & git branch -M $branch | Out-Null
    }

    Add-LogLine -Path $logPath -Message "Pushing branch $branch to origin."
    & git push -u origin $branch 2>&1 | ForEach-Object {
        Add-LogLine -Path $logPath -Message $_
    }
    if ($LASTEXITCODE -ne 0) {
        throw "git push failed."
    }

    Add-LogLine -Path $logPath -Message "Stop hook completed successfully."
    Write-HookJson
}
catch {
    if (-not $logPath) {
        try {
            $fallbackGitDir = Join-Path $startDirectory ".git"
            if (Test-Path -LiteralPath $fallbackGitDir) {
                $logPath = Join-Path $fallbackGitDir "codex-auto-push.log"
            }
        }
        catch {
            $logPath = $null
        }
    }

    Add-LogLine -Path $logPath -Message "ERROR: $($_.Exception.Message)"
    Write-HookJson -Message "Codex auto-push hook failed. Check .git/codex-auto-push.log for details."
    exit 0
}
