import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
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

@WebServlet("/approveDenyVisitor")
public class ApproveDenyVisitor extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static Properties getConnectionData() {
        
    	Properties props = new Properties();

        String fileName = "/home/sidhartha/eclipse-workspace/SocietySecuritySolution/src/main/java/db.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (IOException ioe) {
            Logger lgr = Logger.getLogger(ApproveDenyVisitor.class.getName());
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
        
        String visitorName = request.getParameter("visitorName");
        String visitorMobile = request.getParameter("visitorMobile");
        Date requestDate = java.sql.Date.valueOf(request.getParameter("requestDate"));
        
        String template = "UPDATE NormalVisitor SET ApprovalStatus = ? WHERE Name = ? AND Mobile = ? AND Date = ? AND ApprovalStatus = 'Pending'";
        
        try(Connection conn = DriverManager.getConnection(url, user, password)) {
        	
        	synchronized(session) {
        		
        		PreparedStatement inserter = conn.prepareStatement(template);
        		if((request.getParameter("approve")) != null && (request.getParameter("approve")).equals("Approve")) {
        			inserter.setString(1, "Approved");
        		}
        		else if((request.getParameter("deny")) != null && (request.getParameter("deny")).equals("Deny")) {
        			inserter.setString(1, "Denied");
        		}
        		
        		inserter.setString(2, visitorName);
        		inserter.setString(3, visitorMobile);
        		inserter.setDate(4, requestDate);
        		
        		inserter.executeUpdate();
        		
        	}
        	
        	response.sendRedirect("resident.jsp");

        } catch(SQLException sqle) {
        	Logger lgr = Logger.getLogger(ApproveDenyVisitor.class.getName());
            lgr.log(Level.SEVERE, sqle.getMessage(), "");
		}
        
    }
	
}
