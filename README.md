## Основные сущности

| Сущность | Назначение |
|----------|------------|
| **Category** | Категории товаров |
| **Product** | Товары (цена, остаток, описание, связь с категорией) |
| **Customer** | Покупатели (уникальный email) |
| **Order** | Заказы, статусы (`CREATED`, `PAID`, `CANCELLED`) |
| **OrderItem** | Позиции заказа (количество, цена, товар) |

---

## Используемые технологии

- Java 17+
- Spring Boot
- Spring Data JPA (Hibernate)
- PostgreSQL
- Flyway (миграции БД)
- Maven
- Validation (Jakarta)
- Lombok
- Postman (для тестирования API)

---

## Как запустить проект

### Установить зависимости

- Java 17+
- PostgreSQL
- Maven

###  Создать базу данных

```sql
CREATE DATABASE shop;
```

###  Настроить переменные окружения

Перед запуском задать:

| Переменная | Значение |
|-----------|----------|
| `POSTGRES_USER` | имя пользователя |
| `POSTGRES_PASSWORD` | пароль |
| `POSTGRES_DB` | `shop` |
| `POSTGRES_HOST` | `localhost` |
| `POSTGRES_PORT` | `5432` |



### 4️Запуск приложения

```sh
mvn spring-boot:run
```

При старте Flyway создаст таблицы и вставит тестовые данные.

---

## Flyway миграции

Файлы находятся в:

```
src/main/resources/db/migration/
```

- `V1__schema.sql` → создание таблиц и связей
- `V2__seed.sql` → тестовые данные (категории, товары, покупатели)
- `V3__align_price_types.sql` → корректировки схемы

---

## Postman коллекция

Для тестирования API используется Postman.

Коллекция:  
```
postman/shop-api.postman_collection.json
```

Импортируйте её в Postman → выберите переменную:
```
baseUrl = http://localhost:8080
```

---

## CRUD-операции (по всем сущностям)

- `POST /api/categories`
- `GET /api/categories`
- `PUT /api/categories/{id}`
- `DELETE /api/categories/{id}`

Аналогично реализованы:

- `/api/products`
- `/api/customers`
- `/api/orders`
- `/api/order-items`

---

##  Бизнес-операции

| Операция | Endpoint | Описание |
|----------|----------|----------|
| Checkout | `POST /api/orders/checkout` | Создание заказа, списание остатков, статус → PAID |
| Cancel order | `POST /api/orders/{id}/cancel` | Отмена заказа и возврат остатков на склад |
| Add item | `POST /api/orders/{id}/items` | Добавление позиции (только если статус `CREATED`) |
| Remove item | `DELETE /api/orders/{id}/items/{itemId}` | Удаление позиции из заказа |
| Customer summary | `GET /api/orders/customer/{id}/summary` | Итоговая статистика по клиенту |

---



