/***********************************************************************
* Module:  ExamEntryDAO.java
* Author:  jsandlin
* Purpose: Defines the Class ExamEntryDAO
***********************************************************************/
package exam.daos;
import java.util.*;
import java.sql.*;
import exam.vos.ExamEntryVO;
import exam.EntryType;
import logging.Secretary;
/** Rather than passing vars to the loadFromDB functions, as was done in version 1.0, base the load on the eNum, qNum, and takeNum values held by ExamEntryVO */
public abstract class ExamEntryDAO
{  
    protected ExamEntryVO examEntryVO;
   
   /**************************************************************
    * dbLoadVO loads this ValueObject from the DB
    **************************************************************/
   public abstract boolean dbLoadVO(Connection connWeb);
   
   /**************************************************************
    * dbDeleteFullExam deletes all of this entry type from the
    * appropriate table where exam_num = eNum   
    **************************************************************/
   public abstract boolean dbDeleteFullExam(Connection connWeb, int eNum);
   
   /**************************************************************
    * dbInsertVO inserts this ValueObject into the DB
    **************************************************************/
   public abstract boolean dbInsertVO(Connection connWeb);
   
   /**************************************************************
    * dbDeleteVO deletes this ValueObject from the DB
    **************************************************************/
   public abstract boolean dbDeleteVO(Connection connWeb);
   
   public abstract boolean dbUpdateVO(Connection connWeb);
   public abstract void setVO(ExamEntryVO vo);
   public abstract ExamEntryVO getVO();
   public abstract void dropVO();
      
