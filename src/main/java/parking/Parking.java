package parking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Parking {

    private final int maxCapacity;
    private final Semaphore availablePlaces;
    private final ParkingHistory parkingHistory;
    private Vehicle[] vehicles;

    private java.util.function.Consumer<String> logger;

    public Parking(int capacity) {
        this.maxCapacity = capacity;
        this.availablePlaces = new Semaphore(capacity, true);
        this.parkingHistory = new ParkingHistory();
        this.vehicles = new Vehicle[0];
    }

    public void setLogger(java.util.function.Consumer<String> logger) {
        this.logger = logger;
    }

    private void log(String message) {
        System.out.println(message);
        if (logger != null) {
            logger.accept(message);
        }
    }

    public void setVehiclesFromDb() {
        ParkingRepository repository = new ParkingRepository();
        List<Vehicle> vehicleList = repository.loadVehiclesFromDb(this);
        this.vehicles = vehicleList.toArray(new Vehicle[0]);
    }

    public Vehicle[] getVehicles() {
        return vehicles;
    }

    public ParkingHistory getParkingHistory() {
        return parkingHistory;
    }

    public int getAvailableSpots() {
        return availablePlaces.availablePermits();
    }

    public boolean enter(Vehicle v) throws InterruptedException {
        // Vérification si le véhicule est enregistré
        ParkingRepository repo = new ParkingRepository();
        if (!repo.isVehicleRegistered(v.getPlateNumber())) {
            log("ACCÈS REFUSÉ. Le véhicule " + v.getPlateNumber() + " n'est pas enregistré.");
            return false;
        }

        if (!availablePlaces.tryAcquire()) {
            log("Parking complet. Le véhicule " + v.getId() + " ATTEND...");
            availablePlaces.acquire();
        }

        synchronized (this) {
            log(
                    "Véhicule " + v.getId() + " (" + v.getPlateNumber() + ")" +
                            " ENTRÉ. Places disponibles : " +
                            availablePlaces.availablePermits());
            parkingHistory.setparkinghistory(v, LocalDateTime.now());
            // Persist to DB
            try {
                repo.logHistory(v, LocalDateTime.now());
            } catch (Exception e) {
                log("Erreur lors de la sauvegarde de l'historique en base : " + e.getMessage());
            }
        }
        return true;
    }

    public void leave(Vehicle v) {
        availablePlaces.release();

        synchronized (this) {
            log(
                    "Véhicule " + v.getId() +
                            " SORTI. Places disponibles : " +
                            availablePlaces.availablePermits());
        }
    }

    public void showVehicleList() {
        System.out.println("=== Vehicles loaded from DB ===");
        java.util.Arrays.stream(vehicles)
                .forEach(System.out::println);
    }

    public void searchVehicleByOwnerId(int ownerId) {
        boolean found = java.util.Arrays.stream(vehicles)
                .filter(v -> v.getOwner().getIdOwner() == ownerId)
                .peek(v -> System.out.println("Vehicle found: " + v))
                .count() > 0;

        if (!found) {
            System.out.println("No vehicle found for owner " + ownerId);
        }
    }
}
