/***********************************************************************
* Module:  FillInBlankDAO.java
* Author:  jsandlin
* Purpose: Defines the Class FillInBlankDAO
***********************************************************************/

package exam.daos;
import exam.vos.FillInBlankVO;
import java.util.*;
import java.sql.*;
import logging.Secretary;
public final class FillInBlankDAO extends QuestionDAO
{
    public boolean dbInsertVO(Connection conn)
    {
        Secretary.startFxn("exam", "FillInBlankDAO.dbInsertVO(conn)");
        String dbInsertQuestionStr;
        PreparedStatement dbInsertQuestionStmt = null;
        String dbInsertSolutionStr;
        PreparedStatement dbInsertSolutionStmt = null;
        dbInsertQuestionStr = "INSERT INTO ses_FILL_IN_BLANK "
            + "(insert_time, question) "
            + "VALUES (?, ?)";
        dbInsertSolutionStr = "INSERT INTO ses_FILL_IN_BLANK_SOLUTION "
            + "(insert_time, quest_loc, solution) "
            + "VALUES (?, ?, ?)";
        boolean toReturn = false;
        try{
            dbInsertQuestionStmt = conn.prepareStatement(dbInsertQuestionStr);
            dbInsertSolutionStmt = conn.prepareStatement(dbInsertSolutionStr);
            // INSERT THE QUESTION
            dbInsertQuestionStmt.setLong(1, examEntryVO.getInsertTime());
            dbInsertQuestionStmt.setString(2, ((FillInBlankVO)examEntryVO).getQuestion());
            dbInsertQuestionStmt.executeUpdate();
            // INSERT THE SolutionS
            for(int i=0; i < ((FillInBlankVO)examEntryVO).getSolutionsSize(); i++)
            {
                dbInsertSolutionStmt.setLong(1, examEntryVO.getInsertTime());
                dbInsertSolutionStmt.setInt(2, i);
                dbInsertSolutionStmt.setString (3, ((FillInBlankVO)examEntryVO).getSolutionAt(i).toString());
                dbInsertSolutionStmt.executeUpdate();
            }
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in FillInBlankDAO.dbInsertVO(conn) =>" + e.getMessage());
        }finally{
            try{
                dbInsertQuestionStmt.close();
                dbInsertSolutionStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in FillInBlankDAO.dbInsertVO(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "FillInBlankDAO.dbInsertVO(conn) => " + toReturn);
        return toReturn;
    }

    /*
     * ses_FILL_IN_BLANK will be updated, because there is only 1 entry for each
     *      Fill in the Blank question.
     * ses_FILL_IN_BLANK_SOLUTION will be deleted then reinserted, because the number
     *      of entries varies and we are not sure the # original FIB answers = # updated FIB answers
     *      Therefore, there may be more or less rows in the table.
     */
    public boolean dbUpdateVO(Connection conn)
    {
        Secretary.startFxn("exam", "FillInBlankDAO.dbUpdateVO(conn)");
        String dbUpdateQuestionStr;
        PreparedStatement dbUpdateQuestionStmt = null;
        dbUpdateQuestionStr = "UPDATE ses_FILL_IN_BLANK "
                            + "SET question = ? WHERE insert_time = ?";
        String updQuestion = "UPDATE ses_FILL_IN_BLANK "
                     + "SET question = \"" + ((FillInBlankVO)examEntryVO).getQuestion() + "\" "
                     + "WHERE insert_time = " + examEntryVO.getInsertTime();
        
        String deleteSolution = "DELETE FROM ses_FILL_IN_BLANK_SOLUTION "
            + "WHERE insert_time = " + examEntryVO.getInsertTime();
        Statement stmt = null;
        
        PreparedStatement insertNewSolStmt = null;
        String insertNewSolStr = "INSERT INTO ses_FILL_IN_BLANK_SOLUTION "
            + "(insert_time, quest_loc, solution) "
            + "VALUES (?, ?, ?)";
        String insertLogStr;
        boolean toReturn = false;
        try{
            Secretary.write("exam", updQuestion);
            // UPDATE THE QUESTION
            dbUpdateQuestionStmt = conn.prepareStatement(dbUpdateQuestionStr);
            dbUpdateQuestionStmt.setString(1, ((FillInBlankVO)examEntryVO).getQuestion());
            dbUpdateQuestionStmt.setLong(2, examEntryVO.getInsertTime());
            dbUpdateQuestionStmt.executeUpdate();
            
            // DELETE SOLUTIONS CURRENTLY IN TABLE
            Secretary.write("exam", deleteSolution);
            stmt = conn.createStatement();
            stmt.executeUpdate(deleteSolution);
            
            // INSERT NEW SOLUTIONS
            insertNewSolStmt = conn.prepareStatement(insertNewSolStr);
            
            for(int i=0; i < ((FillInBlankVO)examEntryVO).getSolutionsSize(); i++)
            {
                insertLogStr = "INSERT INTO ses_FILL_IN_BLANK_SOLUTION "
                             + "(insert_time, quest_loc, solution) "
                             + "VALUES (" + examEntryVO.getInsertTime() + ", "
                             + i + ", " + ((FillInBlankVO)examEntryVO).getSolutionAt(i).toString() + ")";
                Secretary.write("exam", insertLogStr);
                insertNewSolStmt.setLong(1, examEntryVO.getInsertTime());
                insertNewSolStmt.setInt(2, i);
                insertNewSolStmt.setString (3, ((FillInBlankVO)examEntryVO).getSolutionAt(i).toString());
                insertNewSolStmt.executeUpdate();
            }
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in FillInBlankDAO.dbUpdateVO(conn) =>" + e.getMessage());
        }finally{
            try{
                stmt.close();
                dbUpdateQuestionStmt.close();
                insertNewSolStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in FillInBlankDAO.dbUpdateVO(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "FillInBlankDAO.dbUpdateVO(conn) => " + toReturn);
        return toReturn;
    }

    public boolean dbLoadVO(Connection conn)
    {
        //Secretary.startFxn("exam", "FillInBlankDAO.dbLoadVO(conn)");
        String select = "SELECT q.question, s.solution FROM "
                + "ses_FILL_IN_BLANK q, ses_FILL_IN_BLANK_SOLUTION s "
                + "WHERE q.insert_time = " + examEntryVO.getInsertTime()
                + " AND s.insert_time =  " + examEntryVO.getInsertTime()
                + " ORDER BY s.quest_loc";
        boolean toReturn = false;
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(select);
            while(rs.next())
            {
                if(((FillInBlankVO)examEntryVO).getQuestion().length() == 0)
                    ((FillInBlankVO)examEntryVO).setQuestion(rs.getString("question").trim());
                ((FillInBlankVO)examEntryVO).addSolution(rs.getString("solution").trim());
            }
            ((FillInBlankVO)examEntryVO).setNumSolutions(((FillInBlankVO)examEntryVO).getNumSolutions());
            ((FillInBlankVO)examEntryVO).computePointValuePerAnswer();
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in FillInBlankDAO.dbLoadVO(conn) => " + e.getMessage());
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in FillInBlankDAO.dbLoadVO(conn) => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in FillInBlankDAO.dbLoadVO(conn) => " + e.getMessage());
            }
        }
        //Secretary.endFxn("exam", "FillInBlankDAO.dbLoadVO(conn)");
        return toReturn;
    }

    public boolean dbDeleteVO(Connection conn)
    {
        Secretary.startFxn("exam", "FillInBlankDAO.dbDeleteVO(conn)");
        boolean toReturn = false;
        String deleteQuestion = "DELETE FROM ses_FILL_IN_BLANK "
            + "WHERE insert_time = " + examEntryVO.getInsertTime();
        String deleteSolution = "DELETE FROM ses_FILL_IN_BLANK_SOLUTION "
            + "WHERE insert_time = " + examEntryVO.getInsertTime();
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(deleteSolution);
            stmt.executeUpdate(deleteQuestion);
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in FillInBlankDAO.dbDeleteVO(conn) => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in FillInBlankDAO.dbDeleteVO(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "FillInBlankDAO.dbDeleteVO(conn)");
        return toReturn;
    }

    public boolean dbDeleteFullExam(Connection conn, int eNum)
    {   
        Secretary.startFxn("exam", "FillInBlankDAO.dbDeleteFullExam(conn, "+eNum+")");
        boolean toReturn = false;
        String delQuestion = "DELETE FROM ses_FILL_IN_BLANK WHERE insert_time IN "
            + "(SELECT insert_time FROM ses_EXAM_ENTRY WHERE exam_num = " + eNum
            + " AND entry_type_code = '" + this.examEntryVO.getEntryTypeCode() + "')";
        String delSolution = "DELETE FROM ses_FILL_IN_BLANK_SOLUTION WHERE insert_time IN "
            + "(SELECT insert_time FROM ses_EXAM_ENTRY WHERE exam_num = " + eNum
            + " AND entry_type_code = '" + this.examEntryVO.getEntryTypeCode() + "')";
        Secretary.write("exam", delQuestion);
        Secretary.write("exam", delSolution);
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(delSolution);
            stmt.executeUpdate(delQuestion);
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in FillInBlankDAO.dbDeleteFullExam(conn, "+eNum+") => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in FillInBlankDAO.dbDeleteFullExam(conn, "+eNum+") => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "FillInBlankDAO.dbDeleteFullExam(conn,"+eNum+")");
        return toReturn;
    }
    
    public boolean dbInsertTakenVO(Connection conn)
    {
        //Secretary.startFxn("exam", "FillInBlankDAO.dbInsertTaken(conn)");
        boolean toReturn = false;
        try{
            dbInsertTakenStmt = conn.prepareStatement(dbInsertTakenStr);
            dbInsertTakenStmt.setInt(1, examEntryVO.getTakeNum());
            //dbInsertTakenStmt.setInt(2, examEntryVO.getExamLoc());
            dbInsertTakenStmt.setLong(2, examEntryVO.getInsertTime());
            dbInsertTakenStmt.setInt(5, 0);
            dbInsertTakenStmt.setInt(6, 0);
            dbInsertTakenStmt.setString(7, "");
            String empAnswer;
            for(int x=0; x < ((FillInBlankVO)examEntryVO).getEmpAnswersSize(); x++)
            {
                dbInsertTakenStmt.setInt(3, x);
                empAnswer = ((FillInBlankVO)examEntryVO).getEmpAnswerAt(x);
                dbInsertTakenStmt.setString(4, empAnswer.toString());
                dbInsertTakenStmt.executeUpdate();
                toReturn = true;
            }
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in FillInBlankDAO.dbInsertTaken(conn) => " + e.getMessage());
        }finally{
            try{
                dbInsertTakenStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in FillInBlankDAO.dbInsertTaken(conn) => " + e.getMessage());
            }
        }
        //Secretary.endFxn("exam", "FillInBlankDAO.dbInsertTaken(conn)");
        return toReturn;
    }
    
    public boolean dbUpdatePointsEarned(Connection conn) 
    {
        //Secretary.startFxn("exam", "FillInBlankDAO.dbUpdatePointsEarned(conn)");
        boolean toReturn = false;
        try{
            dbUpdatePointsStmt = conn.prepareStatement(dbUpdatePointsStr);
            int points;
            boolean ec;
            for(int x=0; x < ((FillInBlankVO)examEntryVO).getEmpAnswersSize(); x++){
                ec = ((FillInBlankVO)examEntryVO).getEmpCorrectAt(x);
                if(ec)
                {
                    dbUpdatePointsStmt.setInt(1, 1);
                    dbUpdatePointsStmt.setInt(2, ((FillInBlankVO)examEntryVO).getPointValuePerAnswer());
                }
                else
                {
                    dbUpdatePointsStmt.setInt(1, -1);
                    dbUpdatePointsStmt.setInt(2, 0);
                }
                dbUpdatePointsStmt.setString(3, ((FillInBlankVO)(this.examEntryVO)).getGradersComment());
                dbUpdatePointsStmt.setInt(4, this.examEntryVO.getTakeNum());
                dbUpdatePointsStmt.setLong(5, this.examEntryVO.getInsertTime());
                dbUpdatePointsStmt.setInt(6, x);
                dbUpdatePointsStmt.executeUpdate();
                toReturn = true;
            }
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in FillInBlankDAO.dbUpdatePointsEarned(conn) => " + e.getMessage());
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in FillInBlankDAO.dbUpdatePointsEarned(conn) => "  + e.getMessage());
        }finally{
            try{
                dbUpdatePointsStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in FillInBlankDAO.dbUpdatePointsEarned(conn) => " + e.getMessage());
            }
        }
        //Secretary.endFxn("exam", "FillInBlankDAO.dbUpdatePointsEarned(conn) => " + toReturn);
        return toReturn;  
    }

    public boolean dbLoadEmpAnswer(Connection conn)
    {
        //Secretary.write("exam", "FillInBlankDAO.dbLoadEmpAnswer(conn)");
        boolean toReturn = false;
        String select = "SELECT answer_entered, correct, points_earned, comment FROM "
            + "ses_EMP_ANSWER WHERE take_num = " + examEntryVO.getTakeNum()
            + " AND insert_time = " + examEntryVO.getInsertTime()
            + " ORDER BY quest_loc";
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(select);
            int pointsEarned = 0;    
            while(rs.next())
            {
                ((FillInBlankVO)examEntryVO).addEmpAnswer(rs.getString("answer_entered").trim());
                if(rs.getInt("correct") == 1)
                    ((FillInBlankVO)examEntryVO).addEmpCorrect(true);
                else if(rs.getInt("correct") == -1)
                    ((FillInBlankVO)examEntryVO).addEmpCorrect(false);
                pointsEarned += rs.getInt("points_earned");
                ((FillInBlankVO)examEntryVO).setGradersComment(rs.getString("comment").trim());
            }
            ((FillInBlankVO)examEntryVO).setPointValueEarnedTotal(pointsEarned);
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in FillInBlankDAO.dbLoadEmpAnswer(conn) => "  + e.getMessage());
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in FillInBlankDAO.dbLoadEmpAnswer(conn) => "  + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in FillInBlankDAO.dbLoadEmpAnswer(conn) => " + e.getMessage());
            }
        }
        //Secretary.write("exam", "FillInBlankDAO.dbLoadEmpAnswer(conn)");
        return toReturn;
    }

    /*
    public void dbIncrementExamLocVO(Connection conn)
    {
        Secretary.startFxn("exam", "FillInBlankDAO.dbIncrementExamLocVO(conn)");
        examEntryVO.logValues();
        String incrementQ = "UPDATE ses_FILL_IN_BLANK"
                        + " SET"
                        + " exam_loc = " + examEntryVO.getExamLoc()
                        + " WHERE exam_num = " + examEntryVO.getExamNum()
                        + " AND exam_loc = " + (examEntryVO.getExamLoc() - 1);
        String incrementS = "UPDATE ses_FILL_IN_BLANK_SOLUTION"
                        + " SET"
                        + " exam_loc = " + examEntryVO.getExamLoc()
                        + " WHERE exam_num = " + examEntryVO.getExamNum()
                        + " AND exam_loc = " + (examEntryVO.getExamLoc() - 1);
        Secretary.write("exam", incrementQ);
        Secretary.write("exam", incrementS);
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(incrementQ);
            stmt.executeUpdate(incrementS);
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in FillInBlankDAO.dbIncrementExamLocVO(conn) => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in FillInBlankDAO.dbIncrementExamLocVO(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "FillInBlankDAO.dbIncrementExamLocVO(conn)");
    }
   */
    public void dropVO() 
    {
        examEntryVO = null;
    }
    public exam.vos.ExamEntryVO getVO() 
    {
        return examEntryVO;
    }
    public void setVO(exam.vos.ExamEntryVO vo) 
    {
        examEntryVO = (FillInBlankVO)vo;
    }

    public FillInBlankDAO()
    {
        super();
    }

}