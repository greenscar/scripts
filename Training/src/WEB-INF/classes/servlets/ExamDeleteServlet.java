/*
* FILE: ExamDelete.java
* FUNCTION: This file handles submits from examDelete.jsp.
*           Here, any sort of processing needed to Delete an exam is done.
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
public class ExamDeleteServlet extends dbConnectionBroker.HttpServletJXGB 
{

    private java.sql.Connection connWeb = null;
    private java.sql.Connection connPS = null;

    public void init(ServletConfig config) throws ServletException {
      java.util.HashMap whereTo = new java.util.HashMap();
      whereTo.put("WEB", "WEB");
      whereTo.put("PS", "PS");
      super.init(config, whereTo);
    }
    public void destroy() {
        super.destroy();
    }

    /** 
    * This processRequest handles submits from ExamDelete.jsp.
    * 
    */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException 
    {
        Secretary.startFxn("exam", "ExamDeleteServlet.processRequest(request, response");
        javax.servlet.http.HttpSession session = request.getSession(false);
        javax.servlet.RequestDispatcher rd = null;
        Integer examNum;
        String toDo = "";
        BeanAdmin admin = ((BeanAdmin)(session.getAttribute(BeanUser.SESSION)));
        connWeb = this.getConnection("WEB");
        if(request.getParameter("act") == null)
        {
            admin.examListLoadActiveNotInUse(connWeb, request.getParameter("orderBy"));
        }
        if(request.getParameter("act") != null)
        {
           toDo = (String)(request.getParameter("act"));
           /*
           * An exam has been selected from the exam list.
           * Load the exam from the DB so it can be displayed to verify.
           */
           if(toDo.equals("verify"))
           {
               examNum = new Integer((String)(request.getParameter("examNum")));
               connPS = this.getConnection("PS");
               admin.setExamFromDB(connWeb, connPS, examNum.intValue());
               this.freeConnection(connPS, "PS");
           }
           /*
           * The user just verified they wish to Delete this exam or not. 
           * If TRUE, Mark the exam inactive in the DB
           * If FALSE, Send back to exam list.
           */
           if(toDo.equals("del"))
           {
               String verify = (String)(request.getParameter("verify"));
               logging.Secretary.write("exam", "verify = " + verify);
               if(verify.equals("TRUE"))
               {
                   connPS = this.getConnection("PS");
                   admin.examDelete(connWeb, connPS);
                   this.freeConnection(connPS, "PS");
               }
           }
           this.freeConnection(connWeb, "WEB");
        }
        rd = getServletConfig().getServletContext().getRequestDispatcher("/examDelete.jsp");
        Secretary.endFxn("exam", "ExamDeleteServlet.processRequest(request, response");
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
