import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
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

@WebServlet("/addBuilding")
public class AddBuilding extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static Properties getConnectionData() {
        
    	Properties props = new Properties();

        String fileName = "/home/sidhartha/eclipse-workspace/SocietySecuritySolution/src/main/java/db.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (IOException ioe) {
            Logger lgr = Logger.getLogger(AddBuilding.class.getName());
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
        
        int buildingID;
        String buildingName = request.getParameter("buildingName");
        String buildingLocation = request.getParameter("buildingLocation");
        String template = "INSERT INTO Building (BuildingName, Location, SupervisorEmail, SupervisorPassword) VALUES (?, ?, ?, ?)";
        String get = "SELECT BuildingID FROM Building ORDER BY BuildingID DESC LIMIT 1";
        String update = "UPDATE Building SET SupervisorEmail = ?, SupervisorPassword = ? WHERE BuildingID = ?";
        
        try(Connection conn = DriverManager.getConnection(url, user, password)) {
        	
        	synchronized(session) {
        		PreparedStatement inserter = conn.prepareStatement(template);
        		inserter.setString(1, buildingName);
        		inserter.setString(2, buildingLocation);
        		inserter.setString(3, "supervisor#@sss.com");
        		inserter.setString(4, "supervisor@#");
        		inserter.executeUpdate();
        		
        		inserter = conn.prepareStatement(get);
        		ResultSet rs = inserter.executeQuery();
        		rs.next();
        		
        		buildingID = rs.getInt("BuildingID");
        		String supervisorEmail = "supervisor" + buildingID + "@sss.com";
        		String supervisorPassword = "supervisor@" + buildingID;
        		
        		inserter = conn.prepareStatement(update);
        		inserter.setString(1, supervisorEmail);
        		inserter.setString(2, supervisorPassword);
        		inserter.setInt(3, buildingID);
        		inserter.executeUpdate();
        	}
        	
        	response.sendRedirect("admin.jsp?building="+ buildingID);

        } catch(SQLException sqle) {
        	Logger lgr = Logger.getLogger(AddBuilding.class.getName());
            lgr.log(Level.SEVERE, sqle.getMessage(), "");
		}
        
    }
	
}
