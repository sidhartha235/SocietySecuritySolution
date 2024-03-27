import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

@WebServlet("/addRemoveStaffDetails")
public class AddRemoveStaffDetails extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static Properties getConnectionData() {
        
    	Properties props = new Properties();

        String fileName = "/home/sidhartha/eclipse-workspace/SocietySecuritySolution/src/main/java/db.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (IOException ioe) {
            Logger lgr = Logger.getLogger(AddRemoveStaffDetails.class.getName());
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
        
        String roleNew = null;
        String nameNew = null;
        String hours = null;
        String contact = null;
        
        int buildingID = 0;
        
        String roleDelete = null;
        String nameDelete = null;
        
        if(request.getParameter("submit6") != null && request.getParameter("submit6").equals("Add")) {
        	roleNew = request.getParameter("role_S");
        	nameNew = request.getParameter("name_S");
        	hours = request.getParameter("hours_S");
        	contact = request.getParameter("mobile_S");
        	buildingID = Integer.parseInt(request.getParameter("buildingID"));      	
        }
        else if(request.getParameter("submit7").equals("Remove")) {
        	roleDelete = request.getParameter("role_F");
        	nameDelete = request.getParameter("name_F");
        	buildingID = Integer.parseInt(request.getParameter("buildingID"));
        }
        
        String template1 = "INSERT INTO Staff (Role, Name, WorkingHours, EmergencyContact, BuildingID) VALUES (?, ?, ?, ?, ?)";
        String template2 = "DELETE FROM Staff WHERE Role = ? AND Name = ? AND BuildingID = ?";
        
        try(Connection conn = DriverManager.getConnection(url, user, password)) {
        	
        	synchronized(session) {
        		if(roleNew != null) {
        			PreparedStatement inserter = conn.prepareStatement(template1);
        			inserter.setString(1, roleNew);
        			inserter.setString(2, nameNew);
        			inserter.setString(3, hours);
        			inserter.setString(4, contact);
        			inserter.setInt(5, buildingID);
        			inserter.executeUpdate();
        		}
        		else if(roleDelete != null) {
        			PreparedStatement inserter = conn.prepareStatement(template2);
        			inserter.setString(1, roleDelete);
        			inserter.setString(2, nameDelete);
        			inserter.setInt(3, buildingID);
        			inserter.executeUpdate();
        		}
        	}
        	
        	response.sendRedirect("supervisor.jsp");

        } catch(SQLException sqle) {
        	Logger lgr = Logger.getLogger(AddRemoveStaffDetails.class.getName());
            lgr.log(Level.SEVERE, sqle.getMessage(), "");
		}
        
    }
	
}
