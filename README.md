# Java Parking Management System 🚗

## 📌 Project Overview

This project is a **Java-based Parking Management System** designed to simulate the behavior of a real parking lot using **object-oriented programming (OOP)** concepts and **multithreading**.

Each vehicle is represented by a thread, and parking availability is controlled using synchronization tools such as **Semaphore**. The system also keeps track of parking history and allows administrative operations.

---

## 🎯 Main Objectives

- Apply **OOP principles** (inheritance, encapsulation, abstraction)
- Practice **UML modeling**
- Use **Java concurrency (Thread & Semaphore)**
- Manage relationships between entities (Owner, Vehicle, Parking, Admin)
- Maintain a **parking history**

---

## 🧱 Project Structure

```
java_parking/
├── src/
│ └── main/
│ └── java/
│ └── main/java/parking/
│ ├── Admin.java
│ ├── Owner.java
│ ├── Parking.java
│ ├── ParkingHistory.java
│ ├── Person.java
│ ├── ParkingRepository.java
│ ├── ParkingGUI.java
│ └── Vehicle.java
├── pom.xml
└── README.md
```

---

## ▶️ How to Run the Project (Tutoriel)

### 1️⃣ Prerequisites
Ensure you have the following installed:
- **Java JDK 8** or higher
- **Maven**
- **MySQL Database**

### 2️⃣ Database Setup
Create a MySQL database naming it `parking_db` (or as per `DatabaseConfig.java`) and run the following SQL script to create necessary tables:

```sql
CREATE DATABASE parking_db;
USE parking_db;

CREATE TABLE person (
    id_person INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    cin VARCHAR(20),
    phone_number VARCHAR(20)
);

CREATE TABLE owner (
    id_owner INT PRIMARY KEY,
    FOREIGN KEY (id_owner) REFERENCES person(id_person)
);

CREATE TABLE vehicle (
    id_vehicle INT AUTO_INCREMENT PRIMARY KEY,
    plate_number VARCHAR(20),
    id_owner INT,
    FOREIGN KEY (id_owner) REFERENCES owner(id_owner)
);
```

### 3️⃣ Configuration
Update `src/main/java/parking/DatabaseConfig.java` with your database credentials:
```java
public class DatabaseConfig {
    public static final String URL = "jdbc:mysql://localhost:3306/parking_db";
    public static final String USER = "root";
    public static final String PASSWORD = "your_password";
}
```

### 4️⃣ Execution
You can run the application using Maven.

**To run the GUI (Recommended):**
```bash
mvn clean compile exec:java -Dexec.mainClass="parking.ParkingGUI"
```

**To run the Console Simulation:**
```bash
mvn clean compile exec:java -Dexec.mainClass="parking.Main"
```

---

## 🧩 Class Descriptions

### 🔹 Person (Abstract Base Class)
- **Attributes**: `name`, `cin`, `phoneNumber`
- **Method**: `showInfos()`

### 🔹 Owner (extends Person)
- **Attributes**: `idOwner`, `vehicle`
- **Method**: `showInfos()`

### 🔹 Admin
- **Attributes**: `idAdmin`, `parking`
- **Methods**: `changeVehicle`, `displayParkingHistory`

### 🔹 Vehicle (implements Runnable)
- **Attributes**: `id`, `plateNumber`, `parking`
- **Method**: `run()` – simulates vehicle behavior

### 🔹 Parking
- **Attributes**: `capacity`, `availablePlaces`, `parkingHistory`
- **Methods**: `enter`, `leave`, `setVehiclesFromDb`

### 🔹 ParkingGUI
- **Purpose**: Provides a graphical interface for simulation and management.

---

## 👨‍💻 Author

Developed as part of a **Java OOP & Concurrency learning project**.
