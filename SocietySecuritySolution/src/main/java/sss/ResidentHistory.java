package sss;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResidentHistory {
	
	private String visitorName;
	private String visitorMobile;
	private Date visitDate;
	private Time checkIn;
	private Time checkOut;
	
	private Date issueDate;
	private int amount;
	private String modeOfPay;
	
	private String staffRole;
	private String staffName;
	private String workingHours;
	private String staffContact;
	
	public ResidentHistory(String visitorName, String visitorMobile, Date visitDate, Time checkIn, Time checkOut) {
		this.visitorName = visitorName;
		this.visitorMobile = visitorMobile;
		this.visitDate = visitDate;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
	}
	
	public ResidentHistory(Date issueDate, int amount, String modeOfPay) {
		this.issueDate = issueDate;
		this.amount = amount;
		this.modeOfPay = modeOfPay;
	}
	
	public ResidentHistory(String staffRole, String staffName, String workingHours, String staffContact) {
		this.staffRole = staffRole;
		this.staffName = staffName;
		this.workingHours = workingHours;
		this.staffContact = staffContact;
	}
	
	private static Properties getConnectionData() {
        
    	Properties props = new Properties();

        String fileName = "/home/sidhartha/eclipse-workspace/SocietySecuritySolution/src/main/java/db.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (IOException ioe) {
            Logger lgr = Logger.getLogger(ResidentHistory.class.getName());
            lgr.log(Level.SEVERE, ioe.getMessage(), ioe);
        }

        return props;
    }
	
	
	public static List<ResidentHistory> getVisitorsHistory(int residentID){
		
		List<ResidentHistory> visitedVisitors = new ArrayList<ResidentHistory>();
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
			
			template = "SELECT Name, Mobile, Date, CheckIn, CheckOut "
					+ "FROM RegularVisitor, RegularVisitor_Record "
					+ "WHERE RegularVisitor.SecurityCode = RegularVisitor_Record.SecurityCode AND Flat_No = ? AND BuildingID = ? AND CheckOut != 'NULL'";
			inserter = conn.prepareStatement(template);
			inserter.setInt(1, flatNumber);
			inserter.setInt(2, buildingID);
			rs = inserter.executeQuery();
			
			while(rs.next()) {
				visitedVisitors.add(new ResidentHistory(rs.getString("Name"), rs.getString("Mobile"), rs.getDate("Date"), rs.getTime("CheckIn"), rs.getTime("CheckOut")));
			}
			
		} catch(SQLException sqle) {
			Logger lgr = Logger.getLogger(ResidentHistory.class.getName());
			lgr.log(Level.SEVERE, sqle.getMessage(), sqle);
		}
		
		return visitedVisitors;
		
	}
	
	public static List<ResidentHistory> getMaintenanceHistory(int residentID){
		
		List<ResidentHistory> paidMaintenances = new ArrayList<ResidentHistory>();
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
			
			template = "SELECT Date, Amount, ModeOfPayment FROM Maintenance WHERE Flat_No = ? AND BuildingID = ? AND Status = 'Paid'";
			inserter = conn.prepareStatement(template);
			inserter.setInt(1, flatNumber);
			inserter.setInt(2, buildingID);
			rs = inserter.executeQuery();
			
			while(rs.next()) {
				paidMaintenances.add(new ResidentHistory(rs.getDate("Date"), rs.getInt("Amount"), rs.getString("ModeOfPayment")));
			}
			
		} catch(SQLException sqle) {
			Logger lgr = Logger.getLogger(ResidentHistory.class.getName());
			lgr.log(Level.SEVERE, sqle.getMessage(), sqle);
		}
		
		return paidMaintenances;
		
	}
	
	public static List<ResidentHistory> getStaffDetails(int residentID){
		
		List<ResidentHistory> staffDetails = new ArrayList<ResidentHistory>();
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
			
			int buildingID = rs.getInt("BuildingID");
			
			template = "SELECT Role, Name, WorkingHours, EmergencyContact FROM Staff WHERE BuildingID = ?";
			inserter = conn.prepareStatement(template);
			inserter.setInt(1, buildingID);
			rs = inserter.executeQuery();
			
			while(rs.next()) {
				staffDetails.add(new ResidentHistory(rs.getString("Role"), rs.getString("Name"), rs.getString("WorkingHours"), rs.getString("EmergencyContact")));
			}
			
		} catch(SQLException sqle) {
			Logger lgr = Logger.getLogger(ResidentHistory.class.getName());
			lgr.log(Level.SEVERE, sqle.getMessage(), sqle);
		}
		
		return staffDetails;
		
	}
	
	
	public String getVisitorName() {
		return this.visitorName;
	}
	
	public String getVisitorMobile() {
		return this.visitorMobile;
	}
	
	public String getVisitDate() {
		return (this.visitDate).toString();
	}
	
	public String getCheckIn() {
		return (this.checkIn).toString();
	}
	
	public String getCheckOut() {
		return (this.checkOut).toString();
	}
	
	public String getIssueDate() {
		return (this.issueDate).toString();
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	public String getModeOfPay() {
		return this.modeOfPay;
	}
	
	public String getStaffRole() {
		return this.staffRole;
	}
	
	public String getStaffName() {
		return this.staffName;
	}
	
	public String getWorkingHours() {
		return this.workingHours;
	}
	
	public String getStaffContact() {
		return this.staffContact;
	}
	
}
