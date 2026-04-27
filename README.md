# 🚀 Notification System

A robust, multi-channel notification system designed to deliver categorized messages to users based on their individual preferences and subscriptions. This project implements a scalable architecture for handling notifications across various channels (SMS, Email, and Push) with a focus on extensibility and auditability.

---

## 📋 System Overview

The system receives messages containing a **Category** and a **Body**. It then evaluates a pre-populated set of users to determine who should receive the message based on two criteria:
1.  **Subscription**: Does the user subscribe to the message's category?
2.  **Channels**: Which communication channels has the user opted into?

### Core Entities

#### **Message Categories**
- 🏅 **Sports**
- 💰 **Finance**
- 🎬 **Movies**

#### **Notification Channels**
- 📱 **SMS**
- 📧 **E-Mail**
- 🔔 **Push Notification**

---

## 🏗️ Architecture

The project is structured as a **Multi-Module Gradle Project** to ensure high cohesion and loose coupling between different notification strategies.

### 🧩 Module Structure
-   `notification-service`: The core orchestrator that receives messages and executes the dispatch strategy.
-   `email-notification`: Dedicated logic for Email delivery.
-   `sms-notification`: Dedicated logic for SMS delivery.
-   `push-notification`: Dedicated logic for Push Notification delivery.

### 🛠️ Design Patterns
-   **Strategy Pattern**: Used to dynamically select the appropriate notification handler based on user preferences.
-   **Independence**: Each channel is managed by its own class/module, ensuring that changes to one delivery method do not impact others.
-   **Audit Logging**: Every notification attempt is persisted with comprehensive metadata (Message Type, Channel, User Data, Timestamp) to verify successful delivery.

---

## 👤 User Model (Mock)

Users are pre-populated in the system with the following data structure:

| Field | Description |
| :--- | :--- |
| **ID** | Unique identifier for the user. |
| **Name** | Full name of the subscriber. |
| **Email** | Valid email address for E-Mail notifications. |
| **Phone Number** | Contact number for SMS notifications. |
| **Subscribed** | List of categories (Sports, Finance, Movies). |
| **Channels** | List of preferred delivery methods (SMS, E-Mail, Push). |

---

## 🚀 Getting Started

### Prerequisites
- Java 21
- Gradle (Wrapper included)

### Build the Project
```bash
./gradlew build
```

### Run the Service
```bash
./gradlew :notification-service:bootRun
```

---

## 📊 Persistence & Verification

To ensure reliability, the system stores all relevant delivery information:
- ✅ **Metadata**: Message type and content.
- ✅ **Delivery Info**: Notification channel used.
- ✅ **User Context**: Specific user data at the time of sending.
- ✅ **Timestamp**: Exact time of the notification attempt.
- ✅ **Status**: Delivery result (for future implementation).

---

---

## 🛠️ Tech Stack
- **Framework**: Spring Boot 4.0.6
- **Language**: Java 21
- **Build Tool**: Gradle (Multi-module)
- **Documentation**: SpringDoc OpenAPI (Swagger)
