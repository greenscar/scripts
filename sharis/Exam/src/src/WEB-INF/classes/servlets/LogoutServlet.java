/*
 * LogoutServlet.java
 *
 * Created on November 2, 2004, 2:03 PM
 */

package servlets;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;
import beans.BeanUser;
/**
 *
 * @author  jsandlin
 * @version
 */
public class LogoutServlet extends dbConnectionBroker.HttpServletJXGB
{
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException
    {
      java.util.HashMap whereTo = new java.util.HashMap();
      super.init(config, whereTo);
    }
    
    /** Destroys the servlet.
     */
    public void destroy()
    {
        super.destroy(10000);
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        response.setContentType("text/html");
        HttpSession session = request.getSession(false);
        BeanUser user = (BeanUser)session.getAttribute(BeanUser.SESSION);
        if(user != null)
            user.logout(request);
        //this.destroy();
        javax.servlet.RequestDispatcher rd = null;
        rd = getServletConfig().getServletContext().getRequestDispatcher("/login.jsp");
        //this.destroy();
        rd.forward(request, response);
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo()
    {
        return "Short description";
    }
    
}
