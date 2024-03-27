import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Time;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/checkInOutRegularVisitor")
public class CheckInOutRegularVisitor extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static Properties getConnectionData() {
        
    	Properties props = new Properties();

        String fileName = "/home/sidhartha/eclipse-workspace/SocietySecuritySolution/src/main/java/db.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (IOException ioe) {
            Logger lgr = Logger.getLogger(CheckInOutRegularVisitor.class.getName());
            lgr.log(Level.SEVERE, ioe.getMessage(), ioe);
        }

        return props;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	HttpSession session = request.getSession(false);
    	if(session == null) {
    		response.sendRedirect("index.jsp?session=expired");
    		return;
    	}
    	
    	Properties props = getConnectionData();
        
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(ClassNotFoundException cnfe) {
        	System.out.println(cnfe);
        }
    	
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        
        String dateString = null;
        Date date = null;
        String securityCodeEntry = null;
        int flatNoEntry = -1;
        String timeString1 = null;
        Time checkIn = null;
        
        String securityCodeExit = null;
        int flatNoExit = -1;
        String timeString2 = null;
        Time checkOut = null;
        
        if((request.getParameter("submit3")) != null && (request.getParameter("submit3")).equals("Check In")) {
	        dateString = request.getParameter("date_E");
	        date = java.sql.Date.valueOf(dateString);
	        securityCodeEntry = request.getParameter("securityCode_E");
	        flatNoEntry = Integer.parseInt(request.getParameter("flatNo_E"));
	        timeString1 = request.getParameter("checkIn") + ":00";
	        checkIn = java.sql.Time.valueOf(timeString1);
        }
        else if((request.getParameter("submit4")).equals("Check Out")) {
	        securityCodeExit = request.getParameter("securityCode_T");
	        flatNoExit = Integer.parseInt(request.getParameter("flatNo_T"));
	        timeString2 = request.getParameter("checkOut") + ":00";
	        checkOut = java.sql.Time.valueOf(timeString2);
        }
        
        String template1 = "INSERT INTO RegularVisitor_Record (SecurityCode, Flat_No, BuildingID, Date, CheckIn) VALUES (?, ?, ?, ?, ?)";
        String template2 = "UPDATE RegularVisitor_Record SET CheckOut = ? WHERE SecurityCode = ? AND Flat_No = ? AND CheckOut IS NULL";
        
        try(Connection conn = DriverManager.getConnection(url, user, password)) {
        	
        	int buildingID = (int)session.getAttribute("building");
        	
        	String flatCheck = "SELECT COUNT(*) FROM Resident WHERE Flat_No = ? AND BuildingID = ?";
        	PreparedStatement inserter = conn.prepareStatement(flatCheck);
        	if(flatNoEntry != -1) {
        		inserter.setInt(1, flatNoEntry);        		
        	}
        	else if(flatNoExit != -1) {
        		inserter.setInt(1, flatNoExit);
        	}
        	inserter.setInt(2, buildingID);
        	ResultSet rs = inserter.executeQuery();
        	rs.next();
        	
        	int count = rs.getInt(1);
        	
        	if(count <= 0) {
        		session.setAttribute("residentExists", "no");
        		response.sendRedirect("supervisor.jsp");
        		return;
        	}
        	else {
	        	synchronized(session) {
	        		if(securityCodeEntry != null) {
	        			String template = "SELECT COUNT(*) FROM RegularVisitor_Record WHERE SecurityCode = ? AND Flat_No = ? AND BuildingID = ? AND CheckOut IS NULL";
	        			inserter = conn.prepareStatement(template);
	        			inserter.setString(1, securityCodeEntry);
	        			inserter.setInt(2, flatNoEntry);
	        			inserter.setInt(3, buildingID);
	        			rs = inserter.executeQuery();
	        			rs.next();
	        			int counter = rs.getInt(1);
	        			if(counter > 0) {
	        				session.setAttribute("regularVisitorEntered", "yes");
	        	        	response.sendRedirect("supervisor.jsp");
	        	        	return;
	        			}
	        			else {
	        				inserter = conn.prepareStatement(template1);
	        				inserter.setString(1, securityCodeEntry);
	        				inserter.setInt(2, flatNoEntry);
	        				inserter.setInt(3, buildingID);
	        				inserter.setDate(4, date);
	        				inserter.setTime(5, checkIn);
	        				inserter.executeUpdate();	        				
	        			}
	        		}
	        		else if(securityCodeExit != null) {
	        			PreparedStatement inserter1 = conn.prepareStatement(template2);
	        			inserter1.setTime(1, checkOut);
	        			inserter1.setString(2, securityCodeExit);
	        			inserter1.setInt(3, flatNoExit);
	        			int result = inserter1.executeUpdate();
	        			if(result <= 0) {
	        				session.setAttribute("regularVisitorExited", "yes");
	        	        	response.sendRedirect("supervisor.jsp");
	        	        	return;
	        			}
	        		}
	        	}
        	}
        	
        	response.sendRedirect("supervisor.jsp");

        } catch(SQLIntegrityConstraintViolationException sqlicve) {
        	session.setAttribute("regularVisitorExists", "no");
        	response.sendRedirect("supervisor.jsp");
        	return;
        } catch(SQLException sqle) {
        	Logger lgr = Logger.getLogger(CheckInOutRegularVisitor.class.getName());
            lgr.log(Level.SEVERE, sqle.getMessage(), "");
		}
        
    }
	
}
