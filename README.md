

Spring Boot REST API для интернет-магазина

## Технологии
- Java 17
- Spring Boot 3.x
- Maven
- Lombok
- Validation

## Сущности
- **Product** - Товар
- **Category** - Категория товара
- **Customer** - Покупатель
- **Order** - Заказ
- **OrderItem** - Позиция заказа


### Products
- `POST /api/products` - Создать товар
- `GET /api/products` - Получить все товары
- `GET /api/products/{id}` - Получить товар по ID
- `PUT /api/products/{id}` - Обновить товар
- `DELETE /api/products/{id}` - Удалить товар

### Categories
- `POST /api/categories` - Создать категорию
- `GET /api/categories` - Получить все категории
- `GET /api/categories/{id}` - Получить категорию по ID
- `PUT /api/categories/{id}` - Обновить категорию
- `DELETE /api/categories/{id}` - Удалить категорию

### Customers
- `POST /api/customers` - Создать покупателя
- `GET /api/customers` - Получить всех покупателей
- `GET /api/customers/{id}` - Получить покупателя по ID
- `PUT /api/customers/{id}` - Обновить покупателя
- `DELETE /api/customers/{id}` - Удалить покупателя

### Orders
- `POST /api/orders` - Создать заказ
- `GET /api/orders` - Получить все заказы
- `GET /api/orders/{id}` - Получить заказ по ID
- `PUT /api/orders/{id}` - Обновить заказ
- `PATCH /api/orders/{id}/status` - Изменить статус заказа
- `DELETE /api/orders/{id}` - Удалить заказ

### Order Items
- `GET /api/order-items/order/{orderId}` - Получить позиции заказа


## 🚀 Запуск приложения
```
./mvnw spring-boot:run
