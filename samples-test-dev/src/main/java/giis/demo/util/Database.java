package giis.demo.util;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbutils.DbUtils;

/**
 * Encapsula los datos de acceso JDBC, lectura de la configuracion
 * y scripts de base de datos para creacion y carga.
 */
public class Database extends DbUtil {
	//Localizacion de ficheros de configuracion y carga de bases de datos
	private static final String APP_PROPERTIES = "src/main/resources/application.properties";
	private static final String SQL_SCHEMA = "src/main/resources/schema.sql";
	private static final String SQL_LOAD = "src/main/resources/data.sql";
	//parametros de la base de datos leidos de application.properties (base de datos local sin usuario/password)
	private String driver;
	private String url;
	private static boolean databaseCreated=false;

	/**
	 * Crea una instancia, leyendo los parametros de driver y url de application.properties
	 */
	public Database() {
		Properties prop=new Properties();
		try (FileInputStream fs=new FileInputStream(APP_PROPERTIES)) {
			prop.load(fs);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		driver=prop.getProperty("datasource.driver");
		url=prop.getProperty("datasource.url");
		if (driver==null || url==null)
			throw new ApplicationException("Configuracion de driver y/o url no encontrada en application.properties");
		DbUtils.loadDriver(driver);
	}
	public String getUrl() {
		return url;
	}
	/** 
	 * Creacion de una base de datos limpia a partir del script schema.sql en src/main/properties
	 * (si onlyOnce=true solo ejecutara el script la primera vez
	 */
	public void createDatabase(boolean onlyOnce) {
		//actua como singleton si onlyOnce=true: solo la primera vez que se instancia para mejorar rendimiento en pruebas
		if (!databaseCreated || !onlyOnce) { 
			executeScript(SQL_SCHEMA);
			databaseCreated=true; //NOSONAR
		}
	}
	/** 
	 * Carga de datos iniciales a partir del script data.sql en src/main/properties
	 * (si onlyOnce=true solo ejecutara el script la primera vez
	 */
	public void loadDatabase() {
		executeScript(SQL_LOAD);
	}
	
	
	/**
     * Ejecuta una consulta SQL y devuelve los resultados como una lista de filas,
     * donde cada fila es una lista de objetos correspondientes a cada columna.
     * 
     * @param sql La consulta SQL a ejecutar.
     * @return Lista de filas con los datos de la consulta.
     */
    public List<List<Object>> executeQuery(String sql) {
        List<List<Object>> rows = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                List<Object> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                rows.add(row);
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(stmt);
            DbUtils.closeQuietly(conn);
        }
        return rows;
    }
	
}