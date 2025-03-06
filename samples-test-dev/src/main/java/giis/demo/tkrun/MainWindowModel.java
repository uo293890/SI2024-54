package giis.demo.tkrun;

import giis.demo.util.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MainWindowModel {
    private Database db = new Database();

    public List<String> getTableNames() {
        String sql = "SELECT name FROM sqlite_master WHERE type='table'";
        return db.queryForList(String.class, sql);
    }

    public ResultSet getTableData(String tableName) throws SQLException {
        return db.executeQuery("SELECT * FROM " + tableName);
    }

    public List<Integer> getActiveActivityIds() {
        String sql = "SELECT activity_id FROM Activity WHERE status = 'Active'";
        return db.queryForList(Integer.class, sql);
    }
    
    public List<Integer> getActiveSponsorIds() {
        String sql = "SELECT sponsor_id FROM Sponsor WHERE sponsorship_status = 'Active'";
        return db.queryForList(Integer.class, sql);
    }
    
    
}