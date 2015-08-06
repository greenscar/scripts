package daos;
import java.beans.*;
import javax.servlet.*;
import java.util.*;
import java.sql.*;
import db.*;
import vos.*;
import beans.*;
import logging.*;
import examParts.*;

public class UserDAO {
    //*********************** STATIC VARS ************************//
    //********************* END STATIC VARS **********************//
    //********************** PROPERTY VARS ***********************//
    //**************** FUNCTIONAL VARS *****************//
    protected String firstName;
    protected String lastName;
    protected int SSN;
    protected int empNum;
    protected String password;
    protected int roleNum;
    protected String roleName;
    protected java.util.Date creationDate;
    protected java.util.Date lastLoginDate;
    //************** END FUNCTIONAL VARS ***************//
    
    //***************** CLASSES VARS *******************//
    protected Secretary log;
    protected UserDAO userDAO;
    protected DBUtil dbUtil;
    //**************** END CLASSES VARS ****************//
    //******************** END PROPERTY VARS *********************//
    
    private String selectAllStmtStr;
    private String selectViaEmpNumStmtStr;
    private String loadViaLoginStr;
    private String loadRoleViaNumStr;
    private String loadRoleViaNameStr;
    private String updateLastLoginStr;
    private String loadViaEmpNumStr;
    // Prepared Statements
    private PreparedStatement selectAllStmt;
    private PreparedStatement selectViaEmpNumStmt;
    private PreparedStatement loadViaLogin;
    private PreparedStatement loadRoleViaNum;
    private PreparedStatement loadRoleViaName;
    private PreparedStatement updateLastLogin;
    private PreparedStatement loadViaEmpNum;
    //******************** END PROPERTY VARS *********************//
    //*********************** CONSTRUCTORS ***********************//
    public UserDAO() {
        this.empNum = 0;
        this.SSN = 0;
        log = new Secretary();
        dbUtil = new DBUtil();
        createPreparedStatements();
    }
    public UserDAO(int empNum){
        this.empNum = empNum;
        this.SSN = 0;
        log = new Secretary();
        dbUtil = new DBUtil();
        createPreparedStatements();
        this.loadViaEmpNum(empNum);
    }
    public UserDAO(int empNum, String pwd){
        this.empNum = empNum;
        this.SSN = 0;
        log = new Secretary();
        dbUtil = new DBUtil();
        createPreparedStatements();
        this.loadViaEmpNumAndPwd(empNum, pwd);
    }
    //********************* END CONSTRUCTORS *********************//
    
    //******************** GET & SET FUNCTIONS *******************//
    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getFullName(){
        log.write("UserBean.getFullName()");
        String t = this.firstName + " " + this.lastName;
        log.write("returning " + t);
        return t;
    }
    public int getSSN(){
        return this.SSN;
    }
    public int getEmpNum(){
        return this.empNum;
    }
    public int getRoleNum(){
        log.write("UserDAO.getRoleNum()");
        return this.roleNum;
    }
    public String getRoleName(){
        log.write("UserDAO.getRoleName()");
        return this.roleName;
    }
    
    // setXXXX
    public void setFirstName(String fn){
        this.firstName = fn;
    }
    public void setLastName(String ln){
        this.lastName = ln;
    }
    public void setSSN(int ssn){
        this.SSN = ssn;
    }
    public void setEmpNum(int en){
        this.empNum = en;
    }
    public void setRoleViaNum(int x){
        this.roleNum = x;
        this.loadSecNameViaNum(x);
    }
    public void setRoleViaName(String n){
        this.roleName = n;
        this.loadSecNumViaName(n);
    }
    public boolean isLoggedIn(){
        log.write("UserDAO.isLoggedIn()");
        if((this.empNum != 0) && (this.SSN != 0))
            return true;
        else return false;
    }
    //****************** END GET & SET FUNCTIONS *****************//
        
