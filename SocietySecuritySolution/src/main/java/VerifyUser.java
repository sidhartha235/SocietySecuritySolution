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

@WebServlet("/verifyUser")
public class VerifyUser extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static Properties getConnectionData() {
        
    	Properties props = new Properties();

        String fileName = "/home/sidhartha/eclipse-workspace/SocietySecuritySolution/src/main/java/db.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (IOException ioe) {
            Logger lgr = Logger.getLogger(VerifyUser.class.getName());
            lgr.log(Level.SEVERE, ioe.getMessage(), ioe);
        }

        return props;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	Properties props = getConnectionData();
        
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(ClassNotFoundException cnfe) {
        	System.out.println(cnfe);
        }
    	
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        
        String email = request.getParameter("username");
        String passwod = request.getParameter("password");
        String template1 = "SELECT BuildingID, SupervisorEmail, SupervisorPassword FROM Building WHERE SupervisorEmail = ? AND SupervisorPassword = ?";
        String template2 = "SELECT ResidentID, RName, ResidentEmail, ResidentPassword FROM Resident WHERE ResidentEmail = ? AND ResidentPassword = ?";
        
        try(Connection conn = DriverManager.getConnection(url, user, password)) {
        	
        	HttpSession session = request.getSession(true);
        	
        	synchronized(session) {
        		PreparedStatement inserter1 = conn.prepareStatement(template1);
        		inserter1.setString(1, email);
        		inserter1.setString(2, passwod);
        		ResultSet rs1 = inserter1.executeQuery();
        		
        		PreparedStatement inserter2 = conn.prepareStatement(template2);
        		inserter2.setString(1, email);
        		inserter2.setString(2, passwod);
        		ResultSet rs2 = inserter2.executeQuery();
        		
        		if(email.equals("admin@sss.com") && passwod.equals("admin@456")) {
        			session.setAttribute("user", "admin");
        			session.setMaxInactiveInterval(24*60*60);
        			response.sendRedirect("admin.jsp");
        			return;
        		}
        		else if(email.contains("supervisor") && email.contains("sss.com")) {
        			while(rs1.next()) {
        				if(email.equals(rs1.getString("SupervisorEmail")) && passwod.equals(rs1.getString("SupervisorPassword"))) {
        					session.setAttribute("user", "supervisor" + rs1.getInt("BuildingID"));
        					session.setAttribute("building", rs1.getInt("BuildingID"));
        					session.setMaxInactiveInterval(24*60*60);
        					response.sendRedirect("supervisor.jsp");
        					return;
        				}
        			}
        			response.sendRedirect("index.jsp?msg=invalid");
        		}
        		else {
        			while(rs2.next()) {
        				if(email.equals(rs2.getString("ResidentEmail")) && passwod.equals(rs2.getString("ResidentPassword"))) {
        					session.setAttribute("user", rs2.getString("RName"));
        					session.setAttribute("id", rs2.getInt("ResidentID"));
        					session.setMaxInactiveInterval(24*60*60);
        					response.sendRedirect("resident.jsp");
        					return;
        				}
        			}
        			response.sendRedirect("index.jsp?msg=invalid");
        		}        		
        	}

        } catch(SQLException sqle) {
        	Logger lgr = Logger.getLogger(VerifyUser.class.getName());
            lgr.log(Level.SEVERE, sqle.getMessage(), "");
		}
        
    }
	
}
