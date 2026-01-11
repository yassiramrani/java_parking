package parking;

public class Vehicle implements Runnable {
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

    public String getPlateNumber() {
        return plateNumber;
    }

    public long getId() {
        return this.id;
    }

    public Owner getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", plateNumber='" + plateNumber + '\'' +
                '}';
    }

    @Override
    public void run() {
        try {
            parking.enter(this);
            Thread.sleep(2000);
            parking.leave(this);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
