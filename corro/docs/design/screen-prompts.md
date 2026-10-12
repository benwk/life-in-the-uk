# 屏幕清单与设计提示词

> 用法：先把 **① 通用背景** 粘贴给 Claude Design，然后逐个粘贴屏幕提示词。提示词用英文写，方便直接用；每个提示词下面附了中文说明。

## ① 通用背景（每次对话开头粘贴一次）

```text
You are designing "Corro", an uptime monitoring, on-call alerting and status
page product for developers and ops teams. It does NOT do logs, tracing or
APM — only uptime, on-call and status pages, done extremely well.

What makes Corro different:
1. Community node network: anyone can run a small "node" (one command:
   curl -fsSL corro.sh | sh) that checks other people's public endpoints and
   earns "credits" in return. Your service is watched from many independent
   countries, networks and operators at once.
2. Corroboration: one node seeing a failure is only "Suspect". Only when
   several independent nodes (different countries, networks, operators)
   agree is it "Corroborated Down" — and only then does anyone get paged.
   Tagline: "Uptime, corroborated."
3. Verifiable: every result is signed and written to an append-only
   transparency log, anchored hourly to a public chain. Users get "Receipts"
   that anyone can verify. NO cryptocurrency, NO token — credits cannot be
   transferred or cashed out. Must never look like a crypto/Web3 product.

Name: "corro" = corroborate; Spanish/Italian "I run"; Spanish "a circle of
people". Core visual metaphor: THE CIRCLE — a ring of dots (nodes) around a
target, each giving a witness mark (✓ / ✕); when enough agree, thin lines
converge to the centre. Logo idea: a ring of dots with a gap forming a "C".

Brand feel: "a quiet control tower with a circle of friends watching with
you; when something breaks, it tells you the most certain facts in the
fewest words." Calm, precise, collective, trustworthy, alive, crafted.
References for mood only: Linear's restraint, Stripe docs' clarity, Swiss
grid typography, thermal-printed receipts (only for Receipt UI).
Avoid: neon gradients, glowing cubes, chains, coins, ECG heartbeat lines,
globes with arcs, radar sweeps, war-room dashboards, mascots, stock photos.

Status system (fixed meaning, always shape + colour + text, colour-blind
safe):
  Up ● #12A150 · Suspect ◐ #E0A100 · Down ▲ #E5484D · Degraded ◆ #F2711C ·
  Maintenance ◇ #8E5CD9 · Paused/Unknown ‖/○ #8A919C.
  Red is used ONLY for Down — never for buttons, errors or decoration.
Neutrals (light): bg #F7F7F5, surface #FFFFFF, line #E3E5E8, ink #0F1419,
  muted #5B6470. Dark "Night shift": bg #0B0E13, surface #141922,
  line #232A35, ink #E8EBEF. Brand colour "circle": #2E4BE0 (dark #6D84FF).
  Receipt paper: #FBF8F1.
Type: Inter with tabular figures for UI; JetBrains Mono for hashes, IDs,
timestamps, terminal and receipts. Numbers (uptime %, latency, durations)
are often the visual focus.
When everything is fine the UI should be almost colourless and quiet; colour
appears only when it means something.
Targets: desktop console 1440×900; mobile 393×852; light (Day) mode first
unless told otherwise.
```

---

## P0：MVP 必须的屏幕

### P0-1 · 首次使用：确认的瞬间

```text
Design the first-run screen right after signup, desktop 1440×900.
Step 1 state: a calm, mostly empty page with one large input:
"What should we watch?" placeholder "https://acme.com", primary button
"Start watching".
Step 2 state (the signature moment — show this one in detail): the URL
"acme.com" sits in the centre of the screen. Around it, a ring of 7 node
dots, each labelled with a city and latency: Tokyo ✓ 182ms, Frankfurt ✓ 41ms,
São Paulo ✓ 230ms, New York ✓ 88ms, Sydney ✓ 260ms, Johannesburg ✓ 190ms,
London ✓ 22ms. Five have already turned into green ✓, two are still
pending (hollow, subtle). Thin lines converge from the confirmed nodes to the
centre. Below the ring: "Corroborated Up · 5 of 7 nodes · 5 countries".
A small step indicator at the top (1 Watch · 2 Who to wake · 3 Status page).
Also sketch a 3–4 frame storyboard of how the ring animates in.
```
> 这是产品最有辨识度的一刻：节点依次打勾、细线汇聚、出现 “Corroborated Up”。见 [03 体验全景](../product/03-experience-map.md)。

