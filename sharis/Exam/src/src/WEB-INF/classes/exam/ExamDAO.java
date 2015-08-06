/* Shari's ses_EXAM System
 * ExamDAO.java
 *
 * Created on September 22, 2003, 1:55 PM
 */

package exam;
import exam.daos.*;
import exam.vos.*;
import user.*;
import java.util.*;
import java.sql.*;
import logging.Secretary;
import tools.ConvertTool;
//import com.javaexchange.dbConnectionBroker.*;
/**
 *
 * @author  jsandlin
 */
public class ExamDAO {
    private ExamVO examVO;
    private Vector examEntryDAOs;
   /**
    * METHOD NAME: dbLoadEntryTypeList
    * ARGUMENTS: a database connection to the exam database
    * REQUIRE: this.examVO NOT NULL
    * FUNCTION: Load a list of possible Exam Entry types from the database.
    *           Store this loaded list in examVO by calling examVO.setEntryTypes
    * RETURN: n/a
    */
   public void dbLoadEntryTypeList(Connection connWeb)
   {
      Secretary.startFxn("exam", "ExamDAO.dbLoadEntryTypeList(connWeb)");
      Vector etv;
      if(examEntryDAOs.size() > 0){
         ExamEntryDAO entryDAO = (ExamEntryDAO)(examEntryDAOs.elementAt(0));
         etv = entryDAO.dbLoadEETypeList(connWeb);
      }
      else{
         // Because we do not have any instance of a QuestionDAO to load 
         // possible EntryTypes, we must create a temp QuestionDAO.
         TrueFalseDAO tfd = new TrueFalseDAO();
         etv = tfd.dbLoadEETypeList(connWeb);
         examEntryDAOs.add(tfd);
      }
      examVO.setEntryTypes(etv);
      Secretary.endFxn("exam", "ExamDAO.dbLoadEntryTypeList(connWeb)");
   }
   
