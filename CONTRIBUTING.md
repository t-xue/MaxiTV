# 贡献指南

感谢您对 MaxiTV 项目的关注！我们欢迎任何形式的贡献。

## 如何贡献

### 报告 Bug

1. 确保 Bug 尚未在 [Issues](https://github.com/t-xue/MaxiTV/issues) 中报告
2. 创建一个新的 Issue，包含：
   - 清晰的标题和描述
   - 复现步骤
   - 预期行为和实际行为
   - 设备信息和 Android 版本
   - 截图或日志（如适用）

### 功能建议

1. 在 Issues 中创建新的 Feature Request
2. 详细描述功能需求和使用场景
3. 等待社区讨论和维护者确认

### 提交代码

1. **Fork 仓库**
   ```bash
   # 点击 GitHub 页面右上角的 Fork 按钮
   ```

2. **克隆你的 Fork**
   ```bash
   git clone https://github.com/YOUR_USERNAME/MaxiTV.git
   cd MaxiTV
   ```

3. **创建功能分支**
   ```bash
   git checkout -b feature/your-feature-name
   ```

4. **进行修改**
   - 遵循项目的代码风格
   - 添加必要的注释
   - 确保代码编译通过

5. **提交更改**
   ```bash
   git add .
   git commit -m "feat: add your feature description"
   ```

6. **推送到 GitHub**
   ```bash
   git push origin feature/your-feature-name
   ```

7. **创建 Pull Request**
   - 在 GitHub 页面点击 "New Pull Request"
   - 填写 PR 描述，说明修改内容
   - 等待代码审查

## 代码规范

### Kotlin 代码风格

- 使用 4 空格缩进
- 遵循 [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- 使用有意义的变量和函数命名
- 添加 KDoc 注释说明公共 API

### Commit 消息格式

使用 [Conventional Commits](https://www.conventionalcommits.org/) 格式：

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

**类型 (type)：**
- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档更新
- `style`: 代码格式调整（不影响功能）
- `refactor`: 重构代码
- `perf`: 性能优化
- `test`: 测试相关
- `chore`: 构建/工具相关

**示例：**
```
feat(player): add PiP mode support
fix(epg): correct timezone parsing issue
docs(readme): update installation instructions
```

## 开发环境设置

### 前置要求

- Android Studio Hedgehog (2023.1.1) 或更新版本
- JDK 17
- Android SDK 35
- Kotlin 2.0+

### 构建项目

```bash
# 克隆仓库
git clone https://github.com/t-xue/MaxiTV.git
cd MaxiTV

# 构建 Debug APK
./gradlew assembleDebug

# 运行测试
./gradlew test

# 构建 Release APK
./gradlew assembleRelease
```

### 项目结构

```
app/src/main/java/com/iptv/player/
├── di/          # 依赖注入模块
├── data/        # 数据层（数据库、网络、解析器）
├── domain/      # 领域模型
├── ui/          # UI 层（Compose 页面）
└── worker/      # 后台任务
```

## Pull Request 检查清单

提交 PR 前，请确保：

- [ ] 代码编译通过，无错误
- [ ] 已添加/更新相关测试
- [ ] 已更新文档（如需要）
- [ ] Commit 消息符合规范
- [ ] 代码遵循项目风格
- [ ] 已解决所有冲突

## 行为准则

- 尊重所有参与者
- 接受建设性批评
- 专注于对社区最有利的事情
- 对他人表示同理心

## 联系方式

如有任何问题，请通过 Issue 或 Discussion 与我们联系。

感谢您的贡献！🎉
