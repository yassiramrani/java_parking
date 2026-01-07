package main.java.parking;

public class ParkingSpot {
    private final int spotId;
    private boolean isOccupied;
    private Vehicle currentVehicle;

    public ParkingSpot(int spotId) {
        if (spotId <= 0) {
            throw new IllegalArgumentException("spotId must be > 0");
        }
        this.spotId = spotId;
        this.isOccupied = false;
        this.currentVehicle = null;
    }

    public synchronized void park(Vehicle v) {
        if (v == null) {
            throw new IllegalArgumentException("vehicle must not be null");
        }
        if (isOccupied) {
            throw new IllegalStateException("Spot " + spotId + " is already occupied");
        }
        this.currentVehicle = v;
        this.isOccupied = true;
    }

    public synchronized Vehicle free() {
        if (!isOccupied) {
            return null;
        }
        Vehicle v = this.currentVehicle;
        this.currentVehicle = null;
        this.isOccupied = false;
        return v;
    }

    public int getSpotId() {
        return spotId;
    }

    public synchronized boolean isOccupied() {
        return isOccupied;
    }

    public synchronized Vehicle getCurrentVehicle() {
        return currentVehicle;
    }
}
