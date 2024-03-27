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

@WebServlet("/maintenanceRequest")
public class MaintenanceRequest extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static Properties getConnectionData() {
        
    	Properties props = new Properties();

        String fileName = "/home/sidhartha/eclipse-workspace/SocietySecuritySolution/src/main/java/db.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (IOException ioe) {
            Logger lgr = Logger.getLogger(MaintenanceRequest.class.getName());
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
        
        String dateString = request.getParameter("date_M");
        Date date = java.sql.Date.valueOf(dateString);
        int flatNo = Integer.parseInt(request.getParameter("flatNo_M"));
        int amount = Integer.parseInt(request.getParameter("amount_M"));
        String template = "INSERT INTO Maintenance (Flat_No, BuildingID, Date, Amount) VALUES (?, ?, ?, ?)";
        
        try(Connection conn = DriverManager.getConnection(url, user, password)) {
        	
        	int buildingID = (int)session.getAttribute("building");
        	
        	String flatCheck = "SELECT COUNT(*) FROM Resident WHERE Flat_No = ? AND BuildingID = ?";
        	PreparedStatement inserter = conn.prepareStatement(flatCheck);
        	inserter.setInt(1, flatNo);
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
	        		inserter = conn.prepareStatement(template);
	        		inserter.setInt(1, flatNo);
	        		inserter.setInt(2, buildingID);
	        		inserter.setDate(3, date);
	        		inserter.setInt(4, amount);
	        		inserter.executeUpdate();
	        	}
        	}
        	
        	response.sendRedirect("supervisor.jsp");

        } catch(SQLException sqle) {
        	Logger lgr = Logger.getLogger(MaintenanceRequest.class.getName());
            lgr.log(Level.SEVERE, sqle.getMessage(), "");
		}
        
    }
	
}
