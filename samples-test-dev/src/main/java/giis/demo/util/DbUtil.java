package giis.demo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase base para utilidades de acceso a la base de datos.
 */
public class DbUtil {
    /**
     * Obtiene una conexión a la base de datos.
     * @return Conexión a la base de datos.
     * @throws SQLException Si ocurre un error al obtener la conexión.
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:sports-competition-management.db");
    }

    /**
     * Obtiene la URL de conexión a la base de datos.
     * @return URL de conexión.
     */
    public String getUrl() {
        return "jdbc:sqlite:sports-competition-management.db";
    }
}