package main.java.parking;


public class Vehicle extends Thread {
    private int id;
    private String plateNumber;
    private Parking parking;

    public Vehicle(int id, Parking parking) {
        this.id = id;
        this.parking = parking;
    }

    @Override
    public void run() {
    }
}
