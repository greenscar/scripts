/***********************************************************************
 * Module:  MatchingDAO.java
 * Author:  jsandlin
 * Purpose: Defines the Class MatchingDAO
 ***********************************************************************/
package exam.daos;
import tools.HTMLVars;
import exam.vos.MatchingVO;
import java.util.*;
import java.sql.*;
import logging.Secretary;

public final class MatchingDAO extends QuestionDAO implements HTMLVars
{
    public boolean dbInsertVO(Connection conn)
    {
        Secretary.startFxn("exam", "MatchingDAO.dbInsertVO(conn)");
        PreparedStatement dbInsertStmt = null;
        PreparedStatement dbInsertQuestionStmt = null;
        PreparedStatement dbInsertChoiceStmt = null;
        String dbInsertStr = "INSERT INTO ses_MATCHING (insert_time, description) VALUES (?, ?)";
        String dbInsertQuestionStr = "INSERT INTO ses_MATCHING_QUESTION (insert_time, quest_loc, question, choice_loc_of_solution) VALUES (?, ?, ?, ?)";
        String dbInsertChoiceStr =  "INSERT INTO ses_MATCHING_CHOICE (insert_time, choice_loc, choice) VALUES (?, ?, ?)";
        boolean toReturn = false;
        try{
            dbInsertStmt = conn.prepareStatement(dbInsertStr);
            dbInsertQuestionStmt = conn.prepareStatement(dbInsertQuestionStr);
            dbInsertChoiceStmt = conn.prepareStatement(dbInsertChoiceStr);
            // dbInsert THE DESCRIPTION
            dbInsertStmt.setLong(1, examEntryVO.getInsertTime());
            dbInsertStmt.setString(2, ((MatchingVO)examEntryVO).getDescription());
            dbInsertStmt.executeUpdate();
            Character temp;
            // dbInsert THE QUESTIONS
            for(int i=0; i < ((MatchingVO)examEntryVO).getNumQuestions(); i++)
            {
                temp = ((MatchingVO)examEntryVO).getSolutionAt(i);
                dbInsertQuestionStmt.setLong(1, examEntryVO.getInsertTime());
                dbInsertQuestionStmt.setInt(2, i);
                if(i < ((MatchingVO)examEntryVO).getNumQuestions())
                    dbInsertQuestionStmt.setString(3, ((MatchingVO)examEntryVO).getQuestionAt(i));
                else
                    dbInsertQuestionStmt.setString(3, "N/A");
                    dbInsertQuestionStmt.setInt(4, (Arrays.binarySearch(LETTER, temp.charValue()) + 1));
                    dbInsertQuestionStmt.executeUpdate();
            }
            // dbInsert THE CHOICES
            for(int i=0; i < ((MatchingVO)examEntryVO).getNumChoices(); i++)
            {
                dbInsertChoiceStmt.setLong(1, examEntryVO.getInsertTime());
                dbInsertChoiceStmt.setInt(2, (i + 1));
                dbInsertChoiceStmt.setString(3, ((MatchingVO)examEntryVO).getChoiceAt(i));
                dbInsertChoiceStmt.executeUpdate();
            }
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in MatchingDAO.dbInsertVO(conn) => " + e.getMessage()); 
        }finally{
            try{
                dbInsertStmt.close();
                dbInsertChoiceStmt.close();
                dbInsertQuestionStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in MatchingDAO.dbInsertVO(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "MatchingDAO.dbInsertVO(conn) => " + toReturn);
        return toReturn;
    }
   
    /*
     * 1) Update ses_MATCHING
     * 2) Delete all from ses_MATCHING_QUESTION
     * 3) Delete all from ses_MATCHING_CHOICE
     * 4) Insert into ses_MATCHING_QUESTION
     * 5) Insert into ses_MATCHING_CHOICE
     */
    public boolean dbUpdateVO(Connection conn)
    {
        Secretary.startFxn("exam", "MatchingDAO.dbUpdateVO(conn)");
        
        Statement stmt = null;
        
        String dbUpdateEntry = "UPDATE ses_MATCHING SET description = ? WHERE insert_time = ?";
        PreparedStatement dbUpdateEntryStmt = null;
        
        String dbDeleteQuestion = "DELETE FROM ses_MATCHING_QUESTION "
            + "WHERE insert_time = " + examEntryVO.getInsertTime();
        String dbInsertQuestionStr = "INSERT INTO ses_MATCHING_QUESTION (insert_time, quest_loc, question, choice_loc_of_solution)  VALUES (?, ?, ?, ?)";
        PreparedStatement dbInsertQuestionStmt = null;
        
        String dbDeleteChoice = "DELETE FROM ses_MATCHING_CHOICE "
            + "WHERE insert_time = " + examEntryVO.getInsertTime();
        String dbInsertChoiceStr =  "INSERT INTO ses_MATCHING_CHOICE (insert_time, choice_loc, choice) VALUES (?, ?, ?)";
        PreparedStatement dbInsertChoiceStmt = null;

        boolean toReturn = false;
        Character temp;
        try{
            stmt = conn.createStatement();
            dbInsertQuestionStmt = conn.prepareStatement(dbInsertQuestionStr);
            dbInsertChoiceStmt = conn.prepareStatement(dbInsertChoiceStr);
            
            // 1) Update ses_MATCHING
            dbUpdateEntryStmt = conn.prepareStatement(dbUpdateEntry);
            String toLog = "UPDATE ses_MATCHING SET description = \""
                         + ((MatchingVO)examEntryVO).getDescription() + "\" "
                         + "WHERE insert_time = " + examEntryVO.getInsertTime();
            Secretary.write("exam", toLog);
            dbUpdateEntryStmt.setString(1, ((MatchingVO)examEntryVO).getDescription());
            dbUpdateEntryStmt.setLong(2, examEntryVO.getInsertTime());
            dbUpdateEntryStmt.executeUpdate();
            
            //2) Delete all from ses_MATCHING_QUESTION
            Secretary.write("exam", dbDeleteQuestion);
            stmt.executeUpdate(dbDeleteQuestion);
            
            //3) Delete all from ses_MATCHING_CHOICE
            Secretary.write("exam", dbDeleteChoice);
            stmt.executeUpdate(dbDeleteChoice);
            
            //4) Insert into ses_MATCHING_QUESTION
            for(int i=0; i < ((MatchingVO)examEntryVO).getNumQuestions(); i++)
            {
                temp = ((MatchingVO)examEntryVO).getSolutionAt(i);
                toLog = "INSERT INTO ses_MATCHING_QUESTION (insert_time, quest_loc, question, choice_loc_of_solution) "
                      + "VALUES (" + examEntryVO.getInsertTime() + ", "
                      + i + ", \"" + ((MatchingVO)examEntryVO).getQuestionAt(i) + "\", "
                      + (Arrays.binarySearch(LETTER, temp.charValue()) + 1) + ");";
                Secretary.write("exam", toLog);
                //int solutionNum = choices.indexOf(solutions.elementAt(i)) + 1;
                dbInsertQuestionStmt.setLong(1, examEntryVO.getInsertTime());
                dbInsertQuestionStmt.setInt(2, (i+1));
                dbInsertQuestionStmt.setString(3, ((MatchingVO)examEntryVO).getQuestionAt(i));
                dbInsertQuestionStmt.setInt(4, Arrays.binarySearch(LETTER, temp.charValue()) + 1);
                dbInsertQuestionStmt.executeUpdate();
            }
            //5) Insert into ses_MATCHING_CHOICE
            // INSERT THE CHOICES
            for(int i=0; i < ((MatchingVO)examEntryVO).getNumChoices(); i++)
            {
                toLog = "INSERT INTO ses_MATCHING_CHOICE (insert_time, choice_loc, choice) "
                      + "VALUES (" + examEntryVO.getInsertTime() + ", "
                      + (i + 1) + ", \"" + ((MatchingVO)examEntryVO).getChoiceAt(i) + "\");";
                Secretary.write("exam", toLog);
                dbInsertChoiceStmt.setLong(1, examEntryVO.getInsertTime());
                dbInsertChoiceStmt.setInt(2, (i + 1));
                dbInsertChoiceStmt.setString(3, ((MatchingVO)examEntryVO).getChoiceAt(i));
                dbInsertChoiceStmt.executeUpdate();
            }
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in MatchingDAO.dbUpdateVO(conn) => " + e.getMessage());
        }finally{
            try{
                dbUpdateEntryStmt.close();
                dbInsertQuestionStmt.close();
                dbInsertChoiceStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in MatchingDAO.dbUpdateVO(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "MatchingDAO.dbUpdateVO(conn) => " + toReturn);
        return toReturn;
    }
    
    public boolean dbLoadVO(Connection conn)
    {
        //Secretary.startFxn("exam", "MatchingDAO.dbLoadVO(conn)");
        String select = "SELECT m.description, mq.question, "
                    + "mq.choice_loc_of_solution, mc.choice_loc, "
                    + "mc.choice FROM "
                    + "ses_MATCHING m, ses_MATCHING_QUESTION mq, ses_MATCHING_CHOICE mc "
                    + "WHERE m.insert_time = " + examEntryVO.getInsertTime()
                    + " AND mq.insert_time = " + examEntryVO.getInsertTime()
                    + " AND mc.insert_time = " + examEntryVO.getInsertTime()
                    + " ORDER BY mc.choice_loc";
        // Temp Vars
        String aChoice, aQuestion, descr;
        int cNum;
        Integer cNumOfSol;
        int cCount = 0, sCount = 0, qCount = 0;
        // END Temp Vars
        boolean toReturn = false;
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(select);
            while(rs.next())
            {
                descr = rs.getString("description").trim();
                aQuestion = rs.getString("question").trim();
                cNumOfSol = new Integer(rs.getInt("choice_loc_of_solution"));
                cNum = rs.getInt("choice_loc");
                aChoice = rs.getString("choice").trim();
                if(((MatchingVO)examEntryVO).getDescription() == "")
                    ((MatchingVO)examEntryVO).setDescription(descr);
                if(!(((MatchingVO)examEntryVO).choiceExists(aChoice)))
                    ((MatchingVO)examEntryVO).addChoice(cCount++, aChoice);
                if(!(((MatchingVO)examEntryVO).questionExists(aQuestion)))
                {
                    ((MatchingVO)examEntryVO).addQuestion(qCount++, aQuestion);
                    Character ans = new Character(LETTER[(cNumOfSol.intValue() - 1)]);
                    ((MatchingVO)examEntryVO).addSolution(ans);
                }
            }
            ((MatchingVO)examEntryVO).setNumSolutions(((MatchingVO)examEntryVO).getNumQuestions());
            ((MatchingVO)examEntryVO).computePointValuePerAnswer();
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in MatchingDAO.dbLoadVO(conn) => " + e.getMessage()); 
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in MatchingDAO.dbLoadVO(conn) => " + e.getMessage());
            }
        }
        //Secretary.endFxn("exam", "MatchingDAO.dbLoadVO(conn) => " + toReturn);
        return toReturn;
    }
   
    public boolean dbLoadEmpAnswer(Connection conn)
    {
        Secretary.startFxn("exam", "MatchingDAO.dbLoadEmpAnswer(conn)");
        boolean toReturn = false;
        String select = "SELECT answer_entered, correct, points_earned, comment "
                    + "FROM ses_EMP_ANSWER WHERE "
                    + "take_num = " + examEntryVO.getTakeNum()
                    + " AND insert_time = " + examEntryVO.getInsertTime()
                    + " ORDER BY quest_loc";
        Secretary.write("exam", select);
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(select);
            String empAnsw;
            int correct, qLoc, pointsEarned = 0;
            while(rs.next())
            {
                correct = rs.getInt(2);
                ((MatchingVO)examEntryVO).addEmpAnswer(new Character((rs.getString(1).trim().charAt(0))));
                if(correct == -1) 
                ((MatchingVO)examEntryVO).addEmpCorrect(new Boolean(false));
                else if (correct == 1) 
                ((MatchingVO)examEntryVO).addEmpCorrect(new Boolean(true));
                pointsEarned += rs.getInt("points_earned");
                ((MatchingVO)examEntryVO).setGradersComment(rs.getString("comment").trim());
            }
            ((MatchingVO)examEntryVO).setPointValueEarnedTotal(pointsEarned);
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in MatchingDAO.dbLoadEmpAnswer() => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in MatchingDAO.dbLoadEmpAnswer(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "MatchingDAO.dbLoadEmpAnswer(conn) => " + toReturn);
        return toReturn;
    }
    
    public boolean dbDeleteVO(Connection conn)
    {
        Secretary.startFxn("exam", "MatchingDAO.dbDeleteVO(conn)");
        String dbDelete = "DELETE FROM ses_MATCHING "
            + "WHERE insert_time = " + examEntryVO.getInsertTime();
        Secretary.write("exam", dbDelete);
        String dbDeleteQuestion = "DELETE FROM ses_MATCHING_QUESTION "
            + "WHERE insert_time = " + examEntryVO.getInsertTime();
        Secretary.write("exam", dbDeleteQuestion);
        String dbDeleteChoice = "DELETE FROM ses_MATCHING_CHOICE "
            + "WHERE insert_time = " + examEntryVO.getInsertTime();
        Secretary.write("exam", dbDeleteChoice);
        boolean toReturn = false;
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(dbDeleteQuestion);
            stmt.executeUpdate(dbDeleteChoice);
            stmt.executeUpdate(dbDelete);
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in MatchingDAO.dbDeleteVO(conn) => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in MatchingDAO.dbDeleteVO(conn) => " + e.getMessage());
            }
        }
        Secretary.startFxn("exam", "MatchingDAO.dbDeleteVO(conn)");
        return toReturn;
    }
   
