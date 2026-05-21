#!/bin/bash

ROOT_DIR="${1:-.}"

find "$ROOT_DIR" -type f | while read -r file; do
    echo "===== $file ====="
    cat "$file"
    echo
done
