package giis.demo.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

public class Database extends DbUtil {
    private static final String APP_PROPERTIES = "src/main/resources/application.properties";
    private String driver;
    private String url;
    private static boolean databaseCreated = false;

    public Database() {
        Properties prop = new Properties();
        try (FileInputStream fs = new FileInputStream(APP_PROPERTIES)) {
            prop.load(fs);
            driver = prop.getProperty("datasource.driver");
            url = prop.getProperty("datasource.url");
            if (driver == null || url == null)
                throw new ApplicationException("Configuración de driver y/o url no encontrada en application.properties");
            DbUtils.loadDriver(driver);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    private void executeScript(String path) {  // Eliminar el parámetro 'clean'
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Añadir limpieza automática de tablas
            System.out.println("Eliminando tablas existentes...");
            stmt.executeUpdate("DROP TABLE IF EXISTS Movement");
            stmt.executeUpdate("DROP TABLE IF EXISTS Invoice");
            stmt.executeUpdate("DROP TABLE IF EXISTS Agreement");
            stmt.executeUpdate("DROP TABLE IF EXISTS Sponsor");
            stmt.executeUpdate("DROP TABLE IF EXISTS Otherie");
            stmt.executeUpdate("DROP TABLE IF EXISTS Edition");
            stmt.executeUpdate("DROP TABLE IF EXISTS Event");
            
            // Mejorar división de queries
            String sqlContent = new String(Files.readAllBytes(Paths.get(path)));
            String[] queries = sqlContent.split(";(\\s|\\r\\n|\\n)*"); // Regex mejorado
            
            for (String query : queries) {
                if (!query.trim().isEmpty()) {
                    System.out.println("Ejecutando: " + query.trim());
                    stmt.executeUpdate(query.trim());
                }
            }
            
        } catch (Exception e) {
            throw new ApplicationException("Error ejecutando script: " + e.getMessage(), e);
        }
    }

    // Corregir métodos que llaman a executeScript
    public void createDatabase(boolean onlyOnce) {
        if (!databaseCreated || !onlyOnce) {
            executeScript("src/main/resources/schema.sql"); // Ahora coincide con la firma
            databaseCreated = true;
        }
    }

    public void loadDatabase() {
        executeScript("src/main/resources/data.sql"); // Ahora usa el mismo método
    }
    
    public <T> List<T> executeQueryPojo(Class<T> type, String sql, Object... params) {
        try (Connection conn = getConnection()) {
            return new QueryRunner().query(conn, sql, new BeanListHandler<>(type), params);
        } catch (SQLException e) {
            throw new ApplicationException("Error en executeQueryPojo: " + e.getMessage(), e);
        }
    }

    // Método para actualizaciones (INSERT/UPDATE/DELETE)
    public int executeUpdate(String sql, Object... params) {
        try (Connection conn = getConnection()) {
            return new QueryRunner().update(conn, sql, params);
        } catch (SQLException e) {
            throw new ApplicationException("Error en executeUpdate: " + e.getMessage(), e);
        }
    }

    // Método para consultas que devuelven listas de valores simples
    public <T> List<T> queryForList(Class<T> type, String sql, Object... params) {
        try (Connection conn = getConnection()) {
            return new QueryRunner().query(conn, sql, new ColumnListHandler<T>(), params);
        } catch (SQLException e) {
            throw new ApplicationException("Error en queryForList: " + e.getMessage(), e);
        }
    }

    // Método para consultas generales (ResultSet)
    public ResultSet executeQuery(String sql) throws SQLException {
        Connection conn = getConnection();
        return conn.createStatement().executeQuery(sql);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }
}