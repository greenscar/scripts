/***********************************************************************
 * Module:  EssayDAO.java
 * Author:  jsandlin
 * Purpose: Defines the Class EssayDAO
 ***********************************************************************/
package exam.daos;
import exam.vos.EssayVO;
import java.util.*;
import java.sql.*;
import logging.Secretary;

public final class EssayDAO extends QuestionDAO
{
    public boolean dbInsertVO(Connection conn)
    {
        Secretary.startFxn("exam", "EssayDAO.dbInsertVO(" + conn + ")");
        String dbInsertStr;
        PreparedStatement dbInsertStmt = null;
        boolean toReturn = false;
        dbInsertStr = "INSERT INTO ses_ESSAY "
            + "(insert_time, question, solution) VALUES ("
            + "?, ?, ?)";
        try{
            dbInsertStmt = conn.prepareStatement(dbInsertStr);
            dbInsertStmt.setLong(1, examEntryVO.getInsertTime());
            dbInsertStmt.setString(2, ((EssayVO)examEntryVO).getQuestion());
            dbInsertStmt.setString(3, ((EssayVO)examEntryVO).getSolution());
            dbInsertStmt.executeUpdate();
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in EssayDAO.dbInsertVO(" + conn.toString() + ") => " + e.getMessage());
        }finally{
            try{
                dbInsertStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in EssayDAO.dbInsertVO(" + conn.toString() + ") => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "EssayDAO.dbInsertVO(" + conn + ")");
        return toReturn;
    }
    public boolean dbUpdateVO(Connection conn)
    {
        Secretary.startFxn("exam", "EssayDAO.dbUpdateVO(" + conn + ")");
        String dbUpdateStr;
        PreparedStatement dbInsertStmt = null;
        boolean toReturn = false;
        dbUpdateStr = "UPDATE ses_ESSAY SET "
                    + "question = ?, solution = ? "
                    + "WHERE insert_time = ?";
        String toLog = "UPDATE ses_ESSAY SET "
                    + "question = \"" + ((EssayVO)examEntryVO).getQuestion()
                    + "\", solution = \"" + ((EssayVO)examEntryVO).getSolution()
                    + "\" WHERE insert_time = " + examEntryVO.getInsertTime();
        Secretary.write("exam", toLog);
        try{
            dbInsertStmt = conn.prepareStatement(dbUpdateStr);
            dbInsertStmt.setString(1, ((EssayVO)examEntryVO).getQuestion());
            dbInsertStmt.setString(2, ((EssayVO)examEntryVO).getSolution());
            dbInsertStmt.setLong(3, examEntryVO.getInsertTime());
            dbInsertStmt.executeUpdate();
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in EssayDAO.dbUpdateVO(" + conn.toString() + ") => " + e.getMessage());
        }finally{
            try{
                dbInsertStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in EssayDAO.dbUpdateVO(" + conn.toString() + ") => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "EssayDAO.dbUpdateVO(" + conn + ")");
        return toReturn;
    }
    public boolean dbLoadVO(Connection conn)
    {
        //Secretary.startFxn("exam", "EssayDAO.dbLoadVO(" + conn + ")");
        boolean toReturn = false;
        String select = "SELECT question, solution FROM ses_ESSAY "
            + "WHERE insert_time = " + this.examEntryVO.getInsertTime();
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(select);
            if(rs.next())
            {
                ((EssayVO)examEntryVO).setQuestion(rs.getString(1).trim());
                ((EssayVO)examEntryVO).setSolution(rs.getString(2).trim());
                ((EssayVO)examEntryVO).setNumSolutions(1);
                ((EssayVO)examEntryVO).computePointValuePerAnswer();
                toReturn = true;
            }
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in EssayDAO.dbLoadVO(" + conn.toString() + ") => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in EssayDAO.dbLoadVO(" + conn.toString() + ") => " + e.getMessage());
            }
        }
        //Secretary.endFxn("exam", "EssayDAO.dbLoadVO(" + conn + ")");
        return toReturn;
    }
   
