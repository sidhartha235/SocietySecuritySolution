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

public class NormalVisitor {
	
	private String visitorMobile;
	private String visitorName;
	private Date requestDate;
	
	public NormalVisitor(String visitorMobile, String visitorName, Date requestDate) {
		this.visitorMobile = visitorMobile;
		this.visitorName = visitorName;
		this.requestDate = requestDate;
	}
	
	private static Properties getConnectionData() {
        
    	Properties props = new Properties();

        String fileName = "/home/sidhartha/eclipse-workspace/SocietySecuritySolution/src/main/java/db.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (IOException ioe) {
            Logger lgr = Logger.getLogger(NormalVisitor.class.getName());
            lgr.log(Level.SEVERE, ioe.getMessage(), ioe);
        }

        return props;
    }
	
	public static List<NormalVisitor> getNormalVisitors(int residentID){
		
		List<NormalVisitor> visitors = new ArrayList<NormalVisitor>();
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
			
			String template = "SELECT Flat_No, BuildingID FROM Resident WHERE ResidentID = ?";
			PreparedStatement inserter = conn.prepareStatement(template);
			inserter.setInt(1, residentID);
			ResultSet rs = inserter.executeQuery();
			rs.next();
			
			int flatNumber = rs.getInt("Flat_No");
			int buildingID = rs.getInt("BuildingID");
			
			template = "SELECT Mobile, Name, Date FROM NormalVisitor WHERE Flat_No = ? AND BuildingID = ? AND ApprovalStatus = 'Pending'";
			inserter = conn.prepareStatement(template);
			inserter.setInt(1, flatNumber);
			inserter.setInt(2, buildingID);
			rs = inserter.executeQuery();
			
			while(rs.next()) {
				visitors.add(new NormalVisitor(rs.getString("Mobile"), rs.getString("Name"), rs.getDate("Date")));
			}
			
		} catch(SQLException sqle) {
			Logger lgr = Logger.getLogger(NormalVisitor.class.getName());
			lgr.log(Level.SEVERE, sqle.getMessage(), sqle);
		}
		
		return visitors;
		
	}
	
	public String getVisitorMobile() {
		return this.visitorMobile;
	}
	
	public String getVisitorName() {
		return this.visitorName;
	}
	
	public String getRequestDate() {
		return (this.requestDate).toString();
	}
	
}