### P0-2 · 控制台概览 Overview

```text
Design the console Overview page, desktop 1440×900, Day mode.
Left sidebar: Corro logo, nav items Overview, Monitors, Incidents, On-call,
Status pages, Integrations, Nodes, Settings. Top-right: "You're on call until
Fri 09:00" pill and a credits balance "128,400 credits".
Main area:
- A one-line summary at the top: "● 46 of 47 monitors up · 1 incident".
- An active incident banner: "▲ api.acme.com — Down · corroborated by 4 nodes
  in 4 countries · 6m · Acked by Tom" with a small witness ring icon
  showing 4/5.
- A "Suspect" row in calm amber: "◐ payments-webhook — 1 node reports
  timeouts, re-checking…" (no alarm styling).
- A dense, quiet monitors table: name, status (shape+colour+text), 24h
  latency sparkline, 90-day uptime %, interval, node ring mini-icon.
  Most rows are green dots and grey text.
- Right column: "On call now" (avatars and schedule), "Last 24h" stats
  (checks run, suspects dismissed: 7, incidents: 1).
The page must feel calm even though there is an incident; the problem rises
to the top on its own.
```

### P0-3 · 监控详情（节点圈）

```text
Design the monitor detail page for "api.acme.com", desktop, Day mode.
Header: name, URL, status "● Up · for 3d 4h", buttons Pause / Edit.
Left: a large Witness Ring — the target in the centre, 9 node dots around it
with city labels, each showing its latest result (✓ + latency). Caption:
"Watched by 9 nodes · 7 countries · 9 operators". Rule in plain words:
"Alert when at least 3 nodes in 2+ countries agree it's down."
Right: response time chart grouped by region (Europe, Americas, Asia-Pacific)
showing median and p95, last 24h; a shaded band where a past incident was.
Below: 90-day uptime bars (one bar per day; 2 days have small amber/red
marks), "99.97%". Below that: "Recent events" list including a dismissed
suspect in light grey: "Tokyo reported a timeout; 4 other nodes were fine.
We didn't wake you."
```

### P0-4 · 事故详情：一次故障的一生

```text
Design the incident detail page for Incident #142, desktop, Day mode.
Header: "▲ api.acme.com is down" → now resolved: "● Resolved · lasted
23m 33s", severity, escalation policy "Production", acked by Tom.
Left 60%: a vertical timeline with monospace timestamps:
09:41:07 Tokyo: connection timeout (10s) — ◐ Suspect
09:41:08 Extra checks dispatched: Frankfurt, São Paulo, New York, Sydney
09:41:12 Frankfurt 503 · São Paulo 503 · New York 503 · Sydney OK
09:41:12 4/5 failing across 4 countries, 4 networks, 4 operators —
         ▲ Corroborated Down
09:41:13 Paged Tom (push) … 09:46:13 Called Tom … 09:47:02 Tom acked by
         phone
09:49:30 Status page update posted: "Investigating API errors"
10:04:44 5/5 healthy for 2 cycles — ● Corroborated Up
Right 40%: a dot-matrix world map showing the witnesses (▲ failed, ● ok),
and below it a Receipt card in thermal-receipt style: "CORRO · INCIDENT
RECEIPT", duration, 23 witnesses, log entries #88412907–#88412929,
"Anchored 10:00 UTC", short sha256, buttons "Verify" and "Download evidence".
```

### P0-5 · 公开状态页

```text
Design a public status page for the fictional company "Acme" at
acme.corro.page. Show desktop (1280 wide) and mobile (393 wide).
Acme branding: their logo and a teal brand colour replace Corro's brand
colour; status colours and shapes stay standard.
Top: logo, "Subscribe to updates" button.
Hero line: "● All systems operational" (or show the variant with an active
incident card on top: "◆ Degraded performance — Payments webhook ·
Investigating · updated 4 min ago").
Sub-line: "Last checked 12s ago · confirmed by nodes in 9 countries".
Components list: API, Dashboard, Payments webhook, each with 90 one-day
uptime bars (30 on mobile) and a percentage.
Sections: Scheduled maintenance, Past incidents.
Footer: a small "✓ Verified by Corro" badge; show its expanded popover:
"Uptime on this page is computed from signed witnesses in a public
transparency log. 0 manual adjustments. Verify it yourself →".
Must be readable in 5 seconds on a phone.
```

