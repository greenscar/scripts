/*
 * ExamGradeServlet.java
 *
 * Created on February 17, 2004, 12:30 PM
 */

package servlets;

import java.io.*;
import java.net.*;
import logging.Secretary;
import javax.servlet.*;
import javax.servlet.http.*;
import beans.*;
/**
 *
 * @author  jsandlin
 * @version
 */
public class ExamGradeServlet extends dbConnectionBroker.HttpServletJXGB {
   
    private java.sql.Connection connWeb = null;
    private java.sql.Connection connPS = null;
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
      java.util.HashMap whereTo = new java.util.HashMap();
      whereTo.put("WEB", "WEB");
      whereTo.put("PS", "PS");
      super.init(config, whereTo);
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        super.destroy();
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        Secretary.startFxn("exam", "ExamGradeServlet.processRequest(request, response)");
        javax.servlet.http.HttpSession session = request.getSession(false);
        javax.servlet.RequestDispatcher rd = null;
        BeanAdmin admin = ((BeanAdmin)(session.getAttribute(BeanUser.SESSION)));
        String goTo = "/index.jsp";
        connWeb = this.getConnection("WEB");
        connPS = this.getConnection("PS");
        String toDo = request.getParameter("toDo");
        if(toDo == null)
        {
            // Just coming to site. Load list of taken exams
            admin.examListLoadViaGraded(connWeb, connPS, false, request.getParameter("orderBy"));
            //admin.examListLoadTaken(connWeb, request.getParameter("orderBy"));
            goTo = "/examGrade.jsp?toDo=select";
        }
        else
        {
            if(toDo.equals("dispToGrade"))
            {
                admin.setExamGradedFromDB(connWeb, connPS, (new Integer(request.getParameter("examTakeNum"))).intValue());
                goTo = "/examGrade.jsp?toDo=dispToGrade&examTakeNum=" + request.getParameter("examTakeNum");
            }
            else if(toDo.equals("verify"))
            {
                goTo = "/examGrade.jsp?toDo=verify";
                admin.examProcessGrading(request);  
            }
            else if(toDo.equals("regrade"))
            {
                goTo = "/examGrade.jsp?toDo=dispToGrade";
            }
            else if(toDo.equals("process"))
            {
                //Secretary.logAllRequestVars(request);
                //admin.examProcessGrading(connWeb, connPS, request);  
                admin.examDbProcessGrading(connWeb, connPS);
                goTo = "/examGrade.jsp?toDo=dispGraded&examTakeNum=" + request.getParameter("examTakeNum");
            }    
        }      
        this.freeConnection(connWeb, "WEB");
        this.freeConnection(connPS, "PS");
        rd = getServletConfig().getServletContext().getRequestDispatcher(goTo);
        Secretary.endFxn("exam", "ExamGradeServlet.processRequest(request, response)");
        //this.destroy();
        rd.forward(request, response);
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    
}
