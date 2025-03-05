package giis.demo.util;

import java.sql.*;
import java.util.List;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.*;
import giis.demo.tkrun.ActivityDTO;

public class Database {
    private Connection getConnection() throws SQLException {
        String dbPath = "C:/Users/Alex/Documents/sisinfo/SI2024-54/samples-test-dev/sports-competition-management.db";
        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }

    public void createDatabase(boolean clean) {
        System.out.println("Creating database...");
        executeScript("src/main/resources/schema.sql", clean);
        System.out.println("Database created successfully!");
    }

    public void loadDatabase() {
        System.out.println("Loading initial data...");
        executeScript("src/main/resources/data.sql", false);
        System.out.println("Initial data loaded successfully!");
    }

    private void executeScript(String path, boolean clean) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            System.out.println("Executing script: " + path);
            if (clean) stmt.executeUpdate("DROP TABLE IF EXISTS financial_reports");
            stmt.executeUpdate(new String(java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get(path))));
        } catch (Exception e) {
            System.err.println("Error executing script: " + e.getMessage());
            throw new RuntimeException("Error executing script SQL", e);
        }
    }

    public <T> List<T> queryForList(Class<T> type, String sql, Object... params) {
        try (Connection conn = getConnection()) {
            return new QueryRunner().query(conn, sql, new BeanListHandler<>(type), params);
        } catch (SQLException e) {
            throw new RuntimeException("Error en consulta", e);
        }
    }

    public int executeUpdate(String sql, Object... params) {
        try (Connection conn = getConnection()) {
            return new QueryRunner().update(conn, sql, params);
        } catch (SQLException e) {
            throw new RuntimeException("Error en actualización", e);
        }
    }

    public List<ActivityDTO> executeQueryPojo(Class<ActivityDTO> class1, String sql, String startDate, String endDate,
            String status) {
        try (Connection conn = getConnection()) {
            QueryRunner runner = new QueryRunner();
            BeanListHandler<ActivityDTO> handler = new BeanListHandler<>(ActivityDTO.class);
            return runner.query(conn, sql, handler, startDate, endDate, status);
        } catch (SQLException e) {
            throw new RuntimeException("Error en consulta", e);
        }
    }
}