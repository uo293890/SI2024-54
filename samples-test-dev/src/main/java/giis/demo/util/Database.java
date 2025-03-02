package giis.demo.util;

import java.sql.*;
import java.util.List;

import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.*;

import giis.demo.tkrun.ActivityDTO;

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

    public <T> java.util.List<T> queryForList(Class<T> type, String sql, Object... params) {
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