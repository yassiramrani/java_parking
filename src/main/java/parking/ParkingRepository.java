package parking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingRepository {

    public List<Vehicle> loadVehiclesFromDb(Parking parking) {
        List<Vehicle> list = new ArrayList<>();

        String sql = "SELECT v.id_vehicle, v.plate_number, o.id_owner, " +
                "p.name, p.cin, p.phone_number " +
                "FROM vehicle v " +
                "JOIN owner o ON v.id_owner = o.id_owner " +
                "JOIN person p ON o.id_owner = p.id_person";

        try (
                Connection conn = DriverManager.getConnection(
                        DatabaseConfig.URL,
                        DatabaseConfig.USER,
                        DatabaseConfig.PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Owner owner = new Owner(
                        rs.getInt("id_owner"),
                        rs.getString("name"),
                        rs.getString("cin"),
                        rs.getString("phone_number"));

                Vehicle vehicle = new Vehicle(
                        rs.getInt("id_vehicle"),
                        rs.getString("plate_number"),
                        owner,
                        parking);
                owner.setVehicle(vehicle); // Assure le lien bidirectionnel si nécessaire
                list.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void addVehicle(Vehicle vehicle) {
        String insertPersonSql = "INSERT INTO person (name, cin, phone_number) VALUES (?, ?, ?)";
        String insertOwnerSql = "INSERT INTO owner (id_owner) VALUES (?)";
        String insertVehicleSql = "INSERT INTO vehicle (plate_number, id_owner) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER,
                DatabaseConfig.PASSWORD)) {
            conn.setAutoCommit(false);

            try {
                // Insertion de la Personne (Détails du propriétaire)
                int personId = -1;
                try (PreparedStatement stmt = conn.prepareStatement(insertPersonSql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, vehicle.getOwner().name);
                    stmt.setString(2, vehicle.getOwner().cin);
                    stmt.setString(3, vehicle.getOwner().phoneNumber);
                    stmt.executeUpdate();
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            personId = rs.getInt(1);
                        }
                    }
                }

                if (personId != -1) {
                    // Insertion du Propriétaire
                    try (PreparedStatement stmt = conn.prepareStatement(insertOwnerSql)) {
                        stmt.setInt(1, personId);
                        stmt.executeUpdate();
                    }

                    // Insertion du Véhicule
                    try (PreparedStatement stmt = conn.prepareStatement(insertVehicleSql)) {
                        stmt.setString(1, vehicle.getPlateNumber());
                        stmt.setInt(2, personId);
                        stmt.executeUpdate();
                    }
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateVehicle(int ownerId, String newPlate) {
        String sql = "UPDATE vehicle SET plate_number = ? WHERE id_owner = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER,
                DatabaseConfig.PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPlate);
            stmt.setInt(2, ownerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
