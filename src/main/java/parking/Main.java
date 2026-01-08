package parking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
       /* String url =
                "jdbc:mysql://localhost:3306/parking_db"
                        + "?useSSL=false"
                        + "&serverTimezone=UTC"
                        + "&allowPublicKeyRetrieval=true";
        String user = "parking_user";
        String password = "password123";


        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println("Connected to MySQL!");
        conn.close();
        */
        Parking parking = new Parking(5);
        for (int i = 0; i < 10; i++) {
            /*Vehicle vehicle = new Vehicle(i, parking);
            vehicle.start();*/
        }
    }
    }


