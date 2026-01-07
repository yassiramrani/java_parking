package main.java.parking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Parking {
    private final int capacity;
    private final Semaphore availablePlaces;
    private final List<ParkingSpot> spots;
    private final ParkingHistory history;

    private final Object lock = new Object();
    private final AtomicInteger ticketSeq = new AtomicInteger(1);

    public Parking(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be > 0");
        }
        this.capacity = capacity;
        this.availablePlaces = new Semaphore(capacity, true);
        this.history = new ParkingHistory();
        List<ParkingSpot> list = new ArrayList<>(capacity);
        for (int i = 1; i <= capacity; i++) {
            list.add(new ParkingSpot(i));
        }
        this.spots = Collections.unmodifiableList(list);
    }

    public ParkingTicket enter(Vehicle v) {
        if (v == null) {
            throw new IllegalArgumentException("vehicle must not be null");
        }

        availablePlaces.acquireUninterruptibly();

        synchronized (lock) {
            ParkingSpot freeSpot = null;
            for (ParkingSpot s : spots) {
                if (!s.isOccupied()) {
                    freeSpot = s;
                    break;
                }
            }

            if (freeSpot == null) {
                availablePlaces.release();
                throw new IllegalStateException("No free spot found despite semaphore permit");
            }

            freeSpot.park(v);

            ParkingTicket ticket = new ParkingTicket(
                    ticketSeq.getAndIncrement(),
                    v,
                    freeSpot
            );
            history.logEntry(ticket);
            return ticket;
        }
    }

    public void leave(ParkingTicket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("ticket must not be null");
        }

        synchronized (lock) {
            ParkingSpot spot = ticket.getSpot();
            if (spot == null) {
                throw new IllegalArgumentException("ticket has no spot");
            }

            Vehicle freed = spot.free();
            if (freed == null) {
                throw new IllegalStateException("Spot was already free");
            }

            ticket.closeTicket();
            history.logExit(ticket);
        }

        availablePlaces.release();
    }

    public int getAvailableSpotsCount() {
        return availablePlaces.availablePermits();
    }

    public int getCapacity() {
        return capacity;
    }

    public List<ParkingSpot> getSpots() {
        return spots;
    }

    public ParkingHistory getHistory() {
        return history;
    }
}
