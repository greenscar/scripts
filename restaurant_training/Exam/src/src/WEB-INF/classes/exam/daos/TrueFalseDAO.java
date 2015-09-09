/***********************************************************************
* Module:  TrueFalseDAO.java
* Author:  jsandlin
* Purpose: Defines the Class TrueFalseDAO
***********************************************************************/
package exam.daos;
import exam.vos.TrueFalseVO;
import java.util.*;
import java.sql.*;
import logging.Secretary;

public final class TrueFalseDAO extends QuestionDAO
{  
    public boolean dbInsertVO(Connection conn)
    {
        Secretary.startFxn("exam", "TrueFalseDAO.dbInsertVO(conn)");
        String dbInsertStr;
        PreparedStatement dbInsertStmt = null;
        //examEntryVO.logValues();
        dbInsertStr = "INSERT INTO ses_TRUE_FALSE "
            + "(insert_time, question, solution) VALUES (?, ?, ?)";
        boolean toReturn = false;
        try{
            dbInsertStmt = conn.prepareStatement(dbInsertStr);
            dbInsertStmt.setLong(1, examEntryVO.getInsertTime());
            dbInsertStmt.setString(2, ((TrueFalseVO)examEntryVO).getQuestion());
            if(((TrueFalseVO)examEntryVO).getSolution())
                dbInsertStmt.setInt(3, 1);
            else
                dbInsertStmt.setInt(3, 0);
            dbInsertStmt.executeUpdate();
            toReturn = true;
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in TrueFalseDAO.dbInsertVO(conn) => " + e.getMessage());
        }finally{
            try{
                dbInsertStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in TrueFalseDAO.dbInsertVO(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "TrueFalseDAO.dbInsertVO(conn)");
        return toReturn;
    }
    public boolean dbUpdateVO(Connection conn)
    {
        Secretary.startFxn("exam", "TrueFalseDAO.dbUpdateVO(conn)");
        String dbUpdateStr;
        PreparedStatement dbUpdateStmt = null;
       // examEntryVO.logValues();
        dbUpdateStr = "UPDATE ses_TRUE_FALSE SET "
                    + "question = ?, solution = ? "
                    + "WHERE insert_time = ?";
        String toLog = "UPDATE ses_TRUE_FALSE SET "
                    + "question = \"" + ((TrueFalseVO)examEntryVO).getQuestion()
                    + "\", solution = " + ((TrueFalseVO)examEntryVO).getSolution()
                    + " WHERE insert_time = " + examEntryVO.getInsertTime();
        Secretary.write("exam", toLog);
        boolean toReturn = false;
        try{
            dbUpdateStmt = conn.prepareStatement(dbUpdateStr);
            dbUpdateStmt.setString(1, ((TrueFalseVO)examEntryVO).getQuestion());
            if(((TrueFalseVO)examEntryVO).getSolution())
                dbUpdateStmt.setInt(2, 1);
            else
                dbUpdateStmt.setInt(2, 0);
            
            dbUpdateStmt.setLong(3, examEntryVO.getInsertTime());
            dbUpdateStmt.executeUpdate();
            toReturn = true;
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in TrueFalseDAO.dbUpdateVO(conn) => " + e.getMessage());
        }finally{
            try{
                dbUpdateStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in TrueFalseDAO.dbUpdateVO(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "TrueFalseDAO.dbUpdateVO(conn)");
        return toReturn;   
    }
    public boolean dbLoadVO(Connection conn)
    {
        //Secretary.startFxn("exam", "TrueFalseDAO.dbLoadVO(conn)");
        /*
        String dbSelectStr = "SELECT question, solution FROM ses_TRUE_FALSE "
                  + "WHERE exam_num = " + this.examEntryVO.getExamNum() 
                  + " AND exam_loc = " + this.examEntryVO.getExamLoc();
        */
        String dbSelectStr = "SELECT question, solution FROM ses_TRUE_FALSE "
                  + "WHERE insert_time = " + examEntryVO.getInsertTime();
        boolean toReturn = false;
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(dbSelectStr);
            if(rs.next())
            {
                String q = rs.getString(1).trim();
                ((TrueFalseVO)examEntryVO).setQuestion(q);
                int solution = rs.getInt(2);
                if(solution == 0) 
                    ((TrueFalseVO)examEntryVO).setSolution(false);
                else
                    ((TrueFalseVO)examEntryVO).setSolution(true);
            }
            ((TrueFalseVO)examEntryVO).setNumSolutions(1);
            ((TrueFalseVO)examEntryVO).computePointValuePerAnswer();
            toReturn = true;
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in TrueFalseDAO.dbInsertVO(conn) => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in TrueFalseDAO.dbInsertVO(conn) => " + e.getMessage());
            }
        }
        //Secretary.endFxn("exam", "TrueFalseDAO.dbLoadVO(conn)");
        return toReturn;
    }
    public boolean dbDeleteFullExam(Connection conn, int eNum) 
    {
        Secretary.startFxn("exam", "TrueFalseDAO.dbDeleteFullExam(conn, "+eNum+")");
        if(this.examEntryVO == null)
            this.examEntryVO = new TrueFalseVO();
        //this.examEntryVO.logValues();
        String delete = "DELETE FROM ses_TRUE_FALSE WHERE insert_time IN "
                      + "(SELECT insert_time FROM ses_EXAM_ENTRY WHERE exam_num = " + eNum
                      + " AND entry_type_code = '" + this.examEntryVO.getEntryTypeCode() + "')";
        Secretary.write("exam", delete);
        boolean toReturn = false;
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            stmt.execute(delete);
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in TrueFalseDAO.dbDeleteFullExam(conn, "+eNum+") => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in TrueFalseDAO.dbDeleteFullExam(conn, "+eNum+") => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "TrueFalseDAO.dbDeleteFullExam(conn, "+eNum+") => " + toReturn);
        return toReturn;
    }
    public boolean dbDeleteVO(Connection conn)
    {
        Secretary.startFxn("exam", "TrueFalseDAO.dbDeleteVO(conn)");
        boolean toReturn = false;
        /*
        String delete = "DELETE FROM ses_TRUE_FALSE "
            + "WHERE exam_num = " + examEntryVO.getExamNum()
            + " AND exam_loc = " + examEntryVO.getExamLoc();
        */
        String delete = "DELETE FROM ses_TRUE_FALSE where insert_time = " + examEntryVO.getInsertTime();
        Secretary.write("exam", delete);
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(delete);
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in TrueFalseDAO.dbDeleteVO(conn) => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in TrueFalseDAO.dbDeleteVO(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "TrueFalseDAO.dbDeleteVO(conn)");
        return toReturn;
    }

    public boolean dbInsertTakenVO(Connection conn)
    {
        //Secretary.startFxn("exam", "TrueFalseDAO.dbInsertTakenVO(conn)");
        boolean toReturn = false;
        try{
            dbInsertTakenStmt = conn.prepareStatement(dbInsertTakenStr);
            dbInsertTakenStmt.setInt(1, examEntryVO.getTakeNum());
            //dbInsertTakenStmt.setInt(2, examEntryVO.getExamLoc());
            dbInsertTakenStmt.setLong(2, examEntryVO.getInsertTime());
            dbInsertTakenStmt.setInt(3, 0);
            if(((TrueFalseVO)examEntryVO).getEmpAnswer()){
                dbInsertTakenStmt.setInt(4, 1);
            }
            else{
                dbInsertTakenStmt.setInt(4, 0);
            }
            if(((TrueFalseVO)examEntryVO).getEmpAnswerCorrect()){
                int x = ((exam.vos.TrueFalseVO)examEntryVO).getPointValuePerAnswer();
                dbInsertTakenStmt.setInt(5, 1);
                dbInsertTakenStmt.setInt(6, x);         
            }
            else{
                dbInsertTakenStmt.setInt(5, -1);
                dbInsertTakenStmt.setInt(6, 0);
            }
            dbInsertTakenStmt.setString(7, "");
            dbInsertTakenStmt.executeUpdate();
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in TrueFalseDAO.dbInsertTakenVO(conn) => " + e.getMessage());
        }finally{
            try{
                dbInsertTakenStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in TrueFalseDAO.dbInsertTakenVO(conn) => " + e.getMessage());
            }
        }
        //Secretary.endFxn("exam", "TrueFalseDAO.dbInsertTakenVO(conn) => " + toReturn);
        return toReturn;  
    }
    
    public boolean dbUpdatePointsEarned(Connection conn) 
    {
        //Secretary.startFxn("exam", "TrueFalseDAO.dbUpdatePointsEarned(conn)");
        boolean toReturn = false;
        try{
            //Secretary.write("exam", examEntryVO.toString());
            //examEntryVO.logValues();
            //Secretary.write("exam", "dbUpdatePointsStr = " + dbUpdatePointsStr);
            dbUpdatePointsStmt = conn.prepareStatement(dbUpdatePointsStr);
            //Secretary.write("exam", "dbUpdatePointsStmt = " + dbUpdatePointsStmt);
            if(((TrueFalseVO)(this.examEntryVO)).getEmpCorrect())
                dbUpdatePointsStmt.setInt(1, 1);
            else
                dbUpdatePointsStmt.setInt(1, -1);
            dbUpdatePointsStmt.setInt(2, ((TrueFalseVO)(this.examEntryVO)).getPointValueEarnedTotal());
            dbUpdatePointsStmt.setString(3, ((TrueFalseVO)(this.examEntryVO)).getGradersComment());
            dbUpdatePointsStmt.setInt(4, this.examEntryVO.getTakeNum());
            //dbUpdatePointsStmt.setInt(5, ((TrueFalseVO)(this.examEntryVO)).getExamLoc());
            //Secretary.write("exam", "examLoc = " + this.examEntryVO.getExamLoc());
            //Secretary.write("exam", "insertTime = " + this.examEntryVO.getInsertTime());
            dbUpdatePointsStmt.setLong(5, this.examEntryVO.getInsertTime());
            dbUpdatePointsStmt.setInt(6, 0);
            //Secretary.write("exam", "B4 executeUpdate()");
            dbUpdatePointsStmt.executeUpdate();
            //Secretary.write("exam", "after executeUpdate()");
            toReturn = true;
            //Secretary.write("exam", "toReturn = true");
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in TrueFalseDAO.dbUpdatePointsEarned() => " + e.getMessage());
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in TrueFalseDAO.dbUpdatePointsEarned(conn) => "  + e.getMessage());
        }finally{
            try{
                dbUpdatePointsStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in TrueFalseDAO.dbUpdatePointsEarned(conn) => " + e.getMessage());
            }
        }
        //Secretary.endFxn("exam", "TrueFalseDAO.dbUpdatePointsEarned(conn) => " + toReturn);
        return toReturn;  
    }
    
    public boolean dbLoadEmpAnswer(Connection conn)
    {
        Secretary.write("exam", "TrueFalseDAO.dbLoadEmpAnswer(conn)");
        /*
        String select = "SELECT answer_entered, correct, points_earned, comment FROM "
            + "ses_EMP_ANSWER WHERE take_num = " + examEntryVO.getTakeNum()
            + " AND exam_loc = " + examEntryVO.getExamLoc();
        */
        String select = "SELECT answer_entered, correct, points_earned, comment FROM "
                    + "ses_EMP_ANSWER WHERE take_num = " + examEntryVO.getTakeNum()
                    + " AND insert_time = " + examEntryVO.getInsertTime();
        
        //Secretary.write("exam", dbSelectTakenStr);
        boolean toReturn = false;
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(select);
            if(rs.next())
            {
                if(rs.getInt("answer_entered") == 1)
                    ((TrueFalseVO)examEntryVO).setEmpAnswer(true);
                else
                    ((TrueFalseVO)examEntryVO).setEmpAnswer(false);
                ((TrueFalseVO)examEntryVO).setEmpCorrect(rs.getInt("correct"));
                ((TrueFalseVO)examEntryVO).setPointValueEarnedTotal(rs.getInt("points_earned"));
                ((TrueFalseVO)examEntryVO).setGradersComment(rs.getString("comment").trim());
                toReturn = true;
            }
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in TrueFalseDAO.dbLoadEmpAnswer(conn) => " + e.getMessage());
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in TrueFalseDAO.dbLoadEmpAnswer(conn) => "  + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in TrueFalseDAO.dbLoadEmpAnswer(conn) => " + e.getMessage());
            }
        }
        Secretary.write("exam", "TrueFalseDAO.dbLoadEmpAnswer(conn) => " + toReturn);
        return toReturn;  
    }
    /*
    public void dbIncrementExamLocVO(Connection conn)
    {
        Secretary.startFxn("exam", "TrueFalseDAO.dbIncrementExamLocVO(conn)");
        examEntryVO.logValues();
        String increment = "UPDATE ses_TRUE_FALSE"
                        + " SET"
                        + " exam_loc = " + examEntryVO.getExamLoc()
                        + " WHERE exam_num = " + examEntryVO.getExamNum()
                        + " AND exam_loc = " + (examEntryVO.getExamLoc() - 1);
        Secretary.write("exam", increment);
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(increment);
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in TrueFalseDAO.dbIncrementExamLocVO(conn) => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in TrueFalseDAO.dbIncrementExamLocVO(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "TrueFalseDAO.dbIncrementExamLocVO(conn)");
    }
    */
    public void dropVO() 
    {
        this.examEntryVO = null;
    }
    public exam.vos.ExamEntryVO getVO() 
    {
        return examEntryVO;
    }
    public void setVO(exam.vos.ExamEntryVO vo) 
    {
        examEntryVO = (TrueFalseVO)vo;
    }
    public TrueFalseDAO()
    {
        super();
    }
}