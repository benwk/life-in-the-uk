# Corro

> **Uptime, corroborated.** 一圈节点一起盯着你的服务：多方确认故障，叫醒对的人，留下谁都改不了的记录。

**Corro** 是一个 uptime 监控、值班告警和状态页产品。它只做这三件事，不做日志、链路追踪这类可观测性功能。它的特别之处：

- 🟢 **社区节点网络**：任何人都可以用一行命令运行一个节点加入网络，帮别人监控，同时给自己挣额度。监控不再依赖某一家公司的几个机房。
- 🤝 **先确认，再叫人**：一个节点看到故障不算数。不同地区、不同运营者的多个节点都确认之后，才算真正宕机，才会叫醒值班的人。
- 🔔 **值班和告警**：排班、升级策略，以及电话、短信、App、Slack 和 Teams 通知，可以替代 Opsgenie 或 PagerDuty。
- 📄 **可验证的状态页**：每一天的可用率、每一次故障都有可以独立验证的凭证（Receipt）。客户不用相信你，也不用相信我们。
- 🚫 **不发币**：额度不能转让，也不能提现。这里用的是透明日志技术，不是加密货币。

## 这个目录是什么

这是 Corro 的**品牌和产品设计交接文档**，可以直接交给 Claude Design 或设计师使用。它暂时放在 `life-in-the-uk` 仓库里，以后会整体搬到 Corro 自己的仓库。整个 `corro/` 目录是自包含的，内部只用相对链接，可以原样搬走。

## 从哪里开始看

| 想做什么 | 看这里 |
|---|---|
| 了解整个产品 | [docs/README.md](docs/README.md) |
| 了解品牌：名字、语气、Logo 方向 | [docs/brand/brand-book.md](docs/brand/brand-book.md) |
| 做视觉设计 | [docs/design/design-brief.md](docs/design/design-brief.md) |
| **直接粘贴给 Claude Design** | [docs/design/screen-prompts.md](docs/design/screen-prompts.md) |
| 了解技术架构 | [docs/tech/architecture.md](docs/tech/architecture.md) |

## 域名

| 域名 | 用途 |
|---|---|
| `corro.run` | 主站和控制台（app.corro.run） |
| `corro.sh` | 节点安装：`curl -fsSL corro.sh \| sh` |
| `corro.page` | 客户的公开状态页：`acme.corro.page` |

## 状态

📝 文档阶段：产品、品牌、设计简报和屏幕提示词已经齐全，视觉设计待定，代码还没开始写。
