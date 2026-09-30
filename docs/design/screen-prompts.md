# 屏幕清单与设计提示词

> 用法：先把 **① 通用背景** 粘贴给 Claude Design，然后逐个粘贴屏幕提示词。提示词用英文写，方便直接用；每个提示词下面附了中文说明。

## ① 通用背景（每次对话开头粘贴一次）

```text
You are designing "Robin", a mobile app (iOS + Android) that helps immigrants
pass the UK "Life in the UK" citizenship test. The test is English-only, but
our users often read English with difficulty, so every piece of content can be
shown in English AND the learner's native language (e.g. Simplified Chinese,
Traditional Chinese, Urdu, Arabic, Ukrainian).

Three journeys:
1. The Journey (learn): a railway-style line map of the handbook. 5 coloured
   lines = 5 chapters, 24 stations = sections. Each station has 5–10 bilingual
   story cards and ends with a passport stamp.
2. Tea Break (practice): 5 questions a day chosen by spaced repetition.
   Forgotten stations get covered in "fog"; practising blows the fog away.
3. Test Day (mock exam): 24 questions, 45 minutes, pass mark 18. An
   "exam mode" skin that is neutral and English-only, like the real test.

Mascot: Robin, a round, friendly European robin with an orange-red breast.
Cute but not childish — adults must feel comfortable using it.

Brand feel: "a beautifully crafted travel journal with a friendly robin
turning the pages". Warm, curious, calm, inclusive, British but not clichéd
(no Union Jack wallpaper, no red phone boxes). Never anxious: wrong answers
use a soft amber, not red.

Palette: paper #FAF6EE, ink #1F2A44, robin #E4572E, racing green #1E4D3A,
amber #F2A541, fog #C9CED6. Line colours: values #6C4AB6, what-is-the-UK
#2E86AB, history #B5651D, modern society #2A9D8F, government #264653.
Type: a warm serif for headings (e.g. Fraunces), Inter/system for body,
Noto family for non-Latin scripts. Must not look like an official GOV.UK
product. Target: iPhone 15 size (393×852), light mode first.
```

---

## P0：MVP 必须的屏幕

### P0-1 · 首次打开：选择语言

```text
Design the first onboarding screen: "Choose your language".
Robin sits at the top holding a small signpost with arrows in different
scripts. Below, a scrollable list of language cards, each showing the language
name in its OWN script (中文（简体）, 繁體中文, اردو, العربية, हिन्दी,
Українська, Polski, English) with a subtle flag-free icon (use a speech-bubble
glyph, not national flags — languages ≠ countries). One card is selected.
A primary button "Continue" at the bottom. Warm paper background.
```
> 注意：用**语言原名**而不是国旗，因为语言不等于国家（比如繁中用户可能来自香港或台湾）。

### P0-2 · 今日 Today

```text
Design the "Today" home tab.
Top: greeting from Robin in the user's language ("早上好，陈太太 ☕") with a
small Robin illustration holding a teacup.
Hero card: "Today's Tea Break — 5 questions · ~3 min" with a big start button
and a row of 7 small teacups showing this week's streak (4 filled).
Second card: "Continue your journey — Next stop: The Tudors (History line)"
with the brown line colour and a tiny line-map snippet.
Third card: Readiness gauge (semi-circle) at 62% "Almost there".
Fourth card: "On this day: 2 June 1953 — Coronation of Elizabeth II".
Bottom tab bar: Today, Journey, Practice, Test Day, Passport.
```

### P0-3 · 旅程线路图 Journey Map

```text
Design the Journey tab: a vertical, scrollable railway line map.
5 coloured lines (values purple, UK blue, history brown, society teal,
government deep green) run top-to-bottom with round station nodes and
interchange stations connecting them. Station states: locked (grey hollow),
available (hollow, line colour, gentle glow), completed (filled with a tiny
stamp icon), foggy (completed but covered with a soft translucent fog).
Robin is perched above the next available station. Each station label shows
English name + native name underneath (e.g. "The Middle Ages / 中世纪").
The final terminus at the bottom is a ticket-shaped "TEST DAY" station.
Must not copy the London Underground roundel or TfL map styling.
```

### P0-4 · 车站故事卡（双语）

```text
Design a story card inside a station. Layout: progress dots on top (card 3
of 8), a card with a paper texture filling most of the screen.
Card content: small label "📅 Key date", huge serif year "1215",
illustration of barons and a king with a sealed document,
English text: "In 1215, the barons forced King John to agree to the Magna
Carta, which limited the power of the king."
Native text below (Simplified Chinese): "1215 年，贵族们迫使约翰国王同意《大宪章》，
限制了国王的权力。"
Key term "Magna Carta" has a dotted underline (tappable).
A memory-hook chip at the bottom: "💡 1215：大宪章让国王也得守规矩".
Top-right: a "Language Lens" toggle with three states
(Native first / Side by side / English first). Bottom: 🔊 listen button,
swipe hint. Show the "Side by side" state.
```

### P0-5 · 答题卡（练习，答错状态）

