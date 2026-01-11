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

    public int getVehicleCount(int ownerId) {
        String sql = "SELECT COUNT(*) FROM vehicle WHERE id_owner = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER,
                DatabaseConfig.PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ownerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean isVehicleRegistered(String plateNumber) {
        String sql = "SELECT id_vehicle FROM vehicle WHERE plate_number = ?";
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER,
                DatabaseConfig.PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, plateNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addVehicle(Vehicle vehicle) throws Exception {
        // Vérification de la limite de véhicules (Max 3)
        // Note: Ici on vérifie par rapport à l'ID propriétaire.
        // Si c'est un NOUVEAU propriétaire (flux actuel), il a 0 véhicule.
        // Cette logique sert surtout si on ajoute un véhicule à un propriétaire
        // EXISTANT.
        // Dans l'implémentation actuelle de l'interface graphique, on crée souvent un
        // NOUVEAU owner.
        // Mais pour la robustesse:
        if (vehicle.getOwner().getIdOwner() > 0) { // Si ID valide
            int count = getVehicleCount(vehicle.getOwner().getIdOwner());
            if (count >= 3) {
                throw new Exception("Le propriétaire a atteint la limite de 3 véhicules.");
            }
        }

        String insertPersonSql = "INSERT INTO person (name, cin, phone_number) VALUES (?, ?, ?)";
        String insertOwnerSql = "INSERT INTO owner (id_owner) VALUES (?)";
        String insertVehicleSql = "INSERT INTO vehicle (plate_number, id_owner) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER,
                DatabaseConfig.PASSWORD)) {
            conn.setAutoCommit(false);

            try {
                int personId = -1;

                // Si l'owner a déjà un ID valide, on l'utilise directement (cas d'ajout
                // véhicule à owner existant)
                // TO-DO: L'interface actuelle crée toujours un 'new Owner(0, ...)'.
                // Pour respecter la demande strictement, on suppose ici le flux actuel.
                // Si on voulait supporter "Ajouter véhicule à Owner existant", il faudrait
                // changer l'IHM.
                // Ici, on insère toujours une nouvelle Personne/Owner.

                // Insertion de la Personne (Détails du propriétaire)
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
            throw e; // Renvoyer l'exception pour l'IHM
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
