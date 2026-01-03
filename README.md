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
│ └── Vehicle.java
├── pom.xml
└── README.md

---

## 📊 UML Class Diagram

![UML Diagram](./uml-diagram.png)

> 🔹 **Note:**  
> Place the UML image in the root of the repository and name it `uml-diagram.png`.

---

## 🧩 Class Descriptions

### 🔹 Person (Abstract Base Class)
- Attributes:
  - `name`
  - `cin`
  - `phoneNumber`
- Method:
  - `showInfos()`

---

### 🔹 Owner (extends Person)
- Attributes:
  - `idOwner`
  - `vehicle`
- Method:
  - `showInfos()`

---

### 🔹 Admin
- Attributes:
  - `idAdmin`
  - `parking`
- Methods:
  - `changeVehicle(Owner owner)`
  - `displayParkingHistory(Parking parking)`

---

### 🔹 Vehicle (extends Thread)
- Attributes:
  - `id`
  - `plateNumber`
  - `parking`
- Method:
  - `run()` – simulates vehicle behavior

---

### 🔹 Parking
- Attributes:
  - `capacity`
  - `availablePlaces : Semaphore`
  - `vehicles : Vehicle[]`
  - `parkingHistory`
- Methods:
  - `enter(Vehicle v, LocalDateTime date)`
  - `leave(int placeIndex)`
  - `showVehicleList()`
  - `searchVehicleByOwnerId(int ownerId)`

---

### 🔹 ParkingHistory
- Attribute:
  - `parkingMap : Map<LocalDateTime, Owner>`
- Purpose:
  - Stores parking activity history

---

## ⚙️ Technologies Used

- Java
- Maven
- Java Concurrency (Thread, Semaphore)
- UML
- Git & GitHub

---

## ▶️ How to Run the Project

### Compile

### Run Tests

---

## 🚀 Future Improvements

- Add a console or GUI interface
- Persist parking history to a database
- Support multiple parkings
- Add pricing and billing system
- Improve thread scheduling and logging

---

## 👨‍💻 Author

Developed as part of a **Java OOP & Concurrency learning project**.

---

## 📜 License

This project is for **educational purposes only**.
