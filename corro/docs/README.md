# 文档目录

## 产品设计 `product/`

| 文档 | 内容 |
|---|---|
| [01 产品愿景](product/01-vision.md) | 要解决的问题、愿景、产品原则、做什么和不做什么 |
| [02 用户画像](product/02-personas.md) | 五类典型用户及其对设计的要求 |
| [03 体验全景](product/03-experience-map.md) | 核心隐喻“围成一圈的见证人”、五个产品界面、信息架构、首次使用、术语表 |
| [04 监控与共识](product/04-monitoring-and-consensus.md) | 监控类型、多节点确认、状态体系、“一次故障的一生” |
| [05 告警与值班](product/05-alerting-and-on-call.md) | 通知渠道、排班、升级策略、确认和解决、从 Opsgenie 迁移 |
| [06 状态页](product/06-status-pages.md) | 公开状态页、组件、订阅、Verified 徽章 |
| [07 节点网络与额度](product/07-network-and-credits.md) | 加入网络、节点类型、额度、信誉、防滥用 |
| [08 可信与验证](product/08-trust-and-verification.md) | 签名结果、透明日志、公链锚定、Receipt、SLA 证明 |
| [09 MVP 与路线图](product/09-mvp-and-roadmap.md) | MVP 范围、版本规划、要验证的假设、商业模式、风险 |

## 品牌 `brand/`

| 文档 | 内容 |
|---|---|
| [品牌手册](brand/brand-book.md) | 名字的故事、定位、口号、语气、写作规则、Logo 方向、禁区 |

## 设计 `design/`

| 文档 | 内容 |
|---|---|
| [设计简报](design/design-brief.md) | 视觉关键词、视觉隐喻、配色、字体、图形和数据可视化、动效、声音 |
| [屏幕提示词](design/screen-prompts.md) | **可以直接粘贴给 Claude Design** 的分屏提示词（P0 / P1 / P2） |

## 技术 `tech/`

| 文档 | 内容 |
|---|---|
| [技术架构概览](tech/architecture.md) | 节点、协调者、共识、透明日志、状态页托管，以及可以复用的已有技术 |

## 阅读顺序建议

- **设计师**：01 愿景 → 03 体验全景 → 品牌手册 → 设计简报 → 屏幕提示词。遇到具体屏幕时再查 04–08。
- **工程师**：01 愿景 → 04 监控与共识 → 07 节点网络 → 08 可信与验证 → 技术架构。
