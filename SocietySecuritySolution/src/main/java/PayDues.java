import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/payDues")
public class PayDues extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static Properties getConnectionData() {
        
    	Properties props = new Properties();

        String fileName = "/home/sidhartha/eclipse-workspace/SocietySecuritySolution/src/main/java/db.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (IOException ioe) {
            Logger lgr = Logger.getLogger(PayDues.class.getName());
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
        
        int residentID = Integer.parseInt(request.getParameter("resID"));
        Date issueDate = java.sql.Date.valueOf(request.getParameter("issueDate"));
        int amount = Integer.parseInt(request.getParameter("amount"));
        String modeOfPay = request.getParameter("modeOfPay");
        
        String template1 = "SELECT Flat_No, BuildingID FROM Resident WHERE ResidentID = ?";
        String template2 = "UPDATE Maintenance SET Status = ?, ModeOfPayment = ? WHERE Flat_No = ? AND BuildingID = ? AND Date = ? AND Amount = ? AND Status = 'Due'";
        
        try(Connection conn = DriverManager.getConnection(url, user, password)) {
        	
        	synchronized(session) {
        		
        		PreparedStatement inserter = conn.prepareStatement(template1);
    			inserter.setInt(1, residentID);
    			ResultSet rs = inserter.executeQuery();
    			rs.next();
    			
    			int flatNumber = rs.getInt("Flat_No");
    			int buildingID = rs.getInt("BuildingID");
    			
    			inserter = conn.prepareStatement(template2);
    			inserter.setString(1, "Paid");
    			inserter.setString(2, modeOfPay);
    			inserter.setInt(3, flatNumber);
    			inserter.setInt(4, buildingID);
    			inserter.setDate(5, issueDate);
    			inserter.setInt(6, amount);
    			inserter.executeUpdate();
        		
        	}
        	
        	response.sendRedirect("resident.jsp");

        } catch(SQLException sqle) {
        	Logger lgr = Logger.getLogger(PayDues.class.getName());
            lgr.log(Level.SEVERE, sqle.getMessage(), "");
		}
        
    }
	
}
