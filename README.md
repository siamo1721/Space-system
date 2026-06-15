# Space System — система управления спутниковой группировкой

Микросервисная система для симуляции управления спутниками: создание спутников, группировок, выполнение миссий и асинхронный обмен событиями через Kafka.

**Репозиторий:** https://github.com/siamo1721/Space-system

## Состав проекта

| Сервис | Порт | Описание |
|--------|------|----------|
| `space-service` | 8080 | REST API управления спутниками и группировками |
| `telemetry-service` | 9091 | gRPC-сервис телеметрии |
| `mission-service` | 8081 | Планировщик миссий |
| `kafka` | 9092 | Брокер сообщений (события о спутниках) |
| PostgreSQL | 5432 | База данных |

## Ключевой пользовательский сценарий

**Роль:** оператор центра управления космической группировкой.

**Бизнес-цель:** обеспечить непрерывную работу орбитальной группировки — от мониторинга состояния до запуска миссий и управления составом аппаратов.

### Поток действий оператора

```
┌─────────────────┐     ┌──────────────────┐     ┌─────────────────┐     ┌──────────────────┐
│ 1. Обзор        │ ──► │ 2. Добавление    │ ──► │ 3. Запуск       │ ──► │ 4. Вывод из      │
│    группировок  │     │    спутника      │     │    миссии       │     │    эксплуатации  │
│    GET /overview│     │ POST /add-sat... │     │ POST /missions  │     │ DELETE /sat...   │
└─────────────────┘     └──────────────────┘     └─────────────────┘     └──────────────────┘
```

| Шаг | Действие оператора | API |
|-----|-------------------|-----|
| 1 | Просмотреть текущее состояние всех спутниковых группировок | `GET /api/overview` |
| 2 | Добавить новый спутник связи в группировку «Орбита-1» | `POST /api/add-satellites` |
| 3 | Запустить миссию (съёмка или передача данных) для группировки | `POST /api/missions` |
| 4 | Удалить спутник при выводе из эксплуатации | `DELETE /api/satellites/{name}` |

После шагов 2 и 4 события публикуются в Kafka — `mission-service` и `telemetry-service` узнают о составе группировки асинхронно.

## API-эндпоинты (`space-service`)

| Метод | Путь | Описание |
|-------|------|----------|
| `GET` | `/api/overview` | Обзор всех спутниковых группировок |
| `POST` | `/api/add-satellites` | Добавление спутника в группировку |
| `POST` | `/api/missions` | Выполнение миссии для группировки |
| `DELETE` | `/api/satellites/{name}` | Удаление спутника |

## Требования

- Java 21
- Docker и Docker Compose
- Gradle 8.x (или `./gradlew` из каталога сервиса)
- [k6](https://k6.io/) — для нагрузочного тестирования

## Быстрый запуск через Docker Compose

Из корня репозитория:

```bash
docker compose up -d --build postgres kafka telemetry server
```
```

Сервис будет доступен по адресу: **http://localhost:8080**

При первом запуске приложение автоматически создаёт тестовые группировки `Орбита-1` и `Орбита-2` с набором спутников.

### Проверка работоспособности

```bash
curl http://localhost:8080/api/overview
```

Ожидаемый ответ: HTTP `200 OK`.

### Остановка

```bash
docker compose down
```

## Нагрузочное тестирование

Тесты находятся в папке [`load-tests/`](load-tests/).

### Профиль нагрузки

- **Разгон:** 0 → 10 → 30 → **50** параллельных пользователей (VU)
- **Удержание:** 60 секунд на пике (50 VU)
- **Сценарий:** комбинированный — `GET` + `POST` + `DELETE` в одном цикле

### Запуск

```bash
# 1. Убедитесь, что приложение запущено
docker compose up -d --build postgres kafka telemetry server

# 2. Установите k6 (macOS)
brew install k6

# 3. Запустите тест
cd load-tests
chmod +x run-load-test.sh
./run-load-test.sh

# 4. Откройте HTML-отчёт
open reports/load-test-report-latest.html
```

Подробности — в [`load-tests/README.md`](load-tests/README.md).

## Локальный запуск без Docker (для разработки)

### 1. Запустите PostgreSQL и Kafka

```bash
docker compose up -d postgres kafka
```

### 2. Запустите telemetry-service

```bash
cd telemetry-service
./gradlew bootRun
```

### 3. Запустите space-service

```bash
cd space-service
./gradlew bootRun
```

Конфигурация по умолчанию (`application.yml`):

- Порт: `8080`
- БД: `jdbc:postgresql://localhost:5432/space_db`
- Kafka: `localhost:9092`

## Примеры запросов

**Обзор группировок:**

```bash
curl -X GET http://localhost:8080/api/overview
```

**Добавление спутника:**

```bash
curl -X POST http://localhost:8080/api/add-satellites \
  -H "Content-Type: application/json" \
  -d '{
    "param": {
      "type": "COMMUNICATION",
      "name": "Связь-Test",
      "batteryLevel": 500,
      "bandwidth": 0.85
    },
    "communicationName": "Орбита-1"
  }'
```

**Выполнение миссии:**

```bash
curl -X POST http://localhost:8080/api/missions \
  -H "Content-Type: application/json" \
  -d '{
    "constellationName": "Орбита-1",
    "missionType": "IMAGING"
  }'
```

**Удаление спутника:**

```bash
curl -X DELETE "http://localhost:8080/api/satellites/Связь-Test"
```

## Kafka — асинхронные события о спутниках (Outbox / Inbox)

| Топик | Описание |
|-------|----------|
| `satellite.created` | Спутник успешно создан |
| `satellite.deleted` | Спутник удалён |
| `satellite.events.dlt` | «Битые» сообщения (Dead Letter Topic) |

### Паттерны согласованности

| Сервис | Паттерн | Описание |
|--------|---------|----------|
| `space-service` | **Transactional Outbox** | Событие пишется в таблицу `outbox` в той же транзакции, что и изменение спутника; планировщик отправляет в Kafka каждые 5 сек |
| `telemetry-service` | **Inbox** | Consumer проверяет `event_id` в таблице `inbox` и игнорирует дубликаты (at-least-once) |

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440000",
  "eventType": "SATELLITE_CREATED",
  "satelliteId": 1,
  "satelliteName": "Связь-1",
  "satelliteType": "COMMUNICATION",
  "constellationName": "Орбита-1",
  "timestamp": "2026-05-31T12:00:00Z"
}
```

### Таблица `outbox` (space-service)

| Поле | Тип | Описание |
|------|-----|----------|
| `id` | UUID | PK, совпадает с `eventId` в payload |
| `aggregate_id` | BIGINT | ID спутника |
| `event_type` | CREATED / DELETED | Тип события |
| `payload` | TEXT (JSON) | Тело события |
| `created_at` | TIMESTAMP | Время создания |
| `status` | PENDING / SENT | Статус доставки в Kafka |

### Таблица `inbox` (telemetry-service)

| Поле | Тип | Описание |
|------|-----|----------|
| `event_id` | UUID | PK, уникальный ID события |
| `aggregate_id` | BIGINT | ID спутника |
| `event_type` | VARCHAR | Тип события |
| `processed_at` | TIMESTAMP | Время обработки |

## Автотесты API

Функциональные API-тесты (JUnit + RestAssured + Allure) — в репозитории **space-service-api-tests** или папке `space-service-api-tests/`.
