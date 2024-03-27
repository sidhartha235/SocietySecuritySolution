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

public class Maintenance {
	
	private Date issueDate;
	private int amount;
	
	public Maintenance(Date issueDate, int amount) {
		this.issueDate = issueDate;
		this.amount = amount;
	}
	
	private static Properties getConnectionData() {
        
    	Properties props = new Properties();

        String fileName = "/home/sidhartha/eclipse-workspace/SocietySecuritySolution/src/main/java/db.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (IOException ioe) {
            Logger lgr = Logger.getLogger(Maintenance.class.getName());
            lgr.log(Level.SEVERE, ioe.getMessage(), ioe);
        }

        return props;
    }
	
	public static List<Maintenance> getDues(int residentID){
		
		List<Maintenance> dues = new ArrayList<Maintenance>();
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
			
			template = "SELECT Date, Amount FROM Maintenance WHERE Flat_No = ? AND BuildingID = ? AND Status = 'Due'";
			inserter = conn.prepareStatement(template);
			inserter.setInt(1, flatNumber);
			inserter.setInt(2, buildingID);
			rs = inserter.executeQuery();
			
			while(rs.next()) {
				dues.add(new Maintenance(rs.getDate("Date"), rs.getInt("Amount")));
			}
			
		} catch(SQLException sqle) {
			Logger lgr = Logger.getLogger(Maintenance.class.getName());
			lgr.log(Level.SEVERE, sqle.getMessage(), sqle);
		}
		
		return dues;
		
	}
	
	public String getIssueDate() {
		return (this.issueDate).toString();
	}
	
	public int getAmount() {
		return this.amount;
	}
	
}
