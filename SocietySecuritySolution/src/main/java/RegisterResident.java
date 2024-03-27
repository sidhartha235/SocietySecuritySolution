import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/registerResident")
public class RegisterResident extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static Properties getConnectionData() {
        
    	Properties props = new Properties();

        String fileName = "/home/sidhartha/eclipse-workspace/SocietySecuritySolution/src/main/java/db.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (IOException ioe) {
            Logger lgr = Logger.getLogger(RegisterResident.class.getName());
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
        
        String residentName = request.getParameter("residentName");
        int buildingID = Integer.parseInt(request.getParameter("buildingName__"));
        int flatNumber = Integer.parseInt(request.getParameter("flatNumber"));
        String residentMobile = request.getParameter("residentMobile");
        String residentEmail = request.getParameter("residentEmail");
        String residentPassword = request.getParameter("residentPassword");
        String template = "INSERT INTO Resident (RName, Flat_No, BuildingID, RMobile, ResidentEmail, ResidentPassword) VALUES (?, ?, ?, ?, ?, ?)";
        
        try(Connection conn = DriverManager.getConnection(url, user, password)) {
        	
        	String flatCheck = "SELECT COUNT(*) FROM Flat WHERE Flat_No = ? AND BuildingID = ?";
        	PreparedStatement inserter = conn.prepareStatement(flatCheck);
        	inserter.setInt(1, flatNumber);
        	inserter.setInt(2, buildingID);
        	ResultSet rs = inserter.executeQuery();
        	rs.next();
        	
        	int count = rs.getInt(1);
        	
        	if(count <= 0) {
        		session.setAttribute("flatExists", "no");
        		response.sendRedirect("admin.jsp");
        		return;
        	}
        	else {
        		synchronized(session) {
	        		inserter = conn.prepareStatement(template);
	        		inserter.setString(1, residentName);
	        		inserter.setInt(2, flatNumber);
	        		inserter.setInt(3, buildingID);
	        		inserter.setString(4, residentMobile);
	        		inserter.setString(5, residentEmail);
	        		inserter.setString(6, residentPassword);
	        		inserter.executeUpdate();
	        	}
        	}
        	
        	response.sendRedirect("admin.jsp");

        } catch(SQLIntegrityConstraintViolationException sqlicve) {
        	System.out.println(sqlicve);
        	session.setAttribute("resident", "exists");
        	response.sendRedirect("admin.jsp");
        	return;
        } catch(SQLException sqle) {
        	Logger lgr = Logger.getLogger(RegisterResident.class.getName());
            lgr.log(Level.SEVERE, sqle.getMessage(), "");
		}
        
    }
	
}
