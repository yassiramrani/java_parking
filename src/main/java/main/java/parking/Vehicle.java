package main.java.parking;

public class Vehicle {
    private final String licensePlate;
    private final Owner owner;

    public Vehicle(String licensePlate, Owner owner) {
        if (licensePlate == null || licensePlate.isBlank()) {
            throw new IllegalArgumentException("licensePlate must not be blank");
        }
        if (owner == null) {
            throw new IllegalArgumentException("owner must not be null");
        }
        this.licensePlate = licensePlate;
        this.owner = owner;
    }

    public String getDetails() {
        return "Vehicle{plate=" + licensePlate + ", owner=" + owner.getName() + "}";
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public Owner getOwner() {
        return owner;
    }
}
