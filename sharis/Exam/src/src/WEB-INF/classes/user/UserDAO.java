/***********************************************************************
 * Module:  UserDAO.java
 * Author:  jsandlin
 * Purpose: Defines the Class UserDAO
 ***********************************************************************/
package user;
import java.util.*;
import java.sql.*;
import tools.ConvertTool;
public class UserDAO
{
   private UserVO userVO;
   private Vector userVOList;
   private int roleNum;
   private String roleName;
   /*
   * METHOD NAME : dbLoadVOViaLogin
   * ARGUMENTS :  The PS Database Connection
   *              The ID entered by the user.
   *              The Password entered by the user
   * REQUIRE: n/a
   * FUNCTION : 1) Check Peoplesoft for this Payroll ID.
   *            2) Ensure pwd = the last 4 digits of the SSN
   *            3) If the person is found in the PS_TRN_INSTRCT_TBL, he is an instructor.
   *            4) If the person is found in the PS_TRAINING table he is a student.
   *            5) If the person is a student, ensure his status is ACTIVE.
   *            6) Create the proper UserBean based on which table the person is found in.
   * RETURN:  TRUE if person found.
   *          FALSE if person not found.
   */
   public boolean dbLoadVOViaLogin(java.sql.Connection connPS, int id, String pwd)
   {
      logging.Secretary.startFxn("exam", "UserDAO.dbLoadVOViaLogin("+connPS+", "+id+", "+pwd+")");
      boolean userFound = false;
      Statement stmt = null;
      /*
       * CHECK THE ENROLLMENT TABLE TO SEE IF THE PERSON IS A STUDENT.
       */
      String checkStudents = "select T.EMPLID, T.ATTENDANCE, T.COURSE, T.SESSION_NBR, " 
          + "NID.NATIONAL_ID, E.FIRST_NAME, E.LAST_NAME "
          + "from PS_TRAINING T, PS_PERS_NID NID, PS_NAMES E "
          + "WHERE T.EMPLID = NID.EMPLID "
          + "AND E.EMPLID = NID.EMPLID "
          + " AND E.EFFDT = (SELECT MAX(EFFDT) FROM PS_NAMES PS2 WHERE E.EMPLID = PS2.EMPLID)"
          + " AND T.EMPLID = " + id;
      /*
       * This part slows the query WAY down. Therefore, check these values here in Java,
       * rather than as part of the query.
       *
         + "AND T.ATTENDANCE = 'A' "
         + "OR T.ATTENDANCE = 'E' "
         + " AND SUBSTRING(NID.NATIONAL_ID, 6, 4) = '" + pwd + "'";
       *
       */
      logging.Secretary.write("exam", checkStudents);
      try
      {
         stmt = connPS.createStatement();
         ResultSet rs = stmt.executeQuery(checkStudents);
         if(rs.next())
         {
            logging.Secretary.write("exam", "USER IS A STUDENT");
            String attendance = rs.getString("ATTENDANCE");
            String ssn = rs.getString("NATIONAL_ID").trim();
            //logging.Secretary.write("exam", "ssn.substring(6, 4) = " + ssn.substring(5, 9));
            if((attendance.equals("A") || attendance.equals("E"))
               &&
               ((ssn.substring(5, 9)).equals(pwd)))
            {
               this.userVO = new UserVO();
               userVO.setEmpNum(id);
               userVO.setPassword(pwd);
               userVO.setPsCourseId(rs.getString("COURSE").trim());
               userVO.setPsSessionNbr(rs.getString("SESSION_NBR").trim());
               userVO.setFirstName(rs.getString("FIRST_NAME").trim());
               userVO.setLastName(rs.getString("LAST_NAME").trim());
               userVO.setSSN(ssn);
               userVO.setRoleNum(10);
               userVO.setRoleName("Student");
               logging.Secretary.write("exam", "Login for " + userVO.getFirstName() + " " + userVO.getLastName() + " Successful");
               userFound = true;
            }
         }
      }catch(SQLException e)
      {
         logging.Secretary.write("exam", "+=+=+= SQLException in UserDAO.dbLoadVOViaLogin(connPS, "+id+", "+pwd+") => " + e.toString());
      }finally{
          try{
            stmt.close();
          }catch(SQLException e){
            logging.Secretary.write("exam", "+=+=+= SQLException in UserDAO.dbLoadVOViaLogin(connPS, "+id+", "+pwd+") => " + e.toString());
          }
      }
      if(userFound)
      {
        logging.Secretary.endFxn("exam", "UserDAO.dbLoadVOViaLogin(connPS, "+id+", "+pwd+") ");
        return userFound;
      }
      /*
       * THE USER IS NOT A STUDENT.
       */
     /*
      * CHECK THE INSTRUCTOR TABLE TO SEE IF THE PERSON IS AN INSTRUCTOR
      */
     String checkProfs = "select IT.INSTRUCTOR_ID, NID.NATIONAL_ID, E.FIRST_NAME, E.LAST_NAME "
         + "from PS_TRN_INSTRCT_TBL IT, PS_PERS_NID NID, PS_NAMES E "
         + "WHERE IT.INSTRUCTOR_ID = NID.EMPLID "
         + "AND E.EMPLID = NID.EMPLID "
         + "AND IT.INSTRUCTOR_ID = " + id
         + " AND E.EFFDT = (SELECT MAX(EFFDT) FROM PS_NAMES PS2 WHERE E.EMPLID = PS2.EMPLID)"
         + " AND SUBSTRING(NID.NATIONAL_ID, 6, 4) = '" + pwd + "'";
     logging.Secretary.write("exam", checkProfs);
     try
     {
        stmt = connPS.createStatement();
        ResultSet rs = stmt.executeQuery(checkProfs);
        if(rs.next())
        {
            logging.Secretary.write("exam", "USER IS AN INSTRUCTOR");
           this.userVO = new UserVO();
           userVO.setEmpNum(id);
           userVO.setPassword(pwd);
           userVO.setFirstName(rs.getString("FIRST_NAME").trim());
           userVO.setLastName(rs.getString("LAST_NAME").trim());
           userVO.setSSN(rs.getString("NATIONAL_ID").trim());
           userVO.setRoleNum(100);
           userVO.setRoleName("Admin");
           logging.Secretary.write("exam", "Login for " + userVO.getFirstName() + " " + userVO.getLastName() + " Successful");
           userFound = true;
        }
     }catch(SQLException e)
     {
        logging.Secretary.write("exam", "+=+=+= SQLException in UserDAO.dbLoadVOViaLogin(connPS, "+id+", "+pwd+") => " + e.toString());
     }finally{
         try{
           stmt.close();
         }catch(SQLException e){
           logging.Secretary.write("exam", "+=+=+= SQLException in UserDAO.dbLoadVOViaLogin(connPS, "+id+", "+pwd+") => " + e.toString());
         }
     }
     if(userFound)
     {
         logging.Secretary.endFxn("exam", "UserDAO.dbLoadVOViaLogin(connPS, "+id+", "+pwd+")");
         return userFound;
     }
      if(id == 129655 && pwd.equals("1406"))
      {
         /*
          * IF LOGIN ID => 129655, THIS IS THE TEST USER ATTEMPTING TO LOG IN.
          *    WE HAD TO USE AN EXISTING EMPLOYEE'S ID DUE TO PEOPLESOFT RESTRICTIONS,
          *    SO WE ARE USING THE EMPLID OF A TERMINTATED EMPLOYEE AND NOT CHECKING IF THEY
          *    ARE REALLY A STUDENT... JUST CREATING THEM AS A STUDENT.
          */
         String checkTestStudent = "select NID.NATIONAL_ID, E.FIRST_NAME, E.LAST_NAME "
                  + "from PS_PERS_NID NID, PS_NAMES E "
                  + "WHERE E.EMPLID = NID.EMPLID AND E.EMPLID = " + id;
         logging.Secretary.write("exam", checkTestStudent);
         try
         {
            stmt = connPS.createStatement();
            ResultSet rs = stmt.executeQuery(checkTestStudent);
            if(rs.next())
            {
                logging.Secretary.write("exam", "USER IS TEST USER");
               String ssn = rs.getString("NATIONAL_ID").trim();
               //logging.Secretary.write("exam", "ssn.substring(6, 4) = " + ssn.substring(5, 9));
               this.userVO = new UserVO();
               userVO.setEmpNum(id);
               userVO.setPassword(pwd);
               userVO.setFirstName(rs.getString("FIRST_NAME").trim());
               userVO.setLastName(rs.getString("LAST_NAME").trim());
               userVO.setSSN(ssn);
               userVO.setRoleNum(10);
               userVO.setRoleName("Student");
               logging.Secretary.write("exam", "Login for " + userVO.getFirstName() + " " + userVO.getLastName() + " Successful");
               userFound = true;
            }
         }catch(SQLException e)
         {
            logging.Secretary.write("exam", "+=+=+= SQLException in UserDAO.dbLoadVOViaLogin(connPS, "+id+", "+pwd+") => " + e.toString());
         }finally{
             try{
               stmt.close();
             }catch(SQLException e){
               logging.Secretary.write("exam", "+=+=+= SQLException in UserDAO.dbLoadVOViaLogin(connPS, "+id+", "+pwd+") => " + e.toString());
             }
         }
      }
     if(userFound)
     {
         logging.Secretary.endFxn("exam", "UserDAO.dbLoadVOViaLogin(connPS, "+id+", "+pwd+")");
         return userFound;
     }
     
     /*
      * THIS PORTION IS TEMPORARY.
      * SUPERVISORS AND ABOVE IN VARIOUS LOCATIONS ARE TAKING THE SERVSAFE QUIZES.
      * THESE USERS ARE NOT IN THE ENROLLMENT SYSTEM AND WE DO NOT WISH TO RECORD THEIR GRADES.
      * HOWEVER, THEY NEED TO BE ABLE TO LOG IN TO TAKE THESE QUIZES. 
      * THEREFORE, CHECK PS TO SEE IF THE PERSON HAS A SECURITY LVL > 199
      */
     String checkEmp = "select N.EMPLID, NID.NATIONAL_ID, N.FIRST_NAME, N.LAST_NAME "
                     + "from PS_PERS_NID NID, PS_NAMES N, PS_JOB J "
                     + "WHERE N.EMPLID = NID.EMPLID "
                     + "AND N.EMPLID = " + id + " "
                     + "AND N.EMPLID = J.EMPLID "
                     + "AND N.NAME_TYPE = 'PRI' "
                     + "AND N.EFFDT = (SELECT MAX(EFFDT) FROM PS_NAMES PS2 WHERE N.EMPLID = PS2.EMPLID) "
                     + "AND J.EFFDT = (SELECT MAX(EFFDT) FROM PS_JOB J4 WHERE J4.EMPLID = J.EMPLID) "
                     + "AND J.EFFSEQ IN (SELECT MAX(EFFSEQ) FROM PS_JOB J2 WHERE J2.EMPLID = N.EMPLID AND J2.EFFDT = J.EFFDT) "
                     + "AND (JOBCODE < 100 OR JOBCODE = 200) "
                     + "AND SUBSTRING(NID.NATIONAL_ID, 6, 4) = '" + pwd + "'";
     logging.Secretary.write("exam", checkEmp);
     try
     {
        stmt = connPS.createStatement();
        ResultSet rs = stmt.executeQuery(checkEmp);
        if(rs.next())
        {
            logging.Secretary.write("exam", "USER IS A SUPERVISOR NOT STUDENT");
           this.userVO = new UserVO();
           userVO.setEmpNum(id);
           userVO.setPassword(pwd);
           userVO.setFirstName(rs.getString("FIRST_NAME").trim());
           userVO.setLastName(rs.getString("LAST_NAME").trim());
           userVO.setSSN(rs.getString("NATIONAL_ID").trim());
           userVO.setRoleNum(10);
           userVO.setRoleName("Student");
           logging.Secretary.write("exam", "Login for " + userVO.getFirstName() + " " + userVO.getLastName() + " Successful");
           userFound = true;
        }
     }catch(SQLException e)
     {
        logging.Secretary.write("exam", "+=+=+= SQLException in UserDAO.dbLoadVOViaLogin(connPS, "+id+", "+pwd+") => " + e.toString());
     }finally{
         try{
           stmt.close();
         }catch(SQLException e){
           logging.Secretary.write("exam", "+=+=+= SQLException in UserDAO.dbLoadVOViaLogin(connPS, "+id+", "+pwd+") => " + e.toString());
         }
     }
      logging.Secretary.endFxn("exam", "UserDAO.dbLoadVOViaLogin(connPS, "+id+", "+pwd+")");
      return userFound;
   }
   
