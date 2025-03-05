package giis.demo.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.*;

public class Database {
    
	private Connection getConnection() throws SQLException {
        // Ruta absoluta a la base de datos
        String dbPath = "C:/Users/Alex/Documents/sisinfo/SI2024-54/samples-test-dev/sports-competition-management.db"; 
        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }

    public void createDatabase(boolean clean) {
        System.out.println("Ejecutando script de esquema...");
        executeScript("src/main/resources/schema.sql", clean);
    }

    public void loadDatabase() {
        System.out.println("Ejecutando script de datos...");
        executeScript("src/main/resources/data.sql", false);
    }

    private void executeScript(String path, boolean clean) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            if (clean) {
                System.out.println("Eliminando tablas existentes...");
                stmt.executeUpdate("DROP TABLE IF EXISTS Movements");
                stmt.executeUpdate("DROP TABLE IF EXISTS Invoice");
                stmt.executeUpdate("DROP TABLE IF EXISTS Agreement");
                stmt.executeUpdate("DROP TABLE IF EXISTS Sponsor");
                stmt.executeUpdate("DROP TABLE IF EXISTS EstimatedIncomeExpenses");
                stmt.executeUpdate("DROP TABLE IF EXISTS Edition");
                stmt.executeUpdate("DROP TABLE IF EXISTS Event");
            }
            
            // Ejecutar cada sentencia del script
            String sql = new String(Files.readAllBytes(Paths.get(path)));
            for (String query : sql.split(";")) {
                if (!query.trim().isEmpty()) {
                    System.out.println("Ejecutando: " + query);
                    stmt.executeUpdate(query);
                }
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Error ejecutando script: " + e.getMessage(), e);
        }
    }

    public <T> List<T> queryForList(Class<T> type, String sql, Object... params) {
        try (Connection conn = getConnection()) {
            return new QueryRunner().query(conn, sql, new ColumnListHandler<>(), params);
        } catch (SQLException e) {
            throw new RuntimeException("Error en consulta", e);
        }
    }

    public int executeUpdate(String sql, Object... params) {
        try (Connection conn = getConnection()) {
            return new QueryRunner().update(conn, sql, params);
        } catch (SQLException e) {
            System.err.println("Error en actualización: " + e.getMessage());
            throw new RuntimeException("Error en actualización: " + e.getMessage(), e);
        }
    }

    public <T> List<T> executeQueryPojo(Class<T> type, String sql, Object... params) {
        try (Connection conn = getConnection()) {
            return new QueryRunner().query(
                conn, 
                sql, 
                new BeanListHandler<>(type), 
                params
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error en consulta: " + e.getMessage(), e);
        }
    }
    
    public ResultSet executeQuery(String sql) throws SQLException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        return stmt.executeQuery(sql);
    }
}