#!/usr/bin/env python3
"""Validate everything under content/. Exits non-zero on errors.

    python3 tools/validate.py            # errors fail, warnings are printed
    python3 tools/validate.py --strict   # warnings fail too
"""

import argparse
import sys

import content_lib


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--strict", action="store_true", help="treat warnings as errors")
    args = parser.parse_args()

    report = content_lib.validate(content_lib.load())

    for line in report.stats:
        print(f"ℹ️  {line}")
    for msg in report.warnings:
        print(f"⚠️  {msg}")
    for msg in report.errors:
        print(f"❌ {msg}")

    failed = bool(report.errors) or (args.strict and bool(report.warnings))
    print("\nFAILED" if failed else "\nOK")
    return 1 if failed else 0


if __name__ == "__main__":
    sys.exit(main())
