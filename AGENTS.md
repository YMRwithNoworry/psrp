# Codex 行为指令

## 自动推送到远程仓库

每次对话结束时，Codex 应当自动执行以下操作：

1. **提交变更**：运行 `git add -A` 暂存所有改动，然后用 `git commit -m "chore: update <简短描述>"` 提交。
2. **检查远程仓库**：运行 `git remote -v` 检查是否存在 `origin`。
3. **若无远程**：使用 `gh repo create psrp --private --source . --remote origin` 创建 GitHub 远程仓库并关联。
4. **推送代码**：运行 `git push -u origin main` 推送到远程。