   public boolean dbLoadVOViaEmpNum(java.sql.Connection connPS, int empNum)
   {
      logging.Secretary.startFxn("exam", "UserDAO.dbLoadVOViaEmpNum(connPS, "+empNum+")");
      boolean userFound = false;
      Statement stmt = null;
      
      // CHECK THE ENROLLMENT TABLE TO SEE IF THE PERSON IS A STUDENT.
      /*
       * SELECT FIRST_NAME, LAST_NAME FROM PS_NAMES PS1 WHERE PS1.EMPLID = 135489
       * AND PS1.EFFDT = (SELECT MAX(EFFDT) FROM PS_NAMES PS2 WHERE PS1.EMPLID = PS2.EMPLID)
       */
      String check = "SELECT FIRST_NAME, LAST_NAME FROM PS_NAMES PS1 WHERE PS1.EMPLID = " 
            + empNum 
            + " AND PS1.EFFDT = (SELECT MAX(EFFDT) FROM PS_NAMES PS2 WHERE PS1.EMPLID = PS2.EMPLID)";
      System.out.println(check);
      try
      {
         stmt = connPS.createStatement();
         ResultSet rs = stmt.executeQuery(check);
         if(rs.next())
         {
            this.userVO = new UserVO();
            userVO.setEmpNum(empNum);
            userVO.setFirstName(rs.getString("FIRST_NAME").trim());
            userVO.setLastName(rs.getString("LAST_NAME").trim());
            //userVO.setRoleNum(10);
            //userVO.setRoleName("Student");
            logging.Secretary.write("exam", "Student Load for " + userVO.getFirstName() + " " + userVO.getLastName() + " Successful");
            userFound = true;
         }
      }catch(SQLException e)
      {
         logging.Secretary.write("exam", "+=+=+= SQLException in UserDAO.dbLoadVOViaEmpNum(connPS, "+empNum+") => " + e.toString());
      }finally{
          try{
            stmt.close();
          }catch(SQLException e){
            logging.Secretary.write("exam", "+=+=+= SQLException in UserDAO.dbLoadVOViaEmpNum(connPS, "+empNum+") => " + e.toString());
          }
      }
      /*
      // CHECK THE ENROLLMENT TABLE TO SEE IF THE PERSON IS A STUDENT.
      String checkStudents = "select T.EMPLID, NID.NATIONAL_ID, E.FIRST_NAME, E.LAST_NAME "
          + "from PS_TRAINING T, PS_PERS_NID NID, PS_EMPLOYEES E "
          + "where T.ATTENDANCE = 'A' "
          + "AND T.EMPLID = NID.EMPLID "
          + "AND E.EMPLID = NID.EMPLID "
          + "AND T.EMPLID = " + empNum;
      System.out.println(checkStudents);
      try
      {
         stmt = connPS.createStatement();
         ResultSet rs = stmt.executeQuery(checkStudents);
         if(rs.next())
         {
            this.userVO = new UserVO();
            userVO.setEmpNum(empNum);
            userVO.setFirstName(rs.getString("FIRST_NAME"));
            userVO.setLastName(rs.getString("LAST_NAME"));
            userVO.setSSN(rs.getString("NATIONAL_ID"));
            userVO.setRoleNum(10);
            userVO.setRoleName("Student");
            logging.Secretary.write("exam", "Student Load for " + userVO.getFirstName() + " " + userVO.getLastName() + " Successful");
            userFound = true;
         }
      }catch(SQLException e)
      {
         logging.Secretary.write("exam", "+=+=+= SQLException in UserDAO.dbLoadVOViaEmpNum(connPS, "+empNum+") => " + e.toString());
      }finally{
          try{
            stmt.close();
          }catch(SQLException e){
            logging.Secretary.write("exam", "+=+=+= SQLException in UserDAO.dbLoadVOViaEmpNum(connPS, "+empNum+") => " + e.toString());
          }
      }
      // CHECK THE INSTRUCTOR TABLE TO SEE IF THE PERSON IS AN INSTRUCTOR
      String checkProfs = "select IT.INSTRUCTOR_ID, NID.NATIONAL_ID, E.FIRST_NAME, E.LAST_NAME "
          + "from PS_TRN_INSTRCT_TBL IT, PS_PERS_NID NID, PS_EMPLOYEES E "
          + "WHERE IT.INSTRUCTOR_ID = NID.EMPLID "
          + "AND E.EMPLID = NID.EMPLID "
          + "AND IT.INSTRUCTOR_ID = " + empNum;
      System.out.println(checkProfs);
      try
      {
         stmt = connPS.createStatement();
         ResultSet rs = stmt.executeQuery(checkStudents);
         if(rs.next())
         {
            this.userVO = new UserVO();
            userVO.setEmpNum(empNum);
            userVO.setFirstName(rs.getString("FIRST_NAME"));
            userVO.setLastName(rs.getString("LAST_NAME"));
            userVO.setSSN(rs.getString("NATIONAL_ID"));
            userVO.setRoleNum(100);
            userVO.setRoleName("Admin");
            logging.Secretary.write("exam", "Administrator Load for " + userVO.getFirstName() + " " + userVO.getLastName() + " Successful");
            userFound = true;
         }
      }catch(SQLException e)
      {
         logging.Secretary.write("exam", "+=+=+= SQLException in UserDAO.dbLoadVOViaEmpNum(connPS, "+empNum+") => " + e.toString());
      }finally{
          try{
            stmt.close();
          }catch(SQLException e){
            logging.Secretary.write("exam", "+=+=+= SQLException in UserDAO.dbLoadVOViaEmpNum(connPS, "+empNum+") => " + e.toString());
          }
      }
      */
      logging.Secretary.endFxn("exam", "UserDAO.dbLoadVOViaEmpNum(connPS, "+empNum+")");
      return userFound;
   }
   
