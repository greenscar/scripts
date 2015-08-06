package vos;
import logging.Secretary;

import java.beans.*;

public class UserVO extends Object implements java.io.Serializable {
    protected String firstName;
    protected String lastName;
    protected int SSN;
    protected int empNum;
    protected int secLvl;
    protected Secretary log;
    
    public UserVO() {
        log = new Secretary();
    }
    public UserVO(int en){
        this.empNum = en;
    }
    public UserVO(String fn, String ln){
        this.firstName = fn;
        this.lastName = ln;
    }
    // getXXXX
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
    public int getSecLvl(){
        return this.secLvl;
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
    public void setSecLvl(int sl){
        this.secLvl = sl;
    }
}
