package sss;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SupervisorAccess {
	
	private int flatNumber;
	private int floorNumber;
	private String flatType;
	
	private int residentID;
	private String residentName;
	private int residentFlat;
	private String residentMobile;
	private String residentEmail;
	
	private String nVisitorName;
	private String nVisitorMobile;
	private String nVisitorStatus;
	private int nVisitorFlat;
	private Date nVisitorDate;
	
	private String rVisitorCode;
	private String rVisitorName;
	private String rVisitorMobile;
	
	public SupervisorAccess(int flatNumber, int floorNumber, String flatType) {
		this.flatNumber = flatNumber;
		this.floorNumber = floorNumber;
		this.flatType = flatType;
	}

	public SupervisorAccess(int residentID, String residentName, int residentFlat, String residentMobile, String residentEmail) {
		this.residentID = residentID;
		this.residentName = residentName;
		this.residentFlat = residentFlat;
		this.residentMobile = residentMobile;
		this.residentEmail = residentEmail;
	}

	public SupervisorAccess(String nVisitorName, String nVisitorMobile, String nVisitorStatus, int nVisitorFlat, Date nVisitorDate) {
		this.nVisitorName = nVisitorName;
		this.nVisitorMobile = nVisitorMobile;
		this.nVisitorStatus = nVisitorStatus;
		this.nVisitorFlat = nVisitorFlat;
		this.nVisitorDate = nVisitorDate;
	}
	
	public SupervisorAccess(String rVisitorCode, String rVisitorName, String rVisitorMobile) {
		this.rVisitorCode = rVisitorCode;
		this.rVisitorName = rVisitorName;
		this.rVisitorMobile = rVisitorMobile;
	}
	
	private static Properties getConnectionData() {
        
    	Properties props = new Properties();

        String fileName = "/home/sidhartha/eclipse-workspace/SocietySecuritySolution/src/main/java/db.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (IOException ioe) {
            Logger lgr = Logger.getLogger(SupervisorAccess.class.getName());
            lgr.log(Level.SEVERE, ioe.getMessage(), ioe);
        }

        return props;
    }
	
	
	public static List<SupervisorAccess> getFlats(int buildingID){
		
		List<SupervisorAccess> flats = new ArrayList<SupervisorAccess>();
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
			
			String template = "SELECT Flat_No, Floor_No, Flat_Type FROM Flat WHERE BuildingID = ?";
			PreparedStatement inserter = conn.prepareStatement(template);
			inserter.setInt(1, buildingID);
			ResultSet rs = inserter.executeQuery();
			
			while(rs.next()) {
				flats.add(new SupervisorAccess(rs.getInt("Flat_No"), rs.getInt("Floor_No"), rs.getString("Flat_Type")));
			}
			
		} catch(SQLException sqle) {
			Logger lgr = Logger.getLogger(SupervisorAccess.class.getName());
			lgr.log(Level.SEVERE, sqle.getMessage(), sqle);
		}
		
		return flats;
		
	}
	
	public static List<SupervisorAccess> getResidents(int buildingID){
		
		List<SupervisorAccess> residents = new ArrayList<SupervisorAccess>();
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
			
			String template = "SELECT ResidentID, RName, Flat_No, RMobile, ResidentEmail FROM Resident WHERE BuildingID = ?";
			PreparedStatement inserter = conn.prepareStatement(template);
			inserter.setInt(1, buildingID);
			ResultSet rs = inserter.executeQuery();
			
			while(rs.next()) {
				residents.add(new SupervisorAccess(rs.getInt("ResidentID"), rs.getString("RName"), rs.getInt("Flat_No"), rs.getString("RMobile"), rs.getString("ResidentEmail")));
			}
			
		} catch(SQLException sqle) {
			Logger lgr = Logger.getLogger(SupervisorAccess.class.getName());
			lgr.log(Level.SEVERE, sqle.getMessage(), sqle);
		}
		
		return residents;
		
	}
	
	public static List<SupervisorAccess> getNormalVisitors(int buildingID){
		
		List<SupervisorAccess> normalVisitors = new ArrayList<SupervisorAccess>();
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
			
			String template = "SELECT Name, Mobile, ApprovalStatus, Flat_No, Date FROM NormalVisitor WHERE BuildingID = ?";
			PreparedStatement inserter = conn.prepareStatement(template);
			inserter.setInt(1, buildingID);
			ResultSet rs = inserter.executeQuery();
			
			while(rs.next()) {
				normalVisitors.add(new SupervisorAccess(rs.getString("Name"), rs.getString("Mobile"), rs.getString("ApprovalStatus"), rs.getInt("Flat_No"), rs.getDate("Date")));
			}
			
		} catch(SQLException sqle) {
			Logger lgr = Logger.getLogger(SupervisorAccess.class.getName());
			lgr.log(Level.SEVERE, sqle.getMessage(), sqle);
		}
		
		return normalVisitors;
		
	}
	
	
	public int getFlatNumber() {
	    return this.flatNumber;
	}

	public int getFloorNumber() {
	    return this.floorNumber;
	}

	public String getFlatType() {
	    return this.flatType;
	}

	public int getResidentID() {
	    return this.residentID;
	}

	public String getResidentName() {
	    return this.residentName;
	}

	public int getResidentFlat() {
	    return this.residentFlat;
	}

	public String getResidentMobile() {
	    return this.residentMobile;
	}

	public String getResidentEmail() {
	    return this.residentEmail;
	}

	public String getNVisitorName() {
	    return this.nVisitorName;
	}

	public String getNVisitorMobile() {
	    return this.nVisitorMobile;
	}

	public String getNVisitorStatus() {
	    return this.nVisitorStatus;
	}

	public int getNVisitorFlat() {
	    return this.nVisitorFlat;
	}

	public Date getNVisitorDate() {
	    return this.nVisitorDate;
	}

	public String getRVisitorCode() {
	    return this.rVisitorCode;
	}

	public String getRVisitorName() {
	    return this.rVisitorName;
	}

	public String getRVisitorMobile() {
	    return this.rVisitorMobile;
	}
	
}
