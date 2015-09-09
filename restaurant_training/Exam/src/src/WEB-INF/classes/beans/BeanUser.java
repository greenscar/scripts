/***********************************************************************
* Module:  BeanUser.java
* Author:  jsandlin
* Purpose: Defines the Class BeanUser
***********************************************************************/
package beans;
import java.beans.*;
import user.*;
import java.util.*;
import logging.Secretary;
import exam.*;
import java.sql.*;
public class BeanUser
{
    public static String SESSION = "BeanUserSession";
    protected UserVO meVO;
    protected UserDAO meDAO;
    protected UserVO userVO;
    protected UserDAO userDAO;
    protected ExamVO examVO;
    protected ExamDAO examDAO;
    protected Iterator iteratorExamVOList;
    protected Iterator iteratorUserVOList;
    protected int currentExamSectionNum;
    
    public String getExamTakerFullName(){ return "Noone Special";};

    /*****************************************************************
     * GET FUNCTIONS
     ****************************************************************/
    public long getEmpNum()
    {
        return this.meVO.getEmpNum();
    }
    public java.lang.String getFullName()
    {
        return this.meVO.getFirstName() + " " + this.meVO.getLastName();
    }
    public UserVO getUserVO()
    {
        return this.meVO;
    }
    public int getExamTakerEmpNum()
    {
        return this.examVO.getTaker().getEmpNum();
    }

    public String getExamDateTaken()
    {
        return examVO.getDateTaken().toString();
    }
    public String getRoleName()
    {
        return this.meVO.getRoleName();
    }

    public String getFirstName()
    {
        return this.meVO.getFirstName();
    }
/*
    public java.util.Date getLastLoginDate()
    {
        return this.meVO.getLastLoginDate();
    }
*/
    public String getLastName()
    {
        return this.meVO.getLastName();
    }

    public java.lang.Boolean getActive()
    {
        return this.meVO.getActive();
    }

    public int getRoleNum()
    {
        return this.meVO.getRoleNum();
    }
    public int getExamPointValueTotal()
    {
        return examVO.getPointValueTotal();
    }
    public int getExamNum()
    {
        return examVO.getExamNum();
    }
    public java.util.Date getExamDateLastMod()
    {
        return examVO.getDateLastMod();
    }
    public String getExamName()
    {
        return examVO.getExamName();
    }
    public String getExamPointsExtra()
    {
       Integer t = new Integer(examVO.getPointsExtra());
       return t.toString();
    }
    public String getExamCreatorFullName()
    {
        return examVO.getCreator().getFullName();
    }
    public int getExamTakeNum()
    {
        //logging.Secretary.startFxn("exam", "BeanUser.getExamTakeNum()");
        int x = examVO.getTakeNum();
        //logging.Secretary.endFxn("exam", "BeanUser.getExamTakeNum() => " + x);
        return x;
    }
    public boolean getExamCanRetake()
    {
       return examVO.getCanRetake();
    }
    public String getExamCanRetakeString()
    {
       if(examVO.getCanRetake())
          return("YES");
       else
          return("NO");
    }
    public String getExamDateCreated()
    {
        return examVO.getDateCreated().toString();
    }
    public String getExamDisplayAfterTakingString()
    {
        if(examVO.getDispAfterTaking())
            return "YES";
        else
            return "NO";
    }
    public boolean getExamDisplayAfterTaking()
    {
        return examVO.getDispAfterTaking();
    }
    public String getExamGradersComment()
    {
        if(this.examVO.getGradersComment() == null) return "";
        else return examVO.getGradersComment();
    }
    public boolean getExamIsGraded(){ return this.examVO.isGraded(); }
    public String getExamGrade()
    {
        //Secretary.startFxn("exam", "BeanUser.getExamGrade()");
        Integer toReturn = new Integer(examVO.getGradePercent());
        //Secretary.write("exam", "toReturn => " + toReturn.toString());
        //toReturn = new Integer(5);
        //Secretary.endFxn("exam", "BeanUser.getExamGrade() => " + toReturn.toString());        
        return toReturn.toString();
    }
    public String getExamPointValueFinalGrade()
    {
        return String.valueOf(examVO.getPointValueFinalGrade());
    }
    public String getFinalGrade()
    {
        return examVO.getGradeString();
    }
    public String getExamBodyGraded()
    {    
        //this.examVO.logExam();
        return examVO.displayBodyGraded();
    }
    public String getExamBodyWithSolutions()
    {
        return examVO.displayBodyWithSolutions();
    }
    public boolean getExamIsSelfGrading()
    {
       return this.examVO.isSelfGrading();
    }
    /*****************************************************************
     * END GET FUNCTIONS
     ****************************************************************/

