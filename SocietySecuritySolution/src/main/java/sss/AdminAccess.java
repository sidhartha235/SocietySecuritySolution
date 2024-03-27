package sss;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminAccess {
	
	private int buildingID;
	private String buildingName;
	private String location;
	private String superEmail;
	
	public AdminAccess(int buildingID, String buildingName, String location, String superEmail) {
		this.buildingID = buildingID;
		this.buildingName = buildingName;
		this.location = location;
		this.superEmail = superEmail;
	}
	
	private static Properties getConnectionData() {
        
    	Properties props = new Properties();

        String fileName = "/home/sidhartha/eclipse-workspace/SocietySecuritySolution/src/main/java/db.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (IOException ioe) {
            Logger lgr = Logger.getLogger(AdminAccess.class.getName());
            lgr.log(Level.SEVERE, ioe.getMessage(), ioe);
        }

        return props;
    }
	
	
	public static List<AdminAccess> getBuildings(){
		
		List<AdminAccess> buildings = new ArrayList<AdminAccess>();
		Properties props = getConnectionData();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch(ClassNotFoundException cnfe) {
			System.out.println(cnfe);
		}
		
		String url = props.getProperty("db.url");
		String user = props.getProperty("db.user");
		String password = props.getProperty("db.password");
		
		try(Connection conn = DriverManager.getConnection(url, user, password)) {
			
			String template = "SELECT BuildingID, BuildingName, Location, SupervisorEmail FROM Building";
			PreparedStatement inserter = conn.prepareStatement(template);
			ResultSet rs = inserter.executeQuery();
			
			while(rs.next()) {
				buildings.add(new AdminAccess(rs.getInt("BuildingID"), rs.getString("BuildingName"), rs.getString("Location"), rs.getString("SupervisorEmail")));
			}
			
		} catch(SQLException sqle) {
			Logger lgr = Logger.getLogger(AdminAccess.class.getName());
			lgr.log(Level.SEVERE, sqle.getMessage(), sqle);
		}
		
		return buildings;
		
	}
	
	
	public int getBuildingID() {
	    return this.buildingID;
	}

	public String getBuildingName() {
	    return this.buildingName;
	}

	public String getLocation() {
	    return this.location;
	}

	public String getSuperEmail() {
	    return this.superEmail;
	}
	
}