    public boolean dbUpdatePointsEarned(Connection conn) 
    {
        Secretary.startFxn("exam", "EssayDAO.dbUpdatePointsEarned(" + conn + ")");
        boolean toReturn = false;
        int pointsEarned = ((EssayVO)(this.examEntryVO)).getPointValueEarnedTotal();
        String comment = ((EssayVO)(this.examEntryVO)).getGradersComment();
        try{
            dbUpdatePointsStmt = conn.prepareStatement(dbUpdatePointsStr);
            if(((EssayVO)(this.examEntryVO)).getEmpCorrect())
                dbUpdatePointsStmt.setInt(1, 1);
            else
                dbUpdatePointsStmt.setInt(1, -1);
            dbUpdatePointsStmt.setInt(2, ((EssayVO)(this.examEntryVO)).getPointValueEarnedTotal());
            dbUpdatePointsStmt.setString(3, ((EssayVO)(this.examEntryVO)).getGradersComment());
            dbUpdatePointsStmt.setInt(4, ((EssayVO)(this.examEntryVO)).getTakeNum());
            //dbUpdatePointsStmt.setInt(5, ((TrueFalseVO)(this.examEntryVO)).getExamLoc());
            dbUpdatePointsStmt.setLong(5, examEntryVO.getInsertTime());
            dbUpdatePointsStmt.setInt(6, 0);
            dbUpdatePointsStmt.executeUpdate();
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in EssayDAO.dbUpdatePointsEarned(" + conn.toString() + ") => " + e.getMessage());
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in EssayDAO.dbUpdatePointsEarned(" + conn.toString() + ") => "  + e.getMessage());
        }finally{
            try{
                dbUpdatePointsStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in EssayDAO.dbUpdatePointsEarned(" + conn.toString() + ") => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "EssayDAO.dbUpdatePointsEarned(" + conn + ")");
        return toReturn;  
    }
    
    public boolean dbLoadEmpAnswer(Connection conn)
    {
        Secretary.startFxn("exam", "EssayDAO.dbLoadEmpAnswer(" + conn + ")");
        boolean toReturn = false;
        String select = "SELECT answer_entered, correct, points_earned, comment FROM "
            + "ses_EMP_ANSWER WHERE take_num = "
            + ((EssayVO)examEntryVO).getTakeNum()
            + " AND insert_time = "
            + examEntryVO.getInsertTime();
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(select);
            if(rs.next())
            {
                ((EssayVO)examEntryVO).setEmpAnswer(rs.getString("answer_entered").trim());
                //Secretary.write("exam", "answer_entered = " + (((EssayVO)examEntryVO).getEmpAnswer()));
                if(rs.getInt("correct") == 1)
                    ((EssayVO)examEntryVO).setEmpCorrect(true);
                else if(rs.getInt("correct") == -1)
                    ((EssayVO)examEntryVO).setEmpCorrect(false);
                ((EssayVO)examEntryVO).setPointValueEarnedTotal(rs.getInt("points_earned"));
                //Secretary.write("exam", "points_earned = " + (((EssayVO)examEntryVO).getPointValueEarnedTotal()));
                ((EssayVO)examEntryVO).setGradersComment(rs.getString("comment").trim());
                //Secretary.write("exam", "comment = " + (((EssayVO)examEntryVO).getGradersComment()));
                toReturn = true;
            }
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in EssayDAO.dbLoadEmpAnswer(" + conn.toString() + ") => " + e.getMessage());
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in EssayDAO.dbLoadEmpAnswer(" + conn.toString() + ") => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in EssayDAO.dbLoadEmpAnswer(" + conn.toString() + ") => " + e.getMessage());
            }
        }
            Secretary.endFxn("exam", "EssayDAO.dbLoadEmpAnswer(" + conn + ")");
        return toReturn;
    }
    public boolean dbDeleteVO(Connection conn)
    {
        Secretary.startFxn("exam", "EssayDAO.dbDeleteVO(" + conn + ")");
        boolean toReturn = false;
        String delete = "DELETE FROM ses_ESSAY " 
            + "WHERE insert_time = " + examEntryVO.getInsertTime();
        Secretary.write("exam", delete);
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(delete);
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in EssayDAO.dbDeleteVO() => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in EssayDAO.dbDeleteVO(" + conn.toString() + ") => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "EssayDAO.dbDeleteVO(" + conn + ")");
        return toReturn;
    }
   
    public boolean dbDeleteFullExam(Connection conn, int eNum)
    {
        Secretary.write("exam", "EssayDAO.dbDeleteFullExam(" + conn.toString() + ", "+eNum+")");
        boolean toReturn = false;
        String delete = "DELETE FROM ses_ESSAY WHERE insert_time IN "
                      + "(SELECT insert_time FROM ses_EXAM_ENTRY WHERE exam_num = " + eNum
                      + " AND entry_type_code = '" + this.examEntryVO.getEntryTypeCode() + "')";
        Secretary.write("exam", delete);
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(delete);
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in EssayDAO.dbDeleteFullExam(conn, "+eNum+") => " + e.getMessage());
        }catch(NullPointerException e){
            Secretary.write("exam", "NullPointerException in EssayDAO.dbDeleteFullExam(conn, "+eNum+") => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in EssayDAO.dbDeleteFullExam(conn, "+eNum+") => " + e.getMessage());
            }
        }
        Secretary.write("exam", "EssayDAO.dbDeleteFullExam(" + conn.toString() + ", "+eNum+") => " + toReturn);
        return toReturn;
    }
    
    public boolean dbInsertTakenVO(Connection conn)
    {
        //Secretary.startFxn("exam", "EssayDAO.dbInsertTakenVO(" + conn + ")");
        boolean toReturn = false;
        try{
            dbInsertTakenStmt = conn.prepareStatement(dbInsertTakenStr);
            dbInsertTakenStmt.setInt(1, examEntryVO.getTakeNum());
            //dbInsertTakenStmt.setInt(2, examEntryVO.getExamLoc());
            dbInsertTakenStmt.setLong(2, examEntryVO.getInsertTime());
            dbInsertTakenStmt.setInt(3, 0);
            dbInsertTakenStmt.setString(4, ((EssayVO)examEntryVO).getEmpAnswer());
            dbInsertTakenStmt.setInt(5, 0);
            dbInsertTakenStmt.setInt(6, 0);
            dbInsertTakenStmt.setString(7, "");
            //dbInsertTakenStmt.setNull(5, java.sql.Types.BIT);
            dbInsertTakenStmt.executeUpdate();
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in EssayDAO.dbInsertTakenVO(" + conn.toString() + ") => " + e.getMessage());
            toReturn = false;
        }finally{
            try{
                dbInsertTakenStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in EssayDAO.dbInsertTakenVO(" + conn.toString() + ") => " + e.getMessage());
            }
        }
        //Secretary.endFxn("exam", "EssayDAO.dbInsertTakenVO(" + conn + ")");
        return toReturn;
    }
    /*
    public void dbIncrementExamLocVO(Connection conn)
    {
        Secretary.startFxn("exam", "EssayDAO.dbIncrementExamLocVO(" + conn + ")");
        examEntryVO.logValues();
        String increment = "UPDATE ses_ESSAY"
                        + " SET"
                        + " exam_loc = " + examEntryVO.getExamLoc()
                        + " WHERE exam_num = " + examEntryVO.getExamNum()
                        + " AND exam_loc = " + (examEntryVO.getExamLoc() - 1);
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(increment);
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in EssayDAO.dbIncrementExamLocVO(" + conn.toString() + ") => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in EssayDAO.dbIncrementExamLocVO(" + conn.toString() + ") => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "EssayDAO.dbIncrementExamLocVO(" + conn + ")");
    }
   */
    public void dropVO() {
        examEntryVO = null;
    }
   
    public exam.vos.ExamEntryVO getVO() {
        return examEntryVO;
    }
   
    public void setVO(exam.vos.ExamEntryVO vo) {
        examEntryVO = (EssayVO)vo;
    }

    public EssayDAO()
    {
        super();
        examEntryVO = new EssayVO();
    }

}