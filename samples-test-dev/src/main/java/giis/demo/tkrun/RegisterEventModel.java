package giis.demo.tkrun;

import java.util.List;
import giis.demo.util.Database;

public class RegisterEventModel {
	private Database db = new Database();
	
	private static final String SQL_GET_TYPES =
			"SELECT type_id AS typeId, type_name AS typeName FROM Type";
	
	/**
     * Fetches all types from the database.
     */
    public List<RegisterEventDTO> getAllTypes() {
        return db.executeQueryPojo(RegisterEventDTO.class, SQL_GET_TYPES);
    }
    
    public void registerEvent(RegisterEventDTO dto) {
    	String sql = "INSERT INTO Event (type_id, event_name, event_inidate, " +
                "event_enddate, event_location, event_status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
    	
    	String sqlL = "INSERT INTO LevelOfSponsorship (event_id, level_name, " +
    	        "level_minamount) VALUES (?, ?, ?)";
        
        db.executeUpdate(sql, 
 	           dto.getTypeId(), 
 	           dto.getEventName(), 
 	           dto.getStartDate(),
 	           dto.getEndDate(), 
 	           dto.getLocation(), 
 	           dto.getStatus());
        
        for (RegisterEventDTO.SponsorshipLevelDTO level : dto.getSponsorshipLevels()) {
        	db.executeUpdate(sqlL, 
      	           level.getEventId(),
      	           level.getLevelName(),
      	           level.getMinAmount());        }
    }
    
}