```text
Design a practice question screen right after the user answered WRONG.
Question type: "Choose TWO answers" — the word TWO is highlighted by the
"Trap Radar" with a small tooltip in Chinese "要选两个答案".
Question: "Which TWO are patron saints of countries in the UK?"
Options: St George ✓ (green), St Andrew ✓ (green), St Nicholas (user's pick,
soft amber with "Your answer" label), St Christopher.
Below: an expanded explanation panel in Chinese with the English original
collapsible, plus a "📖 Back to story card" link. Robin in a thinking pose
says "很多人都会在这里犹豫，我们看看为什么".
Bottom toolbar: 🌐 translate, 🔍 look up, 🔊 listen. Primary "Next" button.
No harsh red anywhere.
```

### P0-6 · 模拟考试（考场模式）

```text
Design the mock exam screen in "exam mode": neutral, white background,
no mascot, no brand colours except a single neutral blue. English only.
Top bar: "Question 7 of 24", a calm timer "32:14" (no red), a "Flag" button.
Question: "Is the statement below TRUE or FALSE? The Channel Islands are part
of the UK." Two large radio options: True / False.
Bottom: "Previous", "Next", and a grid icon that opens a question navigator
(24 numbered squares: answered, flagged, unanswered). Should feel like a
calm, realistic computer-based test.
```

### P0-7 · 成绩单

```text
Design the mock exam results screen. Brand styling returns.
Big result "20 / 24" with a green "PASSED ✅" stamp that looks like a passport
stamp. Time used "31 min". A horizontal bar chart of accuracy per line
(5 line colours). A list of 4 wrong questions, each expandable to show
your answer / correct answer / Chinese explanation. Buttons: "Add mistakes
to Tea Break", "Back to Test Day". Robin applauding.
```

### P0-8 · 护照 Passport

```text
Design the Passport tab as an open passport booklet. Pages show a grid of
circular and oval ink stamps (each station has its own mini icon — a knight's
helmet for The Middle Ages, a crown for The Tudors, a ballot box for
Democracy). Stamps have slight rotation and ink texture, with the real date
they were earned. Faded stamps = foggy stations. Header: "12 / 29 stamps".
Below the booklet: streak teacups, Word Bank entry, Settings.
```

---

## P1：第二优先级

### P1-1 · 迷雾地图（练习页顶部）

```text
Design the top half of the Practice tab: the same line map but showing
memory, not progress. Several stations are covered in soft fog. One foggy
station is tapped, showing a bottom sheet: "The Tudors is getting foggy —
5 questions, ~2 min to clear it". Robin flaps its wings beside it.
Below the map: entry cards for Mistakes (12), Starred (5), Mini games.
```

### P1-2 · 长按查词：词汇卡

```text
Design a popover word card that appears when long-pressing "constituency"
in an English sentence. Shows: word, IPA /kənˈstɪtʃuənsi/, 🔊, meaning in
Chinese in THIS context ("选区：选出一名议员的地区"), a simple-English
definition, the source sentence, and "+ Add to Word Bank".
```

### P1-3 · 准备度

```text
Design the Test Day tab. Top: a large readiness gauge at 82% in green
"Ready to book!", with a note "This is an estimate, not a guarantee".
Below: "Your weakest 3 stations" with line colours and "Practise" buttons.
Mode cards: Full Mock (24 Q · 45 min), Mini Mock (8 Q · 15 min),
Assisted Mock (3 translation lifelines). A test date countdown card
"Your test: 14 Nov · 18 days" with a checklist.
```

### P1-4 · 双语卡片（RTL：英语 + 乌尔都语）

```text
Redesign the P0-4 story card for an Urdu-speaking learner. The whole UI is
mirrored (RTL): back arrow on the right, progress from right to left.
The English paragraph stays left-to-right; the Urdu paragraph (Noto Nastaliq
Urdu, generous line height) is right-to-left. Both must feel balanced on the
same card. Also show the same screen at 200% text size.
```

---

## P2：锦上添花

### P2-1 · 小游戏：时光排序

```text
Design the "Time Traveller" mini game: 4 event cards (Battle of Hastings,
Magna Carta, Black Death, Spanish Armada) to drag onto a horizontal
timeline in the right order. Each card has a tiny illustration and bilingual
title. A "Check" button. Playful but tidy.
```

### P2-2 · 酒馆问答

```text
Design the "Pub Quiz" pass-and-play setup and question screen for 2–4
players at one phone. Cosy pub aesthetic (wood, warm light, chalkboard
scores) while keeping text highly readable. Player avatars are coloured
pint glasses / teacups (users can choose — not everyone drinks alcohol).
```

### P2-3 · 毕业护照分享卡

```text
Design a shareable 1080×1920 graduation card: "I passed the Life in the UK
test!" A passport page filled with all stamps, the user's first name,
study days (38), best mock score (24/24), Robin wearing a graduation cap,
and a small QR code + "Robin – learn the UK in your language".
```

---

## 小贴士

- 先做 P0-3（线路图）和 P0-4（双语卡片）：它们决定整个 App 的视觉语言，其他屏幕都从这两个延伸出来。
- 每个屏幕满意后，让 Claude Design 追加：“Now show the dark mode version” 和 “Now show it in Traditional Chinese”。
- 设计定稿后，把颜色和字体 token 更新到 `docs/design/design-brief.md` 第 4、5 节，开发会以那里为准。
