# Space System — система управления спутниковой группировкой

Микросервисная система для симуляции управления спутниками: создание спутников, группировок и выполнение миссий.

**Репозиторий:** https://github.com/siamo1721/Space-system

## Состав проекта

| Сервис | Порт | Описание |
|--------|------|----------|
| `space-service` | 8080 | REST API управления спутниками и группировками |
| `telemetry-service` | 9091 | gRPC-сервис телеметрии |
| `mission-service` | — | Планировщик миссий (опционально) |
| PostgreSQL | 5432 | База данных |

## API-эндпоинты (`space-service`)

| Метод | Путь | Описание |
|-------|------|----------|
| `GET` | `/api/overview` | Обзор всех спутниковых группировок |
| `POST` | `/api/add-satellites` | Добавление спутника в группировку |
| `POST` | `/api/missions` | Выполнение миссии для группировки |

## Требования

- Java 21
- Docker и Docker Compose
- Gradle 8.x (или используйте `./gradlew` из каталога сервиса)

## Быстрый запуск через Docker Compose

Из корня репозитория:

```bash
docker compose up -d postgres telemetry server
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

## Локальный запуск без Docker (для разработки)

### 1. Запустите PostgreSQL

```bash
docker compose up -d postgres
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
- Логин/пароль: `postgres` / `postgres`
- gRPC telemetry: `localhost:9091`

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

## Автотесты

Проект API-автотестов находится в отдельном репозитории: **space-service-api-tests**.

Перед запуском тестов убедитесь, что `space-service` запущен на `http://localhost:8080`.
