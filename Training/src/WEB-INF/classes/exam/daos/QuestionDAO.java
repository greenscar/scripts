/***********************************************************************
 * Module:  QuestionDAO.java
 * Author:  jsandlin
 * Purpose: Defines the Class QuestionDAO
 ***********************************************************************/
package exam.daos;
import java.util.*;
import java.sql.*;
public abstract class QuestionDAO extends ExamEntryDAO
{
   protected String dbInsertTakenStr;
   protected String dbUpdatePointsStr;
   protected PreparedStatement dbInsertTakenStmt = null;
   protected PreparedStatement dbUpdatePointsStmt = null;
   
   /**************************************************************************
     * dbInsertTakenVO inserts the employee's answers to an entry into the DB
     *************************************************************************/
   public abstract boolean dbInsertTakenVO(Connection conn);
   /**************************************************************
     * dbLoadEmpAnswer will load the answer_entered by the employee
     *      from the DB
     **************************************************************/
   public abstract boolean dbLoadEmpAnswer(Connection conn);
   /**************************************************************************
     * dbUpdatePointsEarned updates the ses_EMP_ANSWER table.
     * It updates correct, points_earned, and comment, which were
     *   entered when the exam was graded.
     *************************************************************************/
   public abstract boolean dbUpdatePointsEarned(Connection conn);
   public QuestionDAO()
   {
      super();
      /*
      dbUpdatePointsStr = "UPDATE ses_EMP_ANSWER set correct = ?, points_earned = ?, "
            + "comment = ? WHERE take_num = ? AND exam_loc = ? AND quest_loc = ?";
      dbInsertTakenStr = "INSERT INTO ses_EMP_ANSWER(take_num, exam_loc, "
             + "quest_loc, answer_entered, correct, points_earned, comment) VALUES "
             + "(?, ?, ?, ?, ?, ?, ?);";
      */
      dbUpdatePointsStr = "UPDATE ses_EMP_ANSWER set correct = ?, points_earned = ?, "
            + "comment = ? WHERE take_num = ? AND insert_time = ? AND quest_loc = ?";
      dbInsertTakenStr = "INSERT INTO ses_EMP_ANSWER(take_num, insert_time, "
             + "quest_loc, answer_entered, correct, points_earned, comment) VALUES "
             + "(?, ?, ?, ?, ?, ?, ?);";
   }

}