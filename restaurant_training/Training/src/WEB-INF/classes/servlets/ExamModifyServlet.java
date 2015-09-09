/*
 * FILE: ExamModify.java
 * FUNCTION: This file handles submits from examModify.jsp.
 *           Here, any sort of processing needed to modify an exam is done.
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
public class ExamModifyServlet extends dbConnectionBroker.HttpServletJXGB {
   
    private java.sql.Connection connWeb = null;
    private java.sql.Connection connPS = null;
    
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
   
   /** 
    * This processRequest handles submits from examModify.jsp.
    * 
    */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException 
    {
        Secretary.startFxn("exam",  "ExamModifyServlet.processRequest(request, response)");
        javax.servlet.http.HttpSession session = request.getSession(false);
        javax.servlet.RequestDispatcher rd = null;
        Integer examNum;
        BeanAdmin BeanUserSession = ((BeanAdmin)(session.getAttribute(BeanUser.SESSION)));
        connWeb = this.getConnection("WEB");
        connPS = this.getConnection("PS");
        String toDo = request.getParameter("act");
        if(toDo == null)
        {
           toDo = "";
           String orderBy = request.getParameter("orderBy");
           BeanUserSession.examListLoadActiveNotInUse(connWeb, orderBy);
        }
        /*
        else
        {
            toDo = (String)(request.getParameter("act"));
        }
         */
        /*
        * An exam has been selected from the exam list.
        * Load the exam from the DB so it can be displayed to verify.
        */
        if(toDo.equals("verify"))
        {
           String en = request.getParameter("examNum");
            examNum = new Integer(en);
            BeanUserSession.setExamFromDB(connWeb, connPS, examNum.intValue());
        }
        /*
        * The user just verified they wish to modify this exam or not. 
        * If TRUE, Make the necessary backups in the DB 
        * If FALSE, Send back to exam list.
        */
        if(toDo.equals("modExam"))
        {
            String verify = (String)(request.getParameter("verify"));
            if(verify.equals("TRUE"))
            {
                Secretary.write("exam", "Prepare exam for mod.");
                if(!BeanUserSession.examPrepareToModify(connWeb))
                {
                    Secretary.write("exam", "Error in BeanUserSession.examPrepareToModify()");
                }
                session.setAttribute("examNumToMod", session.getAttribute("examNumToVer"));
                //Secretary.write("exam", "Finished preparing exam for mod.");
            }
            session.removeAttribute("examNumToVer");
        }
        /************************************************************************
        * MODIFY AN ENTRY
        ***********************************************************************/
        /*
        * Process the entry mod
        */
        else if(toDo.equals("modEntryProcess"))
        {
            Secretary.write("exam", "Process Entry Mod");
            if(request.getParameter("delete") != null)
            {
                BeanUserSession.examDeleteEntry(connWeb);
            }
            else
            { //request.getParameter("process") != null
                BeanUserSession.examProcessEntryMod(connWeb, request);
            }
            request.setAttribute("act", "modExam");
        }
        /************************************************************************
        * END MODIFY AN ENTRY
        ***********************************************************************/
        else if(toDo.equals("modHeader"))
        {
            //BeanUserSession.examCatListLoad(connWeb);
        }
        /*
        * The user has modified the header.
        * Process the information entered
        */
        else if(toDo.equals("modHeaderProcess"))
        {
            //Secretary.write("exam", "Process Header Mod");
            BeanUserSession.examProcessHeaderMod(connWeb, connPS, request);
            request.setAttribute("act", "modExam");
        }     
        else if(toDo.equals("addEntry1"))
        {
            /*
            * Load all possible entry types.
            * Forward user to examModify.jsp, which will display this list
            *    for the user to select entry type they wish to add
            */
            BeanUserSession.entryTypeListLoad(connWeb);
        }
        else if(toDo.equals("addEntry2"))
        {
            //Secretary.write("exam", "toDo = addEntry2");
            Integer loc = new Integer((String)(request.getParameter("eLoc")));
            Integer num = new Integer((String)(request.getParameter("qNum")));
            String eTypeCode = ((String)(request.getParameter("nextEntryType")));
            BeanUserSession.examInsertBlankEntry(connWeb, eTypeCode, loc.intValue(), num.intValue());
        }
        else if(toDo.equals("addEntry3"))
        {
            /*
            * User has entered their new entry data. 
            * Process this info and update all entrys in DB that have been renumbered.
            */
            //Secretary.write("exam", "toDo = addEntry3");
            BeanUserSession.examProcessEntryMod(connWeb, request);
            request.setAttribute("act", "modExam");
        }
        /*
        * The user has clicked finished, saying she is finished with the mod.
        */
        else if(toDo.equals("end"))
        {
            //Make exam active again.
            BeanUserSession.examConcludeModify(connWeb, connPS);
        }
        this.freeConnection(connPS, "PS");
        this.freeConnection(connWeb, "WEB");
        rd = getServletConfig().getServletContext().getRequestDispatcher("/examModify.jsp");
        Secretary.endFxn("exam", "ExamModifyServlet.processRequest(request, response");
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
