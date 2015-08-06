/***********************************************************************
* Module:  loginServlet.java
* Author:  jsandlin
* Purpose: Defines the Class loginServlet
***********************************************************************/
package servlets;
import java.util.*;
import javax.servlet.*;
import java.sql.*;
//import security.Guard;
import beans.*;
public class LoginServlet extends dbConnectionBroker.HttpServletJXGB {
   private BeanSecurity guard;
   private Connection connPS = null;
   public void init(ServletConfig config) throws ServletException {
      HashMap whereTo = new HashMap();
      whereTo.put("PS", "PS");
      super.init(config, whereTo);
      guard = new BeanSecurity();
   }

   public void destroy() {
      super.destroy(10000);
   }

    public void processRequest(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
    throws javax.servlet.ServletException, java.io.IOException
    {
        logging.Secretary.startFxn("exam", "LoginServlet.processRequest()");
        String goTo = "/login.jsp";
        Integer numTries = null;
        javax.servlet.RequestDispatcher rd = null;
        //Load or create a new session
        javax.servlet.http.HttpSession session = request.getSession(true);
        String empNum = (String)(request.getParameter("emp_num"));
        String pwd = (String)(request.getParameter("pwd"));
        if(empNum == null || pwd == null)
        {
            logging.Secretary.write("exam", "empNum == null || pwd == null");
            // USER IS COMING TO SITE. SEND TO LOGIN PAGE
            session.removeAttribute(user.UserVO.SESSION);
            session.setAttribute("numTries", new Integer(1));
            //Set the session to not expire for 30 minutes (1800 secs).
            session.setMaxInactiveInterval(1800);
        }
        else
        {
           Object nts = session.getAttribute("numTries");
           if(nts == null)
              logging.Secretary.write("exam", "numTries = null");
           else
              logging.Secretary.write("exam", "numTries = " + nts.toString());
           //Integer numTries = 0;
            if(nts != null)
                numTries = (Integer)nts;
            else 
                numTries = new Integer(1);
            logging.Secretary.write("exam", "numTries = " + numTries);
            int nt = numTries.intValue();
            if(nt < 5)
            {
                // If this login is successful, beanSecurity will set the session 
                //     variable "user" to the UserVO of this user.
                Connection connPS = this.getConnection("PS");
                if(guard.processLogin(connPS, request))
                {
                    //logging.Secretary.write("exam", "SUCCESSFUL LOGIN");
                    //Forward to menu based on this user's security level.
                    session.removeAttribute("numTries");
                    BeanUser bu = (BeanUser)(session.getAttribute(BeanUser.SESSION));
                    String roleName = bu.getUserVO().getRoleName();
                    goTo = "/menu" + roleName + ".jsp";
                }
                else
                {
                    logging.Secretary.write("exam", "INCORRECT LOGIN");
                    nt++;
                    logging.Secretary.write("exam", "nt = " + nt);
                    session.setAttribute("numTries", new Integer(nt));
                    //Incorrect login. Forward to login.
                }
                this.freeConnection(connPS, "PS");
            }
            else{
                logging.Secretary.write("exam", "tooManyTries");
                //forward user to tooManyTries.jsp
                //if(((String)(request.getHeader("referer"))).endsWith("/login"))
                //{
                    goTo = "/tooManyTries.jsp";
                    session.setAttribute("numTries", new Integer(1));
                    
                //}
                /*
                else
                {
                    session.setAttribute("numTries", new Integer(nt));
                    //User's first visit. Send to login page.
                    session.removeAttribute(user.UserVO.SESSION);
                    //Set the session to not expire for 30 minutes (1800 secs).
                    session.setMaxInactiveInterval(1800);
                    goTo = "/login.jsp";
                }*/
            }
        }
        logging.Secretary.endFxn("exam", "LoginServlet.processRequest() - Forwarding to " + goTo);
        rd = getServletConfig().getServletContext().getRequestDispatcher(goTo);
        //this.destroy();
        rd.forward(request, response);
    }
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        processRequest(request, response);
    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        processRequest(request, response);
    }
}