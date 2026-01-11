package parking;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingHistory {
    private Map<LocalDateTime, Object> parkingMap;

    public ParkingHistory() {
        this.parkingMap = new ConcurrentHashMap<>();
    }

    public void setparkinghistory(Vehicle v, LocalDateTime date) {
        parkingMap.put(date, v);
    }

    public Map<LocalDateTime, Object> getParkingMap() {
        return parkingMap;
    }
}