   public boolean dbLoadEETypeViaCode(Connection connWeb, String code)
   {
      Secretary.startFxn("exam", "ExamEntryDAO.dbLoadEETypeViaCode(" + connWeb + ", "+code+")");
      boolean toReturn = false;
      String query = "SELECT entry_type_name, class_name, self_grading FROM ses_ENTRY_TYPE WHERE entry_type_code = " + code;
      Statement stmt = null;
      try{
         stmt = connWeb.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         rs.next();
         EntryType et = new EntryType();
         et.setCode(code);
         et.setName(rs.getString("entry_type_name"));
         et.setClassName(rs.getString("class_name"));
         int sg = rs.getInt("self_grading");
         if(sg == 1)
            et.setSelfGrading(true);
         else
            et.setSelfGrading(false);
         examEntryVO.setEntryType(et);
         /*
         examEntryVO.setEntryTypeCode(code);
         examEntryVO.setEntryTypeName(rs.getString(1));
         examEntryVO.setEntryTypeClass(rs.getString(2));
         int sg = rs.getInt("self_grading");
         if(sg == 1)
            examEntryVO.setSelfGrading(true);
         else
            examEntryVO.setSelfGrading(false);
          */
         
         toReturn = true;
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in ExamEntryDAO.dBloadEETypeViaCode(" + connWeb + ", " + code + ") => " + e.getMessage());
      }finally{
          try{
            stmt.close();
          }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in ExamEntryDAO.dBloadEETypeViaCode(" + connWeb + ", " + code + ") => " + e.getMessage());
          }
      }
      Secretary.endFxn("exam", "ExamEntryDAO.dbLoadEETypeViaCode(" + connWeb + ", "+code+")");
      return toReturn;
   }
   
   /**
    * METHOD NAME: dbLoadEETypeList
    * ARGUMENTS: a database connection to the exam database
    * REQUIRE: n/a
    * FUNCTION: Load Vector of all possible EntryType objects from the DB
    * RETURN: Vector of EntryType objects
    */
   public Vector dbLoadEETypeList(Connection connWeb){
      Secretary.startFxn("exam", "ExamEntryDAO.dbLoadEETypeList(" + connWeb + ")");
      Vector entryTypes = new Vector();
      boolean toReturn = false;
      String query = "SELECT * FROM ses_ENTRY_TYPE";
      Statement stmt = null;
      try{
         stmt = connWeb.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         while(rs.next()){
            EntryType et = new EntryType();
            et.setCode(rs.getString("entry_type_code"));
            et.setName(rs.getString("entry_type_name"));
            et.setClassName(rs.getString("class_name"));
            int sg = rs.getInt("self_grading");
            et.setSelfGrading(sg);
            entryTypes.add(et);
         }
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in ExamEntryDAO.dbLoadEETypeList(" + connWeb + ") => " + e.getMessage());
      }finally{
          try{
            stmt.close();
          }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in ExamEntryDAO.dbLoadEETypeList(" + connWeb + ") => " + e.getMessage());
          }
      }
      Secretary.endFxn("exam", "ExamEntryDAO.dbLoadEETypeList(" + connWeb + ")");
      return entryTypes;
   }
   public boolean dbInsertEE(Connection connWeb)
    {
        Secretary.startFxn("exam", "ExamEntryDAO.dbInsertEE(" + connWeb + ")");
        boolean toReturn = false;
        int points = 0;
        if(examEntryVO instanceof exam.vos.QuestionVO)
            points = ((exam.vos.QuestionVO)examEntryVO).getPointValueTotal();

        String insert = "INSERT INTO ses_EXAM_ENTRY "
                + "(insert_time, exam_num, exam_loc, question_num, entry_type_code, point_value) "
                + "VALUES ("
                + examEntryVO.getInsertTime() + ", "
                + examEntryVO.getExamNum() + ", "
                + examEntryVO.getExamLoc() + ", "
                + examEntryVO.getQuestionNum() + ", '"
                + examEntryVO.getEntryTypeCode() + "', "
                + points + ");";
        Secretary.write("exam", insert);
        Statement stmt = null;
        try{
            stmt = connWeb.createStatement();
            stmt.executeUpdate(insert);
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in ExamEntryDAO.dbInsertEE(" + connWeb + ") -> " + e.getMessage());
            toReturn = false;
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in ExamEntryDAO.dbInsertEE(" + connWeb + ") -> " + e.getMessage());
            toReturn = false;
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in ExamEntryDAO.dbInsertEE(" + connWeb + ") => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "ExamEntryDAO.dbInsertEE(" + connWeb + ")");
        return toReturn;
    }


   //public abstract void dbIncrementExamLocVO(Connection connWeb);
   public boolean dbIncrementExamLoc(Connection connWeb)
   {
      Secretary.startFxn("exam", "ExamEntryDAO.dbIncrementExamLocEE(" + connWeb + ")");
      boolean toReturn = false;
      /*
      int newExamLoc = examEntryVO.getExamLoc();
      examEntryVO.setExamLoc(newExamLoc - 1);
      toReturn = this.dbDeleteEE(connWeb);
      examEntryVO.setExamLoc(newExamLoc);
      toReturn = toReturn && this.dbInsertEE(connWeb);
      Secretary.endFxn("exam", "ExamEntryDAO.dbIncrementExamLocEE(" + connWeb + ") => " + toReturn);
       */
      return toReturn;
   }
   /** @param examNum */
   public boolean dbUpdateEE(Connection connWeb)
   {
      Secretary.startFxn("exam", "ExamEntryDAO.dbUpdateEE(" + connWeb + ")");
      String dbUpdateEEStr;
      PreparedStatement dbUpdateEEStmt = null;
      boolean toReturn = false;
      int points = 0;
        if(examEntryVO instanceof exam.vos.QuestionVO)
            points = ((exam.vos.QuestionVO)examEntryVO).getPointValueTotal();

      dbUpdateEEStr = "UPDATE ses_EXAM_ENTRY SET "
              + "exam_num = ?, exam_loc = ?, question_num= ?, entry_type_code=?, point_value=? "
              + "WHERE insert_time = ?";
      try{
         dbUpdateEEStmt = connWeb.prepareStatement(dbUpdateEEStr);
         dbUpdateEEStmt.setInt(1, examEntryVO.getExamNum());
         dbUpdateEEStmt.setInt(2, examEntryVO.getExamLoc());
         dbUpdateEEStmt.setInt(3, examEntryVO.getQuestionNum());
         dbUpdateEEStmt.setString(4, examEntryVO.getEntryTypeCode());
         dbUpdateEEStmt.setInt(5, points);
         dbUpdateEEStmt.setLong(6, examEntryVO.getInsertTime());
         dbUpdateEEStmt.executeUpdate();
         toReturn = true;
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in ExamEntryDAO.dbUpdateEE(" + connWeb + ") -> " + e.getMessage());
         toReturn = false;
      }catch(Exception e){
         Secretary.write("exam", "+=+=+= Exception in ExamEntryDAO.dbUpdateEE(" + connWeb + ") -> " + e.getMessage());
         toReturn = false;
      }finally{
          try{
            dbUpdateEEStmt.close();
          }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in ExamEntryDAO.dbUpdateEE(" + connWeb + ") => " + e.getMessage());
          }
      }
      Secretary.endFxn("exam", "ExamEntryDAO.dbUpdateEE(" + connWeb + ")");
      return toReturn;
   }

   public boolean dbDeleteEE(Connection connWeb)
   {
      Secretary.startFxn("exam", "ExamEntryDAO.dbDeleteEE(" + connWeb + ")");
      boolean toReturn = false;
      String delete = "DELETE FROM ses_EXAM_ENTRY"
            + " WHERE insert_time = " + examEntryVO.getInsertTime();
      Secretary.write("exam", delete);
      Statement stmt = null;
      try{
         stmt = connWeb.createStatement();
			stmt.executeUpdate(delete);
         toReturn = true;
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in ExamEntryDAO.dbDeleteEE(" + connWeb + ") -> " + e.getMessage());
         toReturn = false;
      }catch(Exception e){
         Secretary.write("exam", "+=+=+= Exception in ExamEntryDAO.dbDeleteEE(" + connWeb + ") -> " + e.getMessage());
         toReturn = false;
      }finally{
          try{
            stmt.close();
          }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in ExamEntryDAO.dbDeleteEE(" + connWeb + ") => " + e.getMessage());
          }
      }
      Secretary.endFxn("exam", "ExamEntryDAO.dbDeleteEE(" + connWeb + ") => " + toReturn);
      return toReturn;
   }
   
   public boolean dbDeleteFullExamEEs(Connection connWeb, int eNum)
   {
      Secretary.startFxn("exam", "ExamEntryDAO.dbDeleteFullExamEEs(" + connWeb + ", "+eNum+")");
      boolean toReturn = false;
      String delete = "DELETE FROM ses_EXAM_ENTRY WHERE exam_num = " + eNum;
      Secretary.write("exam", delete);
      Statement stmt = null;
      try{
         stmt = connWeb.createStatement();
         stmt.executeUpdate(delete);
         toReturn = true;
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in ExamEntryDAO.dbDeleteFullExamEEs(" + connWeb + ", "+eNum+") => " + e.getMessage());
         toReturn = false;
      }catch(Exception e){
         Secretary.write("exam", "+=+=+= Exception in ExamEntryDAO.dbDeleteFullExamEEs(" + connWeb + ", "+eNum+") => " + e.getMessage());
         toReturn = false;
      }finally{
          try{
            stmt.close();
          }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in ExamEntryDAO.dbDeleteFullExamEEs(" + connWeb + ", "+eNum+") => " + e.getMessage());
          }
      }
      Secretary.endFxn("exam", "ExamEntryDAO.dbDeleteFullExamEEs(" + connWeb + ", "+eNum+") => " + toReturn);
      return toReturn;
   }
   
   public ExamEntryDAO(){}
}