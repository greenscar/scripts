package beans;
import logging.*;
import daos.*;
import java.beans.*;

public class UserBean extends Object implements java.io.Serializable {
    //*********************** STATIC VARS ************************//
   // public final static String SESSION = "user";
    //********************* END STATIC VARS **********************//
    
    //********************** PROPERTY VARS ***********************//
    protected Secretary log;
    protected UserDAO userDAO;
    //******************** END PROPERTY VARS *********************//
    
    //*********************** CONSTRUCTORS ***********************//
    public UserBean() {
        log.write("UserBean() Constructor");
        userDAO = new UserDAO();
        log = new Secretary();
        log.write("END UserBean() Constructor");
    }
    public UserBean(UserDAO ud){
        log.write("UserBean(ud) Constructor");
        this.userDAO = ud;
        log = new Secretary();
        log.write("END UserBean(ud) Constructor");
    }
    //********************* END CONSTRUCTORS *********************//
    
    //******************** GET & SET FUNCTIONS *******************//
    public String getFirstName(){
        return userDAO.getFirstName();
    }
    public String getLastName(){
        return userDAO.getLastName();
    }
    public String getFullName(){
        return userDAO.getFirstName() + " " + userDAO.getLastName();
    }
    public int getSSN(){
        return userDAO.getSSN();
    }
    public int getEmpNum(){
        return userDAO.getEmpNum();
    }
    public int getRoleNum(){
        return userDAO.getRoleNum();
    }
    public String getRoleName(){
        return userDAO.getRoleName();
    }
    public UserDAO getDAO(){
        return this.userDAO;
    }
    // setXXXX
    public void setFirstName(String fn){
        userDAO.setFirstName(fn);
    }
    public void setLastName(String ln){
        userDAO.setLastName(ln);
    }
    public void setSSN(int ssn){
        userDAO.setSSN(ssn);
    }
    public void setEmpNum(int en){
        userDAO.setEmpNum(en);
    }
    public void setRoleViaNum(int x){
        userDAO.setRoleViaNum(x);
    }
    public void setRoleViaName(String n){
        userDAO.setRoleViaName(n);
    }
    public void setDAO(UserDAO ud){
        log.write("UserBean.setDAO(ud)");
        this.userDAO = ud;
    }
    //****************** END GET & SET FUNCTIONS *****************//
    
    //********************* PROCESS FUNCTIONS ********************//
    public void updateLastLogin(){
        userDAO.updateLastLogin();
    }
    //******************** END PROCESS FUNCTIONS ******************//
    //********************** HTML DISPLAY FXNS ********************//
    //******************* END HTML DISPLAY FXNS *******************//
    //************************* DB METHODS ***********************//
    //*********************** END DB METHODS *********************//
}
