package parking;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Parking {

    private final int maxCapacity;
    private final Semaphore availablePlaces;
    private final ParkingHistory parkingHistory;
    private Vehicle[] vehicles;

    public Parking(int capacity) {
        this.maxCapacity = capacity;
        this.availablePlaces = new Semaphore(capacity, true);
        this.parkingHistory = new ParkingHistory();
        this.vehicles = new Vehicle[0];
    }

    public void setVehiclesFromDb() {
        this.vehicles = loadVehiclesFromDb();
    }

    public Vehicle[] getVehicles() {
        return vehicles;
    }

    private Vehicle[] loadVehiclesFromDb() {
        List<Vehicle> list = new ArrayList<>();

        String sql =
                "SELECT v.id_vehicle, v.plate_number, o.id_owner, " +
                        "p.name, p.cin, p.phone_number " +
                        "FROM vehicle v " +
                        "JOIN owner o ON v.id_owner = o.id_owner " +
                        "JOIN person p ON o.id_owner = p.id_person";

        try (
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/parking_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                        "parking_user",
                        "password123"
                );
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Owner owner = new Owner(
                        rs.getInt("id_owner"),
                        rs.getString("name"),
                        rs.getString("cin"),
                        rs.getString("phone_number")
                );

                Vehicle vehicle = new Vehicle(
                        rs.getInt("id_vehicle"),
                        rs.getString("plate_number"),
                        owner,
                        this
                );

                list.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list.toArray(new Vehicle[0]);
    }

    public void enter(Vehicle v) throws InterruptedException {
        if (!availablePlaces.tryAcquire()) {
            System.out.println("Parking full. Vehicle " + v.getId() + " WAITING...");
            availablePlaces.acquire();
        }

        synchronized (this) {
            System.out.println(
                    "Vehicle " + v.getId() +
                            " ENTERED. Available places: " +
                            availablePlaces.availablePermits()
            );
            parkingHistory.setparkinghistory(v, LocalDateTime.now());
        }
    }

    public void leave(Vehicle v) {
        availablePlaces.release();

        synchronized (this) {
            System.out.println(
                    "Vehicle " + v.getId() +
                            " LEFT. Available places: " +
                            availablePlaces.availablePermits()
            );
        }
    }

    public void showVehicleList() {
        System.out.println("=== Vehicles loaded from DB ===");
        for (Vehicle v : vehicles) {
            System.out.println(v);
        }
    }

    public void searchVehicleByOwnerId(int ownerId) {
        boolean found = false;
        for (Vehicle v : vehicles) {
            if (v.getOwner().getIdOwner() == ownerId) {
                System.out.println("Vehicle found: " + v);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No vehicle found for owner " + ownerId);
        }
    }
}
