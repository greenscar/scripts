/***********************************************************************
 * Module:  WordBankDAO.java
 * Author:  jsandlin
 * Purpose: Defines the Class WordBankDAO
 ***********************************************************************/
package exam.daos;
import exam.vos.WordBankVO;
import java.util.*;
import java.sql.*;
import logging.Secretary;

public final class WordBankDAO extends QuestionDAO implements tools.HTMLVars
{
    public boolean dbInsertVO(Connection conn)
    {
        Secretary.startFxn("exam", "WordBankDAO.dbInsertVO(conn)");
        String dbInsertStr;
        PreparedStatement dbInsertStmt = null;
        String dbInsertQuestionStr;
        PreparedStatement dbInsertQuestionStmt = null;
        String dbInsertChoiceStr;
        PreparedStatement dbInsertChoiceStmt = null;
        dbInsertStr = "INSERT INTO ses_WORD_BANK (insert_time, description) VALUES (?, ?)";
        dbInsertQuestionStr = "INSERT INTO ses_WORD_BANK_QUESTION (insert_time, quest_loc, question, choice_loc_of_solution)  VALUES (?, ?, ?, ?)";
        dbInsertChoiceStr =  "INSERT INTO ses_WORD_BANK_CHOICE (insert_time, choice_loc, choice) VALUES (?, ?, ?)";

        boolean toReturn = false;
        try{
            dbInsertStmt = conn.prepareStatement(dbInsertStr);
            dbInsertQuestionStmt = conn.prepareStatement(dbInsertQuestionStr);
            dbInsertChoiceStmt = conn.prepareStatement(dbInsertChoiceStr);
            // INSERT THE DESCRIPTION
            dbInsertStmt.setLong(1, examEntryVO.getInsertTime());
            dbInsertStmt.setString(2, ((WordBankVO)examEntryVO).getDescription());
            dbInsertStmt.executeUpdate();
            Character temp;
            // INSERT THE QUESTIONS
            for(int i=0; i < ((WordBankVO)examEntryVO).getNumQuestions(); i++)
            {
                //int solutionNum = choices.indexOf(solutions.elementAt(i)) + 1;
                temp = ((WordBankVO)examEntryVO).getSolutionAt(i);
                dbInsertQuestionStmt.setLong(1, examEntryVO.getInsertTime());
                dbInsertQuestionStmt.setInt(2, i);
                dbInsertQuestionStmt.setString(3, ((WordBankVO)examEntryVO).getQuestionAt(i));
                dbInsertQuestionStmt.setInt(4, Arrays.binarySearch(LETTER, temp.charValue()) + 1);
                dbInsertQuestionStmt.executeUpdate();
            }
            // INSERT THE CHOICES
            for(int i=0; i < ((WordBankVO)examEntryVO).getNumChoices(); i++)
            {
                dbInsertChoiceStmt.setLong(1, examEntryVO.getInsertTime());
                dbInsertChoiceStmt.setInt(2, (i + 1));
                dbInsertChoiceStmt.setString(3, ((WordBankVO)examEntryVO).getChoiceAt(i));
                dbInsertChoiceStmt.executeUpdate();
            }
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in WordBankDAO.dbInsertVO(conn) => " + e.getMessage());
        }finally{
            try{
                dbInsertStmt.close();
                dbInsertQuestionStmt.close();
                dbInsertChoiceStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in WordBankDAO.dbInsertVO(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "WordBankDAO.dbInsertVO(conn) => " + toReturn);
        return toReturn;
    }
    /*
     * 1) Update ses_WORD_BANK
     * 2) Delete all from ses_WORD_BANK_QUESTION
     * 3) Delete all from ses_WORD_BANK_CHOICE
     * 4) Insert into ses_WORD_BANK_QUESTION
     * 5) Insert into ses_WORD_BANK_CHOICE
     */
    public boolean dbUpdateVO(Connection conn)
    {
        Secretary.startFxn("exam", "WordBankDAO.dbUpdateVO(conn)");
        
        Statement stmt = null;
        
        String dbUpdateEntry = "UPDATE ses_WORD_BANK SET description = ? WHERE insert_time = ?";
        PreparedStatement dbUpdateEntryStmt = null;
        
        String dbDeleteQuestion = "DELETE FROM ses_WORD_BANK_QUESTION "
            + "WHERE insert_time = " + examEntryVO.getInsertTime();
        String dbInsertQuestionStr = "INSERT INTO ses_WORD_BANK_QUESTION (insert_time, quest_loc, question, choice_loc_of_solution)  VALUES (?, ?, ?, ?)";
        PreparedStatement dbInsertQuestionStmt = null;
        
        String dbDeleteChoice = "DELETE FROM ses_WORD_BANK_CHOICE "
            + "WHERE insert_time = " + examEntryVO.getInsertTime();
        String dbInsertChoiceStr =  "INSERT INTO ses_WORD_BANK_CHOICE (insert_time, choice_loc, choice) VALUES (?, ?, ?)";
        PreparedStatement dbInsertChoiceStmt = null;

        boolean toReturn = false;
        Character temp;
        try{
            stmt = conn.createStatement();
            dbInsertQuestionStmt = conn.prepareStatement(dbInsertQuestionStr);
            dbInsertChoiceStmt = conn.prepareStatement(dbInsertChoiceStr);
            
            // 1) Update ses_WORD_BANK
            dbUpdateEntryStmt = conn.prepareStatement(dbUpdateEntry);
            String toLog = "UPDATE ses_WORD_BANK SET description = \""
                         + ((WordBankVO)examEntryVO).getDescription() + "\" "
                         + "WHERE insert_time = " + examEntryVO.getInsertTime();
            Secretary.write("exam", toLog);
            dbUpdateEntryStmt.setString(1, ((WordBankVO)examEntryVO).getDescription());
            dbUpdateEntryStmt.setLong(2, examEntryVO.getInsertTime());
            dbUpdateEntryStmt.executeUpdate();
            
            //2) Delete all from ses_WORD_BANK_QUESTION
            Secretary.write("exam", dbDeleteQuestion);
            stmt.executeUpdate(dbDeleteQuestion);
            
            //3) Delete all from ses_WORD_BANK_CHOICE
            Secretary.write("exam", dbDeleteChoice);
            stmt.executeUpdate(dbDeleteChoice);
            
            //4) Insert into ses_WORD_BANK_QUESTION
            for(int i=0; i < ((WordBankVO)examEntryVO).getNumQuestions(); i++)
            {
                temp = ((WordBankVO)examEntryVO).getSolutionAt(i);
                toLog = "INSERT INTO ses_WORD_BANK_QUESTION (insert_time, quest_loc, question, choice_loc_of_solution) "
                      + "VALUES (" + examEntryVO.getInsertTime() + ", "
                      + i + ", \"" + ((WordBankVO)examEntryVO).getQuestionAt(i) + "\", "
                      + (Arrays.binarySearch(LETTER, temp.charValue()) + 1) + ");";
                Secretary.write("exam", toLog);
                //int solutionNum = choices.indexOf(solutions.elementAt(i)) + 1;
                dbInsertQuestionStmt.setLong(1, examEntryVO.getInsertTime());
                dbInsertQuestionStmt.setInt(2, i);
                dbInsertQuestionStmt.setString(3, ((WordBankVO)examEntryVO).getQuestionAt(i));
                dbInsertQuestionStmt.setInt(4, Arrays.binarySearch(LETTER, temp.charValue()) + 1);
                dbInsertQuestionStmt.executeUpdate();
            }
            //5) Insert into ses_WORD_BANK_CHOICE
            // INSERT THE CHOICES
            for(int i=0; i < ((WordBankVO)examEntryVO).getNumChoices(); i++)
            {
                toLog = "INSERT INTO ses_WORD_BANK_CHOICE (insert_time, choice_loc, choice) "
                      + "VALUES (" + examEntryVO.getInsertTime() + ", "
                      + (i + 1) + ", \"" + ((WordBankVO)examEntryVO).getChoiceAt(i) + "\");";
                Secretary.write("exam", toLog);
                dbInsertChoiceStmt.setLong(1, examEntryVO.getInsertTime());
                dbInsertChoiceStmt.setInt(2, (i + 1));
                dbInsertChoiceStmt.setString(3, ((WordBankVO)examEntryVO).getChoiceAt(i));
                dbInsertChoiceStmt.executeUpdate();
            }
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in WordBankDAO.dbUpdateVO(conn) => " + e.getMessage());
        }finally{
            try{
                dbUpdateEntryStmt.close();
                dbInsertQuestionStmt.close();
                dbInsertChoiceStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in WordBankDAO.dbUpdateVO(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "WordBankDAO.dbUpdateVO(conn) => " + toReturn);
        return toReturn;
    }
    
    
   public boolean dbLoadVO(Connection conn)
   {
      //Secretary.startFxn("exam", "WordBankDAO.dbLoadVO(conn)");
      String select = "SELECT wb.description, wbq.question, "
                    + "wbq.choice_loc_of_solution, wbc.choice_loc, "
                    + "wbc.choice FROM "
                    + "ses_WORD_BANK wb, ses_WORD_BANK_QUESTION wbq, ses_WORD_BANK_CHOICE wbc "
                    + "WHERE wb.insert_time = " + examEntryVO.getInsertTime()
                    + " AND wbq.insert_time = " + examEntryVO.getInsertTime()
                    + " AND wbc.insert_time = " + examEntryVO.getInsertTime()
                    + " ORDER BY wbc.choice_loc";
      boolean toReturn = false;
      Statement stmt = null;
      try{
         stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(select);
         int cCount = 0, sCount = 0, qCount = 0;
         while(rs.next()){
            String descr = rs.getString(1).trim();
            String aQuestion = rs.getString(2).trim();
            Integer cNumOfSol = new Integer(rs.getInt(3));
            int cNum = rs.getInt(4);
            String aChoice = rs.getString(5).trim();
            if(((WordBankVO)examEntryVO).getDescription() == ""){
                ((WordBankVO)examEntryVO).setDescription(descr);
            }
            if(!(((WordBankVO)examEntryVO).choiceExists(aChoice))){
                ((WordBankVO)examEntryVO).addChoice(cCount, aChoice);
                cCount++;
            }
            if(!(((WordBankVO)examEntryVO).questionExists(aQuestion))){
                ((WordBankVO)examEntryVO).addQuestion(qCount, aQuestion);
                qCount++;
                Character ans = new Character(LETTER[(cNumOfSol.intValue() - 1)]);
                ((WordBankVO)examEntryVO).addSolution(ans);
            }
         }
         ((WordBankVO)examEntryVO).setNumSolutions(((WordBankVO)examEntryVO).getNumQuestions());
         ((WordBankVO)examEntryVO).computePointValuePerAnswer();
         toReturn = true;
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in WordBankDAO.dbLoadVO(): " + e.getMessage());
      }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in WordBankDAO.dbLoadVO(conn) => " + e.getMessage());
            }
        }
      //Secretary.endFxn("exam", "WordBankDAO.dbLoadVO(conn) => " + toReturn);
      return toReturn;
   }
   
    public boolean dbLoadEmpAnswer(Connection conn)
    {
        Secretary.startFxn("exam", "WordBankDAO.dbLoadEmpAnswer(conn)");
        String query = "SELECT answer_entered, correct, points_earned, comment "
                        + "FROM ses_EMP_ANSWER WHERE "
                        + "take_num = " + examEntryVO.getTakeNum()
                        + " AND insert_time = " + examEntryVO.getInsertTime()
                        + " ORDER BY quest_loc";
        boolean toReturn = false;
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int pointsEarned = 0;
            while(rs.next())
            {
                int correct = rs.getInt(2);
                ((WordBankVO)examEntryVO).addEmpAnswer(new Character((rs.getString(1).trim().charAt(0))));
                if(correct == -1) 
                    ((WordBankVO)examEntryVO).addEmpCorrect(new Boolean(false));
                else if (correct == 1) 
                    ((WordBankVO)examEntryVO).addEmpCorrect(new Boolean(true));
                pointsEarned += rs.getInt("points_earned");
                ((WordBankVO)examEntryVO).setGradersComment(rs.getString("comment").trim());
            }
            ((WordBankVO)examEntryVO).setPointValueEarnedTotal(pointsEarned);
            toReturn = true;
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in WordBankDAO.dbLoadEmpAnswer(conn) => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in WordBankDAO.dbLoadEmpAnswer(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "WordBankDAO.dbLoadEmpAnswer(conn) => " + toReturn);
        return toReturn;
    }
   public boolean dbDeleteVO(Connection conn)
   {    
      Secretary.startFxn("exam", "WordBankDAO.dbDeleteVO(conn)");
      String dbDelete = "DELETE FROM ses_WORD_BANK "
            + "WHERE insert_time = " + examEntryVO.getInsertTime();
      String dbDeleteQuestion = "DELETE FROM ses_WORD_BANK_QUESTION "
            + "WHERE insert_time = " + examEntryVO.getInsertTime();
      String dbDeleteChoice = "DELETE FROM ses_WORD_BANK_CHOICE "
            + "WHERE insert_time = " + examEntryVO.getInsertTime();
      boolean toReturn = false;
      Statement stmt = null;
      try{
         stmt = conn.createStatement();
			stmt.executeUpdate(dbDeleteChoice);
			stmt.executeUpdate(dbDeleteQuestion);
         stmt.executeUpdate(dbDelete);
         toReturn = true;
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in WordBankDAO.dbDeleteVO(conn) => " + e.getMessage());
      }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in WordBankDAO.dbDeleteVO(conn) => " + e.getMessage());
            }
        }
      Secretary.endFxn("exam", "WordBankDAO.dbDeleteVO(conn) => " + toReturn);
      return toReturn;
   }
   
