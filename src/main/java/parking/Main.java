package parking;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== PARKING SYSTEM START ===");

        // 1️⃣ Create parking with capacity
        Parking parking = new Parking(3);

        // 2️⃣ Load vehicles from database
        parking.setVehiclesFromDb();

        // 3️⃣ Show loaded vehicles
        parking.showVehicleList();

        System.out.println("\n=== VEHICLES ENTERING CONCURRENTLY ===");

        // 4️⃣ Get vehicles
        Vehicle[] vehicles = parking.getVehicles();

        // 5️⃣ Simulate concurrent entry
        for (Vehicle vehicle : vehicles) {
            new Thread(() -> {
                try {
                    parking.enter(vehicle);

                    // Vehicle stays parked for a while
                    Thread.sleep(2000);

                    parking.leave(vehicle);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "Vehicle-" + vehicle.getId()).start();
        }

        // 6️⃣ Wait before ending program
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n=== PARKING SYSTEM END ===");
    }
}