### P0-6 · 值班：排班和升级策略

```text
Design the On-call page, desktop, Day mode, two tabs.
Tab "Schedules" (show this): a horizontal timeline for the next 14 days,
rows = layers ("Primary", "Secondary"), coloured blocks = people with
avatars (Tom, Maya, Ali, Sofia). A "Now" vertical line. One override block
with a dashed outline: "Ali covering for Maya, Sat". Timezone selector
"Europe/London" with "Show in my time". Buttons: "Add override",
"Export to calendar".
Tab "Escalation policies" (show as a second frame): policy "Production" as a
vertical step list: Level 1 Immediately → Primary on-call; Level 2 After
5 min → Secondary on-call; Level 3 After 15 min → Tom (Platform lead);
"Repeat 2 times if nobody acknowledges". Severity rules: "Down → phone",
"Regional degraded → Slack only". A "Send a test page" button.
```

### P0-7 · 加入网络：节点安装和节点仪表盘

```text
Frame 1 — terminal (dark, monospace, 100 columns) after running
"curl -fsSL corro.sh | sh":
  corro node v0.1.0
  ✓ Generated node identity (ed25519)
  ✓ Connected to corro.run (outbound HTTPS only — no open ports)
  ✓ Location: Vienna, Austria · AS1901
  Claim this node: https://app.corro.run/nodes/claim/7KQ-M2X
  Local status: http://localhost:7979
Use subtle colour (brand blue for links, green ticks). Tidy and friendly.

Frame 2 — console Nodes page, desktop, Day mode: "My nodes" list with
"vienna-nuc" (● online 12d, reputation 87/100, witnesses today 1,204,
credits earned today +3,612). A credits panel: balance, 30-day earned vs
spent chart, and a clear note "Credits can't be transferred or cashed out."
A small dot-matrix map of Europe with Vienna lit and thin lines to the
targets it helped confirm today. A friendly line: "Your node helped confirm
the status of 312 services today. Thank you."
```

### P0-8 · 营销站首页首屏（corro.run）

```text
Design the hero section of corro.run, desktop 1440 wide, Day mode.
Headline: "Uptime, corroborated."
Sub: "Monitoring, on-call and status pages — confirmed by a community
network of independent nodes, with records anyone can verify. Only wake up
for what's real."
Primary CTA "Start free", secondary "Run a node →" showing
"curl -fsSL corro.sh | sh" in a copyable monospace pill.
Visual: a large live dot-matrix world map; some cities lit; a ring animation
around one example target showing nodes confirming. A small live ticker:
"2,431 nodes online · 61 countries · 1.2M witnesses in the last hour".
Below the fold teaser: three columns — "Watched by many", "Confirmed before
we wake you", "Don't trust us. Verify."
No crypto aesthetics.
```

---

## P1：第二优先级

### P1-1 · 值班 App：凌晨三点的告警

```text
Design the mobile on-call experience, iPhone 393×852, Night shift (dark).
Frame 1 — lock screen critical alert notification:
"▲ api.acme.com down · confirmed by 4 nodes in 4 countries"
"HTTP 503 · since 03:12 · Production · #142", actions "Acknowledge".
Frame 2 — in-app alert screen designed for someone half-asleep: top half
shows the red triangle, service name in large type, one sentence of fact,
a small 4/5 witness ring, elapsed time "2m". Bottom half: one HUGE
"Acknowledge" button within thumb reach; secondary "Escalate to next" and
"Details". Low brightness overall; only the status and the button glow.
Frame 3 — after acknowledging: calm confirmation, "You've got it. We've
told the team.", and quick actions "Post status update", "Open incident".
```
> 见 [05 告警与值班](../product/05-alerting-and-on-call.md) 的“凌晨三点设计”。

### P1-2 · Receipt 验证页（corro.network/verify）

```text
Design the public receipt verification page, desktop, Day mode.
Input: "Paste a Receipt ID or drop a receipt file". After verifying, show a
step-by-step checklist that a non-technical person can follow:
✓ Signatures — 23 witnesses signed by 23 registered nodes
✓ Included in log — entries #88412907–#88412929 (inclusion proof)
✓ Log not rewritten — consistent with tree head 10:00 UTC
✓ Anchored — timestamp proof on Bitcoin via OpenTimestamps
Each step expandable for technical details (monospace hashes).
Next to the checklist, the Receipt rendered as a thermal-paper receipt card.
Also show "Verify offline: corro verify receipt.json".
Must feel trustworthy and simple, not like a blockchain explorer.
```

