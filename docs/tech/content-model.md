# 内容模型

完整定义见 `content/schema/*.schema.json`。这里说明各部分之间的关系和设计理由。

```
Line 线路（手册章节）
 └─ Station 车站（手册小节）
     ├─ Fact 知识点 ──── termIds ───► Term 词汇
     │    ▲
     │    │ factIds（一道题可以考多个知识点）
     └─ Question 题目
```

## 多语言文本 `LocalizedText`

```json
{ "en": "In 1215, King John …", "zh-Hans": "1215 年，约翰国王……", "zh-Hant": "1215 年，約翰國王……" }
```

- `en` 必填，其他语言可选；缺失时 App 回退到英语。
- 支持的语言在 `content/meta.json` 的 `locales` 里声明；使用未声明的语言代码会被 `validate.py` 拦下。
- 每条内容可以带 `translationQuality`，按语言标注 `human` / `machine` / `community`。
- `memoryHook`（记忆钩子）是**按语言单独创作**的，所以连 `en` 也是可选的。

## Fact 知识点

最小的学习、出题、复习单位。

| 字段 | 说明 |
|---|---|
| `kind` | 故事卡类型：`story` / `date` / `person` / `map` / `comparison` / `number` / `exam-tip` |
| `year` | 有年份的知识点会进入“时间轴”专题 |
| `examNote` | 考试提醒，比如手册与现实不一致 |
| `source` | 手册章节和小节，用于追溯和手册改版 |

## Question 题目

| `type` | 真考题型 | 回答（`answer`）的含义 |
|---|---|---|
| `single` | ✅ | 1 个正确选项 |
| `multiple` | ✅ | 2 个正确选项（“choose TWO”） |
| `true_false` | ✅ | 选项 id 固定为 `true` / `false` |
| `statement` | ✅ | 2 个陈述句选 1 |
| `order` | 练习专属 | 正确的顺序 |
| `match` | 练习专属 | 所有选项 id，**按选项顺序**；每个选项的 `text` 和它自己的 `matchText` 配对 |

- `examEligible: true` 的题才会进入模拟考试；练习专属题型必须是 `false`。
- `traps` 标注这道题的陷阱，供陷阱雷达和统计使用。

判分规则写在两端的 `Grader` 里，并由 `content/fixtures/core-cases.json` 约束两端一致：

- 选择题：选中的集合与答案集合**完全相等**（顺序无关，多选少选都算错）。
- 排序和连线：序列完全相等。
- 模考及格线：完整模考是 24 题答对 18 题；题数更少的模考按比例折算并**向上取整**（8 题要答对 6 题）。

## 打包后的 `content.json`

```json
{
  "meta": { … },
  "lines": [ … ],
  "facts": [ … ],
  "questions": [ … ],
  "terms": [ … ]
}
```

- 标记为 `retired: true` 的车站、知识点和题目不会进入内容包，但 ID 保留，避免和用户已有的学习记录冲突。
- 现在只有样例内容，大约 34 KB；完整题库预计 1–2 MB，完全可以直接打包进 App。