   /********************************************************************
    * INSERTS
    ********************************************************************/
   /*
    * Insert the header into the DB and into PS
    * Called by BeanAdmin
    * This function is called when creating an exam. 
    */
   public boolean dbInsertHeader(Connection connWeb)
   {  
      Secretary.startFxn("exam", "ExamDAO.dbInsertHeader(connWeb)");
      /*
       * INSERT INTO WEB DB
       */
      boolean toReturn = false;
      String dbInsertHeaderStr;
      PreparedStatement dbInsertHeaderStmt = null;
      dbInsertHeaderStr = "INSERT INTO ses_EXAM (exam_num, exam_name, ps_num, "
                       + "creator_emp_num, date_created, date_last_mod, "
                       + "is_active, display_after_taking, point_value, can_retake)"
                       + "VALUES(?,?,?,?,?,?,?,?,?, ?)";
      try{
         dbInsertHeaderStmt = connWeb.prepareStatement(dbInsertHeaderStr);
         dbInsertHeaderStmt.setInt(1, examVO.getExamNum());
         dbInsertHeaderStmt.setString(2, examVO.getExamName());
         dbInsertHeaderStmt.setString(3, examVO.getPsID());
         dbInsertHeaderStmt.setInt(4, examVO.getCreatorEmpNum());
         dbInsertHeaderStmt.setFloat(5, ConvertTool.getFloatFromDate(examVO.getDateCreated()));
         dbInsertHeaderStmt.setFloat(6, ConvertTool.getFloatFromDate(examVO.getDateLastMod()));
         if(examVO.getIsActive())
             dbInsertHeaderStmt.setInt(7, 1);
         else
             dbInsertHeaderStmt.setInt(7, 0);
         if(examVO.getDispAfterTaking())
             dbInsertHeaderStmt.setInt(8, 1);
         else
             dbInsertHeaderStmt.setInt(8, 0);
         dbInsertHeaderStmt.setInt(9, examVO.getPointValueTotal());
         if(examVO.getCanRetake())
            dbInsertHeaderStmt.setInt(10, 1);
         else
            dbInsertHeaderStmt.setInt(10, 0);
         dbInsertHeaderStmt.executeUpdate();
         toReturn = true;
     }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbInsertHeader(connWeb) => " + e.getMessage());
     }finally{
          try{
            dbInsertHeaderStmt.close();
          }catch(SQLException e){
            logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbInsertHeader(connWeb) => " + e.getMessage());
          }
      }
      Secretary.endFxn("exam", "ExamDAO.dbInsertHeader(connWeb)");
      return toReturn;
   }
   public void dbInsertHeaderToPS(Connection connPS)
   {
      /*
      * INSERT INTO PS DB
      */
      String nameShort = "";
      if(examVO.getExamName().length() < 10)
         nameShort = examVO.getExamName().substring(0, examVO.getExamName().length());
      else
         nameShort = examVO.getExamName().substring(0, 9);
      String insertPsStr = "INSERT INTO PS_ACCOMP_TBL"
                    + " (ACCOMPLISHMENT, DESCR, DESCRSHORT, ACCOMP_CATEGORY, "
                    + " RENEWAL_REQUIRED, RENEWAL_PERIOD, RENEWAL_LENGTH, YEARS_OF_EDUCATN, "
                    + " EDUCATION_LVL, COUNTRY, FIELD_OF_STUDY_GER, TYPE_OF_STUDY_GER, "
                    + " DURATION_MONTH, NVQ_TITLE, NVQ_LEVEL, HP_STATS_DEG_LVL, FP_DEGREE_LVL)"
                    + " VALUES (?, ?, ?, 'TST', "
                    + " ' ', ' ', 0, 0, "
                    + " ' ', ' ', ' ', ' ',"
                    + " 0, ' ', ' ', ' ', ' ');";
      String ins = "INSERT INTO PS_ACCOMP_TBL"
                    + " (ACCOMPLISHMENT, DESCR, DESCRSHORT, ACCOMP_CATEGORY)"
                    + " VALUES ('"
                    + examVO.getPsID() + "', '"
                    + examVO.getExamName() + "', '"
                    + nameShort + "', '"
                    + "TST')";
      Secretary.write("exam", ins);
      PreparedStatement insertPsStmt = null;
      try{
         insertPsStmt = connPS.prepareStatement(insertPsStr);
         insertPsStmt.setString(1, examVO.getPsID());
         insertPsStmt.setString(2, examVO.getExamName());
         insertPsStmt.setString(3, nameShort);
         insertPsStmt.executeUpdate();
     }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbInsertHeader(connPS) => " + e.getMessage());
     }finally{
          try{
            insertPsStmt.close();
          }catch(SQLException e){
            logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbInsertHeader(connPS) => " + e.getMessage());
          }
      }
   }
   /*
    * Insert the entire body to the DB
    * dbInsertBody is called from BeanAdmin when prepping to modify an exam.
    * If the exam has been taken before, we insert a new copy into the DB
    *    so we don't modify the one that has been taken.
    */
   public boolean dbInsertBody(Connection connWeb){
     Secretary.startFxn("exam", "ExamDAO.dbInsertBody(connWeb)");
     int numEntries = examVO.getNumEntries();
     ExamEntryVO tempVO;
     for(int x = 1; x <= numEntries; x++){
          // Create correct ExamEntryDAO based on examVO
          tempVO = examVO.getEntryAtLoc(x);
          ExamEntryDAO examEntryDAO = createExamEntryDAO(tempVO.getEntryTypeClass());
          examEntryDAO.setVO(tempVO);
          if(!examEntryDAO.dbInsertEE(connWeb))
             return false;
          if(!examEntryDAO.dbInsertVO(connWeb))
            return false;
     }
     Secretary.endFxn("exam", "ExamDAO.dbInsertBody(connWeb)");
     return true;
   }
   /*
    * Insert the last entry in the entry vector to the DB.
    * Called by BeanAdmin
    * Used to insert an entry into the DB when creating an exam.
    */
   public void dbInsertLastEntry(Connection connWeb){
     Secretary.startFxn("exam", "ExamDAO.dbInsertLastEntry(connWeb)");
     //examVO.logExam();
     int numEntries = examVO.getNumEntries();
     //Secretary.write("exam", "numEntries = " + numEntries);
     ExamEntryVO entryVO = (ExamEntryVO)(examVO.getEntryAtLoc(numEntries));
     ExamEntryDAO entryDAO = createExamEntryDAO(entryVO.getEntryTypeClass());
     try
     {
        connWeb.setAutoCommit(false);
        entryDAO.setVO(entryVO);
        entryDAO.dbInsertEE(connWeb);
        entryDAO.dbInsertVO(connWeb);
        connWeb.commit();
        connWeb.setAutoCommit(true);
     }catch(SQLException e)
     {       
        try{
            connWeb.rollback();
            connWeb.setAutoCommit(true) ;
        }catch(SQLException e1)
        {
         Secretary.write("exam", "SQLException in ExamEntryDAO.dbInsertLastEntry(connWeb) => " + e.toString());
        }
         Secretary.write("exam", "SQLException in ExamEntryDAO.dbInsertLastEntry(connWeb) => " + e.toString());
     }
     this.dbUpdateHeader(connWeb);
     //this.dbUpdatePointValueTotal(connWeb);
     Secretary.endFxn("exam", "ExamDAO.dbInsertLastEntry(connWeb)");
   }
   
   /*
    * Insert a new Entry in the DB and update all entries coming 
    *    after this one in the exam because their examLoc & questionNum will change.
    * Called when an exam is being modified and an entry is added.
    */
   public void dbInsertNewEntry(Connection connWeb, int eLoc)
   {
       logging.Secretary.startFxn("exam", "ExamDAO.dbInsertNewEntry(" + connWeb + ", " + eLoc + ")");
       try
       {
            connWeb.setAutoCommit(false);
            //Secretary.write("exam", "eLoc = " + eLoc);
            //Secretary.write("exam", "this.examVO.getNumEntries() = " + this.examVO.getNumEntries());
            long time = System.currentTimeMillis();
            // 1) Insert new blank entry into db
            ExamEntryVO entryVO = (ExamEntryVO)(examVO.getEntryAtLoc(eLoc));
            Secretary.startFxn("exam", "INSERT THIS ENTRY INTO THE DB:");
            entryVO.logValues();
            Secretary.endFxn("exam", "INSERT THIS ENTRY INTO THE DB");
            ExamEntryDAO entryDAO = createExamEntryDAO(entryVO.getEntryTypeClass());
            entryDAO.setVO(entryVO);
            entryDAO.dbInsertEE(connWeb);
            entryDAO.dbInsertVO(connWeb);
            // 2) Update all entries with eLoc > eLoc
            Vector tempSection = new Vector();
            Vector fullExam = this.examVO.getFullExamVector();
            for(int x = 0; x < fullExam.size(); x++)
            {
                tempSection = (Vector)(fullExam.elementAt(x));
                for(int y = 0; y < tempSection.size(); y++)
                {
                    entryVO = ((ExamEntryVO)(tempSection.elementAt(y)));
                    if(entryVO.getExamLoc() > eLoc)
                    {
                        entryDAO = createExamEntryDAO(entryVO.getEntryTypeClass());
                        entryDAO.setVO(entryVO);
                        entryDAO.dbUpdateEE(connWeb);
                    }
                }
            }
            connWeb.commit();
            connWeb.setAutoCommit(true);
       }catch(SQLException e){
            try{
                connWeb.rollback();
                connWeb.setAutoCommit(true) ;
            }catch(SQLException e1)
            {
                Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbInsertNewEntry(connWeb, " + eLoc + ") => " + e.toString());
            }
            Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbInsertNewEntry(connWeb, " + eLoc + ") => " + e.toString());
       }catch(Exception e){
           Secretary.write("exam", "+=+=+= Exception in ExamDAO.dbInsertNewEntry(" + connWeb + ", " + eLoc + ") => COULD NOT ROLLBACK => " + e.toString());
       }
       logging.Secretary.endFxn("exam", "ExamDAO.dbInsertNewEntry(" + connWeb + ", " + eLoc + ")");
   }
   
   /*
    * Insert a taken exam
    * 1) Insert the header
    * 2) Insert the body.
    * 3) Delete from exams open table.
    */
   public void dbInsertTaken(Connection connWeb, Connection connPS)
   {
      try
      {
         connWeb.setAutoCommit(false);
         connPS.setAutoCommit(false);
         /*
         * IF EXAM IS SELF GRADING, INSERT INTO PS & MARK IT GRADED
         */
         if(examVO.isSelfGrading())
         {
            this.dbUpdatePSGrade(connPS);
         }
         this.dbInsertTakenHeader(connWeb);
         this.dbInsertTakenBody(connWeb);
         this.dbDeleteFromExamsOpen(connWeb);
         connWeb.commit();
         connPS.commit();
         connWeb.setAutoCommit(true);
         connPS.setAutoCommit(true);
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbInsertTaken(connWeb) => " + e.getMessage());
      }
      finally{
         try{
            connWeb.setAutoCommit(true);
         }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbInsertTaken(connWeb) => " + e.getMessage());
         }
      }
   }
   
   /*
    * Insert the header of a taken exam to the DB.
    * Called from this.dbInsertTaken
    */
   private void dbInsertTakenHeader(Connection connWeb)
   {
      Secretary.startFxn("exam", "ExamDAO.dbInsertTakenHeader(connWeb)");
      String dbInsertHeaderTakenStr;   
      PreparedStatement dbInsertHeaderTakenStmt = null;
      dbInsertHeaderTakenStr = "INSERT INTO ses_EXAM_TAKE (take_num, exam_num, "
                 + "date_submitted, emp_num, final_grade, graded, points_extra) VALUES (?, ?, ?, ?, ?, ?, ?)";
      int takeNum = dbLoadMaxTakeNum(connWeb) + 1;
      this.examVO.setTakeNum(takeNum);
      String insertLog = "INSERT INTO ses_EXAM_TAKE (take_num, exam_num, date_submitted, emp_num, final_grade, graded, points_extra) "
            + "VALUES (" + examVO.getTakeNum() + ", " + examVO.getExamNum() 
            + ", " + ConvertTool.getFloatFromDate(examVO.getDateTaken()) 
            + ", " + examVO.getTaker().getEmpNum() 
            + ", " + examVO.getPointValueFinalGrade()
            + ", " + examVO.isGraded()
            + ", " + examVO.getPointsExtra()
            + ");";
      Secretary.write("exam", insertLog);
      try{
         dbInsertHeaderTakenStmt = connWeb.prepareStatement(dbInsertHeaderTakenStr);
         dbInsertHeaderTakenStmt.setInt(1, examVO.getTakeNum());
         dbInsertHeaderTakenStmt.setInt(2, examVO.getExamNum());
         dbInsertHeaderTakenStmt.setFloat(3, ConvertTool.getFloatFromDate(examVO.getDateTaken()));
         dbInsertHeaderTakenStmt.setInt(4, examVO.getTaker().getEmpNum());
         dbInsertHeaderTakenStmt.setInt(5, examVO.getPointValueFinalGrade());
         dbInsertHeaderTakenStmt.setBoolean(6, examVO.isGraded());
         dbInsertHeaderTakenStmt.setInt(7, examVO.getPointsExtra());
         dbInsertHeaderTakenStmt.execute();
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbInsertTakenHeader(connWeb) => " + e.getMessage());
      }finally{
          try{
            dbInsertHeaderTakenStmt.close();
          }catch(SQLException e){
            logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbInsertTakenHeader(connWeb) => " + e.getMessage());
          }
      }
      Secretary.endFxn("exam", "ExamDAO.dbInsertTakenHeader(connWeb)");
   }
   
   /*
    * Insert the body of a taken exam to the DB
    * Called from this.dbInsertTaken
    */
    private void dbInsertTakenBody(Connection connWeb){
        Secretary.startFxn("exam", "ExamDAO.dbInsertTakenBody(connWeb)");
        ExamEntryVO entryVO = null;
        Vector tempSection = new Vector();
        Vector fullExam = this.examVO.getFullExamVector();
        for(int x = 0; x < fullExam.size(); x++)
        {
            tempSection = (Vector)(fullExam.elementAt(x));
            for(int y = 0; y < tempSection.size(); y++)
            {
                entryVO = ((ExamEntryVO)(tempSection.elementAt(y)));
                if(entryVO instanceof QuestionVO){
                    QuestionDAO qd = (QuestionDAO)(createExamEntryDAO(entryVO.getEntryTypeClass()));
                    qd.setVO(entryVO);
                    qd.dbInsertTakenVO(connWeb);
                }
            }
        }
        Secretary.endFxn("exam", "ExamDAO.dbInsertTakenBody(connWeb)");
    }
    
    /*
     * When a user starts taking an exam, a row gets inserted into the ses_EXAMS_OPEN.
     * When the user submits the exam, this row gets deleted.
     * This makes it possible to prevent the modifications of exams currently 
     *      being taken.
     * Called from BeanStudent.startExamTake
     */
    public void dbInsertIntoExamsOpen(Connection connWeb)
    {
        Secretary.startFxn("exam", "ExamDAO.dbInsertIntoExamsOpen(connWeb)");
        String ins = "INSERT INTO ses_EXAMS_OPEN (emp_num, exam_num, start_time) "
            + "VALUES (" 
            + examVO.getTaker().getEmpNum() + ", "
            + examVO.getExamNum() + ", "
            + ConvertTool.getFloatFromDate(new java.util.Date())
            + ");";
        Secretary.write("exam", ins);
        Statement stmt = null;
        try
        {
            stmt = connWeb.createStatement();
            stmt.execute(ins);
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbInsertIntoExamsOpen(connWeb) => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbInsertIntoExamsOpen(connWeb) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "ExamDAO.dbInsertIntoExamsOpen(connWeb)");
    }
   /********************************************************************
    * END INSERTS
    ********************************************************************/
   /********************************************************************
    * LOADS
    ********************************************************************/
   /*
    * Load headers for all exams based on if they are active or not active
    * This function does not check if they are in use or not.
    */
    public Iterator dbLoadHeadersViaActive(Connection connWeb, boolean active, String sortBy)
   {
      Secretary.startFxn("exam", "ExamDAO.dbLoadHeadersViaActive(connWeb, "+active + ", " +sortBy+")");
      this.dbDeleteOldOpenExams(connWeb);
      String query = "SELECT "
                 + " e.exam_num, e.exam_name, e.date_created, "
                 + " e.display_after_taking, e.point_value, e.can_retake"
                 + " FROM ses_EXAM e WHERE"
                 + " e.is_active = ";
      if(active) query += "1 ";
      else query += "0 ";
      query += " ORDER BY " + sortBy;
      Secretary.write("exam", query);
      Vector v = new Vector();
      ExamVO temp = null;
      Statement stmt = null;
      try{
         stmt = connWeb.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         while(rs.next()){
             temp = new ExamVO();
             temp.setExamNum(rs.getInt("exam_num"));
             temp.setExamName(rs.getString("exam_name").trim());
             temp.setDateCreated(ConvertTool.getDateFromFloat(rs.getFloat("date_created")));
             if(rs.getInt("display_after_taking") == 0)
                 temp.setDispAfterTaking(false);
             else
                 temp.setDispAfterTaking(true);
             temp.setPointValueTotal(rs.getInt("point_value"));
             temp.setCanRetake(rs.getInt("can_retake"));
             temp.setIsActive(true);
             v.add(temp);
         }
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadHeadersViaActive(connWeb, "+active + ", " +sortBy+") => " + e.getMessage());
      }catch(Exception e){
         Secretary.write("exam", "+=+=+= Exception in ExamDAO.dbLoadHeadersViaActive(connWeb, "+active + ", " +sortBy+") => " + e.toString());
      }finally{
          try{
            stmt.close();
          }catch(SQLException e){
            logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadHeaderAllActive(connWeb, "+sortBy+") => " + e.getMessage());
          }
      }
      Secretary.endFxn("exam", "ExamDAO.dbLoadHeadersViaActive(connWeb, "+active + ", " +sortBy+") => " + v.size());
      return v.iterator();
   }
    
   /*
    * Load headers for all active exams based on if they are in use or not.
    */
   public Iterator dbLoadHeadersActiveViaInUse(Connection connWeb, boolean inUse, String sortBy)
   {
      Secretary.startFxn("exam", "ExamDAO.dbLoadHeadersActiveViaInUse(connWeb, " + inUse + ", " +sortBy+")");
      this.dbDeleteOldOpenExams(connWeb);
      String query = "SELECT "
                 + " e.exam_num, e.exam_name, e.date_created, "
                 + " e.display_after_taking, e.point_value, e.can_retake"
                 + " FROM ses_EXAM e WHERE"
                 + " e.is_active = 1"
                 + " AND e.exam_num ";
      if(!inUse) query += "NOT ";
      query += "IN (SELECT exam_num from ses_EXAMS_OPEN)"
                 + " ORDER BY " + sortBy;
      Secretary.write("exam", query);
      Vector v = new Vector();
      ExamVO temp = null;
      Statement stmt = null;
      try{
         stmt = connWeb.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         while(rs.next()){
             temp = new ExamVO();
             temp.setExamNum(rs.getInt("exam_num"));
             temp.setExamName(rs.getString("exam_name").trim());
             temp.setDateCreated(ConvertTool.getDateFromFloat(rs.getFloat("date_created")));
             if(rs.getInt("display_after_taking") == 0)
                 temp.setDispAfterTaking(false);
             else
                 temp.setDispAfterTaking(true);
             temp.setPointValueTotal(rs.getInt("point_value"));
             temp.setCanRetake(rs.getInt("can_retake"));
             temp.setIsActive(true);
             v.add(temp);
         }
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadHeadersActiveViaInUse(connWeb, " + inUse + ", " +sortBy+") " + e.getMessage());
      }catch(Exception e){
         Secretary.write("exam", "+=+=+= Exception in ExamDAO.dbLoadHeadersActiveViaInUse(connWeb, " + inUse + ", " +sortBy+") => " + e.toString());
      }finally{
          try{
            stmt.close();
          }catch(SQLException e){
            logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadHeadersActiveViaInUse(connWeb, " + inUse + ", " +sortBy+") => " + e.getMessage());
          }
      }
      Secretary.endFxn("exam", "ExamDAO.dbLoadHeadersActiveViaInUse(connWeb, " + inUse + ", " +sortBy+") => " + v.size());
      return v.iterator();
   }
   
    /*
     * Load a list of ALL taken exams from the DB
     */
   /*
    public Iterator AAAdbLoadHeadersTakenAll(Connection connWeb, Connection connPS, String sortBy)
    {
        Secretary.startFxn("exam", "ExamDAO.dbLoadHeadersTakenAll(connWeb, connPS, "+sortBy+")");
        Vector examVector = new Vector();
        ExamVO temp = null;
        String select = "SELECT"
            + " e.exam_num, e.exam_name, e.point_value, et.take_num, et.date_submitted, et.graded, et.emp_num, "
            + " ec.category_code, ec.category_name, et.final_grade, et.graders_comment"
            + " FROM ses_EXAM e, ses_EXAM_CATEGORY ec, ses_EXAM_TAKE et WHERE"
            + " ec.category_code = e.category_code AND"
            + " e.exam_num = et.exam_num ORDER BY ";
        if(sortBy.equalsIgnoreCase("taker"))
            //select += "ui.name_last";
           select += "e.exam_num";
        if((sortBy.equalsIgnoreCase("take_num")) 
            || (sortBy.equalsIgnoreCase("graders_comment"))
            || (sortBy.equalsIgnoreCase("date_submitted"))
            )
            select += "et." + sortBy;
        else if(sortBy.equals("final_grade"))
            select += "(cast(et.final_grade as float) / cast(e.point_value as float))";
        else
            select += "e." + sortBy;
        Secretary.write("exam", select);
        Statement stmt = null;
        try{
            stmt = connWeb.createStatement();
            ResultSet rs = stmt.executeQuery(select);
            while(rs.next())
            {
                temp = new ExamVO();
                temp.setExamNum(rs.getInt("exam_num"));
                temp.setExamName(rs.getString("exam_name").trim());
                temp.setTakeNum(rs.getInt("take_num"));
                temp.setPointValueTotal(rs.getInt("point_value"));
                temp.setDateTaken(ConvertTool.getDateFromFloat(rs.getFloat("date_submitted")));
                temp.setGraded(rs.getBoolean("graded"));
                temp.setTakerEmpNum(rs.getInt("emp_num"));
                temp.setCatID(rs.getString("category_code").trim());
                temp.setCatName(rs.getString("category_name").trim());
                temp.setPointValueFinalGrade(rs.getInt("final_grade"));
                temp.setGradersComment(rs.getString("graders_comment"));
                examVector.add(temp);
            }
        }catch(SQLException e){
            Secretary.write("exam", "ExamDAO.dbLoadHeadersTakenAll(connWeb, connPS, "+sortBy+") SQLException => " + e.getMessage());
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in ExamDAO.dbLoadHeadersTakenAll(connWeb, connPS, "+sortBy+") => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                logging.Secretary.write("exam", "ExamDAO.dbLoadHeadersTakenAll(connWeb, connPS, "+sortBy+") => " + e.getMessage());
            }
        }
        
        Iterator examIterator = examVector.iterator();
        while(examIterator.hasNext())
        {
            ExamVO ev = (ExamVO)(examIterator.next());
            select = "SELECT FIRST_NAME, LAST_NAME FROM PS_NAMES PS1 WHERE PS1.EMPLID = "
                  + ev.getTakerEmpNum() + " AND PS1.EFFDT = "
                  + "(SELECT MAX(EFFDT) FROM PS_NAMES PS2 WHERE PS1.EMPLID = PS2.EMPLID)";
            Secretary.write("exam", select);
            try{
               stmt = connWeb.createStatement();
               ResultSet rs = stmt.executeQuery(select);
               ev.setTakerFirstName(rs.getString("FIRST_NAME").trim());
               ev.setTakerLastName(rs.getString("LAST_NAME").trim());
               System.out.println(ev.toString());
            }catch(SQLException e){
               Secretary.write("exam", "ExamDAO.dbLoadHeadersTakenAll(connWeb, connPS, "+sortBy+") SQLException => " + e.getMessage());
            }catch(Exception e){
               Secretary.write("exam", "+=+=+= Exception in ExamDAO.dbLoadHeadersTakenAll(connWeb, connPS, "+sortBy+") => " + e.getMessage());
            }finally{
               try{
                   stmt.close();
               }catch(SQLException e){
                   logging.Secretary.write("exam", "ExamDAO.dbLoadHeadersTakenAll(connWeb, connPS, "+sortBy+") => " + e.getMessage());
               }
            }
        }  
        Secretary.endFxn("exam", "ExamDAO.dbLoadHeadersTakenAll(connWeb, connPS, "+sortBy+") => " + examVector.size());
        return examVector.iterator();
    }
   */
    /*
     * Load a list of taken exams from the DB based on if they are graded or not.
     */
    public Iterator dbLoadHeadersViaGraded(Connection connWeb, Connection connPS, boolean graded, String sortBy)
    {
        Secretary.startFxn("exam", "ExamDAO.dbLoadHeadersViaGraded(connWeb, connPS, " + graded + ", " + sortBy+")");
        Vector v = new Vector();
        ExamVO temp = null;
        String select = "SELECT"
            + " e.exam_num, e.exam_name, e.point_value, et.take_num, et.date_submitted, et.graded, et.emp_num, "
            + " et.final_grade, et.graders_comment"
            + " FROM ses_EXAM e, ses_EXAM_TAKE et WHERE";
        if(graded) select += " et.graded = 1 AND";
        else select += " et.graded = 0 AND";
        select += " e.exam_num = et.exam_num ORDER BY ";
        if(sortBy.equalsIgnoreCase("taker"))
            //select += "ui.name_last";
           select += "e.exam_num";
        else if((sortBy.equalsIgnoreCase("take_num")) 
            || (sortBy.equalsIgnoreCase("graders_comment"))
            || (sortBy.equalsIgnoreCase("date_submitted"))
            )
            select += "et." + sortBy;
        else if(sortBy.equals("final_grade"))
            select += "(cast(et.final_grade as float) / cast(e.point_value as float))";
        else
            select += "e." + sortBy;
        Secretary.write("exam", select);
        Statement stmt = null;
        try{
            stmt = connWeb.createStatement();
            ResultSet rs = stmt.executeQuery(select);
            while(rs.next())
            {
                temp = new ExamVO();
                temp.setExamNum(rs.getInt("exam_num"));
                temp.setExamName(rs.getString("exam_name").trim());
                temp.setTakeNum(rs.getInt("take_num"));
                temp.setPointValueTotal(rs.getInt("point_value"));
                temp.setDateTaken(ConvertTool.getDateFromFloat(rs.getFloat("date_submitted")));
                temp.setGraded(rs.getBoolean("graded"));
                temp.setTakerEmpNum(rs.getInt("emp_num"));
                temp.setPointValueFinalGrade(rs.getInt("final_grade"));
                temp.setGradersComment(rs.getString("graders_comment"));
                v.add(temp);
            }
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadHeadersViaGraded(connWeb, connPS, " + graded + ", " + sortBy+") => " + e.getMessage());
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in ExamDAO.dbLoadHeadersViaGraded(connWeb, connPS, " + graded + ", " + sortBy+") => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadHeadersViaGraded(connWeb, connPS, " + graded + ", " + sortBy+") => " + e.getMessage());
            }
        }
        
        Iterator examIterator = v.iterator();
        /*
        select = "SELECT FIRST_NAME, LAST_NAME FROM PS_NAMES PS1 WHERE PS1.EMPLID = ? AND PS1.EFFDT = "
                  + "(SELECT MAX(EFFDT) FROM PS_NAMES PS2 WHERE PS1.EMPLID = PS2.EMPLID)";
        PreparedStatement selectPsStmt = null;
         */
        while(examIterator.hasNext())
        {
            ExamVO ev = (ExamVO)(examIterator.next());
            select = "SELECT * FROM PS_NAMES PS1 WHERE PS1.EMPLID = " + ev.getTakerEmpNum() + " AND PS1.EFFDT = "
                  + "(SELECT MAX(EFFDT) FROM PS_NAMES PS2 WHERE PS1.EMPLID = PS2.EMPLID)";
            try{
               stmt = connPS.createStatement();
               ResultSet rs = stmt.executeQuery(select);
               rs.next();
               ev.setTakerFirstName(rs.getString("FIRST_NAME").trim());
               ev.setTakerLastName(rs.getString("LAST_NAME").trim());
               System.out.println(ev.toString());
            }catch(SQLException e){
               Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadHeadersTakenAll(connWeb, connPS, "+sortBy+") => " + e.getMessage());
            }catch(Exception e){
               Secretary.write("exam", "+=+=+= Exception in ExamDAO.dbLoadHeadersTakenAll(connWeb, connPS, "+sortBy+") => " + e.getMessage());
            }finally{
               try{
                   stmt.close();
               }catch(SQLException e){
                   logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadHeadersTakenAll(connWeb, connPS, "+sortBy+") => " + e.getMessage());
               }
            }
        }  
        Secretary.endFxn("exam", "ExamDAO.dbLoadHeadersViaGraded(connWeb, " + graded + ", " + sortBy+") => " + v.size());
        return v.iterator();
    }
    /*
     * Load a list of taken exams from the DB based on who took them.
     */
    public Iterator dbLoadHeadersViaTaker(Connection connWeb, Connection connPS, int takenByEmpNum, String sortBy)
    {
        Secretary.startFxn("exam", "ExamDAO.dbLoadHeadersViaTaker(connWeb, " + takenByEmpNum + ", " + sortBy + ")");
        String select = "SELECT"
            + " e.exam_num, e.exam_name, e.point_value, et.take_num, et.date_submitted, et.emp_num, et.graded, et.final_grade, "
            + " et.graders_comment"
            + " FROM ses_EXAM e, ses_EXAM_TAKE et WHERE"
            + " et.emp_num = " + takenByEmpNum + " AND "
            + " e.exam_num = et.exam_num ORDER BY ";
        Vector v = new Vector();
        ExamVO temp = null;
        if(sortBy.equalsIgnoreCase("taker"))
            //select += "ui.name_last";
            select += "e.exam_num";
        else if((sortBy.equalsIgnoreCase("take_num")) 
            || (sortBy.equalsIgnoreCase("graders_comment"))
            || (sortBy.equalsIgnoreCase("date_submitted"))
            )
            select += "et." + sortBy;
        else if(sortBy.equals("final_grade"))
            select += "(cast(et.final_grade as float) / cast(e.point_value as float))";
        else
            select += "e." + sortBy;
        Secretary.write("exam", select);
        Statement stmt = null;
        try{
            stmt = connWeb.createStatement();
            ResultSet rs = stmt.executeQuery(select);
            while(rs.next())
            {
                temp = new ExamVO();
                temp.setExamNum(rs.getInt("exam_num"));
                temp.setExamName(rs.getString("exam_name").trim());
                temp.setPointValueTotal(rs.getInt("point_value"));
                temp.setTakeNum(rs.getInt("take_num"));
                temp.setDateTaken(ConvertTool.getDateFromFloat(rs.getFloat("date_submitted")));
                temp.setTakerEmpNum(rs.getInt("emp_num"));
                temp.setGraded(rs.getBoolean("graded"));
                temp.setPointValueFinalGrade(rs.getInt("final_grade"));
                temp.setGradersComment(rs.getString("graders_comment"));
                v.add(temp);
            }
        }catch(SQLException e){
            Secretary.write("exam", "ExamDAO.dbLoadHeadersViaTaker(connWeb, " + takenByEmpNum + ", " + sortBy + ") SQLException => " + e.getMessage());
        }catch(Exception e){
            Secretary.write("exam", "ExamDAO.dbLoadHeadersViaTaker(connWeb, " + takenByEmpNum + ", " + sortBy + ") Exception => " + e.toString());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                logging.Secretary.write("exam", "ExamDAO.dbLoadHeadersViaTaker(connWeb, " + takenByEmpNum + ", " + sortBy + ") => " + e.getMessage());
            }
        }
        
        Iterator examIterator = v.iterator();
        while(examIterator.hasNext())
        {
            ExamVO ev = (ExamVO)(examIterator.next());
            select = "SELECT FIRST_NAME, LAST_NAME FROM PS_NAMES PS1 WHERE PS1.EMPLID = "
                  + ev.getTakerEmpNum() + " AND PS1.EFFDT = "
                  + "(SELECT MAX(EFFDT) FROM PS_NAMES PS2 WHERE PS1.EMPLID = PS2.EMPLID)";
            Secretary.write("exam", select);
            try{
               stmt = connPS.createStatement();
               ResultSet rs = stmt.executeQuery(select);
               rs.next();
               ev.setTakerFirstName(rs.getString("FIRST_NAME").trim());
               ev.setTakerLastName(rs.getString("LAST_NAME").trim());
               System.out.println(ev.toString());
            }catch(SQLException e){
               Secretary.write("exam", "ExamDAO.dbLoadHeadersTakenAll(connWeb, connPS, "+sortBy+") SQLException => " + e.getMessage());
            }catch(Exception e){
               Secretary.write("exam", "+=+=+= Exception in ExamDAO.dbLoadHeadersTakenAll(connWeb, connPS, "+sortBy+") => " + e.getMessage());
            }finally{
               try{
                   stmt.close();
               }catch(SQLException e){
                   logging.Secretary.write("exam", "ExamDAO.dbLoadHeadersTakenAll(connWeb, connPS, "+sortBy+") => " + e.getMessage());
               }
            }
        }  
        Secretary.endFxn("exam", "ExamDAO.dbLoadHeadersViaTaker(connWeb, " + takenByEmpNum + ", " + sortBy + ") => " + v.size());
        return v.iterator();
    }
    /*
     * Load a list of taken exams from the DB based on who took them.
     */
    public Iterator dbLoadHeadersViaTakerViaGraded(Connection connWeb, Connection connPS, int takenByEmpNum, boolean graded, String sortBy)
    {
        Secretary.startFxn("exam", "ExamDAO.dbLoadHeadersViaTakerViaGraded(connWeb, " + takenByEmpNum + ", " + graded + ", " + sortBy + ")");
        String select = "SELECT"
            + " e.exam_num, e.exam_name, e.point_value, et.take_num, et.date_submitted, et.emp_num, et.graded, et.final_grade, "
            + " et.graders_comment"
            + " FROM ses_EXAM e, ses_EXAM_TAKE et WHERE"
            + " et.emp_num = " + takenByEmpNum + " AND ";
        if(graded) select += " et.graded = 1 AND";
        else select += " et.graded = 0 AND";
        select += " e.exam_num = et.exam_num ORDER BY ";
        Vector v = new Vector();
        ExamVO temp = null;
        if(sortBy.equalsIgnoreCase("taker"))
            //select += "ui.name_last";
            select += "e.exam_num";
        else if((sortBy.equalsIgnoreCase("take_num")) 
            || (sortBy.equalsIgnoreCase("graders_comment"))
            || (sortBy.equalsIgnoreCase("date_submitted"))
            )
            select += "et." + sortBy;
        else if(sortBy.equals("final_grade"))
            select += "(cast(et.final_grade as float) / cast(e.point_value as float))";
        else
            select += "e." + sortBy;
        Secretary.write("exam", select);
        Statement stmt = null;
        try{
            stmt = connWeb.createStatement();
            ResultSet rs = stmt.executeQuery(select);
            while(rs.next())
            {
                temp = new ExamVO();
                temp.setExamNum(rs.getInt("exam_num"));
                temp.setExamName(rs.getString("exam_name").trim());
                temp.setPointValueTotal(rs.getInt("point_value"));
                temp.setTakeNum(rs.getInt("take_num"));
                temp.setDateTaken(ConvertTool.getDateFromFloat(rs.getFloat("date_submitted")));
                temp.setTakerEmpNum(rs.getInt("emp_num"));
                temp.setGraded(rs.getBoolean("graded"));
                temp.setPointValueFinalGrade(rs.getInt("final_grade"));
                temp.setGradersComment(rs.getString("graders_comment"));
                v.add(temp);
            }
        }catch(SQLException e){
            Secretary.write("exam", "ExamDAO.dbLoadHeadersViaTakerViaGraded(connWeb, " + takenByEmpNum + ", " + graded + ", " + sortBy + ") SQLException => " + e.getMessage());
        }catch(Exception e){
            Secretary.write("exam", "ExamDAO.dbLoadHeadersViaTakerViaGraded(connWeb, " + takenByEmpNum + ", " + graded + ", " + sortBy + ") Exception => " + e.toString());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                logging.Secretary.write("exam", "ExamDAO.dbLoadHeadersViaTakerViaGraded(connWeb, " + takenByEmpNum + ", " + graded + ", " + sortBy + ") => " + e.getMessage());
            }
        }
        
        Iterator examIterator = v.iterator();
        while(examIterator.hasNext())
        {
            ExamVO ev = (ExamVO)(examIterator.next());
            select = "SELECT FIRST_NAME, LAST_NAME FROM PS_NAMES PS1 WHERE PS1.EMPLID = "
                  + ev.getTakerEmpNum() + " AND PS1.EFFDT = "
                  + "(SELECT MAX(EFFDT) FROM PS_NAMES PS2 WHERE PS1.EMPLID = PS2.EMPLID)";
            Secretary.write("exam", select);
            try{
               stmt = connPS.createStatement();
               ResultSet rs = stmt.executeQuery(select);
               rs.next();
               ev.setTakerFirstName(rs.getString("FIRST_NAME").trim());
               ev.setTakerLastName(rs.getString("LAST_NAME").trim());
               System.out.println(ev.toString());
            }catch(SQLException e){
               Secretary.write("exam", "ExamDAO.dbLoadHeadersTakenAll(connWeb, connPS, "+sortBy+") SQLException => " + e.getMessage());
            }catch(Exception e){
               Secretary.write("exam", "+=+=+= Exception in ExamDAO.dbLoadHeadersTakenAll(connWeb, connPS, "+sortBy+") => " + e.getMessage());
            }finally{
               try{
                   stmt.close();
               }catch(SQLException e){
                   logging.Secretary.write("exam", "ExamDAO.dbLoadHeadersTakenAll(connWeb, connPS, "+sortBy+") => " + e.getMessage());
               }
            }
        }  
        Secretary.endFxn("exam", "ExamDAO.dbLoadHeadersViaTakerViaGraded(connWeb, " + takenByEmpNum + ", " + graded + ", " + sortBy + ") => " + v.size());
        return v.iterator();
    }
   /*
    * LOAD HEADERS FOR ALL EXAMS NOT TAKEN BY THE PROVIDED EMPLOYEE
    * OR WHERE EXAM IS RETAKEABLE
    */
   public Iterator dbLoadHeadersNotTakenBy(Connection connWeb, long empID)
   {  
      Secretary.startFxn("exam", "ExamDAO.dbLoadHeadersNotTakenBy(connWeb, "+empID+")");
      this.dbDeleteOldOpenExams(connWeb);
      String query = "SELECT "
                 + " e.exam_num, e.exam_name, e.date_created, "
                 + " e.display_after_taking"
                 + " FROM ses_EXAM e WHERE"
                 + " e.is_active = 1"
                 + " AND ((e.exam_num NOT IN"
                 + " (SELECT exam_num from ses_EXAM_TAKE WHERE emp_num = " + empID + "))"
                 + " OR (can_retake = 1))";
      Secretary.write("exam", query);
      Vector v = new Vector();
      ExamVO temp = null;
      Statement stmt = null;
      try{
         stmt = connWeb.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         while(rs.next()){
             temp = new ExamVO();
             temp.setExamNum(rs.getInt("exam_num"));
             temp.setExamName(rs.getString("exam_name").trim());
             temp.setCanRetake(true);
             temp.setDateCreated(ConvertTool.getDateFromFloat(rs.getFloat("date_created")));
             temp.setDispAfterTaking(rs.getInt("display_after_taking"));
             temp.setIsActive(true);
             v.add(temp);
         }
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadHeadersNotTakenBy(connWeb, "+empID+") " + e.getMessage());
      }catch(Exception e){
         Secretary.write("exam", "+=+=+= Exception in ExamDAO.dbLoadHeadersNotTakenBy(connWeb, "+empID+") => " + e.toString());
      }finally{
          try{
            stmt.close();
          }catch(SQLException e){
            logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadHeadersNotTakenBy(connWeb, "+empID+") => " + e.getMessage());
          }
      }
      Secretary.endFxn("exam", "ExamDAO.dbLoadHeadersNotTakenBy(connWeb, "+empID+") => " + v.size());
      return v.iterator();
   }
   
   
   
   /*
    * Load an exam from the DB based on the provided examNum
    */
   public boolean dbLoadVO(Connection connWeb, int examNum)
   {
      Secretary.startFxn("exam", "ExamDAO.dbLoadVO(connWeb, "+examNum+")");
      boolean toReturn = false;
      ResultSet rs;
      this.examVO = new ExamVO();
      this.examVO.setExamNum(examNum);
      if(!dbLoadHeader(connWeb) || !dbLoadBody(connWeb)) toReturn = false;
      //examVO.logExam();
      Secretary.endFxn("exam", "ExamDAO.dbLoadVO(connWeb, "+examNum+") => " + toReturn);
      return true;
   }
   
   /*
    * Load the full header of the current examVO.examNum
    * Called from this.dbLoadVO()
    */
   private boolean dbLoadHeader(Connection connWeb)
   {
      //Secretary.startFxn("exam", "ExamDAO.dbLoadHeader(connWeb)");
      boolean toReturn = false;
      String select = "SELECT "
                 + " e.exam_name, e.ps_num, e.creator_emp_num,"
                 + " e.date_created, e.date_last_mod, e.is_active, "
                 + " e.display_after_taking, e.point_value, e.can_retake"
                 + " FROM ses_EXAM e WHERE"
                 + " e.exam_num = " + examVO.getExamNum();
      Statement stmt = null;
      try{
         stmt = connWeb.createStatement();
         ResultSet rs = stmt.executeQuery(select);
         if((rs.next())){
            examVO.setExamName(rs.getString("exam_name").trim());
            String psNum = rs.getString("ps_num");
            if(psNum != null)
               examVO.setPsID(psNum.trim());
            examVO.setCreatorEmpNum(rs.getInt("creator_emp_num"));
            examVO.setDateCreated(ConvertTool.getDateFromFloat(rs.getFloat("date_created")));
            examVO.setDateLastMod(ConvertTool.getDateFromFloat(rs.getFloat("date_last_mod")));
            examVO.setPointValueTotal(rs.getInt("point_value"));
            examVO.setCanRetake(rs.getInt("can_retake"));
            examVO.setIsActive(rs.getInt("is_active"));
            examVO.setDispAfterTaking(rs.getInt("display_after_taking"));
            toReturn = true;
         }
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadHeader(connWeb) => " + e.getMessage());
      }finally{
          try{
            stmt.close();
          }catch(SQLException e){
            logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadHeader(connWeb) => " + e.getMessage());
          }
      }
      //Secretary.endFxn("exam", "ExamDAO.dbLoadHeader(connWeb) => " + toReturn);
      return toReturn;
   }
   
   
   /*
    * Load the full body of the current examVO.examNum
    * Called from this.dbLoadVO() && dbLoadTakenVO()
    */
   private boolean dbLoadBody(Connection connWeb){
      Secretary.startFxn("exam", "ExamDAO.dbLoadBody(connWeb)");
      boolean toReturn = false;
      int eLoc, qNum, pointValue;
      //String entryTypeCode, entryTypeName, entryTypeClass;
      Class theVOClass;
      ExamEntryVO tempVO;
      long insertTime = 0;
      String query = "SELECT ee.insert_time, ee.exam_loc, ee.question_num, "
                 + " ee.entry_type_code, ee.point_value, et.entry_type_name, et.class_name, et.self_grading"
                 + " FROM ses_EXAM_ENTRY ee, ses_ENTRY_TYPE et WHERE exam_num = " + examVO.getExamNum()
                 + " AND ee.entry_type_code = et.entry_type_code"
                 + " ORDER BY ee.exam_loc";
      Statement stmt = null;
      try{
         stmt = connWeb.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         while(rs.next()){
            insertTime = rs.getLong("insert_time");
            eLoc = rs.getInt("exam_loc");
            qNum = rs.getInt("question_num");
            EntryType et = new EntryType();
            et.setCode(rs.getString("entry_type_code").trim());
            et.setName(rs.getString("entry_type_name").trim());
            et.setClassName(rs.getString("class_name").trim());
            et.setSelfGrading(rs.getInt("self_grading"));
            pointValue = rs.getInt("point_value");
            /*
             * 1) Create appropriate entryDAO
             * 2) Load entryDAO.entryVO from DB
             * 3) Append entryDAO.entryVO to examVO
             */
            ExamEntryDAO examEntryDAO = createExamEntryDAO(et.getClassName());
            theVOClass = Class.forName("exam.vos." + et.getClassName() + "VO");
            tempVO = (ExamEntryVO)theVOClass.newInstance();
            tempVO.setInsertTime(insertTime);
            tempVO.setExamLoc(eLoc);
            tempVO.setExamNum(examVO.getExamNum());
            tempVO.setQuestionNum(qNum);
            tempVO.setEntryType(et);
            if(tempVO instanceof QuestionVO)
                ((QuestionVO)tempVO).setPointValueTotal(pointValue);
            if(this.examVO.getTakeNum() != 0)
                tempVO.setTakeNum(this.examVO.getTakeNum());
            examEntryDAO.setVO(tempVO);
            examEntryDAO.dbLoadVO(connWeb);
            //examEntryDAO.getVO().logValues();
            examVO.addEntry(examEntryDAO.getVO());
            //examEntryDAO.dropVO();
         }//END while(rs.next())
         toReturn = true;
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadBody(connWeb)=> " + e.getMessage());
      }catch(ClassNotFoundException e){
         Secretary.write("exam", "+=+=+= ClassNotFoundException in ExamDAO.dbLoadBody(connWeb)=> " + e.getMessage());
      }catch(InstantiationException e){
         Secretary.write("exam", "InstantiationException in ExamDAO.dbLoadBody(connWeb)=> " + e.getMessage());
      }catch(IllegalAccessException e){
         Secretary.write("exam", "llegalAccessException in ExamDAO.dbLoadBody(connWeb)=> " + e.getMessage());
      }finally{
          try{
            stmt.close();
            //Secretary.endFxn("exam", "ExamDAO.dbLoadBody(connWeb) => " + toReturn);
          }catch(SQLException e){
            logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadBody(connWeb) => " + e.getMessage());
          }
      }
      //examVO.figureIsSelfGrading();
      Secretary.startFxn("exam", "ExamDAO.dbLoadBody(connWeb)");
      return toReturn;
   }
   
   /*
    * Load a taken exam based on the provided takeNum into this.examVO
    */
   public boolean dbLoadTakenVO(Connection connWeb, int takeNum)
   {
      Secretary.startFxn("exam", "ExamDAO.dbLoadTakenVO(connWeb, " + takeNum + ")");
      ResultSet rs;
      examVO = new ExamVO();
      examVO.setTakeNum(takeNum);
      boolean toReturn = false;
      if(dbLoadHeaderTaken(connWeb) && dbLoadBody(connWeb) && dbLoadEmpAnswers(connWeb))
          toReturn = true;
      Secretary.endFxn("exam", "ExamDAO.dbLoadTakenVO(connWeb, " + takeNum + ") => " + toReturn);
      return toReturn;
   }

    /*
     * Load a detailed header of the examVO currently being held.
     * Called from dbLoadTakenVO
     * REQUIRE: this.examVO has been set.
     *          this.examVO.takeNum has been set.
     */
    private boolean dbLoadHeaderTaken(Connection connWeb)
    {
        Secretary.startFxn("exam", "ExamDAO.dbLoadHeaderTaken(connWeb)");
        boolean toReturn = false;
        String select = "SELECT "
            + "e.exam_num, e.ps_num, e.exam_name, e.point_value, "
            + "et.date_submitted, et.emp_num, et.graded, et.points_extra, "
            + "et.final_grade, et.graders_comment "
            + "FROM ses_EXAM e, ses_EXAM_TAKE et WHERE "
            + "et.take_num = " + examVO.getTakeNum()
            + " AND et.exam_num = e.exam_num";
        //System.out.println(select);
        Statement stmt = null;
        try{
            stmt = connWeb.createStatement();
            ResultSet rs = stmt.executeQuery(select);
            if(rs.next())
            {
               examVO.setExamNum(rs.getInt("exam_num"));
               String psNum = rs.getString("ps_num").trim();
               if(psNum != null)
                  examVO.setPsID(psNum.trim());
               examVO.setExamName(rs.getString("exam_name").trim());
               examVO.setPointValueTotal(rs.getInt("point_value"));
               examVO.setDateTaken(ConvertTool.getDateFromFloat(rs.getFloat("date_submitted")));
               examVO.setTakerEmpNum(rs.getInt("emp_num"));
               examVO.setGraded(rs.getBoolean("graded"));
               examVO.setPointsExtra(rs.getInt("points_extra"));
               examVO.setPointValueFinalGrade(rs.getInt("final_grade"));
               examVO.setGradersComment(rs.getString("graders_comment"));
               toReturn = true;
            }
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadHeaderTaken(connWeb) => " + e.getMessage());
        }catch(NullPointerException e){
           Secretary.write("exam", "+=+=+= NullPointerException in ExamDAO.dbLoadHeaderTaken(connWeb) => " + e.getMessage());
        }
        finally{
            try{
                stmt.close();
            }catch(SQLException e){
                logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadHeaderTaken(connWeb) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "ExamDAO.dbLoadHeaderTaken(connWeb) => " + toReturn);
        return toReturn;
    }
   /*
    * Load the employees answers for this.examVO.
    * Called from dbLoadTakenVO
    * REQUIRE: dbLoadHeaderTaken & dbLoadBody have been called
    */
    private boolean dbLoadEmpAnswers(Connection connWeb)
    {
       Secretary.startFxn("exam", "ExamDAO.dbLoadEmpAnswers(connWeb)");
       int numEntries = this.examVO.getNumEntries();
       ExamEntryVO tempVO;
       for(int x = 1; x <= numEntries; x++)
       {
           tempVO = examVO.getEntryAtLoc(x);
           ExamEntryDAO examEntryDAO = createExamEntryDAO(tempVO.getEntryTypeClass());
           examEntryDAO.setVO(tempVO);
           if(examEntryDAO instanceof QuestionDAO)
               ((QuestionDAO)examEntryDAO).dbLoadEmpAnswer(connWeb);
       }
       Secretary.endFxn("exam", "ExamDAO.dbLoadEmpAnswers(connWeb)");
       return true;
    }
   
   /**
    * METHOD NAME: dbLoadMaxExamNum
    * ARGUMENTS: a database connection to the exam database
    * REQUIRE: n/a
    * FUNCTION: Load the maximum exam number from the database
    * RETURN: the max existing exam number
    */    
    public int dbLoadMaxExamNum(Connection connWeb)
    {
        Secretary.startFxn("exam", "ExamDAO.dbLoadMaxExamNum(connWeb)");
        String select = "SELECT MAX(exam_num) FROM ses_EXAM";
        int max = 0;
        Statement stmt = null;
        try{
            stmt = connWeb.createStatement();
            ResultSet rs = stmt.executeQuery(select);
            if(rs.next()) 
                max = rs.getInt(1);
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadMaxExamNum(connWeb) => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadMaxExamNum(connWeb) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "ExamDAO.dbLoadMaxExamNum(connWeb) => " + max);
        return max;
    }
   
   /**
    * METHOD NAME: dbLoadMaxPsID
    * ARGUMENTS: a database connection to the exam database
    * REQUIRE: n/a
    * FUNCTION: Load the maximum Peoplesoft ID from the database and
    *           remove the letters, returning the number only.
    * RETURN: the max PSID number
    */    
    public int dbLoadMaxPsID(Connection connWeb)
    {
        Secretary.startFxn("exam", "ExamDAO.dbLoadMaxPsID(connWeb)");
        String select = "select max(ps_num) from ses_EXAM";
        int max = 0;
        Statement stmt = null;
        try{
            stmt = connWeb.createStatement();
            ResultSet rs = stmt.executeQuery(select);
            if(rs.next())
            {
                String temp = rs.getString(1).trim();
                Integer t = new Integer(temp.substring(3).trim());
					max = t.intValue();       
            }
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadMaxPsID(connWeb) => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadMaxPsID(connWeb) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "ExamDAO.dbLoadMaxPsID(connWeb) => " + max);
        return max;
    }
    
    
    /*
     * Load the max take_num from the EXAM_TAKE table.
     * This will be used to determine the takeNum to be assigned to new 
     *      exam takes.
     */
    public int dbLoadMaxTakeNum(Connection connWeb)
    {
        Secretary.startFxn("exam", "ExamDAO.dbLoadMaxTakeNum(connWeb)");
        String selectMax = "SELECT MAX(take_num) FROM ses_EXAM_TAKE";
        int count = 0;
        Statement stmt = null;
        try{
            stmt = connWeb.createStatement();
            ResultSet rs = stmt.executeQuery(selectMax);
            if(rs.next())
                count = rs.getInt(1);
        }catch(SQLException e){
            Secretary.write("exam", "ExamDAO.dbLoadMaxTakeNum(connWeb) Exception => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadMaxTakeNum(connWeb) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "ExamDAO.dbLoadMaxTakeNum(connWeb) => " + count);
        return count;
    }

    /*
     * Load the number of times this.examVO has been taken.
     * REQUIRE: this.examVO.examNum is set.
     */
    public int dbLoadNumTakes(Connection connWeb)
    {
        Secretary.startFxn("exam", "ExamDAO.dbLoadNumTakes(connWeb)");
        String select = "SELECT COUNT(take_num) FROM ses_EXAM_TAKE WHERE exam_num = " + this.examVO.getExamNum();
        int num = 0;
        Statement stmt = null;
        try{
            stmt = connWeb.createStatement();
            ResultSet rs = stmt.executeQuery(select);
            if(rs.next())
                num = rs.getInt(1);
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadNumTakes(connWeb) => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbLoadNumTakes(connWeb) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "ExamDAO.dbLoadNumTakes(connWeb) => " + num);
        return num;
    }
    /********************************************************************
     * END LOADS
     ********************************************************************/
    
    
    /********************************************************************
     * UPDATES
     ********************************************************************/
    
    /*
     * Update the point value of this examVO in the DB
     * Called when an entry is deleted from the exam.
     * Replacing with dbUpdateHeader.
     */
    /*
    public boolean dbUpdatePointValueTotal(Connection connWeb){
        Secretary.startFxn("exam", "ExamDAO.dbUpdatePointValueTotal(connWeb)");
        String update = "UPDATE ses_EXAM SET "
                      + "point_value = " + examVO.getPointValueTotal()
                      + " WHERE exam_num = " + examVO.getExamNum();
        boolean toReturn = false;
        Statement stmt = null;
        try{
            stmt = connWeb.createStatement();
            stmt.executeUpdate(update);
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "ExamDAO.dbUpdatePointValueTotal(connWeb) SQLException =>" + e.getMessage());
        }catch(Exception e){
            Secretary.write("exam", "ExamDAO.dbUpdatePointValueTotal(connWeb) Exception =>" + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbUpdatePointValueTotal(connWeb) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "ExamDAO.dbUpdatePointValueTotal(connWeb)");
        return toReturn;
    }
    */
    
 
    /*
     * THIS FUNCTION WILL BE CALLED WHENEVER AN EXAM IS GRADED.
     * BECAUSE AN INSTRUCTOR MAY GO INTO AN EXAM THAT IS ALREADY GRADED
     *   AND MODIFY THE GRADE, WE MUST FOLLOW THESE STEPS:
     * 1) Check PS to see if this exam is already in it.
     * 2) IF this take is already in PS, modify the PS fields.
     * 3) ELSE (this exam take is not in PS), insert the information into PS.
     */
   public void dbUpdatePSGrade(Connection connPS)
   {
      Secretary.startFxn("exam", "ExamDAO.dbUpdatePSGrade(connPS)");
      boolean alreadyThere = false;
      String count = "SELECT COUNT(*) FROM PS_ACCOMPLISHMENTS "
         + "WHERE EMPLID = " + this.examVO.getTakerEmpNum() + " "
         + "AND ACCOMPLISHMENT = '" + this.examVO.getPsID() + "';";
      Statement stmt = null;
      try{
         stmt = connPS.createStatement();
         ResultSet rs = stmt.executeQuery(count);
         if(rs.next())
         {
            int c = rs.getInt(1);
            if(c > 0)
            {
               // UPDATE SCORE
               String upd = "UPDATE PS_ACCOMPLISHMENTS SET SCORE = " 
                     + this.examVO.getPointValueFinalGrade()
                     + ", GPA = " + this.examVO.getGradePercent()
                     + "WHERE EMPLID = " + this.examVO.getTakerEmpNum() + " "
                     + "AND ACCOMPLISHMENT = '" + this.examVO.getPsID() + "';";
               Secretary.write("exam", upd);
               stmt.executeUpdate(upd);
            }
            else
            {
               // INSERT
               String insert = "INSERT INTO PS_ACCOMPLISHMENTS ( "
                     + "EMPLID, ACCOMPLISHMENT, DT_ISSUED, MAJOR_CODE, SCORE, PASSED, LICENSE_NBR, ISSUED_BY, STATE, COUNTRY, "
                     + "LICENSE_VERIFIED, RENEWAL, NATIVE_LANGUAGE, TRANSLATOR, TEACHER, SPEAK_PROFICIENCY, "
                     + "READ_PROFICIENCY, WRITE_PROFICIENCY, MANDATE, MANDATE_FUNCTION, MAJOR, "
                     + "IPE_SW, AVERAGE_GRADE, EDUCATOR, GRADUATE_INDICATOR, SCHOOL_CODE, SCHOOL, STATE_OTHER, "
                     + "COUNTRY_OTHER, TERMINAL_DEGREE, PRACTIC_GRADE_GER, THEORY_GRADE_GER, GRANTOR, BONUS_AMOUNT_FRA, "
                     + "GVT_CREDIT_HOURS, GVT_CRED_HRS_TYPE, GPA, YR_ACQUIRED, EDUC_LVL_AUS, APS_HEDUC_CD_AUS, "
                     + "APS_MINOR_CODE_AUS, APS_MINOR_AUS, NVQ_STATUS, NVQ_CAND_NBR, FP_SUBJECT_CD, "
                     + "FACULTY_CODE, DESCR, SUBFACULTY_CODE, SUBFACULTY_NAME, MAJOR_CATEGORY "
                     + ") VALUES ("
                     + this.examVO.getTakerEmpNum() + ", '"
                     + this.examVO.getPsID() + "', '"
                     + ConvertTool.dateToYYYYMMDD(this.examVO.getDateTaken())
                     + "', ' ', " + this.examVO.getPointValueFinalGrade()
                     + ", 'N', ' ', ' ', ' ', ' ', "
                     + "' ', ' ', ' ', ' ', ' ', ' ', "
                     + "' ', ' ', ' ', ' ', ' ',"
                     + "' ', ' ', ' ', ' ', ' ', ' ', ' ',"
                     + "' ', ' ', ' ', ' ', ' ', 0,"
                     + "' ', ' ', " + this.examVO.getGradePercent() + ", 0, ' ', ' ', "
                     + "' ', ' ', ' ', ' ', ' '," +
                     " ' ', ' ', ' ', ' ', ' ')";
               Secretary.write("exam", insert);
               stmt.execute(insert);
            }
         }
      }catch(SQLException e){
         Secretary.write("exam", "ExamDAO.dbUpdatePSGrade(connPS) SQLException => " + e.getMessage());
      }catch(Exception e){
         Secretary.write("exam", "ExamDAO.dbUpdatePSGrade(connPS) Exception => " + e.getMessage());
      }finally{
         try{
            stmt.close();
         }catch(SQLException e){
            logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbUpdatePSGrade(connPS) => " + e.getMessage());
         }
         Secretary.endFxn("exam", "ExamDAO.dbUpdatePSGrade(connPS)");
      }
   }
   
    /*
     * Update the graded variable and the graders_comment in the DB
     * Called from BeanAdmin.examProcessGrading()
     */
    public boolean dbUpdateGradersComment(Connection connWeb)
    {
        Secretary.startFxn("exam", "ExamDAO.dbUpdateGradersComment(connWeb)");
        String dbUpdateStr;
        PreparedStatement dbUpdateStmt = null;
        dbUpdateStr = "UPDATE ses_EXAM_TAKE SET graders_comment = ?, graded = ? WHERE take_num = ?";
        String upd = "UPDATE ses_EXAM_TAKE SET graders_comment = \""
                   + examVO.getGradersComment()
                   + "\", graded = " + examVO.isGraded()
                   + "WHERE take_num = " + examVO.getTakeNum();
        Secretary.write("exam", upd);
        boolean toReturn = false;
        try{
            dbUpdateStmt = connWeb.prepareStatement(dbUpdateStr);
            dbUpdateStmt.setString(1, examVO.getGradersComment());
            dbUpdateStmt.setBoolean(2, examVO.isGraded());
            dbUpdateStmt.setInt(3, examVO.getTakeNum());
            dbUpdateStmt.executeUpdate();
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "ExamDAO.dbUpdateGradersComment() SQLException => " + e.getMessage());
        }catch(Exception e){
            Secretary.write("exam", "ExamDAO.dbUpdateGradersComment() Exception => " + e.getMessage());
        }finally{
            try{
                dbUpdateStmt.close();
            }catch(SQLException e){
                logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbUpdateGradersComment(connWeb) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "ExamDAO.dbUpdateGradersComment(connWeb) => " + toReturn);
        return toReturn;       
    }
    
   /*
    * Update the final_grad and the graded field in ses_EXAM_TAKE
    */
    private boolean dbUpdatePointValueFinalGrade(Connection connWeb)
    {
        Secretary.startFxn("exam", "ExamDAO.dbUpdatePointValueFinalGrade(connWeb)");
        String dbUpdatePVStr = "UPDATE ses_EXAM_TAKE SET "
                             + "final_grade = " + examVO.getPointValueFinalGrade() 
                             + ", points_extra = " + examVO.getPointsExtra()
                             + ", graded = ";
        if(examVO.isGraded())
           dbUpdatePVStr += "1 ";
        else
           dbUpdatePVStr += "0 ";
        dbUpdatePVStr += "WHERE take_num = " + examVO.getTakeNum();
        Secretary.write("exam", dbUpdatePVStr);
        boolean toReturn = false;
        Statement stmt = null;
        try{
            stmt = connWeb.createStatement();
            stmt.executeUpdate(dbUpdatePVStr);
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "ExamDAO.dbUpdatePointValueFinalGrade(connWeb) SQLException => " + e.getMessage());
        }catch(Exception e){
            Secretary.write("exam", "ExamDAO.dbUpdatePointValueFinalGrade(connWeb) Exception => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbUpdatePointValueFinalGrade(connWeb) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "ExamDAO.dbUpdatePointValueFinalGrade(connWeb)");
        return toReturn;
    }
    
    /*
     * Update the number of points earned by the exam taker
     * Called from BeanAdmin.examProcessGrading()
     */
    public void dbUpdatePointsEarned(Connection connWeb){
        Secretary.startFxn("exam", "ExamDAO.dbUpdatePointsEarned(connWeb)");
        ExamEntryVO entryVO;
        for(int i=1; i <= this.examVO.getNumEntries(); i++){
            entryVO = (ExamEntryVO)(examVO.getEntryAtLoc(i));
            if(entryVO instanceof QuestionVO){
                QuestionDAO questionDAO = ((QuestionDAO)(createExamEntryDAO(entryVO.getEntryTypeClass())));
                questionDAO.setVO(entryVO);
                questionDAO.dbUpdatePointsEarned(connWeb);
            }
        }
        this.dbUpdatePointValueFinalGrade(connWeb);
        Secretary.endFxn("exam", "ExamDAO.dbUpdatePointsEarned(connWeb)");
    }
    
    /*
     * Update this exam's header information in the DB
     */
   public boolean dbUpdateHeader(Connection connWeb)
   {
      Secretary.startFxn("exam", "ExamDAO.dbUpdateHeader(connWeb)");
      /*
       * Update SES
       */
      String dbUpdateHeaderStr;
      PreparedStatement dbUpdateHeaderStmt = null;
      dbUpdateHeaderStr = "UPDATE ses_EXAM SET exam_name = ?, date_last_mod = ?, "
         + "is_active = ?, display_after_taking = ?, point_value = ?, can_retake =  ? WHERE exam_num = ?";
      boolean toReturn = false;
      try{
         dbUpdateHeaderStmt = connWeb.prepareStatement(dbUpdateHeaderStr);
         dbUpdateHeaderStmt.setString(1, examVO.getExamName());
         dbUpdateHeaderStmt.setFloat(2, ConvertTool.getFloatFromDate(examVO.getDateLastMod()));
         if(examVO.getIsActive()) dbUpdateHeaderStmt.setInt(3, 1);
         else dbUpdateHeaderStmt.setInt(3, 0);
         if(examVO.getDispAfterTaking()) dbUpdateHeaderStmt.setInt(4, 1);
         else dbUpdateHeaderStmt.setInt(4, 0);
         //dbUpdateHeaderStmt.setInt(5,  examVO.getExamNum());
         dbUpdateHeaderStmt.setInt(5, examVO.getPointValueTotal());
         if(examVO.getCanRetake())
            dbUpdateHeaderStmt.setInt(6, 1);
         else
            dbUpdateHeaderStmt.setInt(6, 0);
         dbUpdateHeaderStmt.setInt(7, examVO.getExamNum());
         dbUpdateHeaderStmt.executeUpdate();
         toReturn = true;
      }catch(SQLException e){
         Secretary.write("exam", "ExamDAO.dbUpdateHeader() SQLException => " + e.getMessage());
      }catch(Exception e){
         Secretary.write("exam", "ExamDAO.dbUpdateHeader() Exception => " + e.getMessage());
      }finally{
         try{
            dbUpdateHeaderStmt.close();
         }catch(SQLException e){
            logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbUpdateHeader(connWeb) => " + e.getMessage());
         }
      }
      Secretary.endFxn("exam", "ExamDAO.dbUpdateHeader(connWeb) => " + toReturn);
      return toReturn;
   }
   
   /*
    * Update the header info in PS
    *    The DESCR field and the DESCRSHORT field
    */
   public void dbUpdateHeaderPS(Connection connPS, boolean toDelete)
   {
      Secretary.startFxn("exam", "ExamDAO.dbUpdateHeader(connPS, " + toDelete + ")");
      /*
       * Update PS
       */
      String dbUpdatePsHeaderStr;
      PreparedStatement dbUpdatePsHeaderStmt = null;
      String nameShort = "";
      if(toDelete)
      {
         nameShort = "DELETED";
      }
      else
      {
         if(examVO.getExamName().length() < 10)
            nameShort = examVO.getExamName().substring(0, examVO.getExamName().length());
         else
            nameShort = examVO.getExamName().substring(0, 9);
      }
      dbUpdatePsHeaderStr = "UPDATE PS_ACCOMP_TBL SET DESCR = ?, DESCRSHORT = ? WHERE ACCOMPLISHMENT = ?";
      Secretary.write("exam", "UPDATE PS_ACCOMP_TBL SET DESCR = '" + examVO.getExamName() 
                + "', DESCRSHORT = " + nameShort 
                + " WHERE ACCOMPLISHMENT = '" 
                + examVO.getPsID().trim() + "'");
      try{
         dbUpdatePsHeaderStmt = connPS.prepareStatement(dbUpdatePsHeaderStr);
         dbUpdatePsHeaderStmt.setString(1, examVO.getExamName());
         dbUpdatePsHeaderStmt.setString(2, nameShort);
         dbUpdatePsHeaderStmt.setString(3, examVO.getPsID());
         dbUpdatePsHeaderStmt.execute();
      }catch(SQLException e){
         Secretary.write("exam", "ExamDAO.dbUpdateHeader(connPS) SQLException => " + e.getMessage());
      }catch(Exception e){
         Secretary.write("exam", "ExamDAO.dbUpdateHeader(connPS) Exception => " + e.getMessage());
      }finally{
         try{
            dbUpdatePsHeaderStmt.close();
         }catch(SQLException e){
            logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbUpdateHeaderPS(connPS, " + toDelete + ") => " + e.getMessage());
         }
      }
      Secretary.endFxn("exam", "ExamDAO.dbUpdateHeader(connPS, " + toDelete + ")");
   }
       
   /*
    * Update an entry in the DB.
    * This is done when an ses_EXAM is being modified.
    * This is done by deleting the entry from the DB and then inserting the entry in memory.
    * The reason this is done as such is because certain entries may have more or less
    *    DB entries for choices or questions or solutions
    */
   public void dbUpdateEntry(Connection connWeb, int examLoc)
   {
        Secretary.startFxn("exam", "ExamDAO.dbUpdateEntry(connWeb, " + examLoc + ")");
        ExamEntryVO entryVO = (ExamEntryVO)(examVO.getEntryAtLoc(examLoc));
        ExamEntryDAO entryDAO = createExamEntryDAO(entryVO.getEntryTypeClass());
        entryDAO.setVO(entryVO);
        try
        {
            connWeb.setAutoCommit(false);
            entryDAO.dbUpdateVO(connWeb);
            entryDAO.dbUpdateEE(connWeb);
            connWeb.commit();
            connWeb.setAutoCommit(true);
        }catch(SQLException e)
        {
            try{
                connWeb.rollback();
                connWeb.setAutoCommit(true) ;
            }catch(SQLException e1)
            {
                logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbUpdateEntry(connWeb) => " + e.getMessage());
            }
            logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbUpdateEntry(connWeb) => " + e.getMessage());
        }
      Secretary.endFxn("exam", "ExamDAO.dbUpdateEntry(connWeb, " + examLoc + ")");
   }
   /********************************************************************
    * END UPDATES
    ********************************************************************/
   
   /********************************************************************
    * DELETES
    ********************************************************************/
    public void dbDeleteExam(Connection connWeb, Connection connPS)
    {
        Secretary.startFxn("exam", "ExamDAO.dbDeleteExam(" + connWeb+", "+connPS + ")");
        //Secretary.write("exam", "examEntryDAOs.size() = " + this.examEntryDAOs.size());
        PreparedStatement ps = null;
        try
        {
            connWeb.setAutoCommit(false);
            connPS.setAutoCommit(false);
            // DELETE ALL ENTRIES FROM ENTRY TABLES
            for(int x = 0; x < this.examEntryDAOs.size(); x++)
            {
                ExamEntryDAO tempDAO = (ExamEntryDAO)(examEntryDAOs.elementAt(x));
                tempDAO.dbDeleteFullExam(connWeb, this.examVO.getExamNum());
            }
            // DELETE ALL ENTRIES FROM ses_EXAM_ENTRY
            if(this.examEntryDAOs.size() > 0)
            {
                ((ExamEntryDAO)(examEntryDAOs.elementAt(0))).dbDeleteFullExamEEs(connWeb, examVO.getExamNum());
            }
            this.dbDeleteHeader(connWeb);
            logging.Secretary.write("exam", "DELETE FROM PS_ACCOMP_TBL WHERE ACCOMPLISHMENT = '" + examVO.getPsID() + "'");
            String del = "DELETE FROM PS_ACCOMP_TBL WHERE ACCOMPLISHMENT = ?";
            ps = connPS.prepareStatement(del);
            ps.setString(1, examVO.getPsID().trim());
            ps.execute();
            connWeb.commit();
            connPS.commit();
            connWeb.setAutoCommit(true);
            connPS.setAutoCommit(true);
        }catch(SQLException e)
        {
            try{
                connWeb.rollback();
                connWeb.setAutoCommit(true) ;
            }catch(SQLException e1)
            {
                logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbDeleteExam(" + connWeb+", "+connPS + ") => " + e.getMessage());
            }
            logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbDeleteExam(" + connWeb+", "+connPS + ") => " + e.getMessage());
        }finally{
            try{
                ps.close();
            }catch(SQLException e){
                logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbDeleteExam(" + connWeb+", "+connPS + ") => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "ExamDAO.dbDeleteExam(connWeb)");
    }
    public void dbDeleteHeader(Connection connWeb){
        Secretary.startFxn("exam", "ExamDAO.dbDeleteHeader(connWeb)");
        String del = "DELETE FROM ses_EXAM WHERE exam_num = " + examVO.getExamNum();
        Secretary.write("exam", del);
        Statement stmt = null;
        try{
            stmt = connWeb.createStatement();
            stmt.executeUpdate(del);
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbDeleteHeader(): " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbDeleteHeader(connWeb) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "ExamDAO.dbDeleteHeader(connWeb)");
    }
    
   public void dbDeleteEntry(Connection connWeb)
   {
       Secretary.startFxn("exam", "ExamDAO.dbDeleteEntry(connWeb)");
       /*
        * 1) Renumber all entries in DB with examLoc > eLoc
        * 2) Renumber all entries in DB with qNum > eLoc's qNum
        * 3) Delete eLoc
        */
       try
       {
            connWeb.setAutoCommit(false);      
            ExamEntryVO entryToDelete = examVO.getCurrentEntry();            
            ExamEntryDAO entryToDeleteDAO = createExamEntryDAO(entryToDelete.getEntryTypeClass());
            Secretary.write("exam", "-=-=-=-=-=-=-=-=-=-=-=-=- DELETE THIS ENTRY -=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            entryToDelete.logValues();
            Secretary.write("exam", "-=-=-=-=-=-=-=-=-=-=-=- END DELETE THIS ENTRY -=-=-=-=-=-=-=-=-=-=-=-=-=-");
            entryToDeleteDAO.setVO(entryToDelete);
            entryToDeleteDAO.dbDeleteVO(connWeb);
            entryToDeleteDAO.dbDeleteEE(connWeb); 
            Vector fullExam = this.examVO.getFullExamVector();
            Vector tempSection = null;
            ExamEntryVO tempVO = null;
            ExamEntryDAO tempDAO = null;
            for(int x = 0; x < fullExam.size(); x++)
            {
                tempSection = (Vector)(fullExam.elementAt(x));
                for(int y = 0; y < tempSection.size(); y++)
                {
                    tempVO = ((ExamEntryVO)(tempSection.elementAt(y)));
                    if(tempVO.getExamLoc() >= entryToDelete.getExamLoc())
                    {
                        tempDAO = createExamEntryDAO(tempVO.getEntryTypeClass());
                        tempDAO.setVO(tempVO);
                        tempDAO.dbUpdateEE(connWeb);
                    }
                }
            }
            connWeb.commit();
            connWeb.setAutoCommit(true);
        }catch(SQLException e)
        {
            try{
                connWeb.rollback();
                connWeb.setAutoCommit(true) ;
            }catch(SQLException e1)
            {
                Secretary.write("exam", "SQLException in ExamDAO.dbDeleteEntry(connWeb) => " + e.toString());
            }
            Secretary.write("exam", "SQLException in ExamDAO.dbDeleteEntry(connWeb) => " + e.toString());
        }
       Secretary.endFxn("exam", "ExamDAO.dbDeleteEntry(connWeb)");
   }
   
    /*
     * When a user starts an exam, a row gets inserted into the ses_EXAMS_OPEN.
     * When the user submits the exam, this row gets deleted.
     * This makes it possible to prevent the modifications of exams currently 
     *      being taken.
     */
    private void dbDeleteFromExamsOpen(Connection connWeb)
    {
        Secretary.startFxn("exam", "ExamDAO.dbDeleteFromExamsOpen(connWeb)");
        String del = "DELETE FROM ses_EXAMS_OPEN WHERE emp_num = "
            + examVO.getTaker().getEmpNum();
        Secretary.write("exam", del);
        Statement stmt = null;
        try
        {
            stmt = connWeb.createStatement();
            stmt.execute(del);
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbInsertTakenHeader(connWeb) => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbInsertIntoExamsOpen(connWeb) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "ExamDAO.dbDeleteFromExamsOpen(connWeb)");
    }
    
   /*
    * Delete all exams in ses_EXAMS_OPEN 
    *   where the date is older than 2 hrs
    *   and the exam is not continuable.
    */
    private void dbDeleteOldOpenExams(Connection connWeb)
    {
      Secretary.startFxn("exam", "ExamDAO.dbDeleteOldOpenExams(connWeb)");
        // B4 loading active exams, delete clean EXAMS_OPEN table.
      Float oldDate = new Float(ConvertTool.getDateXHoursAgo(2));
      String del = "DELETE FROM ses_EXAMS_OPEN where start_time < " + oldDate.longValue()
                 + " AND exam_num not in("
                 + " (select exam_num from ses_EXAMS_OPEN where exam_num not in"
                 + " (select eo.exam_num from ses_EXAMS_OPEN eo, ses_EXAM_ENTRY e"
                 + " where e.exam_num = eo.exam_num"
                 + " and eo.exam_num not in"
                 + " (select exam_num from ses_EXAM_ENTRY where entry_type_code = 'pb')"
                 + "group by eo.exam_num)))";
      Secretary.write("exam", del);
      Statement stmt = null;
      try{
         stmt = connWeb.createStatement();
         stmt.execute(del);
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbDeleteOldOpenExams(connWeb) => " + e.getMessage());
      }catch(Exception e){
         Secretary.write("exam", "+=+=+= Exception in ExamDAO.dbDeleteOldOpenExams(connWeb) => " + e.toString());
      }finally{
          try{
            stmt.close();
          }catch(SQLException e){
            logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbDeleteOldOpenExams(connWeb) => " + e.getMessage());
          }
      }
      /*
       * Delete all exams in ses_EXAMS_OPEN
       *    where the exam is continuable and that employee number
       *    doesn't have a session open with a saved exam.
       */
      
      String sel = "SELECT emp_num, exam_num FROM ses_EXAMS_OPEN where start_time < " + oldDate.longValue()
                 + " AND exam_num in("
                 + " (select exam_num from ses_EXAMS_OPEN where exam_num not in"
                 + " (select eo.exam_num from ses_EXAMS_OPEN eo, ses_EXAM_ENTRY e"
                 + " where e.exam_num = eo.exam_num"
                 + " and eo.exam_num not in"
                 + " (select exam_num from ses_EXAM_ENTRY where entry_type_code = 'pb')"
                 + "group by eo.exam_num)))";
      Secretary.write("exam", sel);
      try{
         stmt = connWeb.createStatement();
         ResultSet rs = stmt.executeQuery(sel);
         while(rs.next()){
             beans.BeanStudent s = new beans.BeanStudent();
             user.UserVO uv = new user.UserVO();
             uv.setEmpNum(rs.getInt("emp_num"));
             Secretary.write("exam", "emp_num = " + uv.getEmpNum());
             s.setUserVO(uv);
             if(!(s.loadSerializedExam()))
             {
                 // THE EXAM IS NOT SERIALIZED. DELETE FROM TABLE.
                del = "DELETE FROM ses_EXAMS_OPEN where emp_num = " + uv.getEmpNum() 
                    + " AND exam_num = " + rs.getInt("exam_num");
                Secretary.write("exam", del);
                try
                {
                    stmt = connWeb.createStatement();
                    stmt.execute(del);
                }catch(SQLException e){
                    Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbDeleteOldOpenExams(connWeb) => " + e.getMessage());
                }catch(Exception e){
                    Secretary.write("exam", "+=+=+= Exception in ExamDAO.dbDeleteOldOpenExams(connWeb) => " + e.toString());
                }finally{
                  try{
                    stmt.close();
                  }catch(SQLException e){
                    logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbDeleteOldOpenExams(connWeb) => " + e.getMessage());
                  }
                }
           }
         }
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbDeleteOldOpenExams(connWeb) => " + e.getMessage());
      }catch(Exception e){
         Secretary.write("exam", "+=+=+= Exception in ExamDAO.dbDeleteOldOpenExams(connWeb) => " + e.toString());
      }finally{
          try{
            stmt.close();
          }catch(SQLException e){
            logging.Secretary.write("exam", "+=+=+= SQLException in ExamDAO.dbDeleteOldOpenExams(connWeb) => " + e.getMessage());
          }
      }
      
      Secretary.endFxn("exam", "ExamDAO.dbDeleteOldOpenExams(connWeb)");
    }
   /********************************************************************
    * END DELETES
    ********************************************************************/
   /***************************************************************************
    * Go through our examEntryDAOs Vector and see if a DAO of thisDAOClassName
    *    is already created. If so, use this DAO. If not, create an instance
    *    and add it to the Vector.
    * Return the ExamEntryDAO created or found in the Vector.
    ***************************************************************************/
    private ExamEntryDAO createExamEntryDAO(String thisDAOClassName){
        //Secretary.startFxn("exam", "ExamDAO.createExamEntryDAO("+thisDAOClassName+")");
        thisDAOClassName = "exam.daos." + thisDAOClassName + "DAO";
        ExamEntryDAO examEntryDAO = null;
        for(int vl=0; vl < examEntryDAOs.size(); vl++)
        {
            if(examEntryDAOs.elementAt(vl).getClass().getName().compareTo(thisDAOClassName) == 0)
            {
                examEntryDAO = (ExamEntryDAO)examEntryDAOs.elementAt(vl);
            }
        }
        if(examEntryDAO == null){
            try{
                Class theDAOClass = Class.forName(thisDAOClassName);
                examEntryDAO = (ExamEntryDAO)theDAOClass.newInstance();
                examEntryDAOs.add(examEntryDAO);
            }catch(ClassNotFoundException e){
                Secretary.write("exam", "+=+=+= ClassNotFoundException in ExamDAO.createExamEntryDAO(connWeb) => " + e.getMessage());
            }catch(InstantiationException e){
                Secretary.write("exam", "InstantiationException in ExamDAO.createExamEntryDAO(connWeb) => " + e.getMessage());
            }catch(IllegalAccessException e){
                Secretary.write("exam", "llegalAccessException in ExamDAO.createExamEntryDAO(connWeb) => " + e.getMessage());
            }catch(NullPointerException e){
                Secretary.write("exam", "NullPointerException in ExamDAO.createExamEntryDAO(connWeb) => " + e.getMessage());
            }catch(Exception e){
                Secretary.write("exam", "Exception in ExamDAO.createExamEntryDAO(connWeb) => " + e.getMessage());
            }
        }
        //Secretary.endFxn("exam", "ExamDAO.createExamEntryDAO("+thisDAOClassName+")");
        return examEntryDAO;
    }
   
   /********************************************************************
    * GETS & SETS
    ********************************************************************/
   public ExamVO getExamVO(){ return examVO; }
   
   public void setExamVO(ExamVO newExamVO){ examVO = newExamVO; }
   /********************************************************************
    * END GETS & SETS
    ********************************************************************/

   /********************************************************************
    * CONSTRUCTOR & DESTRUCTOR
    ********************************************************************/
   public ExamDAO() 
   {
      Secretary.write("exam", "ExamDAO CONSTRUCTOR");
      this.examEntryDAOs = new Vector();
   }
   /********************************************************************
    * END CONSTRUCTOR & DESTRUCTOR
    ********************************************************************/
}
