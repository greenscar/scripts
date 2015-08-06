package servlets;
import logging.*;
import daos.*;
import beans.*;
import security.Guard;

public class SecurityServlet extends javax.servlet.http.HttpServlet {
    Secretary log;
    UserDAO userDAO;
    Guard guard;
    public void init(javax.servlet.ServletConfig config) throws javax.servlet.ServletException {
        super.init(config);
        log = new Secretary();
        guard = new Guard();
    }
    
    public void destroy() {
    }
    
    protected void processRequest(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        log.write("-=-=-=-=-=-=-=-=-=-=-=-= SecurityServlet.processRequest()=-=-=-=-=-=-=-=-=-=-=-=-");
        javax.servlet.RequestDispatcher rd = null;
        //Load or create a new session
        javax.servlet.http.HttpSession session = request.getSession(true);
        if(session.getAttribute("numTries") == null || request.getParameter("toDo") == null){
            //User's first visit. Send to login page.
            Integer numTries = new Integer(0);
            session.setAttribute("numTries", numTries);
            log.write("Forwarding user to login; numTries = " + numTries);
            rd = getServletConfig().getServletContext().getRequestDispatcher("/login.jsp");
        }
        else{
            Integer n = (Integer)(session.getAttribute("numTries"));
            String toDo = request.getParameter("toDo");
            int numTries = n.intValue();
            if(numTries >= 4){
                log.write("Forwarding to tooManyTries.jsp");
                rd = getServletConfig().getServletContext().getRequestDispatcher("/tooManyTries.jsp");
            }
            else{
                if(toDo.compareTo("processLogin") == 0){
                    log.write("calling guard.processLogin(request, response)");
                    guard.processLogin(request, response);
                }   
                // Check session. If session is not active, attempt to process login.
                if(session.getAttribute(AdminBean.SESSION) != null){
                    log.write("Forwarding to AdminServlet");
                    // Forward user to the page they are trying to reach.
                    AdminServlet as = new AdminServlet();
                    //as.processRequest(request, response);
                    rd = getServletConfig().getServletContext().getRequestDispatcher("/admin");
                }
                else if (session.getAttribute(StudentBean.SESSION) != null){
                    log.write("Forwarding to StudentServlet");
                    // Forward user to the page they are trying to reach.
                    StudentServlet ss = new StudentServlet();
                    rd = getServletConfig().getServletContext().getRequestDispatcher("/student");
                }
                else if (session.getAttribute(GodBean.SESSION) != null){
                    log.write("Forwarding to AdminServlet");
                    // Forward user to the page they are trying to reach.
                    AdminServlet gs = new AdminServlet();
                    //gs.processRequest(request, response);
                    rd = getServletConfig().getServletContext().getRequestDispatcher("/admin");
                }
                else{
                    numTries++;
                    log.write("Forwarding user to login; numTries = " + numTries);
                    session.setAttribute("numTries", new Integer(numTries));
                    rd = getServletConfig().getServletContext().getRequestDispatcher("/login.jsp");
                }
            }  
        }
        rd.forward(request, response); 
    }
    
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        processRequest(request, response);
    }
    
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        processRequest(request, response);
    }
    
    public String getServletInfo() {
        return "Short description";
    }
}
