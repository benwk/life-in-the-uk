# 技术架构

## 总览

```
                content/  （唯一的内容来源：JSON）
                   │
                   │  tools/validate.py       校验结构、引用、答案、翻译覆盖率
                   │  tools/build_content.py  打包
                   ▼
            content.json（内容包，App 构建时自动生成）
             │                               │
             ▼                               ▼
   ios/  SwiftUI App                 android/  Compose App
   └─ RobinCore（Swift Package）      └─ :core（纯 Kotlin 模块）
        模型、判分、组卷、茶歇、陷阱雷达      同样的逻辑，同样的 API
             ▲                               ▲
             └──── content/fixtures/core-cases.json ────┘
                   两端共用的测试用例，保证判分行为一致
```

## 设计决策

| 决策 | 选择 | 理由 |
|---|---|---|
| 跨平台方式 | **两端纯原生**（SwiftUI + Jetpack Compose） | 语言排版（RTL、CJK、乌尔都语）、TTS、无障碍都依赖系统能力，原生体验最好 |
| 代码组织 | 单仓库（monorepo） | 内容只维护一份；改题目、改规则时两端同步 |
| 共享逻辑 | 两端各写一份 core，**用同一组测试用例约束** | 逻辑很小（几百行），比引入 Kotlin Multiplatform 简单；以后逻辑变多再考虑 KMP |
| 内容格式 | JSON + JSON Schema | 人能直接编辑和 review；Schema 可以用 quicktype 生成 Swift / Kotlin 模型 |
| 内容进 App | 构建时由脚本生成 `content.json` 打进包里 | 离线可用；生成物不进 git，避免两端内容不同步 |
| 本地存储 | 骨架阶段用 UserDefaults / SharedPreferences | 加入 FSRS 复习记录后换成 SwiftData / Room |
| 账号和后端 | MVP 不做 | 全部本地，隐私友好，零运维 |

## 目录说明

| 路径 | 内容 |
|---|---|
| `content/meta.json` | 内容版本、支持的语言、考试规则（24 题 / 45 分钟 / 18 题及格） |
| `content/journey/lines.json` | 5 条线路、24 个车站 |
| `content/facts/*.json` | 知识点，按线路分文件 |
| `content/questions/*.json` | 题目，按线路分文件 |
| `content/glossary/terms.json` | 词汇表 |
| `content/schema/` | JSON Schema |
| `content/fixtures/core-cases.json` | 两端共用的测试用例 |
| `tools/` | 校验和打包脚本（Python 标准库；装了 `jsonschema` 会额外做 Schema 校验） |
| `ios/project.yml` | XcodeGen 配置（`.xcodeproj` 由它生成，不提交） |
| `ios/RobinCore/` | Swift Package：模型和逻辑，带单元测试 |
| `ios/Robin/` | SwiftUI App |
| `android/core/` | 纯 Kotlin 模块：模型和逻辑，带单元测试 |
| `android/app/` | Compose App |

## 本地开发

### 内容

```bash
pip install -r tools/requirements.txt   # 可选，启用 JSON Schema 校验
python3 tools/validate.py
python3 tools/build_content.py          # 生成 build/content.json
```

### iOS（需要 macOS + Xcode 16）

```bash
brew install xcodegen
cd ios && xcodegen && open Robin.xcodeproj
# 逻辑层测试
python3 tools/build_content.py && (cd ios/RobinCore && swift test)
```

Xcode 构建时会自动运行 `tools/build_content.py`，把内容包放进 App（见 `project.yml` 里的 *Bundle content* 脚本）。

### Android（需要 Android Studio 或 Android SDK + JDK 17）

```bash
cd android
./gradlew :core:test          # 逻辑层测试（会自动生成内容包）
./gradlew :app:assembleDebug  # 构建 APK
```

Gradle 构建时会自动运行 `tools/build_content.py`，把内容包放进 `assets/`（见 `app/build.gradle.kts` 里的 `bundleContent` 任务）。

## CI

| 工作流 | 触发路径 | 做什么 |
|---|---|---|
| `content.yml` | `content/`、`tools/` | 校验并打包内容 |
| `android.yml` | `android/`、`content/`、`tools/` | core 单元测试 + 构建 debug APK |
| `ios.yml` | `ios/`、`content/`、`tools/` | RobinCore 单元测试 + 生成工程 + 为模拟器构建 |

按路径触发，只改 Android 不会跑 macOS 任务（macOS 的 CI 分钟数更贵）。

## 下一步的技术工作

1. **FSRS 间隔重复**：按知识点记录每次作答，替换 `TeaBreak` 的简单规则。
2. **持久化**：SwiftData / Room，存储复习记录、错题、印章日期。
3. **TTS 朗读**：AVSpeechSynthesizer / Android TextToSpeech。
4. **长按查词**：把英文文本按词切分，匹配 `glossary` 里的术语。
5. **界面文字本地化**：iOS 用 String Catalog（`.xcstrings`），Android 用 `values-*/strings.xml`。
6. **RTL**：加入乌尔都语、阿拉伯语后，用 UI 测试覆盖镜像布局。
7. **内容生成管线**：`tools/generate/`，用大模型从知识点生成题目和翻译初稿，人工审校后入库（见 [内容规范](../content/content-guidelines.md)）。