### P1-3 · 网络浏览器（corro.network）

```text
Design the public network explorer homepage, desktop, Night shift (dark).
A large dot-matrix world map with lit cities (aggregated to city level —
never exact locations). Stats row: nodes online, countries, networks (ASNs),
witnesses last 24h, incidents corroborated last 24h. Below: "Coverage gaps —
nodes wanted in: Nigeria, Peru, Vietnam" with "Run a node" CTA.
A small leaderboard of opt-in node nicknames by region with reputation.
A "Transparency log" panel showing the latest signed tree head and the last
anchor time. It should feel alive and communal, never like a crypto
explorer.
```

### P1-4 · 从 Opsgenie 导入

```text
Design a 4-step import wizard in the console: "Move from Opsgenie".
Step 2 of 4 shown: preview of what will be imported — 18 users, 4 teams,
6 schedules, 5 escalation policies, 9 integrations — each with a checkbox
and count. Step 3 preview as a second frame: user mapping table (Opsgenie
user → Corro user, matched by email; 2 unmatched highlighted calmly with a
dropdown). Reassuring note: "Run both side by side until you're ready to
switch."
```

### P1-5 · 月度 SLA 报告

```text
Design a monthly SLA report page (also exportable as PDF), Day mode.
Header: "Acme · API · October 2026 · 99.982% (target 99.9%) ✓ Met".
A calendar heatmap of the month, incidents list with durations and Receipt
links, total downtime, number of witnesses and countries. A verification
footer styled like the end of a receipt: log range, anchor proofs, QR code
to the verify page. Clean enough to send to an auditor.
```

### P1-6 · 通知消息

```text
Design how Corro alerts look in three channels, side by side:
1) Slack message: header "▲ api.acme.com is down", context line
   "Confirmed by 4 nodes in 4 countries · HTTP 503 · Production · #142",
   buttons Acknowledge / Resolve / Open incident.
2) Email (desktop email client): subject "▲ Down: api.acme.com (confirmed by
   4 nodes)"; minimal body with a witness summary and one button.
3) SMS: "Corro ▲ api.acme.com DOWN, confirmed by 4 nodes/4 countries.
   #142. Reply ACK to acknowledge."
And the matching "● Recovered after 23m 33s" variants.
```

---

## P2：锦上添花

### P2-1 · Logo 探索

```text
Explore the Corro logo in three directions, each shown at 512px, 32px and
16px, on light and dark:
A) A ring of 8–12 dots with a gap forming a "C"; three dots slightly
   darker and connected by hairlines (corroboration).
B) Three partly overlapping circles; the shared centre is the "truth".
C) Lowercase wordmark "corro" where the two o's read as two nodes (one
   filled, one hollow) — clean developer-tool feel.
Also show a "status" variant of A where one dot takes the current status
shape/colour, for favicons on status pages.
```

### P2-2 · 嵌入组件和 README 徽章

```text
Design (1) a tiny embeddable status widget for customers' websites: a
pill "● All systems operational" that expands into a small card; and
(2) SVG README badges: "uptime | 99.98% ✓ verified" in flat style, plus
Down and Degraded variants.
```

### P2-3 · 节点运营者分享卡

```text
Design a 1200×630 share card for node operators: "My Corro node in Vienna
helped confirm 41,208 checks this month." Dot-matrix map snippet with
Vienna lit, reputation 92/100, nickname, small logo and "corro.network".
Proud but understated.
```

### P2-4 · 大屏模式（TV / NOC）

```text
Design a full-screen TV mode (1920×1080, Night shift) for an office wall:
large tiles per service group, only status shapes and names; any non-Up
service expands to show witness ring and duration. Readable from 5 metres.
Calm when everything is fine.
```

---

## 小贴士

- 先做 **P0-1（确认的瞬间）** 和 **P0-3（监控详情的节点圈）**：节点圈组件决定了整个产品的视觉语言，其他屏幕都从它延伸出来。
- 每个屏幕满意后，让 Claude Design 追加：“Now show the Night shift (dark) version”。状态页再追加：“Now show it on mobile” 和 “Now with a customer's brand colour”。
- 设计定稿后，把颜色和字体 token 更新到 [设计简报](design-brief.md) 第 4、5 节，开发以那里为准。
