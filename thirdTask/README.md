# Hotel Management System

REST API для управления отелями, построенный с использованием Spring Boot, PostgreSQL, Thymeleaf и Swagger.

## Технологии

- **Java 17**
- **Spring Boot 3.2.1**
- **Spring Data JPA**
- **PostgreSQL 15**
- **Flyway** - миграции базы данных
- **Thymeleaf** - шаблонизатор для HTML
- **Swagger/OpenAPI** - документация API
- **Lombok** - уменьшение boilerplate кода
- **Docker** - контейнеризация PostgreSQL

## Архитектура

Проект следует паттерну **MVC** и принципам **REST API**:

```
src/main/java/me/prod/hotel/
├── config/          # Конфигурационные классы (OpenAPI)
├── controller/      # REST и MVC контроллеры
├── dto/             # Data Transfer Objects
├── entity/          # JPA сущности
├── exception/       # Обработчики исключений
├── repository/      # JPA репозитории
└── service/         # Бизнес-логика
```

## Функциональность

### REST API Endpoints (версия v1)

- `GET /api/v1/hotel` - Получить список всех отелей
- `GET /api/v1/hotel/{id}` - Получить отель по ID
- `POST /api/v1/hotel` - Создать новый отель
- `PUT /api/v1/hotel/{id}` - Обновить информацию об отеле (JSON)
- `DELETE /api/v1/hotel/{id}` - Удалить отель

### Web-интерфейс

- `/` - Главная страница со списком отелей
- `/api/v1/hotel/{id}` - Страница детальной информации об отеле

## Запуск проекта

### Предварительные требования

- Java 17 или выше
- Maven 3.6+
- Docker и Docker Compose

### Шаги запуска

1. **Клонируйте репозиторий**

```bash
git clone <repository-url>
cd thirdTask
```

2. **Запустите PostgreSQL через Docker**

```bash
docker-compose up -d
```

Это создаст контейнер PostgreSQL на порту 5432 с базой данных `hotel_db`.

3. **Соберите проект**

```bash
mvn clean install
```

4. **Запустите приложение**

```bash
mvn spring-boot:run
```

Или запустите JAR файл:

```bash
java -jar target/thirdTask.jar
```

5. **Проверьте работу приложения**

- Web-интерфейс: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI спецификация: http://localhost:8080/api-docs

## База данных

### Подключение

- **URL**: `jdbc:postgresql://localhost:5432/hotel_db`
- **Username**: `hotel_user`
- **Password**: `hotel_password`

### Миграции

Миграции выполняются автоматически при запуске приложения с помощью Flyway.

Файл миграции: `src/main/resources/db/migration/V1__create_hotels_table.sql`

### Структура таблицы hotels

| Колонка     | Тип          | Описание                    |
|-------------|--------------|----------------------------|
| id          | BIGSERIAL    | Первичный ключ             |
| name        | VARCHAR(255) | Название отеля             |
| description | TEXT         | Описание отеля             |
| stars       | INTEGER      | Количество звезд (1-5)     |
| created_at  | TIMESTAMP    | Дата создания              |
| updated_at  | TIMESTAMP    | Дата последнего обновления |

## Примеры использования API

### Создание отеля (POST)

```bash
curl -X POST http://localhost:8080/api/v1/hotel \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Luxury Hotel",
    "description": "Отель класса люкс",
    "stars": 5
  }'
```

### Получение списка отелей (GET)

```bash
curl http://localhost:8080/api/v1/hotel
```

### Обновление отеля (PUT)

```bash
curl -X PUT http://localhost:8080/api/v1/hotel/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Hotel Name",
    "description": "Обновленное описание",
    "stars": 4
  }'
```

### Удаление отеля (DELETE)

```bash
curl -X DELETE http://localhost:8080/api/v1/hotel/1
```

## Особенности реализации

1. **REST принципы**:
   - Использование HTTP методов (GET, POST, PUT, DELETE)
   - Правильные HTTP статус-коды (200, 201, 204, 404)
   - Версионирование API (`/api/v1`)

2. **MVC паттерн**:
   - Четкое разделение на Model, View, Controller
   - Service слой для бизнес-логики
   - Repository для работы с данными

3. **Обработка ошибок**:
   - Собственное исключение `HotelNotFoundException`
   - Глобальный обработчик исключений `@ControllerAdvice`
   - Кастомные error-страницы

4. **PUT-запросы через JSON**:
   - Обновление отеля выполняется через PUT с JSON
   - JavaScript на странице детального просмотра отеля

5. **Swagger документация**:
   - Автоматическая генерация документации
   - Аннотации для описания endpoints
   - Интерактивный UI для тестирования

## Тестирование

Используйте **Postman** или **Swagger UI** для тестирования API endpoints.

Коллекция Postman может быть импортирована из OpenAPI спецификации:
http://localhost:8080/api-docs

## Остановка проекта

Для остановки PostgreSQL контейнера:

```bash
docker-compose down
```

Для полного удаления с данными:

```bash
docker-compose down -v
```

## Автор

Hotel Management Team

## Лицензия

MIT License
