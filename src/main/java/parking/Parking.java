package parking;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.sql.DriverManager;
import java.util.stream.IntStream;

public class Parking {

    private final int maxCapacity;
    private final Semaphore availablePlaces;
    private final ParkingHistory parkingHistory;
    private  Vehicle[] vehicles;

    public Parking(int capacity) {
        this.maxCapacity = capacity;
        this.availablePlaces = new Semaphore(capacity, true);
        this.parkingHistory = new ParkingHistory();
        this.vehicles = new Vehicle[0]; // empty at start
    }


    private Vehicle[] loadVehiclesFromDb() {
        List<Vehicle> vehicles = new ArrayList<>();

        String sql =
                "SELECT " +
                        "v.id_vehicle, " +
                        "v.plate_number, " +
                        "o.id_owner, " +
                        "p.name, " +
                        "p.cin, " +
                        "p.phone_number, " +
                        "pk.id_parking, " +
                        "pk.capacity " +
                        "FROM vehicle v " +
                        "JOIN owner o ON v.id_owner = o.id_owner " +
                        "JOIN person p ON o.id_owner = p.id_person " +
                        "LEFT JOIN parking pk ON v.id_parking = pk.id_parking";

        try (
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/parking_db" +
                                "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
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

                Parking parking = this; // use the current parking instance

                Vehicle vehicle = new Vehicle(
                        rs.getInt("id_vehicle"),
                        rs.getString("plate_number"),
                        owner,
                        parking
                );

                vehicles.add(vehicle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicles.toArray(new Vehicle[0]);
    }




    public void enter(Vehicle v) throws InterruptedException {


        if (!availablePlaces.tryAcquire()) {
            System.out.println("Parking full. Vehicle " + v.getId() + " is WAITING...");
            availablePlaces.acquire(); // blocks here
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
        availablePlaces.release(); // free place FIRST

        synchronized (this) {
            System.out.println(
                    "Vehicle " + v.getId() +
                            " LEFT. Available places: " +
                            availablePlaces.availablePermits()
            );
        }
    }
    public void showVehicleList() {
        System.out.println("=== Véhicules dans le parking ===");

        IntStream.range(0, maxCapacity)
                .filter(i -> vehicles[i] != null)
                .forEach(i ->
                        System.out.println("Place " + i + " : " + vehicles[i])
                );
    }
    public void searchVehicleByOwnerId(int ownerId) {
        boolean found = IntStream.range(0, maxCapacity)
                .filter(i -> vehicles[i] != null)
                .filter(i -> vehicles[i].getOwner().getIdOwner()== ownerId)
                .mapToObj(i -> {
                    System.out.println("Véhicule trouvé à la place " + i + " : " + vehicles[i]);
                    return i;
                })
                .count() > 0;

        if (!found) {
            System.out.println("Aucun véhicule trouvé pour le propriétaire " + ownerId);
        }
    }



}

