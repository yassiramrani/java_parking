package main.java.parking;

import java.time.LocalDateTime;
import java.util.concurrent.Semaphore;

public class Parking {
    private int capacity;
    private Semaphore availablePlaces;
    private Vehicle[] vehicles;
    private ParkingHistory parkingHistory;

    public Parking(int capacity) {
        this.capacity = capacity;
        this.availablePlaces = new Semaphore(capacity);
        this.vehicles = new Vehicle[capacity];
        this.parkingHistory = new ParkingHistory();
    }

    public int enter(Vehicle v, LocalDateTime date) {
        return 0;
    }

    public void leave(int placeIndex) {
    }

    public void showVehicleList() {
    }

    public void searchVehicleByOwnerId(int ownerId) {
    }
}
