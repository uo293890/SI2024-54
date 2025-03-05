package giis.demo.tkrun;

import giis.demo.util.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class MainWindowModel {
    private Database db = new Database();

    public Vector<String> getAllTableNames() {
        String sql = "SELECT name FROM sqlite_master WHERE type='table'";
        return new Vector<>(db.queryForList(String.class, sql));
    }

    public ResultSet getTableData(String tableName) throws SQLException {
        String sql = "SELECT * FROM " + tableName;
        return db.executeQuery(sql);
    }
}