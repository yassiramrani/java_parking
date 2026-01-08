package parking;

public class Main {
    public static void main(String[] args) {
        Parking parking = new Parking(5);
        for (int i = 0; i < 10; i++) {
            Vehicle vehicle = new Vehicle(i, parking);
            vehicle.start();
        }
    }
}
