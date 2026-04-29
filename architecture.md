# 🏗️ System Architecture: Notification System

This document provides a technical deep-dive into the architectural design of the Notification System, explaining how various components interact to deliver messages across multiple channels via an event-driven approach.

---

## 🛰️ High-Level Component Diagram

The system architecture is centered around an **Event-Driven Core** with **Keycloak** managing identity and **MongoDB** handling persistence.

```mermaid
graph TD
    User((User)) -->|Interacts with| FE[notifications-app :5173]
    
    FE -->|POST /realms/dev/token| KC[Keycloak :8000]
    FE -->|POST /notifications/send| NS[notification-service :8080]
    FE -->|GET /notifications/my| NS

    KC -->|issues JWT| FE
    NS -->|validates JWT with| KC
    US[user-service :8084] -->|validates JWT with| KC

    NS -->|Persists Data| Mongo[(MongoDB :27017)]

    subgraph "Messaging Layer (Kafka)"
        NS -->|Publish NotificationMessage| EmailTopic[email-notification.v1]
        NS -->|Publish NotificationMessage| SMSTopic[sms-notification.v1]
        NS -->|Publish NotificationMessage| PushTopic[push-notification.v1]
    end

    subgraph "Notification Consumers"
        EmailTopic -->|Consume| EmailService[email-notification :8081]
        SMSTopic -->|Consume| SMSService[sms-notification :8082]
        PushTopic -->|Consume| PushService[push-notification :8083]
    end

    EmailService -->|Query Users| US
    SMSService -->|Query Users| US
    PushService -->|Query Users| US

    subgraph "External Providers"
        EmailService -->|SMTP| ExtEmail[Email Service Provider]
        SMSService -->|REST/SMPP| ExtSMS[SMS Gateway]
        PushService -->|FCM/APNS| ExtPush[Push Notification Service]
    end
```

---

## 🔄 Notification Flow

```mermaid
sequenceDiagram
    participant U as User
    participant FE as notifications-app :5173
    participant KC as Keycloak :8000
    participant NS as notification-service :8080
    participant K as Kafka Topics
    participant CH as Channel Consumers
    participant US as user-service :8084

    U->>FE: Enters Credentials
    FE->>KC: POST /token (password grant)
    KC-->>FE: JWT (with preferred_username)
    FE->>FE: Store JWT in AuthContext

    U->>FE: Submits Notification Form
    FE->>NS: POST /notifications/send (Bearer JWT)
    NS->>NS: Validate JWT with Keycloak
    NS->>NS: Select Strategy (by Channel)
    NS->>NS: Persist to MongoDB
    NS->>K: Publish NotificationMessage {message, category, userName}
    NS-->>FE: 202 Accepted
    FE-->>U: Show Success Toast

    par Asynchronous Delivery
        K->>CH: Consume NotificationMessage
        CH->>KC: POST /token (client_credentials)
        KC-->>CH: Service JWT
        CH->>US: GET /users?channel=...&category=... (Service JWT)
        US-->>CH: List<UserDto>
        CH->>CH: Deliver to External Providers
    end
```

---

## 🔄 User Query Flow

```mermaid
sequenceDiagram
    participant C as Client
    participant US as user-service :8084

    C->>US: GET /users?channel=EMAIL&category=SPORTS
    US-->>C: 200 OK [UserDto...]
```

---

## 🛠️ Design Patterns

### 1. Strategy Pattern
The `NotificationService` acts as the context and selects the appropriate `NotificationStrategy` implementation based on the requested `Channel`.

- **Interface**: `NotificationStrategy`
- **Implementations**: `EmailNotificationStrategy`, `SmsNotificationStrategy`, `PushNotificationStrategy`
- Each strategy builds a `NotificationMessage` and publishes it to the corresponding Kafka topic.

### 2. Event-Driven (Producer-Consumer)
The `notification-service` is a **Kafka Producer**. The channel modules (`email-notification`, `sms-notification`, `push-notification`) act as **Kafka Consumers**, allowing fully asynchronous and decoupled delivery.

### 3. Configuration Properties
Kafka topic names are externalized via `@ConfigurationProperties` in `KafkaTopicProperties`, preventing hardcoded strings in strategy implementations.

---

## 💻 Frontend Architecture

The **notifications-app** is built using a modern React stack, designed to interact seamlessly with the event-driven backend.

### 1. State Management & Auth
- **React Context API**: Used for global state management, specifically for handling authentication status and user profile data.
- **JWT Handling**: Tokens are stored in memory/context and included in the `Authorization` header for all API requests.

### 2. Networking
- **Axios Interceptors**: Global interceptors handle token injection and respond to `401 Unauthorized` errors by redirecting users to the login page, ensuring a secure session lifecycle.

### 3. UI/UX
- **Material UI (MUI)**: Provides a consistent, responsive design system.
- **React Router**: Manages client-side routing, including protected routes that require authentication.

---

## 📨 Kafka Message Schema (`NotificationMessage`)

| Field | Type | Description |
| :--- | :--- | :--- |
| `message` | `String` | The notification body. |
| `category` | `NotificationCategory` | `SPORTS`, `FINANCE`, or `MOVIES`. |
| `userName` | `String` | The username of the authenticated publisher (from JWT `sub` claim). |

---

## 🔐 Security Architecture

The system implements a **Centralized Identity Provider** model using **Keycloak**:

1.  **Authorization Server (Keycloak)**: Issues JWTs via `password`, `authorization_code`, or `client_credentials` grants.
2.  **Resource Servers**: Both `notification-service` and `user-service` are Resource Servers that validate incoming JWTs against Keycloak's public keys.
3.  **Service-to-Service Auth**: Consumers use the `client_credentials` grant to obtain a system token, allowing them to query the `user-service` securely.
4.  **JWT Claims**: The system relies on the `preferred_username` claim to identify the subject.

---

## 📊 Deployment & Ports

| Module | Port | Responsibility |
| :--- | :--- | :--- |
| `keycloak` | `8000` | Central Identity Provider (OIDC/OAuth2) |
| `kafka` | `9092` | Event Streaming Platform |
| `mongodb` | `27017` | Notification Persistence |
| `notifications-app` | `5173` | Frontend Web Application (React/Vite) |
| `notification-service` | `8080` | Notification API, Strategy Selection, Kafka Producer |
| `user-service` | `8084` | User Management API (Subscriber Data) |
| `email-notification` | `8081` | Email Consumer & Delivery |
| `sms-notification` | `8082` | SMS Consumer & Delivery |
| `push-notification` | `8083` | Push Consumer & Delivery |

---

## 📈 Observability

- **Metrics**: Spring Boot Actuator enabled on all microservices for health checks and performance monitoring.
- **Persistence**: `notification-service` stores all accepted requests in **MongoDB** for audit trails and history.
- **Logging**: Log4j2 with structured logging, including MDC context for tracing request flow through Kafka.
