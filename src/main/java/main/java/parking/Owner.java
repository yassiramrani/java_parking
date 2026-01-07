package main.java.parking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Owner extends Person {
    private final int id;
    private final List<Vehicle> vehicles;

    public Owner(int id, String name, String cin, String phone) {
        super(name, cin, phone);
        if (id <= 0) {
            throw new IllegalArgumentException("id must be > 0");
        }
        this.id = id;
        this.vehicles = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public List<Vehicle> getVehicles() {
        return Collections.unmodifiableList(vehicles);
    }

    public void addVehicle(Vehicle v) {
        if (v == null) {
            throw new IllegalArgumentException("vehicle must not be null");
        }
        vehicles.add(v);
    }
}
