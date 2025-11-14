# Shop — RBPO Labs 2–4

Проект представляет собой  интернет-магазин.  
Реализованы CRUD-операции, бизнес-логика заказов, работа с PostgreSQL, миграции Flyway и защита API (Basic Auth, роли, CSRF).

---

## Основные сущности

| Сущность | Описание |
|----------|----------|
| Category | Категории товаров |
| Product | Товары (связь с категорией, цена, остаток) |
| Customer | Покупатели (email уникален) |
| Order | Заказы (CREATED, PAID, CANCELLED) |
| OrderItem | Позиции заказа |
| UserAccount | Пользователь системы (логин, пароль, роль) |

---

## Используемые технологии

- Java 17+
- Spring Boot (Web, Data JPA, Security, Validation)
- PostgreSQL
- Flyway
- Maven
- Lombok
- Postman

---

## Как запустить проект

### Требования

- Java 17+
- PostgreSQL
- Maven

### Создать базу данных

```sql
CREATE DATABASE shop;
```

### Настроить переменные окружения

| Переменная | Значение |
|-----------|----------|
| POSTGRES_USER | пользователь БД |
| POSTGRES_PASSWORD | пароль |
| POSTGRES_DB | shop |
| POSTGRES_HOST | localhost |
| POSTGRES_PORT | 5432 |

### Запуск приложения

```bash
mvn spring-boot:run
```

После запуска Flyway автоматически создаст таблицы и тестовые данные.

---

## Flyway миграции

Путь:

```
src/main/resources/db/migration/
```

| Файл | Назначение |
|------|-----------|
| V1__schema.sql | создание схемы БД |
| V2__seed.sql | тестовые данные |
| V3__align_price_types.sql | корректировки |
| V4__users.sql | таблица пользователей |

---

## Postman коллекция

Файл:

```
postman/shop-api.postman_collection.json
```

Перед использованием установить переменную:

```
baseUrl = http://localhost:8080
```

---

## CRUD-операции

Реализованы маршруты для сущностей:

- `/api/categories`
- `/api/products`
- `/api/customers`
- `/api/orders`
- `/api/order-items`

---

## Бизнес-операции

| Операция | Endpoint | Описание |
|----------|----------|----------|
| Checkout | POST /api/orders/checkout | оформление заказа и списание остатков |
| Cancel order | POST /api/orders/{id}/cancel | отмена заказа и возврат товаров |
| Add item | POST /api/orders/{id}/items | добавить товар в заказ |
| Remove item | DELETE /api/orders/{id}/items/{itemId} | удалить товар из заказа |
| Customer summary | GET /api/orders/customer/{id}/summary | статистика заказов клиента |

---

# Безопасность (Лабораторная №4)

## Аутентификация

- Используется Basic Auth
- Пользователи хранятся в таблице `users`
- Пароли — BCrypt-хэш
- Реализована регистрация пользователей:

```
POST /api/auth/register
```

Пример JSON:

```json
{
  "username": "user@example.com",
  "password": "Qwerty!1"
}
```

Включена проверка сложности пароля.

---

## Авторизация по ролям

Роли:

| Роль | Доступ |
|------|--------|
| ROLE_USER | операции с заказами, просмотр каталога |
| ROLE_ADMIN | управление товарами, категориями и клиентами |

Матрица доступа:

| Endpoint | Доступ |
|----------|--------|
| POST /api/auth/register | доступен без авторизации |
| GET /api/products, GET /api/categories | доступно всем |
| /api/products/**, /api/categories/**, /api/customers/** | только ROLE_ADMIN |
| /api/orders/**, /api/order-items/** | ROLE_USER и ROLE_ADMIN |
| Остальные | требуют авторизацию |

---

## CSRF защита

Включён `CookieCsrfTokenRepository`.

- Токен хранится в cookie `XSRF-TOKEN`
- Клиент отправляет его в заголовке:

```
X-XSRF-TOKEN: <token>
```

Для удобства работы через Postman CSRF отключён для:

```
/api/auth/**
/api/orders/**
/api/order-items/**
```

Для административных запросов CSRF включён.

---

## Статус выполнения

| Компонент | Состояние |
|----------|-----------|
| CRUD | выполнено |
| Работа с БД + Flyway | выполнено |
| Бизнес-логика | выполнено |
| Регистрация и проверка пароля | выполнено |
| Авторизация по ролям | выполнено |
| Basic Auth | выполнено |
| CSRF | выполнено |
| Postman коллекция | добавлена |

---
