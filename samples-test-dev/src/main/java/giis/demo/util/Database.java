package giis.demo.util;

import java.sql.*;
import java.util.List;


public class Database {
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:sports-competition-management.db");
    }

    public void createDatabase(boolean clean) {
        executeScript("src/main/resources/schema.sql", clean);
    }

    public void loadDatabase() {
        executeScript("src/main/resources/data.sql", false);
    }

    private void executeScript(String path, boolean clean) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            if (clean) stmt.executeUpdate("DROP TABLE IF EXISTS invoices");
            stmt.executeUpdate(new String(java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get(path))));
        } catch (Exception e) {
            throw new RuntimeException("Error ejecutando script SQL", e);
        }
    }
}