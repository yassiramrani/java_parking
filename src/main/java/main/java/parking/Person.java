package main.java.parking;

public class Person {
    protected final String name;
    protected final String cin;
    protected final String phone;

    public Person(String name, String cin, String phone) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (cin == null || cin.isBlank()) {
            throw new IllegalArgumentException("cin must not be blank");
        }
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("phone must not be blank");
        }
        this.name = name;
        this.cin = cin;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getCin() {
        return cin;
    }

    public String getPhone() {
        return phone;
    }
}
