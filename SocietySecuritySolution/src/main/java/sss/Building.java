package sss;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Building {
	
	private int buildingID;
	private String buildingName;
	private String buildingLocation;
	
	public Building(int buildingID, String buildingName, String buildingLocation) {
		this.buildingID = buildingID;
		this.buildingName = buildingName;
		this.buildingLocation = buildingLocation;
	}
	
	private static Properties getConnectionData() {
        
    	Properties props = new Properties();

        String fileName = "/home/sidhartha/eclipse-workspace/SocietySecuritySolution/src/main/java/db.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (IOException ioe) {
            Logger lgr = Logger.getLogger(Building.class.getName());
            lgr.log(Level.SEVERE, ioe.getMessage(), ioe);
        }

        return props;
    }
	
	public static List<Building> getBuildings(){
		
		List<Building> buildings = new ArrayList<Building>();
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
			
			String template = "SELECT BuildingID, BuildingName, Location FROM Building";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(template);
			
			while(rs.next()) {
				buildings.add(new Building(rs.getInt("BuildingID"), rs.getString("BuildingName"), rs.getString("Location")));
			}
			
		} catch(SQLException sqle) {
			Logger lgr = Logger.getLogger(Building.class.getName());
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
	
	public String getBuildingLocation() {
		return this.buildingLocation;
	}
	
}
