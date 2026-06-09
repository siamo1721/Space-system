#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPORTS_DIR="${SCRIPT_DIR}/reports"
BASE_URL="${BASE_URL:-http://localhost:8080}"
MAX_ATTEMPTS="${MAX_ATTEMPTS:-40}"
WAIT_SECONDS="${WAIT_SECONDS:-3}"

mkdir -p "${REPORTS_DIR}"

wait_for_service() {
    local attempt=1
    while [ "${attempt}" -le "${MAX_ATTEMPTS}" ]; do
        HTTP_CODE="000"
        HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" "${BASE_URL}/api/overview" 2>/dev/null || true)

        if [ "${HTTP_CODE}" = "200" ]; then
            echo "==> space-service доступен (попытка ${attempt}/${MAX_ATTEMPTS})"
            return 0
        fi

        echo "    Ожидание space-service... (${attempt}/${MAX_ATTEMPTS}, HTTP ${HTTP_CODE})"
        sleep "${WAIT_SECONDS}"
        attempt=$((attempt + 1))
    done

    echo "Ошибка: space-service недоступен после $((MAX_ATTEMPTS * WAIT_SECONDS)) сек."
    echo "Проверьте логи: docker logs space-service --tail 50"
    echo "Перезапуск: docker compose up -d --build postgres kafka telemetry server"
    return 1
}

echo "==> Проверка доступности ${BASE_URL}/api/overview"
wait_for_service

echo "==> Проверка эндпоинта DELETE /api/satellites/{name}"
TEST_NAME="PreflightCheck-$$"
curl -s -o /dev/null -X POST "${BASE_URL}/api/add-satellites" \
    -H "Content-Type: application/json" \
    -d "{\"param\":{\"type\":\"COMMUNICATION\",\"name\":\"${TEST_NAME}\",\"batteryLevel\":100,\"bandwidth\":0.5},\"communicationName\":\"Орбита-1\"}"
DELETE_CODE=$(curl -s -o /dev/null -w "%{http_code}" -X DELETE "${BASE_URL}/api/satellites/${TEST_NAME}" 2>/dev/null || echo "000")
if [ "${DELETE_CODE}" != "200" ]; then
    echo "Ошибка: DELETE /api/satellites/{name} недоступен (HTTP ${DELETE_CODE})."
    echo "Пересоберите образ: docker compose up -d --build server"
    exit 1
fi

echo "==> Запуск нагрузочного теста k6"
echo "    BASE_URL=${BASE_URL}"
echo "    Профиль: 10 → 30 → 50 VU, удержание 60 сек"

k6 run \
    --env "BASE_URL=${BASE_URL}" \
    "${SCRIPT_DIR}/scripts/operator-scenario.js"

echo ""
echo "==> Готово. HTML-отчёт: ${REPORTS_DIR}/load-test-report-latest.html"
echo "    Открыть: open ${REPORTS_DIR}/load-test-report-latest.html"
