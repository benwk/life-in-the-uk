"""Load, validate and bundle the Robin content files.

Used by validate.py and build_content.py. Standard library only; JSON Schema
validation runs as well when the optional `jsonschema` package is installed
(see tools/requirements.txt).
"""

from __future__ import annotations

import json
from dataclasses import dataclass, field
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
CONTENT = ROOT / "content"
SCHEMA_DIR = CONTENT / "schema"

EXAM_TYPES = {"single", "multiple", "true_false", "statement"}
PRACTICE_ONLY_TYPES = {"order", "match"}


@dataclass
class Content:
    meta: dict
    lines: list[dict]
    facts: list[dict]
    questions: list[dict]
    terms: list[dict]
    # (file, document, schema file name) for JSON Schema validation
    documents: list[tuple[Path, dict, str]] = field(default_factory=list)


@dataclass
class Report:
    errors: list[str] = field(default_factory=list)
    warnings: list[str] = field(default_factory=list)
    stats: list[str] = field(default_factory=list)

    def error(self, msg: str) -> None:
        self.errors.append(msg)

    def warn(self, msg: str) -> None:
        self.warnings.append(msg)


def _read(path: Path) -> dict:
    with path.open(encoding="utf-8") as f:
        return json.load(f)


def load() -> Content:
    meta_path = CONTENT / "meta.json"
    lines_path = CONTENT / "journey" / "lines.json"
    meta = _read(meta_path)
    lines_doc = _read(lines_path)
    documents = [(meta_path, meta, "meta.schema.json"), (lines_path, lines_doc, "lines.schema.json")]

    def collect(folder: str, key: str, schema: str) -> list[dict]:
        items: list[dict] = []
        for path in sorted((CONTENT / folder).glob("*.json")):
            doc = _read(path)
            documents.append((path, doc, schema))
            items.extend(doc.get(key, []))
        return items

    return Content(
        meta=meta,
        lines=lines_doc["lines"],
        facts=collect("facts", "facts", "facts.schema.json"),
        questions=collect("questions", "questions", "questions.schema.json"),
        terms=collect("glossary", "terms", "terms.schema.json"),
        documents=documents,
    )


def _schema_validate(content: Content, report: Report) -> bool:
    try:
        import jsonschema
        from referencing import Registry, Resource
    except ImportError:
        return False

    schemas = {p.name: _read(p) for p in SCHEMA_DIR.glob("*.schema.json")}
    registry = Registry().with_resources(
        (name, Resource.from_contents(schema)) for name, schema in schemas.items()
    )
    for path, doc, schema_name in content.documents:
        validator = jsonschema.Draft202012Validator(schemas[schema_name], registry=registry)
        for err in sorted(validator.iter_errors(doc), key=lambda e: list(e.path)):
            location = "/".join(str(p) for p in err.path)
            report.error(f"{path.relative_to(ROOT)}: {location}: {err.message}")
    return True


def _localized_texts(content: Content):
    """Yield (owner id, field name, localized dict) for every localized field."""
    for line in content.lines:
        yield line["id"], "title", line["title"]
        for st in line["stations"]:
            yield st["id"], "title", st["title"]
            if "summary" in st:
                yield st["id"], "summary", st["summary"]
    for fact in content.facts:
        for key in ("text", "memoryHook", "examNote"):
            if key in fact:
                yield fact["id"], key, fact[key]
    for q in content.questions:
        yield q["id"], "prompt", q["prompt"]
        yield q["id"], "explanation", q["explanation"]
        for opt in q["options"]:
            yield q["id"], f"option {opt['id']}", opt["text"]
            if "matchText" in opt:
                yield q["id"], f"option {opt['id']} matchText", opt["matchText"]
    for term in content.terms:
        yield term["id"], "definition", term["definition"]


def _check_question(q: dict, report: Report) -> None:
    qid = q["id"]
    option_ids = [o["id"] for o in q["options"]]
    answer = q["answer"]
    qtype = q["type"]

    if len(set(option_ids)) != len(option_ids):
        report.error(f"{qid}: duplicate option ids")
    unknown = [a for a in answer if a not in option_ids]
    if unknown:
        report.error(f"{qid}: answer refers to unknown option(s) {unknown}")

    if qtype == "single":
        if len(answer) != 1:
            report.error(f"{qid}: single-choice question must have exactly 1 answer")
        if len(option_ids) != 4:
            report.warn(f"{qid}: the real test uses 4 options for single-choice questions")
    elif qtype == "multiple":
        if len(answer) != 2:
            report.error(f"{qid}: 'choose TWO' question must have exactly 2 answers")
        if len(option_ids) != 4:
            report.warn(f"{qid}: the real test uses 4 options for 'choose TWO' questions")
    elif qtype == "true_false":
        if sorted(option_ids) != ["false", "true"]:
            report.error(f"{qid}: true/false question options must be exactly 'true' and 'false'")
        if len(answer) != 1:
            report.error(f"{qid}: true/false question must have exactly 1 answer")
    elif qtype == "statement":
        if len(option_ids) != 2 or len(answer) != 1:
            report.error(f"{qid}: statement question needs 2 options and 1 answer")
    elif qtype == "order":
        if sorted(answer) != sorted(option_ids):
            report.error(f"{qid}: order question answer must list every option exactly once")
    elif qtype == "match":
        if answer != option_ids:
            report.error(f"{qid}: match question answer must list every option id in option order")
        if any("matchText" not in o for o in q["options"]):
            report.error(f"{qid}: every option of a match question needs matchText")

    if qtype in PRACTICE_ONLY_TYPES and q["examEligible"]:
        report.error(f"{qid}: '{qtype}' is practice-only and cannot be examEligible")


