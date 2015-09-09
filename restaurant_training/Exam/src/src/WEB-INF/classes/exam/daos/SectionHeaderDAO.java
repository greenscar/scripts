/***********************************************************************
 * Module:  SectionHeaderDAO.java
 * Author:  jsandlin
 * Purpose: Defines the Class SectionHeaderDAO
 ***********************************************************************/
package exam.daos;
import java.sql.*;
import java.util.*;
import exam.vos.SectionHeaderVO;
import logging.Secretary;

public class SectionHeaderDAO extends ExamEntryDAO
{ 
    public boolean dbInsertVO(Connection conn)
    {
        Secretary.startFxn("exam", "SectionHeaderDAO.dbInsertVO(conn)");
        String dbInsertStr;
        PreparedStatement dbInsertStmt = null;
        boolean toReturn = false;
        dbInsertStr = "INSERT INTO ses_SECTION_HEADER (insert_time, description) VALUES (?, ?)";
        try{
            dbInsertStmt = conn.prepareStatement(dbInsertStr);
            dbInsertStmt.setLong(1, examEntryVO.getInsertTime());
            dbInsertStmt.setString(2, ((SectionHeaderVO)examEntryVO).getValue());
            dbInsertStmt.executeUpdate();
            toReturn  = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in SectionHeaderDAO.dbInsertVO(conn) => " + e.getMessage());
            toReturn  = false;
        }finally{
            try{
                dbInsertStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in SectionHeaderDAO.dbInsertVO(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "SectionHeaderDAO.dbInsertVO(conn) => " + toReturn);
        return toReturn;
    }
   
   
    public boolean dbUpdateVO(Connection conn)
    {
        Secretary.startFxn("exam", "SectionHeaderDAO.dbUpdateVO(conn)");
        boolean toReturn = false;
        String updStr = "UPDATE ses_SECTION_HEADER SET "
                      + "description = ? WHERE insert_time = ?";
        String toLog = "UPDATE ses_SECTION_HEADER SET "
                      + "description = \""
                      + ((SectionHeaderVO)examEntryVO).getValue() + "\""
                      + "WHERE insert_time = " + examEntryVO.getInsertTime();
        Secretary.write("exam", toLog);
        PreparedStatement updStmt = null;
        try{
            updStmt = conn.prepareStatement(updStr);
            updStmt.setString(1, ((SectionHeaderVO)examEntryVO).getValue());
            updStmt.setLong(2, examEntryVO.getInsertTime());
            updStmt.executeUpdate();
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in SectionHeaderDAO.dbUpdateVO(conn) => " + e.getMessage());
            toReturn  = false;
        }finally{
            try{
                updStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in SectionHeaderDAO.dbUpdateVO(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "SectionHeaderDAO.dbUpdateVO(conn)");
        return toReturn;
    }
    public boolean dbDeleteVO(Connection conn)
   {
      Secretary.startFxn("exam", "SectionHeaderDAO.dbDeleteVO(conn)");
      boolean toReturn = false;
      String delete = "DELETE FROM ses_SECTION_HEADER WHERE"
                    + " insert_time = " + examEntryVO.getInsertTime();
      Statement stmt = null;
      try{
         stmt = conn.createStatement();
         stmt.executeUpdate(delete);
         toReturn  = true;
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in SectionHeaderDAO.dbDeleteVO(conn) => " + e.getMessage());
         toReturn  = false;
      }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in SectionHeaderDAO.dbDeleteVO(conn) => " + e.getMessage());
            }
        }
      Secretary.endFxn("exam", "SectionHeaderDAO.dbDeleteVO(conn)");
      return toReturn;
   }
   public boolean dbLoadVO(Connection conn)
   {
      //Secretary.startFxn("exam", "SectionHeaderDAO.dbLoadVO(conn)");
      String dbSelectStr;
      PreparedStatement dbSelectStmt = null;
      boolean toReturn = false;
      dbSelectStr = "SELECT description FROM ses_SECTION_HEADER WHERE"
                    + " insert_time = " + examEntryVO.getInsertTime();
      Statement stmt = null;
      try{
         stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(dbSelectStr);
         if(rs.next()){
            ((SectionHeaderVO)examEntryVO).setValue(rs.getString(1).trim());
            toReturn  = true;
         }
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in SectionHeaderDAO.dbLoadVO(conn) => " + e.getMessage());
      }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in SectionHeaderDAO.dbLoadVO(conn) => " + e.getMessage());
            }
        }
      //Secretary.endFxn("exam", "SectionHeaderDAO.dbLoadVO(conn) => " + toReturn);
      return toReturn;
   }
   
   public boolean dbLoadEmpAnswer()
   {
      return true;
   }
   public exam.vos.ExamEntryVO getVO() {
       return examEntryVO;
   }
   
   public void setVO(exam.vos.ExamEntryVO vo) {
       examEntryVO = (SectionHeaderVO)vo;
   }
   
   public void dropVO() {
       examEntryVO = null;
   }
    public boolean dbDeleteFullExam(Connection conn, int eNum) {
        Secretary.startFxn("exam", "SectionHeaderDAO.dbDeleteFullExam(conn, "+eNum+")");
        boolean toReturn = false;
        String delete = "DELETE FROM ses_SECTION_HEADER WHERE insert_time IN "
            + "(SELECT insert_time FROM ses_EXAM_ENTRY WHERE exam_num = " + eNum
            + " AND entry_type_code = '" + this.examEntryVO.getEntryTypeCode() + "')";
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(delete);
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in SectionHeaderDAO.dbDeleteFullExam(conn, "+eNum+") => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in SectionHeaderDAO.dbDeleteFullExam(conn, "+eNum+") => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "SectionHeaderDAO.dbDeleteFullExam(conn, "+eNum+") => "+toReturn);
        return toReturn;
    }
/*
   public void dbIncrementExamLocVO(Connection conn)
   {
      Secretary.startFxn("exam", "SectionHeaderDAO.dbIncrementExamLocVO(conn)");
      String increment = "UPDATE ses_SECTION_HEADER SET"
                    + " exam_loc = " + examEntryVO.getExamLoc()
                    + " WHERE exam_num = " + examEntryVO.getExamNum()
                    + " AND exam_loc = " + (examEntryVO.getExamLoc() - 1);
      Secretary.write("exam", increment);
      Statement stmt = null;
      try{
         stmt = conn.createStatement();
         stmt.executeUpdate(increment);
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in SectionHeaderDAO.dbIncrementExamLocVO(conn) => " + e.getMessage());
      }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in SectionHeaderDAO.dbIncrementExamLocVO(conn) => " + e.getMessage());
            }
        }
      Secretary.endFxn("exam", "SectionHeaderDAO.dbIncrementExamLocVO(conn)");
   }
   */
   public SectionHeaderDAO()
   {
      super();
   }

}