   /*
   public boolean dbLoadVOViaLogin(java.sql.Connection connPS, int id, String pwd)
   {
      // Load user from DB and set 
      // userVO = loaded user.
      // If user returned, userFound = true.
      // Else userFound = false.
      logging.Secretary.startFxn("exam", "UserDAO.dbLoadVOViaLogin(connPS, "+id+", "+pwd+")");
      String dbSelectViaLoginStr;
      //PreparedStatement dbSelectViaLoginStmt = null;
      boolean userFound = false;
      dbSelectViaLoginStr = "SELECT ui.emp_num, ui.name_first, ui.name_last, "
                 + "ui.role_num, ui.creation_date, "
                 + "li.password, li.last_login_date, ur.role_name "
                 + "FROM ses_USER_INFO ui, ses_LOGIN_INFO li, ses_USER_ROLE ur "
                 + "WHERE ui.emp_num = li.emp_num "
                 + "AND ui.role_num = ur.role_num "
                 + "AND ui.active = 1 "
                 + "AND li.emp_num = " + id + " AND li.password = '" + pwd + "'";
      logging.Secretary.write("exam", dbSelectViaLoginStr);
      Statement stmt = null;
      try{
         stmt = connPS.createStatement();
         ResultSet rs = stmt.executeQuery(dbSelectViaLoginStr);
         //dbSelectViaLoginStmt = connPS.prepareStatement(dbSelectViaLoginStr);
         userVO = new UserVO();
         //dbSelectViaLoginStmt.setInt(1, id);
         //dbSelectViaLoginStmt.setString(2, pwd);
         //ResultSet rs = dbSelectViaLoginStmt.executeQuery();
         if(rs.next()){
            userVO.setEmpNum(id);
            userVO.setPassword(pwd);
            userVO.setFirstName(rs.getString("name_first"));
            userVO.setLastName(rs.getString("name_last"));
            userVO.setRoleNum(rs.getInt("role_num"));
            userVO.setCreationDate(ConvertTool.getDateFromFloat(rs.getFloat("creation_date")));
            userVO.setLastLoginDate(ConvertTool.getDateFromFloat(rs.getFloat("last_login_date")));
            userVO.setRoleName(rs.getString("role_name"));
            logging.Secretary.write("exam", "Login for " + userVO.getFirstName() + " " + userVO.getLastName() + " Successful");
            userFound = true;
         }
         //dbSelectViaLoginStmt.close();
         rs.close();
      }catch(SQLException e){
         logging.Secretary.write("exam", "SQLException in UserDAO.dbLoadVOViaLogin() => " + e.getMessage());
      }
      logging.Secretary.endFxn("exam", "UserDAO.dbLoadVOViaLogin(connPS, "+id+", "+pwd+")");
      return userFound;
   }
   
   public boolean dbLoadVOViaEmpNum(java.sql.Connection connPS, int empNum)
   {
      //logging.Secretary.startFxn("exam", "UserDAO.dbLoadVOViaEmpNum(connPS, "+empNum+")");
      String dbSelect = "SELECT ui.emp_num, ui.name_first, ui.name_last, "
              + "ui.role_num, ui.creation_date, "
              + "li.password, li.last_login_date, ur.role_name "
              + "FROM ses_USER_INFO ui, ses_LOGIN_INFO li, ses_USER_ROLE ur "
              + "WHERE ui.emp_num = li.emp_num "
              + "AND ui.role_num = ur.role_num "
              + "AND li.emp_num = " + empNum;
      boolean userFound = false;
      Statement stmt = null;
      try{
         userVO = new UserVO();
         stmt = connPS.createStatement();
         ResultSet rs = stmt.executeQuery(dbSelect);
         rs.next();
         userVO.setEmpNum(empNum);
         userVO.setFirstName(rs.getString("name_first"));
         userVO.setLastName(rs.getString("name_last"));
         userVO.setRoleNum(rs.getInt("role_num"));
         userVO.setCreationDate(ConvertTool.getDateFromFloat(rs.getFloat("creation_date")));
         userVO.setPassword(rs.getString("password"));
         userVO.setLastLoginDate(ConvertTool.getDateFromFloat(rs.getFloat("last_login_date")));
         userVO.setRoleName(rs.getString("role_name"));
         userFound = true;
         rs.close();
      }catch(SQLException e){
         logging.Secretary.write("exam", "SQLException in UserDAO.dbLoadVOViaEmpNum(connPS, "+empNum+") => " + e.getMessage());
      }finally{
          try{
            stmt.close();
          }catch(SQLException e){
            logging.Secretary.write("exam", "SQLException in UserDAO.dbLoadVOViaEmpNum(connPS, "+empNum+") => " + e.getMessage());
          }
      }
      //logging.Secretary.endFxn("exam", "UserDAO.dbLoadVOViaEmpNum(connPS, "+empNum+")");
      return userFound;
   }
   public boolean dbLoadRoleName(java.sql.Connection connPS)
   {
      logging.Secretary.startFxn("exam", "UserDAO.dbLoadRoleName(connPS)");
      String query = "SELECT role_name FROM USER_ROLE WHERE role_num = " + userVO.getRoleNum();
      boolean userFound = false;
      Statement stmt = null;
      try{
         stmt = connPS.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         rs.next();
         this.roleName = rs.getString(1);
         userFound = true;
         stmt.close();
         rs.close();
      }catch(SQLException e){
         logging.Secretary.write("exam", "SQLException in UserDAO.dbLoadRoleName(connPS) => " + e.getMessage());
      }finally{
          try{
            stmt.close();
          }catch(SQLException e){
            logging.Secretary.write("exam", "SQLException in UserDAO.dbLoadRoleName(connPS) => " + e.getMessage());
          }
      }
      logging.Secretary.endFxn("exam", "UserDAO.dbLoadRoleName(connPS)");
      return userFound;
   }
   public boolean dbLoadNewStudentInfo(java.sql.Connection connPS){
      logging.Secretary.startFxn("exam", "UserDAO.dbLoadNewStudentInfo(connPS)");
      String dbSelectRoleStr;
      PreparedStatement dbSelectRoleStmt = null;
      boolean userFound = false;
      String query = "SELECT * FROM EMPLOYEE_UPDATE WHERE emp_num = " + userVO.getEmpNum();
      Statement stmt = null;
      try{
         stmt = connPS.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         rs.next();
         userVO.setFirstName(rs.getString("first_name"));
         userVO.setLastName(rs.getString("last_name"));
         userFound = true;
         stmt.close();
         rs.close();
      }catch(SQLException e){
            logging.Secretary.write("exam", "SQLException in UserDAO.dbLoadNewStudentInfo(connPS) = " + e.getMessage());
      }finally{
          try{
            stmt.close();
          }catch(SQLException e){
            logging.Secretary.write("exam", "SQLException in UserDAO.dbLoadNewStudentInfo(connPS) => " + e.getMessage());
          }
      }
      logging.Secretary.endFxn("exam", "UserDAO.dbLoadNewStudentInfo(connPS)");
      return userFound;
   }
   public void dbUpdateLastLogin(java.sql.Connection connPS)
   {
      logging.Secretary.startFxn("exam", "UserDAO.dbUpdateLastLogin(connPS)");
      String dbUpdateLastLoginStr;
      PreparedStatement dbUpdateLastLoginStmt = null;
      dbUpdateLastLoginStr = "UPDATE ses_LOGIN_INFO SET last_login_date = ? "
                       + "WHERE emp_num = ?";
      boolean userFound = false;
      try{
         dbUpdateLastLoginStmt = connPS.prepareStatement(dbUpdateLastLoginStr);
         dbUpdateLastLoginStmt.setFloat(1, ConvertTool.getNowAsFloat());
         dbUpdateLastLoginStmt.setInt(2, userVO.getEmpNum());
         dbUpdateLastLoginStmt.executeUpdate();
         userFound = true;
      }catch(SQLException e){
         logging.Secretary.write("exam", "SQLException in UserDAO.dbUpdateLastLogin() => " + e.getMessage());
      }finally{
          try{
            dbUpdateLastLoginStmt.close();
          }catch(SQLException e){
            logging.Secretary.write("exam", "SQLException in UserDAO.dbUpdateLastLogin() => " + e.getMessage());
          }
      }
      logging.Secretary.endFxn("exam", "UserDAO.dbUpdateLastLogin(connPS)");
   }
   */
   public UserVO getVO()
   {
      return this.userVO;
   }
   
   public void setVO(UserVO vo)
   {
      this.userVO = vo;
   }
   public UserDAO()
   {
   }
   public UserDAO(Connection connPS, int eNum)
   {
      this.userVO = new UserVO();
      this.userVO.setEmpNum(eNum);
      this.dbLoadVOViaEmpNum(connPS, eNum);
   }
   
}