    //*************************** DB FXNS *************************//
    public void createPreparedStatements(){
        log.write("UserDAO.createPreparedStatements()");
        try{
            loadViaLoginStr = "SELECT ui.emp_num, ui.name_first, ui.name_last, "
                    + "ui.soc_sec_num, ui.role_num, ui.creation_date, "
                    + "li.password, li.last_login_date, ur.role_name "
                    + "FROM USER_INFO ui, LOGIN_INFO li, USER_ROLE ur "
                    + "WHERE ui.emp_num = li.emp_num "
                    + "AND ui.role_num = ur.role_num "
                    + "AND li.emp_num = ? AND li.password = ?";
            loadViaLogin = dbUtil.createPreparedStatement(loadViaLoginStr);
            
            loadViaEmpNumStr = "SELECT ui.emp_num, ui.name_first, ui.name_last, "
                    + "ui.soc_sec_num, ui.role_num, ui.creation_date, "
                    + "li.password, li.last_login_date, ur.role_name "
                    + "FROM USER_INFO ui, LOGIN_INFO li, USER_ROLE ur "
                    + "WHERE ui.emp_num = li.emp_num "
                    + "AND ui.role_num = ur.role_num "
                    + "AND li.emp_num = ?";
            loadViaEmpNum = dbUtil.createPreparedStatement(loadViaEmpNumStr);
            
            loadRoleViaNumStr = "SELECT role_name FROM USER_ROLE WHERE role_num = ?";
            loadRoleViaNum = dbUtil.createPreparedStatement(loadRoleViaNumStr);
            
            loadRoleViaNameStr = "SELECT role_num FROM USER_ROLE WHERE role_name = ?";
            loadRoleViaName = dbUtil.createPreparedStatement(loadRoleViaNameStr);
            
            updateLastLoginStr = "UPDATE LOGIN_INFO SET last_login_date = ? "
                    + "WHERE emp_num = ? AND password = ?";
            updateLastLogin = dbUtil.createPreparedStatement(updateLastLoginStr);
            
            selectAllStmtStr = "SELECT * FROM USERS";
            selectAllStmt = dbUtil.createPreparedStatement(selectAllStmtStr);
            
            selectViaEmpNumStmtStr = "SELECT * FROM USERS WHERE emp_num = ?";
            selectViaEmpNumStmt = dbUtil.createPreparedStatement(selectViaEmpNumStmtStr);
            
        }catch(Exception e){
            log.write("Exception in UserDAO.createPreparedStatements(): " 
                    + e.toString());
        }
        log.write("UserDAO.createPreparedStatements() Complete");
    }
    public void loadSecNameViaNum(int n){
        log.write("UserDAO.loadSecNameViaNum("+n+")");
        try{
            loadRoleViaNum.setInt(1, n);
            ResultSet rs = loadRoleViaNum.executeQuery();
            rs.next();
            this.roleName = rs.getString(1);
        }catch(SQLException e){
            log.write("SQLException in UserDAO.loadSecNameViaNum("+n+") = " + e.toString());
        }
        log.write("UserDAO.loadSecNameViaNum("+n+") completed.");
    }
    
    public void loadSecNumViaName(String n){
        log.write("UserDAO.loadSecNumViaName("+n+")");
        try{
            loadRoleViaName.setString(1, n);
            ResultSet rs = loadRoleViaName.executeQuery();
            rs.next();
            this.roleName = rs.getString(1);
        }catch(SQLException e){
            log.write("SQLException in UserDAO.loadSecNumViaName("+n+") = " + e.toString());
        }
        log.write("UserDAO.loadSecNumViaName("+n+") completed.");
    }
    public void updateLastLogin(){
        log.write("UserDAO.updateLastLogin()");
        try{
            updateLastLogin.setFloat(1, dbUtil.rightNowAsFloat());
            updateLastLogin.setInt(2, this.empNum);
            updateLastLogin.setString(3, this.password);
            updateLastLogin.executeUpdate();
        }catch(SQLException e){
            log.write("SQLException in UserDAO.updateLastLogin() = " + e.toString());
        }
    }
            
    public void loadViaEmpNumAndPwd(int num, String pwd){
        log.write("UserDAO.loadViaEmpNumAndPwd("+num+", "+pwd+")");
        try{
            loadViaLogin.setInt(1, num);
            loadViaLogin.setString(2, pwd);
            ResultSet rs = loadViaLogin.executeQuery();
            if(rs.next()){
                this.empNum = rs.getInt("emp_num");
                this.firstName = rs.getString("name_first");
                this.lastName = rs.getString("name_last");
                this.SSN = rs.getInt("soc_sec_num");
                this.roleNum = rs.getInt("role_num");
                this.creationDate = dbUtil.getDateFromFloat(rs.getFloat("creation_date"));
                this.password = rs.getString("password");
                this.lastLoginDate = dbUtil.getDateFromFloat(rs.getFloat("last_login_date"));
                this.roleName = rs.getString("role_name");
            }
        }catch(SQLException e){
            log.write("SQLException in UserDAO.loadViaEmpNumAndPwd() = " + e.toString());
        }
    }
    public void loadViaEmpNum(int num){
        log.write("UserDAO.loadViaEmpNum("+num+")");
        try{
            loadViaEmpNum.setInt(1, num);
            ResultSet rs = loadViaEmpNum.executeQuery();
            if(rs.next()){
                this.empNum = rs.getInt("emp_num");
                this.firstName = rs.getString("name_first");
                this.lastName = rs.getString("name_last");
                this.SSN = rs.getInt("soc_sec_num");
                this.roleNum = rs.getInt("role_num");
                this.creationDate = dbUtil.getDateFromFloat(rs.getFloat("creation_date"));
                this.password = rs.getString("password");
                this.lastLoginDate = dbUtil.getDateFromFloat(rs.getFloat("last_login_date"));
                this.roleName = rs.getString("role_name");
            }
        }catch(SQLException e){
            log.write("SQLException in UserDAO.loadViaEmpNumAndPwd() = " + e.toString());
        }
    }
    //************************* END DB FXNS ***********************//
}
