package main.java.parking;

import java.time.LocalDateTime;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

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
        System.out.println("=== Véhicules dans le parking ===");

        IntStream.range(0, capacity)
                .filter(i -> vehicles[i] != null)
                .forEach(i ->	
                        System.out.println("Place " + i + " : " + vehicles[i])
                );
    }

    public void searchVehicleByOwnerId(int ownerId) {
        boolean found = IntStream.range(0, capacity)
                .filter(i -> vehicles[i] != null)
                .filter(i -> vehicles[i].getOwner().getOwnerId()== ownerId)
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