def validate(content: Content) -> Report:
    report = Report()
    if not _schema_validate(content, report):
        report.warn("jsonschema not installed; skipped JSON Schema checks (pip install -r tools/requirements.txt)")

    # Unique ids across every kind of item
    seen: dict[str, str] = {}

    def register(item_id: str, kind: str) -> None:
        if item_id in seen:
            report.error(f"duplicate id '{item_id}' ({seen[item_id]} and {kind})")
        seen[item_id] = kind

    station_chapter: dict[str, int] = {}
    for line in content.lines:
        register(line["id"], "line")
        for st in line["stations"]:
            register(st["id"], "station")
            station_chapter[st["id"]] = line["chapter"]
    for fact in content.facts:
        register(fact["id"], "fact")
    for q in content.questions:
        register(q["id"], "question")
    for term in content.terms:
        register(term["id"], "term")

    fact_ids = {f["id"] for f in content.facts}
    term_ids = {t["id"] for t in content.terms}

    for fact in content.facts:
        fid = fact["id"]
        chapter = station_chapter.get(fact["stationId"])
        if chapter is None:
            report.error(f"{fid}: unknown stationId '{fact['stationId']}'")
        elif fact["source"]["chapter"] != chapter:
            report.error(f"{fid}: source chapter {fact['source']['chapter']} does not match station chapter {chapter}")
        for tid in fact.get("termIds", []):
            if tid not in term_ids:
                report.error(f"{fid}: unknown termId '{tid}'")

    for q in content.questions:
        if q["stationId"] not in station_chapter:
            report.error(f"{q['id']}: unknown stationId '{q['stationId']}'")
        for fid in q["factIds"]:
            if fid not in fact_ids:
                report.error(f"{q['id']}: unknown factId '{fid}'")
        _check_question(q, report)

    exam = content.meta["exam"]
    if exam["passMark"] > exam["questionCount"]:
        report.error("meta.exam.passMark is greater than questionCount")

    # Locales and translation coverage
    locales = [l["code"] for l in content.meta["locales"]]
    if "en" not in locales:
        report.error("meta.locales must include 'en'")
    totals = {code: 0 for code in locales}
    fields = 0
    for owner, name, text in _localized_texts(content):
        for code in text:
            if code not in totals:
                report.error(f"{owner}: {name} uses undeclared locale '{code}'")
        if name == "memoryHook":
            continue  # written per language, not translated, so not counted
        fields += 1
        for code in locales:
            if code in text:
                totals[code] += 1

    # Stats and soft warnings
    questioned = {fid for q in content.questions for fid in q["factIds"]}
    for fact in content.facts:
        if fact["id"] not in questioned:
            report.warn(f"{fact['id']}: no questions yet (aim for at least 2 per fact)")

    stations_with_facts = {f["stationId"] for f in content.facts}
    exam_ready = sum(1 for q in content.questions if q["examEligible"])
    report.stats.append(
        f"{len(content.lines)} lines, {len(station_chapter)} stations "
        f"({len(stations_with_facts)} with facts), {len(content.facts)} facts, "
        f"{len(content.questions)} questions ({exam_ready} exam-eligible), {len(content.terms)} terms"
    )
    coverage = ", ".join(f"{code} {totals[code] / fields:.0%}" for code in locales) if fields else "n/a"
    report.stats.append(f"translation coverage: {coverage}")
    return report


def bundle(content: Content) -> dict:
    """The single JSON document shipped inside both apps."""

    def live(items: list[dict]) -> list[dict]:
        return [i for i in items if not i.get("retired")]

    lines = []
    for line in content.lines:
        lines.append({**line, "stations": live(line["stations"])})
    return {
        "meta": content.meta,
        "lines": lines,
        "facts": live(content.facts),
        "questions": live(content.questions),
        "terms": content.terms,
    }