    public boolean dbDeleteFullExam(Connection conn, int eNum)
    {
        Secretary.startFxn("exam", "MatchingDAO.dbDeleteFullExam(conn, "+eNum+")");
        
        String delDescr = "DELETE FROM ses_MATCHING WHERE insert_time IN "
            + "(SELECT insert_time FROM ses_EXAM_ENTRY WHERE exam_num = " + eNum
            + " AND entry_type_code = '" + this.examEntryVO.getEntryTypeCode() + "')";
        String delQuestion = "DELETE FROM ses_MATCHING_QUESTION WHERE insert_time IN "
            + "(SELECT insert_time FROM ses_EXAM_ENTRY WHERE exam_num = " + eNum
            + " AND entry_type_code = '" + this.examEntryVO.getEntryTypeCode() + "')";
        String delChoice = "DELETE FROM ses_MATCHING_CHOICE WHERE insert_time IN "
            + "(SELECT insert_time FROM ses_EXAM_ENTRY WHERE exam_num = " + eNum
            + " AND entry_type_code = '" + this.examEntryVO.getEntryTypeCode() + "')";
        
        boolean toReturn = false;
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(delQuestion);
            stmt.executeUpdate(delChoice);
            stmt.executeUpdate(delDescr);
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in MatchingDAO.dbDeleteFullExam(conn, "+eNum+") => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in MatchingDAO.dbDeleteFullExam(conn, "+eNum+") => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "MatchingDAO.dbDeleteFullExam(conn, "+eNum+")");
        return toReturn;
    }
    
    public boolean dbInsertTakenVO(Connection conn)
    {
        //Secretary.startFxn("exam", "MatchingDAO.dbInsertTakenVO(conn)");
        boolean toReturn = false;
        try{
            //examEntryVO.logValues();
            dbInsertTakenStmt = conn.prepareStatement(dbInsertTakenStr);
            dbInsertTakenStmt.setInt(1, examEntryVO.getTakeNum());
            //dbInsertTakenStmt.setInt(2, examEntryVO.getExamLoc());
            dbInsertTakenStmt.setLong(2, examEntryVO.getInsertTime());
            Character empAnswer, correctAnswer;
            for(int x=0; x < ((MatchingVO)examEntryVO).getNumEmpAnswers(); x++)
            {
                empAnswer = ((MatchingVO)examEntryVO).getEmpAnswerAt(x);
                correctAnswer = ((MatchingVO)examEntryVO).getSolutionAt(x);
                dbInsertTakenStmt.setInt(3, x);
                dbInsertTakenStmt.setString(4, empAnswer.toString());
                String toLog = "INSERT INTO ses_EMP_ANSWER(take_num, insert_time, "
                    + "quest_loc, answer_entered, correct, points_earned, comment) VALUES "
                    + "(" + this.examEntryVO.getTakeNum() 
                    + ", " + this.examEntryVO.getInsertTime()
                    + ", " + x
                    + ", " + empAnswer.toString();
                if(((MatchingVO)examEntryVO).getEmpCorrectAt(x))
                {
                    //Secretary.write("exam", "solution("+x+") is CORRECT");
                    int y = ((exam.vos.QuestionVO)examEntryVO).getPointValuePerAnswer();
                    dbInsertTakenStmt.setInt(5, 1);
                    dbInsertTakenStmt.setInt(6, y);
                    toLog += ", 1, " + y;
                }
                else
                {
                    //Secretary.write("exam", "solution("+x+") is WRONG");
                    dbInsertTakenStmt.setInt(5, -1);
                    dbInsertTakenStmt.setInt(6, 0);
                    toLog += ", -1, 0";
                }
                toLog += "\"\");";
                Secretary.write("exam", toLog);
                dbInsertTakenStmt.setString(7, "");
                dbInsertTakenStmt.executeUpdate();
            }
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in MatchingDAO.dbInsertTakenVO(conn) => " + e.getMessage());
        }finally{
            try{
                dbInsertTakenStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in MatchingDAO.dbInsertTakenVO(conn) => " + e.getMessage());
            }
        }
        //Secretary.endFxn("exam", "MatchingDAO.dbInsertTakenVO(conn) => " + toReturn);
        return toReturn;
    }
   
    public boolean dbUpdatePointsEarned(Connection conn) 
    {
        Secretary.startFxn("exam", "MatchingDAO.dbUpdatePointsEarned(conn)");
        boolean toReturn = false;
        try{
            dbUpdatePointsStmt = conn.prepareStatement(dbUpdatePointsStr);
            int points;
            boolean ec;
            for(int x=0; x < ((MatchingVO)examEntryVO).getNumEmpAnswers(); x++)
            {
                ec = ((MatchingVO)examEntryVO).getEmpCorrectAt(x);
                if(ec)
                {
                    dbUpdatePointsStmt.setInt(1, 1);
                    dbUpdatePointsStmt.setInt(2, ((MatchingVO)examEntryVO).getPointValuePerAnswer());
                }
                else
                {
                    dbUpdatePointsStmt.setInt(1, -1);
                    dbUpdatePointsStmt.setInt(2, 0);
                }
                dbUpdatePointsStmt.setString(3, ((MatchingVO)(this.examEntryVO)).getGradersComment());
                dbUpdatePointsStmt.setInt(4, this.examEntryVO.getTakeNum());
                dbUpdatePointsStmt.setLong(5, this.examEntryVO.getInsertTime());
                dbUpdatePointsStmt.setInt(6, x);
                dbUpdatePointsStmt.executeUpdate();
                toReturn = true;
            }
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in MatchingDAO.dbUpdatePointsEarned(conn) => " + e.getMessage());
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in MatchingDAO.dbUpdatePointsEarned(conn) => "  + e.getMessage());
        }finally{
            try{
                dbUpdatePointsStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in MatchingDAO.dbUpdatePointsEarned(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "MatchingDAO.dbUpdatePointsEarned(conn)");
        return toReturn;  
    }
    /*
    public void dbIncrementExamLocVO(Connection conn)
    {
        Secretary.startFxn("exam", "MatchingDAO.dbIncrementExamLocVO(conn)");
        String increment = "UPDATE ses_MATCHING"
                        + " SET"
                        + " exam_loc = " + examEntryVO.getExamLoc()
                        + " WHERE exam_num = " + examEntryVO.getExamNum()
                        + " AND exam_loc = " + (examEntryVO.getExamLoc() - 1);
        String incrementQ = "UPDATE ses_MATCHING_QUESTION"
                        + " SET"
                        + " exam_loc = " + examEntryVO.getExamLoc()
                        + " WHERE exam_num = " + examEntryVO.getExamNum()
                        + " AND exam_loc = " + (examEntryVO.getExamLoc() - 1);
        String incrementS = "UPDATE ses_MATCHING_CHOICE"
                        + " SET"
                        + " exam_loc = " + examEntryVO.getExamLoc()
                        + " WHERE exam_num = " + examEntryVO.getExamNum()
                        + " AND exam_loc = " + (examEntryVO.getExamLoc() - 1);
        Secretary.write("exam", increment);
        Secretary.write("exam", incrementQ);
        Secretary.write("exam", incrementS);
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(increment);
            stmt.executeUpdate(incrementQ);
            stmt.executeUpdate(incrementS);
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in MatchingDAO.dbIncrementExamLocVO(conn) => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in MatchingDAO.dbIncrementExamLocVO(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "MatchingDAO.dbIncrementExamLocVO(conn)");
    }
     */
    public void dropVO() {
        examEntryVO = null;
    }

    public exam.vos.ExamEntryVO getVO() {
        return examEntryVO;
    }

    public void setVO(exam.vos.ExamEntryVO vo) {
        examEntryVO = (MatchingVO)vo;
    }

    public MatchingDAO()
    {
        super();
    }
}