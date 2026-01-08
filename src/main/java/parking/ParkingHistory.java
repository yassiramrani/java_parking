package parking;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ParkingHistory {
    private Map<LocalDateTime, Object> parkingMap;

    public ParkingHistory() {
        this.parkingMap = new HashMap<>();
    }
    public void setparkinghistory(Vehicle v, LocalDateTime date){
        parkingMap.put(date, v);
    }

}