    /*****************************************************************
     * SET FUNCTIONS
     ****************************************************************/
    public void setUserVO(UserVO tempVO)
    {
        this.meVO = tempVO;
    }
    /*
    public void setExamFromDB(Connection connWeb, Integer eNum)
    {
        Secretary.startFxn("exam", "BeanUser.setExamFromDB(connWeb, Integer "+eNum+")");
        this.setExamFromDB(connWeb, eNum.intValue());
        Secretary.endFxn("exam", "BeanUser.setExamFromDB(connWeb, "+eNum+")");
    }
    public void setExamFromDB(Connection connWeb, java.lang.String eNum)
    {
        Secretary.startFxn("exam", "BeanUser.setExamFromDB(connWeb, String "+eNum+")");
        Integer en = new Integer(eNum);
        this.setExamFromDB(connWeb, en.intValue());
        Secretary.endFxn("exam", "BeanUser.setExamFromDB(connWeb, String "+eNum+")");
    }
    */
    public void setExamFromDB(Connection connWeb, Connection connPS, int examNum)
    {
        Secretary.startFxn("exam", "BeanUser.setExamFromDB(connWeb, int "+examNum+")");
        if(this.examDAO == null)
        {
            this.examDAO = new ExamDAO();
        }
        if(this.userDAO == null)
        {
            this.userDAO = new UserDAO();
        }
        if(examDAO.dbLoadVO(connWeb, examNum))
            this.examVO = examDAO.getExamVO();
        //this.examVO.logExam();
        userDAO.dbLoadVOViaEmpNum(connPS, examVO.getCreatorEmpNum());
        examVO.setCreator(userDAO.getVO()); 
        this.currentExamSectionNum = 1;
        Secretary.endFxn("exam", "BeanUser.setExamFromDB(connWeb, int "+examNum+")");
    }
    public void setExamGradedFromDB(Connection connWeb, Connection connPS, int takeNum)
    {
        Secretary.startFxn("exam", "BeanUser.setExamGradedFromDB(connWeb, "+takeNum+")");
        if(this.examDAO == null)
        {
            this.examDAO = new ExamDAO();
        }
        if(this.userDAO == null)
        {
            this.userDAO = new UserDAO();
        }
        examDAO.dbLoadTakenVO(connWeb, takeNum);
        this.examVO = examDAO.getExamVO();
        userDAO.dbLoadVOViaEmpNum(connPS, examVO.getTakerEmpNum());
        examVO.setTaker(userDAO.getVO());      
        //this.examVO.logExam();
        Secretary.endFxn("exam", "BeanUser.setExamGradedFromDB(connWeb, "+takeNum+")");
    }
    /*****************************************************************
     * END SET FUNCTIONS
     ****************************************************************/
    /*****************************************************************
     * examListLoadActive loads all active exam's headers into 
     *   the exam iterator
     ****************************************************************/
    public void examListLoadActiveAll(java.sql.Connection connWeb, String orderBy)
    {
        logging.Secretary.startFxn("exam", "BeanUser.examListLoadActive(connWeb, " + orderBy + ")");
        //Load all active exams into the exam iterator
        if(orderBy == null)
        {
            orderBy = "exam_num";
        }
        if(this.examDAO == null)
        {
            this.examDAO = new ExamDAO();
        }
        iteratorExamVOList = examDAO.dbLoadHeadersViaActive(connWeb, true, orderBy);
        logging.Secretary.endFxn("exam", "BeanUser.examListLoadActive(connWeb, " + orderBy + ")");
    }
    
