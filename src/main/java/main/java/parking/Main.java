package main.java.parking;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class Main {
    public static void main(String[] args) throws InterruptedException {
        Parking parking = new Parking(3);

        Owner owner1 = new Owner(1, "Alice", "CIN001", "0600000001");
        Owner owner2 = new Owner(2, "Bob", "CIN002", "0600000002");

        Vehicle v1 = new Vehicle("AA-111-AA", owner1);
        Vehicle v2 = new Vehicle("BB-222-BB", owner1);
        Vehicle v3 = new Vehicle("CC-333-CC", owner2);
        Vehicle v4 = new Vehicle("DD-444-DD", owner2);

        owner1.addVehicle(v1);
        owner1.addVehicle(v2);
        owner2.addVehicle(v3);
        owner2.addVehicle(v4);

        ExecutorService pool = Executors.newFixedThreadPool(4);
        try {
            pool.submit(new VehicleSimulationTask(v1, parking));
            pool.submit(new VehicleSimulationTask(v2, parking));
            pool.submit(new VehicleSimulationTask(v3, parking));
            pool.submit(new VehicleSimulationTask(v4, parking));
        } finally {
            pool.shutdown();
            pool.awaitTermination(10, TimeUnit.SECONDS);
        }

        System.out.println("Capacity: " + parking.getCapacity());
        System.out.println("Available spots: " + parking.getAvailableSpotsCount());
        System.out.println("Tickets logged: " + parking.getHistory().getLogs().size());
    }
}
