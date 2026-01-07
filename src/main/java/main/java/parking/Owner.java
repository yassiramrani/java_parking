package main.java.parking;

public class Owner {
    private int idOwner;
    private Vehicle vehicle;

    public Owner(int idOwner, Vehicle vehicle) {
        this.idOwner = idOwner;
        this.vehicle = vehicle;
    }

    public int getOwnerId() {
        return idOwner;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void showInfos() {
        System.out.println("Owner ID: " + idOwner);
        if (vehicle != null) {
            System.out.println("Vehicle: " + vehicle);
        } else {
            System.out.println("No vehicle assigned");
        }
    }
}
