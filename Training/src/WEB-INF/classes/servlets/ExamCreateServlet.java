/*
 * ExamCreateServlet.java
 *
 * Created on October 13, 2003, 11:09 AM
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
public class ExamCreateServlet extends dbConnectionBroker.HttpServletJXGB {
   
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
    
   
   /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        /*
        * SCREEN 1: Header info is gathered. 
        * SCREEN 2: Entry type of first entry is selected.
        * SCREEN 3 to N: Entry is entered and next entry type is selected.
        */
        Secretary.startFxn("exam", "ExamCreateServlet.processRequest(request, response)");
        javax.servlet.http.HttpSession session = request.getSession(false);
        javax.servlet.RequestDispatcher rd = null;
        int step = new Integer(request.getParameter("s")).intValue();
        BeanAdmin admin = ((BeanAdmin)(session.getAttribute(BeanUser.SESSION)));
        String goTo = "/examCreate.jsp?s=" + step;
        if(step == 0)
        {
            // GET READY TO DISPLAY THE HEADER FORM.
            connWeb = this.getConnection("WEB");
            //admin.examCatListLoad(connWeb);
            this.freeConnection(connWeb, "WEB");
        }
        else if(step == 1)
        {
            // PROCESS HEADER FORM AND GET READY TO DISPLAY ENTRY TYPE LIST
            connWeb = this.getConnection("WEB");
            connPS = this.getConnection("PS");
            admin.examProcessNewHeader(connWeb, connPS, request);
            admin.entryTypeListLoad(connWeb);
            this.freeConnection(connWeb, "WEB");
            this.freeConnection(connPS, "PS");
        }
        else if(step == 2)
        {
            // CREATE ENTRY FORM AND RESET ENTRY TYPE LIST
            String nextEntryType = request.getParameter("nextEntryType");
            admin.examAddBlankEntryToEnd(nextEntryType);
        }
        else if(step > 2)
        {
            // PROCESS ENTRY 
            // CHECK ENTRY TYPE. 
            //     IF END OF TEST, FORWARD TO DISPLAY TEST
            //     ELSE FORWARD TO ENTRY FORM
            connWeb = this.getConnection("WEB");
            admin.examProcessPrevEntry(connWeb, request);
            this.freeConnection(connWeb, "WEB");
            String nextEntryType = request.getParameter("nextEntryType");
            if(nextEntryType.equals("end"))
            {
               goTo = "/examDisplayWithSolutions.jsp?examNum=" +  admin.getExamNum();
            }
            else
            {
                admin.examAddBlankEntryToEnd(nextEntryType);
            }
            
        }
        rd = getServletConfig().getServletContext().getRequestDispatcher(goTo);
        Secretary.endFxn("exam", "ExamCreateServlet.processRequest(request, response)");
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
