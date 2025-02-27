package giis.demo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	private static final String DB_URL = "C:\\Users\\Alex\\Documents\\sisinfo\\SI2024-54\\samples-test-dev.sports-competition-management.db";
    private static Connection connection;

    // Obtiene la conexión a la base de datos
    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }
}
