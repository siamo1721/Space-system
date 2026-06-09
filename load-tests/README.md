# Нагрузочное тестирование Space System

Нагрузочные тесты для REST API `space-service` на базе [k6](https://k6.io/).

## Пользовательский сценарий

**Роль:** оператор центра управления спутниковыми группировками.

**Цель:** контролировать состояние орбитальной группировки, пополнять её спутниками, запускать миссии и выводить аппараты из эксплуатации.

| Шаг | Действие | HTTP | Эндпоинт |
|-----|----------|------|----------|
| 1 | Получить обзор всех группировок | `GET` | `/api/overview` |
| 2 | Добавить новый спутник связи в группировку «Орбита-1» | `POST` | `/api/add-satellites` |
| 3 | Запустить миссию (съёмка или связь) для группировки | `POST` | `/api/missions` |
| 4 | Удалить ранее добавленный спутник | `DELETE` | `/api/satellites/{name}` |

Каждый виртуальный пользователь (VU) выполняет полный цикл из 4 шагов.

## Профиль нагрузки

| Фаза | Длительность | VU |
|------|--------------|-----|
| Разгон | 15 сек | 0 → 10 |
| Разгон | 15 сек | 10 → 30 |
| Удержание | **60 сек** | 50 |
| Спад | 10 сек | 50 → 0 |

**Итого:** ~100 секунд, пик — **50 параллельных пользователей**, непрерывная нагрузка ≥ 60 сек.

## Требования

- [k6](https://k6.io/docs/get-started/installation/) установлен локально
- Запущен `space-service` на `http://localhost:8080`

```bash
# macOS
brew install k6

# Linux (Debian/Ubuntu)
sudo gpg -k
sudo gpg --no-default-keyring --keyring /usr/share/keyrings/k6-archive-keyring.gpg \
  --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys C5AD17C747E3415A3642D57D77C782C7C4B3ACAA
echo "deb [signed-by=/usr/share/keyrings/k6-archive-keyring.gpg] https://dl.k6.io/deb stable main" | \
  sudo tee /etc/apt/sources.list.d/k6.list
sudo apt-get update && sudo apt-get install k6
```

## Запуск

### 1. Поднять приложение

```bash
# из корня репозитория
docker compose up -d --build postgres kafka telemetry server
```

### 2. Запустить нагрузочный тест

```bash
cd load-tests
chmod +x run-load-test.sh
./run-load-test.sh
```

Другой базовый URL:

```bash
BASE_URL=http://localhost:9090 ./run-load-test.sh
```

### 3. Просмотр отчёта

После прогона откройте HTML-отчёт:

```bash
open reports/load-test-report-latest.html
```

Отчёт содержит:
- общее число запросов и проверок (checks)
- процент ошибок
- перцентили времени отклика (p95, p99)
- графики по эндпоинтам и группам сценария

Архивные отчёты с timestamp сохраняются в `reports/`.

## Структура

```
load-tests/
├── README.md
├── run-load-test.sh
├── scripts/
│   └── operator-scenario.js   # k6-сценарий
└── reports/
    └── load-test-report-latest.html
```

## Пороги качества (thresholds)

| Метрика | Порог |
|---------|-------|
| `http_req_duration` p95 | < 3000 мс |
| `http_req_failed` | < 15% |
| `checks` | > 85% успешных |
