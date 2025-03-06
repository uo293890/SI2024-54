package giis.demo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
	public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:event.db");
    }

	public String getUrl() {
        return "jdbc:sqlite:event.db";
    }
}