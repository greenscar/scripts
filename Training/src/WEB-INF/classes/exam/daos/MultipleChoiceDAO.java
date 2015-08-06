/***********************************************************************
* Module:  MultipleChoiceDAO.java
* Author:  jsandlin
* Purpose: Defines the Class MultipleChoiceDAO
***********************************************************************/
package exam.daos;
import exam.vos.*;
import java.util.*;
import java.sql.*;
import logging.Secretary;

public final class MultipleChoiceDAO extends QuestionDAO
{
    public boolean dbInsertVO(Connection conn)
    {
        Secretary.startFxn("exam", "MultipleChoiceDAO.dbInsertVO(conn)");        
        boolean toReturn = false;
        String dbInsertStr;
        PreparedStatement dbInsertStmt = null;
        dbInsertStr = "INSERT INTO ses_MULT_CHOICE "
            + "(insert_time, question, choice_1, "
            + "choice_2, choice_3, choice_4, solution) VALUES "
            + "(?, ?, ?, ?, ?, ?, ?)";
        try{
            dbInsertStmt = conn.prepareStatement(dbInsertStr);
            dbInsertStmt.setLong(1, examEntryVO.getInsertTime());
            dbInsertStmt.setString(2, ((MultipleChoiceVO)examEntryVO).getQuestion());
            for(int x=0; x<4; x++)
            {
                String choice = ((MultipleChoiceVO)examEntryVO).getChoiceNum(x);
                if(choice == null) choice = "n/a";
                dbInsertStmt.setString((3+x), choice);
            }
            dbInsertStmt.setString(7, ((MultipleChoiceVO)examEntryVO).getSolution());
            dbInsertStmt.executeUpdate();
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in MultipleChoiceDAO.dbInsertVO(conn) => " + e.toString());
        }finally{
            try{
                dbInsertStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in MultipleChoiceDAO.dbInsertVO(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "MultipleChoiceDAO.dbInsertVO(conn) => " + toReturn);        
        return toReturn;
    }
    public boolean dbUpdateVO(Connection conn)
    {
        Secretary.startFxn("exam", "MultipleChoiceDAO.dbUpdateVO(conn)");        
        boolean toReturn = false;
        String dbUpdateStr;
        PreparedStatement dbUpdateStmt = null;
        dbUpdateStr = "UPDATE ses_MULT_CHOICE SET "
                    + "question = ?, "
                    + "choice_1 = ?, choice_2 = ?, choice_3 = ?, choice_4 = ?, "
                    + "solution = ? "
                    + "WHERE insert_time = ?";
        String toLog = "UPDATE ses_MULT_CHOICE SET "
                    + "question = \"" + ((MultipleChoiceVO)examEntryVO).getQuestion() + "\", "
                    + "choice_1 = \"" + ((MultipleChoiceVO)examEntryVO).getChoiceNum(0) + "\", "
                    + "choice_2 = \"" + ((MultipleChoiceVO)examEntryVO).getChoiceNum(1) + "\", "
                    + "choice_3 = \"" + ((MultipleChoiceVO)examEntryVO).getChoiceNum(2) + "\", "
                    + "choice_4 = \"" + ((MultipleChoiceVO)examEntryVO).getChoiceNum(3) + "\", "
                    + "solution = \"" + ((MultipleChoiceVO)examEntryVO).getSolution() + "\""
                    + "WHERE insert_time = " + examEntryVO.getInsertTime();
        Secretary.write("exam", toLog);
        try{
            dbUpdateStmt = conn.prepareStatement(dbUpdateStr);
            dbUpdateStmt.setString(1, ((MultipleChoiceVO)examEntryVO).getQuestion());
            for(int x=0; x<4; x++)
                dbUpdateStmt.setString((2+x), ((MultipleChoiceVO)examEntryVO).getChoiceNum(x));
            dbUpdateStmt.setString(6, ((MultipleChoiceVO)examEntryVO).getSolution());
            dbUpdateStmt.setLong(7, examEntryVO.getInsertTime());
            dbUpdateStmt.executeUpdate();
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in MultipleChoiceDAO.dbUpdateVO(conn) => " + e.toString());
        }finally{
            try{
                dbUpdateStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in MultipleChoiceDAO.dbUpdateVO(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "MultipleChoiceDAO.dbUpdateVO(conn) => " + toReturn);        
        return toReturn;
    }

    public boolean dbLoadVO(Connection conn)
    {
        //Secretary.startFxn("exam", "MultipleChoiceDAO.dbLoadVO(conn)");
        String sel = "SELECT question, choice_1, choice_2, choice_3, "
            + "choice_4, solution FROM ses_MULT_CHOICE "
            + "WHERE insert_time = " + examEntryVO.getInsertTime();
        //Secretary.write("exam", dbSelectStr);
        boolean toReturn = false;
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sel);
            if(rs.next())
            {
                ((MultipleChoiceVO)examEntryVO).setQuestion(rs.getString(1).trim());
                String[] theChoices = new String[4];
                for(int k=2; k < 6; k++)
                    theChoices[k-2] = rs.getString(k).trim();
                ((MultipleChoiceVO)examEntryVO).setChoices(theChoices);
                ((MultipleChoiceVO)examEntryVO).setSolution(rs.getString(6).trim());
                ((MultipleChoiceVO)examEntryVO).setNumSolutions(1);
                ((MultipleChoiceVO)examEntryVO).computePointValuePerAnswer();
                toReturn = true;
            }
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in MultipleChoiceDAO.dbLoadVO(conn) => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in MultipleChoiceDAO.dbLoadVO(conn) => " + e.getMessage());
            }
        }
        //Secretary.endFxn("exam", "MultipleChoiceDAO.dbLoadVO(conn)");
        return toReturn;
    }

    
    public boolean dbUpdatePointsEarned(Connection conn) 
    {
        //Secretary.startFxn("exam", "MultipleChoiceDAO.dbUpdatePointsEarned(conn)");
        boolean toReturn = false;
        int pointsEarned = ((MultipleChoiceVO)(this.examEntryVO)).getPointValueEarnedTotal();
        String comment = ((MultipleChoiceVO)(this.examEntryVO)).getGradersComment();
        try{
            dbUpdatePointsStmt = conn.prepareStatement(dbUpdatePointsStr);
            if(((MultipleChoiceVO)(this.examEntryVO)).getEmpCorrect())
                dbUpdatePointsStmt.setInt(1, 1);
            else
                dbUpdatePointsStmt.setInt(1, -1);
            dbUpdatePointsStmt.setInt(2, ((MultipleChoiceVO)(this.examEntryVO)).getPointValueEarnedTotal());
            dbUpdatePointsStmt.setString(3, ((MultipleChoiceVO)(this.examEntryVO)).getGradersComment());
            dbUpdatePointsStmt.setInt(4, ((MultipleChoiceVO)(this.examEntryVO)).getTakeNum());
            //dbUpdatePointsStmt.setInt(5, ((MultipleChoiceVO)(this.examEntryVO)).getExamLoc());
            dbUpdatePointsStmt.setLong(5, this.examEntryVO.getInsertTime());
            dbUpdatePointsStmt.setInt(6, 0);
            dbUpdatePointsStmt.executeUpdate();
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in MultipleChoiceDAO.dbUpdatePointsEarned(conn) => " + e.getMessage());
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in MultipleChoiceDAO.dbUpdatePointsEarned(conn) => "  + e.getMessage());
        }finally{
            try{
                dbUpdatePointsStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in MultipleChoiceDAO.dbUpdatePointsEarned(conn) => " + e.getMessage());
            }
        }
        //Secretary.endFxn("exam", "MultipleChoiceDAO.dbUpdatePointsEarned(conn)");
        return toReturn;  
    }
    public boolean dbLoadEmpAnswer(Connection conn)
    {
        //Secretary.startFxn("exam", "MultipleChoiceDAO.dbLoadEmpAnswer(conn)");
        String select = "SELECT answer_entered, correct, points_earned, comment "
            + "FROM ses_EMP_ANSWER WHERE "
            + "take_num = " + examEntryVO.getTakeNum()
            + " AND insert_time = " + examEntryVO.getInsertTime()
            + " ORDER BY quest_loc";
        boolean toReturn = false;
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(select);
            if(rs.next())
            {
                ((MultipleChoiceVO)examEntryVO).setEmpAnswer(rs.getString("answer_entered").trim());
                if(rs.getInt("correct") == 1)
                    ((MultipleChoiceVO)examEntryVO).setEmpCorrect(true);
                else if(rs.getInt("correct") == -1)
                    ((MultipleChoiceVO)examEntryVO).setEmpCorrect(false);
                ((MultipleChoiceVO)examEntryVO).setPointValueEarnedTotal(rs.getInt("points_earned"));
                ((MultipleChoiceVO)examEntryVO).setGradersComment(rs.getString("comment").trim());
                //Secretary.write("exam", examEntryVO.getExamLoc() + " -> " + ((MultipleChoiceVO)examEntryVO).getEmpCorrect());
                toReturn = true;
            }
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in MultipleChoiceDAO.dbLoadEmpAnswer(conn) => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in MultipleChoiceDAO.dbLoadEmpAnswer(conn) => " + e.getMessage());
            }
        }
        //Secretary.endFxn("exam", "MultipleChoiceDAO.dbLoadEmpAnswer(conn)");
        return toReturn;
    }

    /*
     * dbDeleteFullExam deletes all of this type of entry of the provided exam num
     *      from the db.
     */
    public boolean dbDeleteFullExam(Connection conn, int eNum) 
    {
        Secretary.startFxn("exam", "MultipleChoiceDAO.dbDeleteFullExam(conn, "+eNum+")");
        boolean toReturn = false;
        String delete = "DELETE FROM ses_MULT_CHOICE WHERE insert_time IN "
                      + "(SELECT insert_time FROM ses_EXAM_ENTRY WHERE exam_num = " + eNum
                      + " AND entry_type_code = '" + this.examEntryVO.getEntryTypeCode() + "')";
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(delete);
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in MultipleChoiceDAO.dbDeleteFullExam(conn, "+eNum+") => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in MultipleChoiceDAO.dbDeleteFullExam(conn, "+eNum+") => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "MultipleChoiceDAO.dbDeleteFullExam(conn, "+eNum+")");   
        return toReturn;
    }
    public boolean dbDeleteVO(Connection conn)
    {
        Secretary.startFxn("exam", "MultipleChoiceDAO.dbDeleteVO(conn)");
        boolean toReturn = false;
        String del = "DELETE FROM ses_MULT_CHOICE "
            + "WHERE insert_time = " + examEntryVO.getInsertTime();
        Secretary.write("exam", del);
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(del);
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in MultipleChoiceDAO.dbDeleteVO(conn) => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in MultipleChoiceDAO.dbDeleteVO(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "MultipleChoiceDAO.dbDeleteVO(conn)");
        return toReturn;
    }
    public boolean dbInsertTakenVO(Connection conn)
    {
        //Secretary.startFxn("exam", "MultipleChoiceDAO.dbInsertTakenStmt(conn)");
        boolean toReturn = false;
        try{
            dbInsertTakenStmt = conn.prepareStatement(dbInsertTakenStr);
            dbInsertTakenStmt.setInt(1, examEntryVO.getTakeNum());
            //dbInsertTakenStmt.setInt(2, examEntryVO.getExamLoc());
            dbInsertTakenStmt.setLong(2, examEntryVO.getInsertTime());
            dbInsertTakenStmt.setInt(3, 0);
            dbInsertTakenStmt.setString(4, ((MultipleChoiceVO)examEntryVO).getEmpAnswer());
            if(((MultipleChoiceVO)examEntryVO).getEmpCorrect())
            {
                int x = ((exam.vos.QuestionVO)examEntryVO).getPointValuePerAnswer();
                dbInsertTakenStmt.setInt(5, 1);
                dbInsertTakenStmt.setInt(6, x);         
            }
            else
            {
                dbInsertTakenStmt.setInt(5, -1);
                dbInsertTakenStmt.setInt(6, 0);
            }
            dbInsertTakenStmt.setString(7, "");
            dbInsertTakenStmt.executeUpdate();
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in MultipleChoiceDAO.dbInsertTakenStmt(conn) => " + e.getMessage());
        }finally{
            try{
                dbInsertTakenStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in MultipleChoiceDAO.dbInsertTakenStmt(conn) => " + e.getMessage());
            }
        }
        //Secretary.endFxn("exam", "MultipleChoiceDAO.dbInsertTakenStmt(conn)");
        return toReturn;
    }
    /*
    public void dbIncrementExamLocVO(Connection conn)
    {
        Secretary.startFxn("exam", "MultipleChoiceDAO.dbIncrementExamLocVO(conn)");
        examEntryVO.logValues();
        String increment = "UPDATE ses_MULT_CHOICE"
                        + " SET"
                        + " exam_loc = " + examEntryVO.getExamLoc()
                        + " WHERE exam_num = " + examEntryVO.getExamNum()
                        + " AND exam_loc = " + (examEntryVO.getExamLoc() - 1);
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(increment);
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in MultipleChoiceDAO.dbIncrementExamLocVO(conn) => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in MultipleChoiceDAO.dbIncrementExamLocVO(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "MultipleChoiceDAO.dbIncrementExamLocVO(conn)");
    }
   */
    public exam.vos.ExamEntryVO getVO() 
    {
        return examEntryVO;
    }
    public void setVO(exam.vos.ExamEntryVO vo) 
    {
        examEntryVO = (MultipleChoiceVO)vo;
    }
    public void dropVO() 
    {
        examEntryVO = null;
    }
    
    public MultipleChoiceDAO()
    {
        super();
    }
}