package main.java.parking;

public class VehicleSimulationTask implements Runnable {
    private final Vehicle vehicle;
    private final Parking parking;

    public VehicleSimulationTask(Vehicle vehicle, Parking parking) {
        if (vehicle == null) {
            throw new IllegalArgumentException("vehicle must not be null");
        }
        if (parking == null) {
            throw new IllegalArgumentException("parking must not be null");
        }
        this.vehicle = vehicle;
        this.parking = parking;
    }

    @Override
    public void run() {
        ParkingTicket ticket = parking.enter(vehicle);
        try {
            // Simulated stay\.
        } finally {
            parking.leave(ticket);
        }
    }
}
