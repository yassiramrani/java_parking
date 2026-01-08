package parking;

public class Owner extends Person {

    private int idOwner;
    private Vehicle vehicle;

    public Owner(int idOwner, String name, String cin, String phoneNumber) {
        super(name, cin, phoneNumber);
        this.idOwner = idOwner;
    }

    public int getIdOwner() {
        return idOwner;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public void showInfos() {
        System.out.println("Owner ID: " + idOwner);
        super.showInfos();
        if (vehicle != null) {
            System.out.println("Vehicle: " + vehicle);
        }
    }
}
