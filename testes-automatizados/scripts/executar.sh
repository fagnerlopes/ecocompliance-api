#!/usr/bin/env bash
set -euo pipefail

BASE="${API_BASE_URL:-http://localhost:8080}"
echo "Aguardando ${BASE}/up"

pronto=0
for _ in $(seq 1 60); do
  if curl -fsS "${BASE}/up" >/dev/null; then
    pronto=1
    break
  fi
  sleep 5
done

if [[ "${pronto}" -ne 1 ]]; then
  echo "A API não respondeu em ${BASE}/up. Suba o Compose na raiz do repositório: docker compose up --build -d" >&2
  exit 1
fi

cd "$(dirname "$0")/.."
mvn clean test
