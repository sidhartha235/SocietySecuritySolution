import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

@WebServlet("/newRegularVisitor")
public class NewRegularVisitor extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static Properties getConnectionData() {
        
    	Properties props = new Properties();

        String fileName = "/home/sidhartha/eclipse-workspace/SocietySecuritySolution/src/main/java/db.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (IOException ioe) {
            Logger lgr = Logger.getLogger(NewRegularVisitor.class.getName());
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
        
        String securityCode = request.getParameter("securityCode_R");
        String name = request.getParameter("name_R");
        String mobile = request.getParameter("mobile_R");
        
        String template = "INSERT INTO RegularVisitor (SecurityCode, Name, Mobile) VALUES (?, ?, ?)";
        
        try(Connection conn = DriverManager.getConnection(url, user, password)) {
        	
        	synchronized(session) {
        		PreparedStatement inserter = conn.prepareStatement(template);
        		inserter.setString(1, securityCode);
        		inserter.setString(2, name);
        		inserter.setString(3, mobile);
        		inserter.executeUpdate();
        	}
        	
        	response.sendRedirect("supervisor.jsp");

        } catch(SQLIntegrityConstraintViolationException sqlicve) {
        	session.setAttribute("regularVisitor", "exists");
        	response.sendRedirect("supervisor.jsp");
        	return;
        } catch(SQLException sqle) {
        	Logger lgr = Logger.getLogger(NewRegularVisitor.class.getName());
            lgr.log(Level.SEVERE, sqle.getMessage(), "");
		}
        
    }
	
}
