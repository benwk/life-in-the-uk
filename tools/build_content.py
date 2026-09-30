#!/usr/bin/env python3
"""Validate content/ and write the bundle that both apps ship with.

    python3 tools/build_content.py                      # -> build/content.json
    python3 tools/build_content.py --out path/to.json   # used by the Xcode and Gradle builds
"""

import argparse
import json
import sys
from pathlib import Path

import content_lib


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--out", type=Path, default=content_lib.ROOT / "build" / "content.json")
    args = parser.parse_args()

    content = content_lib.load()
    report = content_lib.validate(content)
    if report.errors:
        for msg in report.errors:
            print(f"❌ {msg}", file=sys.stderr)
        print("Content has errors; run tools/validate.py for the full report.", file=sys.stderr)
        return 1

    args.out.parent.mkdir(parents=True, exist_ok=True)
    with args.out.open("w", encoding="utf-8") as f:
        json.dump(content_lib.bundle(content), f, ensure_ascii=False, separators=(",", ":"))
    print(f"Wrote {args.out} ({args.out.stat().st_size / 1024:.1f} KB)")
    return 0


if __name__ == "__main__":
    sys.exit(main())
