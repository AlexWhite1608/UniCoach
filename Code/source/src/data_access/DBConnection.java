package data_access;

import java.sql.*;

public class DBConnection {

    private static Connection conn;
    private static final String dbPath = "Code/database/unicoachdb.db";

    public static Connection connect() {

        try {
            // Verifica se la connessione esiste già
            if (conn != null && !conn.isClosed()) {
                return conn;
            }

            // Carica il driver JDBC per SQLite
            Class.forName("org.sqlite.JDBC");

            // Apre la connessione al database SQLite
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            System.out.println("Connected to database");

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC driver not found");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("Failed to connect to database");
            e.printStackTrace();

        }
        return conn;
    }

    public static Connection disconnect() {

        try {
            // Verifica se la connessione esiste e chiude la connessione
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Disconnected from database");
                return conn;
            }

        } catch (SQLException e) {
            System.out.println("Failed to disconnect from database");
            e.printStackTrace();
        }

        return conn;
    }
}

