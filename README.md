# Robin · Life in the UK

> 用你最熟悉的语言读懂英国，再用英语自信地通过 Life in the UK 考试。

**Robin**（工作名）是一个帮助移民备考英国 **Life in the UK** 考试的 iOS 和 Android App。它的特别之处：

- 🌐 **母语对照 + 语言渐变线**：每句话都能在英语和母语之间切换，并帮你逐步过渡到只看英语。
- 🗺️ **学习之旅**：把官方手册变成一张火车线路图，5 条线路、24 个车站，每站几分钟的双语故事卡，集齐护照印章。
- ☕ **刷题之旅**：每天一杯茶的时间做 5 道题，间隔重复把快忘的知识点拉回来，吹散地图上的迷雾。
- 🎫 **模拟考试之旅**：和真考一样的 24 题、45 分钟、18 题及格，全英文考场模式，考前心里有底。

> 非官方 App。考试以 [GOV.UK](https://www.gov.uk/life-in-the-uk-test) 和官方手册为准。

## 从哪里开始看

- **产品和设计**：[docs/README.md](docs/README.md)
- **给 Claude Design 的提示词**：[docs/design/screen-prompts.md](docs/design/screen-prompts.md)
- **技术架构**：[docs/tech/architecture.md](docs/tech/architecture.md)

## 仓库结构

```
content/   题库和知识点（JSON，唯一的内容来源）+ Schema + 两端共用的测试用例
tools/     内容校验和打包脚本
docs/      产品、设计、内容、技术文档
ios/       SwiftUI App + RobinCore（Swift Package）
android/   Compose App + core（纯 Kotlin 模块）
```

## 快速开始

```bash
python3 tools/validate.py         # 校验内容
python3 tools/build_content.py    # 打包成 build/content.json

cd android && ./gradlew :core:test :app:assembleDebug        # Android
cd ios && xcodegen && open Robin.xcodeproj                    # iOS（macOS）
```

## 状态

🚧 骨架阶段：产品文档齐全；内容有 24 个车站的结构和少量样例知识点、题目；两端 App 能走通“学习 → 练习 → 模考”的最小流程，视觉设计待定。
