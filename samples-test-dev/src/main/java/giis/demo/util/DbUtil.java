package giis.demo.util;

import java.sql.Connection;
import java.sql.SQLException;

public class DbUtil {
    public static Connection getConnection() throws SQLException {
        return Database.getConnection();
    }

    public static String getUrl() {
        return "jdbc:sqlite:sports-competition-management.db"; // URL de conexión
    }
}
