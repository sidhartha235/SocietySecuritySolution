import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
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

@WebServlet("/requestNormalVisitor")
public class RequestNormalVisitor extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static Properties getConnectionData() {
        
    	Properties props = new Properties();

        String fileName = "/home/sidhartha/eclipse-workspace/SocietySecuritySolution/src/main/java/db.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (IOException ioe) {
            Logger lgr = Logger.getLogger(RequestNormalVisitor.class.getName());
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
        
        String dateString = request.getParameter("date_N");
        Date date = java.sql.Date.valueOf(dateString);
        String mobile = request.getParameter("mobile_N");
        String name = request.getParameter("name_N");
        int flatNo = Integer.parseInt(request.getParameter("flatNo_N"));
        
        String template = "INSERT INTO NormalVisitor (Mobile, Name, Flat_No, BuildingID, Date) VALUES (?, ?, ?, ?, ?)";
        
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
	        		inserter.setString(1, mobile);
	        		inserter.setString(2, name);
	        		inserter.setInt(3, flatNo);
	        		inserter.setInt(4, buildingID);
	        		inserter.setDate(5, date);
	        		inserter.executeUpdate();
	        	}
        	}
        	
        	response.sendRedirect("supervisor.jsp");

        } catch(SQLIntegrityConstraintViolationException sqlicve) {
        	session.setAttribute("visitor", "exists");
        	response.sendRedirect("supervisor.jsp");
        	return;
        } catch(SQLException sqle) {
        	Logger lgr = Logger.getLogger(RequestNormalVisitor.class.getName());
            lgr.log(Level.SEVERE, sqle.getMessage(), "");
		}
        
    }
	
}
