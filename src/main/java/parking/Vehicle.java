package parking;


public class Vehicle extends Thread {
    private int id;
    private String plateNumber;
    private Parking parking;

    public Vehicle(int id, Parking parking) {
        this.id = id;
        this.parking = parking;
    }

    @Override
    public long getId() {
        return this.id;
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
