package main.java.parking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParkingHistory {
    private final List<ParkingTicket> logs;

    public ParkingHistory() {
        this.logs = new ArrayList<>();
    }

    public synchronized void logEntry(ParkingTicket t) {
        if (t == null) {
            throw new IllegalArgumentException("ticket must not be null");
        }
        logs.add(t);
    }

    public synchronized void logExit(ParkingTicket t) {
        if (t == null) {
            throw new IllegalArgumentException("ticket must not be null");
        }
        if (t.getExitTime() == null) {
            throw new IllegalStateException("ticket is not closed");
        }
        if (!logs.contains(t)) {
            logs.add(t);
        }
    }

    public synchronized List<ParkingTicket> getLogs() {
        return Collections.unmodifiableList(new ArrayList<>(logs));
    }
}
