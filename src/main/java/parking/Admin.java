package parking;

public class Admin extends Person {
    private int idAdmin;
    private Parking parking;

    public Admin(int idAdmin, String name, String cin, String phoneNumber, Parking parking) {
        super(name, cin, phoneNumber);
        this.idAdmin = idAdmin;
        this.parking = parking;
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public Parking getParking() {
        return parking;
    }

    public void changeVehicle(String oldPlate, String newPlate) {
        ParkingRepository repository = new ParkingRepository();
        repository.updateVehicle(oldPlate, newPlate);

        System.out.println("Admin " + name + " a changé le véhicule " + oldPlate + " vers " + newPlate);
    }

    public void display_parking_history() {
        // Délégation à une logique d'affichage de l'historique.
        // ParkingHistory n'a pas encore de méthode d'affichage, mais on peut y accéder
        // via Parking.
        // Pour l'instant, on affiche un message temporaire.
        System.out.println("Affichage de l'historique du parking...");
        // Dans un scénario réel, on appellerait parking.getParkingHistory().show() ou
        // similaire.
    }
}
