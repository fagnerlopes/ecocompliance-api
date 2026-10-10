#!/usr/bin/env bash
set -euo pipefail

ENV_FILE="${1:?Uso: scripts/deploy-ambiente.sh deploy/staging.env|deploy/production.env}"
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

if [[ ! -f "$ENV_FILE" ]]; then
  echo "Arquivo nao encontrado: $ENV_FILE" >&2
  exit 1
fi

set -a
# shellcheck disable=SC1090
source "$ENV_FILE"
set +a

: "${COMPOSE_PROJECT_NAME:?COMPOSE_PROJECT_NAME ausente em $ENV_FILE}"
: "${API_PORT:?API_PORT ausente em $ENV_FILE}"

echo "Subindo ${APP_ENV} (projeto ${COMPOSE_PROJECT_NAME}) na porta ${API_PORT}"
docker compose --env-file "$ENV_FILE" -p "$COMPOSE_PROJECT_NAME" up -d --build

pronto=0
for _ in $(seq 1 60); do
  if curl -fsS "http://localhost:${API_PORT}/ambiente" >/dev/null 2>&1; then
    pronto=1
    break
  fi
  sleep 5
done

if [[ "$pronto" -ne 1 ]]; then
  echo "A API de ${APP_ENV} nao respondeu em http://localhost:${API_PORT}/ambiente" >&2
  docker compose --env-file "$ENV_FILE" -p "$COMPOSE_PROJECT_NAME" logs --tail 80 ecocompliance-api || true
  exit 1
fi

echo "Smoke ${APP_ENV}:"
curl -fsS "http://localhost:${API_PORT}/ambiente"
echo
curl -fsS -o /dev/null -w "login:%{http_code}\n" \
  -X POST "http://localhost:${API_PORT}/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@ecocompliance.com","senha":"admin123"}'

if [[ "${CI_TEARDOWN:-0}" == "1" ]]; then
  echo "CI_TEARDOWN=1: encerrando o ambiente ${APP_ENV}"
  docker compose --env-file "$ENV_FILE" -p "$COMPOSE_PROJECT_NAME" down -v
fi
