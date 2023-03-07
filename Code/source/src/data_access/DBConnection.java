package data_access;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Instaura direttamente la connessione al DB dal costruttore
    static public void createConnection(String path){
        try (Connection conn = DriverManager.getConnection(path)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    static public void disconnect(){
        //Controlla che prima ci sia la connessione
    }

}
