# KLU Inventory & Shipping Microservices

A ready-to-import Maven multi-module Spring Boot project using an API Gateway.

## Project structure

- `inventory-service` — REST API on port **8081**
- `shipping-service` — REST API on port **8082**
- `api-gateway` — Spring Cloud Gateway on port **8080**
- Base Java package: `com.klu`

The gateway forwards `/api/inventory/**` to the inventory service and `/api/shipping/**` to the shipping service.

## Requirements

- JDK 17
- Maven 3.8+
- Internet connection for the first Maven dependency download

## Import into Spring Tool Suite / Eclipse

1. Extract this ZIP file.
2. Open **File → Import → Maven → Existing Maven Projects**.
3. Select the extracted `klu-microservices` folder (the folder containing the parent `pom.xml`).
4. Select all three modules and click **Finish**.
5. Wait for Maven to download dependencies.

## Run

Run each main application class as **Spring Boot App**, in this order:

1. `InventoryServiceApplication` — port 8081
2. `ShippingServiceApplication` — port 8082
3. `ApiGatewayApplication` — port 8080

Or, from the extracted project root, build everything:

```bash
mvn clean install
```

Then run each module separately:

```bash
mvn -pl inventory-service spring-boot:run
mvn -pl shipping-service spring-boot:run
mvn -pl api-gateway spring-boot:run
```

Run each command in a separate terminal.

## Test through the API Gateway

All requests below go through `http://localhost:8080`.

### Inventory

List inventory:

```http
GET http://localhost:8080/api/inventory
```

Get item:

```http
GET http://localhost:8080/api/inventory/1
```

Add item:

```http
POST http://localhost:8080/api/inventory
Content-Type: application/json

{
  "name": "Mouse",
  "quantity": 15
}
```

Update item:

```http
PUT http://localhost:8080/api/inventory/1
Content-Type: application/json

{
  "name": "Laptop",
  "quantity": 20
}
```

Delete item:

```http
DELETE http://localhost:8080/api/inventory/1
```

### Shipping

List shipments:

```http
GET http://localhost:8080/api/shipping
```

Create shipment:

```http
POST http://localhost:8080/api/shipping
Content-Type: application/json

{
  "orderId": "ORD-1001",
  "destination": "Vijayawada"
}
```

Update shipment status:

```http
PATCH http://localhost:8080/api/shipping/1/status?status=SHIPPED
```

## Notes

- This starter project uses in-memory storage, so data resets when a service restarts.
- No database, service discovery, authentication, or authorization is configured.
- The gateway uses fixed localhost service URLs, which is suitable for running locally.
