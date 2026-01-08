package parking;


public class Vehicle extends Thread {
    private int id;
    private String plateNumber;
    private Parking parking;
    private Owner owner;



    @Override
    public long getId() {
        return this.id;
    }
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
        try {
            parking.enter(this);
            sleep(2000);
            parking.leave(this);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
