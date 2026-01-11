package parking;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== PARKING SYSTEM START ===");

        // 1️⃣ Création du parking avec une capacité donnée
        Parking parking = new Parking(3);

        // 2️⃣ Chargement des véhicules depuis la base de données
        parking.setVehiclesFromDb();

        // 3️⃣ Affichage des véhicules chargés
        parking.showVehicleList();

        System.out.println("\n=== VÉHICULES ENTRANT EN SILMULATION ===");

        // 4️⃣ Récupération de la liste des véhicules
        Vehicle[] vehicles = parking.getVehicles();

        // 5️⃣ Simulation d'entrée concurrente (plusieurs threads)
        for (Vehicle vehicle : vehicles) {
            new Thread(vehicle, "Vehicle-" + vehicle.getId()).start();
        }

        // 6️⃣ Attente avant la fin du programme
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n=== PARKING SYSTEM END ===");
    }
}