   public boolean dbDeleteFullExam(Connection conn, int eNum)
   {
        Secretary.startFxn("exam", "WordBankDAO.dbDeleteFullExam(conn, "+eNum+")");
        String delDescr = "DELETE FROM ses_WORD_BANK WHERE insert_time IN "
            + "(SELECT insert_time FROM ses_EXAM_ENTRY WHERE exam_num = " + eNum
            + " AND entry_type_code = '" + this.examEntryVO.getEntryTypeCode() + "')";
        String delQuestion = "DELETE FROM ses_WORD_BANK_QUESTION WHERE insert_time IN "
            + "(SELECT insert_time FROM ses_EXAM_ENTRY WHERE exam_num = " + eNum
            + " AND entry_type_code = '" + this.examEntryVO.getEntryTypeCode() + "')";
        String delChoice = "DELETE FROM ses_WORD_BANK_CHOICE WHERE insert_time IN "
            + "(SELECT insert_time FROM ses_EXAM_ENTRY WHERE exam_num = " + eNum
            + " AND entry_type_code = '" + this.examEntryVO.getEntryTypeCode() + "')";
        Secretary.write("exam", delQuestion);
        Secretary.write("exam", delChoice);
        Secretary.write("exam", delDescr);
        /*      
      String dbDelete = "DELETE FROM ses_WORD_BANK WHERE exam_num = " + eNum;
      String dbDeleteQuestion = "DELETE FROM ses_WORD_BANK_QUESTION WHERE exam_num = " + eNum;
      String dbDeleteChoice = "DELETE FROM ses_WORD_BANK_CHOICE WHERE exam_num = " + eNum;
         */
      boolean toReturn = false;
      Statement stmt = null;
      try{
         stmt = conn.createStatement();
         stmt.executeUpdate(delQuestion);
         stmt.executeUpdate(delChoice);
         stmt.executeUpdate(delDescr);
         toReturn = true;
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in WordBankDAO.dbDeleteFullExam(conn, "+eNum+") => " + e.getMessage());
      }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in WordBankDAO.dbDeleteFullExam(conn, "+eNum+") => " + e.getMessage());
            }
        }
      Secretary.endFxn("exam", "WordBankDAO.dbDeleteFullExam(conn, "+eNum+")");
      // TODO: implement
      return toReturn;
   }
   
   public boolean dbInsertTakenVO(Connection conn)
   {
      Secretary.startFxn("exam", "WordBankDAO.dbInsertTakenVO(conn)");
      boolean toReturn = false;
      try{
         dbInsertTakenStmt = conn.prepareStatement(dbInsertTakenStr);
         dbInsertTakenStmt.setInt(1, examEntryVO.getTakeNum());
        //dbInsertTakenStmt.setInt(2, examEntryVO.getExamLoc());
        dbInsertTakenStmt.setLong(2, examEntryVO.getInsertTime());
         Character empAnswer, correctAnswer;
         for(int x=0; x < ((WordBankVO)examEntryVO).getEmpAnswersSize(); x++){
             empAnswer = ((WordBankVO)examEntryVO).getEmpAnswerAt(x);
             correctAnswer = ((WordBankVO)examEntryVO).getSolutionAt(x);
             dbInsertTakenStmt.setInt(3, x);
             dbInsertTakenStmt.setString(4, empAnswer.toString());
             if(empAnswer.compareTo(correctAnswer) == 0)
             {
                int y = ((exam.vos.QuestionVO)examEntryVO).getPointValuePerAnswer();
                dbInsertTakenStmt.setInt(5, 1);
                dbInsertTakenStmt.setInt(6, y);         
             }
             else
             {
                dbInsertTakenStmt.setInt(5, -1);
                dbInsertTakenStmt.setInt(6, 0);
             }
             dbInsertTakenStmt.setString(7, "");
             dbInsertTakenStmt.executeUpdate();
         }
         toReturn = true;
      }catch(SQLException e){
         Secretary.write("exam", "+=+=+= SQLException in WordBankDAO.dbInsertTakenVO(conn) => " + e.getMessage());
      }finally{
        try{
            dbInsertTakenStmt.close();
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in WordBankDAO.dbInsertTakenVO(conn) => " + e.getMessage());
        }
      }
      Secretary.endFxn("exam", "WordBankDAO.dbInsertTakenVO(conn)");
      return toReturn;
   }
   
   public boolean dbUpdatePointsEarned(Connection conn) 
   {
        Secretary.startFxn("exam", "WordBankDAO.dbUpdatePointsEarned(conn)");
        boolean toReturn = false;
        try{
            dbUpdatePointsStmt = conn.prepareStatement(dbUpdatePointsStr);
            int points;
            boolean ec;
            for(int x=0; x < ((WordBankVO)examEntryVO).getEmpAnswersSize(); x++){
                ec = ((WordBankVO)examEntryVO).getEmpCorrectAt(x);
                if(ec)
                {
                    dbUpdatePointsStmt.setInt(1, 1);
                    dbUpdatePointsStmt.setInt(2, ((WordBankVO)examEntryVO).getPointValuePerAnswer());
                }
                else
                {
                    dbUpdatePointsStmt.setInt(1, -1);
                    dbUpdatePointsStmt.setInt(2, 0);
                }
                dbUpdatePointsStmt.setString(3, ((WordBankVO)(this.examEntryVO)).getGradersComment());
                dbUpdatePointsStmt.setInt(4, this.examEntryVO.getTakeNum());
                dbUpdatePointsStmt.setLong(5, this.examEntryVO.getInsertTime());
                dbUpdatePointsStmt.setInt(6, x);
                dbUpdatePointsStmt.executeUpdate();
                toReturn = true;
            }
        }catch(SQLException e){
            Secretary.write("exam", "+=+=+= SQLException in WordBankDAO.dbUpdatePointsEarned(conn) => " + e.getMessage());
        }catch(Exception e){
            Secretary.write("exam", "+=+=+= Exception in WordBankDAO.dbUpdatePointsEarned(conn) => "  + e.getMessage());
        }finally{
            try{
                dbUpdatePointsStmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in WordBankDAO.dbUpdatePointsEarned(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "WordBankDAO.dbUpdatePointsEarned(conn) => " + toReturn);
        return toReturn;  
   }
   /*
    public void dbIncrementExamLocVO(Connection conn)
    {
        Secretary.startFxn("exam", "WordBankDAO.dbIncrementExamLocVO(conn)");
        String increment = "UPDATE ses_WORD_BANK"
                        + " SET"
                        + " exam_loc = " + examEntryVO.getExamLoc()
                        + " WHERE exam_num = " + examEntryVO.getExamNum()
                        + " AND exam_loc = " + (examEntryVO.getExamLoc() - 1);
        String incrementQ = "UPDATE ses_WORD_BANK_QUESTION"
                        + " SET"
                        + " exam_loc = " + examEntryVO.getExamLoc()
                        + " WHERE exam_num = " + examEntryVO.getExamNum()
                        + " AND exam_loc = " + (examEntryVO.getExamLoc() - 1);
        String incrementS = "UPDATE ses_WORD_BANK_CHOICE"
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
            Secretary.write("exam", "+=+=+= Exception in WordBankDAO.dbIncrementExamLocVO(conn) => " + e.getMessage());
        }finally{
            try{
                stmt.close();
            }catch(SQLException e){
                Secretary.write("exam", "+=+=+= SQLException in WordBankDAO.dbIncrementExamLocVO(conn) => " + e.getMessage());
            }
        }
        Secretary.endFxn("exam", "WordBankDAO.dbIncrementExamLocVO(conn)");
    }
   */
   public void dropVO() {
      examEntryVO = null;
   }
   
   public exam.vos.ExamEntryVO getVO() {
      return examEntryVO;
   }
   
   public void setVO(exam.vos.ExamEntryVO vo) {
      examEntryVO  = (WordBankVO)vo;
   }
   
   public WordBankDAO()
   {
      super();
   }

}