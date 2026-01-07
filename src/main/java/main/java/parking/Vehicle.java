package main.java.parking;

public class Vehicle extends Thread {
    private int id;
    private String plateNumber;
    private Parking parking;
    private Owner owner;

    public Vehicle(int id, String plateNumber, Owner owner, Parking parking) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.owner = owner;
        this.parking = parking;
    }

    public Owner getOwner() {
        return owner;
    }

    @Override
    public void run() {
        // Tu peux ajouter le code pour entrer/sortir plus tard
    }

    @Override
    public String toString() {
        return "Vehicle{id=" + id + ", plateNumber='" + plateNumber + '\'' +
                ", ownerId=" + owner.getOwnerId() + '}';
    }
}
