package main.java.parking;

import java.time.LocalDateTime;

public class ParkingTicket {
    private final int ticketId;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private final Vehicle vehicle;
    private final ParkingSpot spot;

    public ParkingTicket(int ticketId, Vehicle vehicle, ParkingSpot spot) {
        if (ticketId <= 0) {
            throw new IllegalArgumentException("ticketId must be > 0");
        }
        if (vehicle == null) {
            throw new IllegalArgumentException("vehicle must not be null");
        }
        if (spot == null) {
            throw new IllegalArgumentException("spot must not be null");
        }
        this.ticketId = ticketId;
        this.entryTime = LocalDateTime.now();
        this.vehicle = vehicle;
        this.spot = spot;
    }

    public void closeTicket() {
        if (this.exitTime == null) {
            this.exitTime = LocalDateTime.now();
        }
    }

    public int getTicketId() {
        return ticketId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getSpot() {
        return spot;
    }
}
