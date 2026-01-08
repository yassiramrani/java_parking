package parking;

import java.time.LocalDateTime;
import java.util.concurrent.Semaphore;

public class Parking {

    private final int maxCapacity;
    private final Semaphore availablePlaces;
    private final ParkingHistory parkingHistory;

    public Parking(int capacity) {
        this.maxCapacity = capacity;
        this.availablePlaces = new Semaphore(capacity, true); // FAIR semaphore
        this.parkingHistory = new ParkingHistory();
    }

    public void enter(Vehicle v) throws InterruptedException {
        System.out.println("Vehicle " + v.getId() + " is trying to enter...");

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
}