    /*****************************************************************
     * examListLoadActive loads all active exam's headers into 
     *   the exam iterator
     ****************************************************************/
    public void examListLoadActiveNotInUse(java.sql.Connection connWeb, String orderBy)
    {
        Secretary.startFxn("exam", "BeanAdmin.examListLoadActive(connWeb, " + orderBy + ")");
        //Load all active exams into the exam iterator
        if(orderBy == null)
        {
            orderBy = "exam_num";
        }
        if(this.examDAO == null)
        {
            this.examDAO = new ExamDAO();
        }
        iteratorExamVOList = examDAO.dbLoadHeadersActiveViaInUse(connWeb, false, orderBy);
        Secretary.endFxn("exam", "BeanAdmin.examListLoadActive(connWeb, " + orderBy + ")");
    }
    /*****************************************************************
     * examListLoadTaken loads all taken exam's headers into 
     *   the exam iterator
     ****************************************************************/
    /*
    public void examListLoadTaken(Connection connWeb, Connection connPS, String orderBy)
    {
        Secretary.startFxn("exam", "BeanUser.examListLoadTaken(connWeb, " + orderBy + ")");
        if(orderBy == null) 
        {
            orderBy = "exam_num";
        }
        if(this.examDAO == null)
        {
            this.examDAO = new ExamDAO();
        }
        if(this.userDAO == null)
        {
            this.userDAO = new UserDAO();
        }
        if(meVO.getRoleNum() == 100)
        {
            iteratorExamVOList = examDAO.AAAdbLoadHeadersTakenAll(connWeb, connPS, orderBy);
        }
        else
        {
            iteratorExamVOList = examDAO.dbLoadHeadersViaTaker(connWeb, meVO.getEmpNum(), orderBy);
        }
        Secretary.endFxn("exam", "BeanUser.examListLoadTaken(connWeb, " + orderBy + ")");
    }
     */
    public void examListLoadViaGraded(Connection connWeb, Connection connPS, boolean graded, String orderBy)
    {
        Secretary.startFxn("exam", "BeanUser.examListLoadViaGraded(connWeb, " + graded + ", " + orderBy+")");
        if(orderBy == null)  orderBy = "exam_num";
        if(this.examDAO == null) this.examDAO = new ExamDAO();
        if(this.userDAO == null) this.userDAO = new UserDAO();
        if(meVO.getRoleNum() == 100)
            iteratorExamVOList = examDAO.dbLoadHeadersViaGraded(connWeb, connPS, graded, orderBy);
        else
            iteratorExamVOList = examDAO.dbLoadHeadersViaTakerViaGraded(connWeb, connPS, meVO.getEmpNum(), graded, orderBy);
        Secretary.endFxn("exam", "BeanUser.examListLoadViaGraded(connWeb, " + graded + ", " + orderBy+")");
    }
    /*
    public void examListLoadGraded(Connection connWeb, String orderBy)
    {
        Secretary.startFxn("exam", "BeanUser.examListLoadGraded(connWeb, " + orderBy + ")");
        if(orderBy == null) 
        {
            orderBy = "exam_num";
        }
        if(this.examDAO == null)
        {
            this.examDAO = new ExamDAO();
        }
        if(this.userDAO == null)
        {
            this.userDAO = new UserDAO();
        }
        if(meVO.getRoleNum() == 100)
        {
            iteratorExamVOList = examDAO.dbLoadHeadersViaGraded(connWeb, true, orderBy);
        }
        else
        {
            iteratorExamVOList = examDAO.dbLoadHeadersViaTakerViaGraded(connWeb, meVO.getEmpNum(), true, orderBy);
        }
        Secretary.endFxn("exam", "BeanUser.examListLoadGraded(connWeb, " + orderBy + ")");
    }
     */
    /*****************************************************************
     * examListHasMore checks the exam list iterator to see if we
     *   have reached the end.
     ****************************************************************/
    public boolean examListHasMore(){
        //Secretary.startFxn("exam", "BeanUser.examListHasMore()");
        boolean x = iteratorExamVOList.hasNext();
        //Secretary.endFxn("exam", "BeanUser.examListHasMore() => " + x);
        return x;
    }
    
    public void setNextExamVO(boolean val)
    {
        //Secretary.startFxn("exam", "BeanUser.setNextExamVO("+val+")");
        if(examListHasMore())
        {
            examVO = (ExamVO)iteratorExamVOList.next();
        }
        //examVO.logExam();
        //Secretary.endFxn("exam", "BeanUser.setNextExamVO("+val+")");
    }
    /*
    public void examLogValues()
    {
        examVO.logExam();
    }
     */
    public boolean examOnFirstSection()
    {
        return (this.currentExamSectionNum == 1);
    }
    public boolean examOnLastSection()
    {
        return (this.currentExamSectionNum == this.examVO.getNumSections());
    }
   /**
    * METHOD NAME: logout
    * ARGUMENTS: The HttpServletRequest
    * REQUIRE: a session exists
    * FUNCTION: Destroy the session in the request provided
    * RETURN: n/a
    */
    public void logout(javax.servlet.http.HttpServletRequest request)
    {
        //Destroy the session
        javax.servlet.http.HttpSession theSession = request.getSession(false);
        theSession.invalidate(); 
    }
    public BeanUser(){   
        examVO = new ExamVO();
        meDAO = null;
        //examDAO = new ExamDAO();
    }

}