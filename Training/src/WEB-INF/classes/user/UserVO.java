/***********************************************************************
 * Module:  UserVO.java
 * Author:  jsandlin
 * Purpose: Defines the Class UserVO
 ***********************************************************************/
package user;
import java.util.*;
import logging.Secretary;

public class UserVO implements java.io.Serializable
{
   public static String SESSION = "userVOSession";
   private String password = "";
   private int roleNum = 0;
   private int empNum = -100;
   private String ssn;
   private String roleName = "invalid";
   private String firstName = "";
   private String lastName = "";
   //private java.util.Date creationDate;
   //private java.util.Date lastLoginDate;
   private Secretary log;
   private java.lang.Boolean active;
   
   public java.lang.String getFullName()
   {
      return this.firstName + " " + this.lastName;
   }
   
   public int getEmpNum()
   {
      return this.empNum;
   }
   
   public String getRoleName()
   {
      return roleName;
   }
   
   public String getFirstName()
   {
      return firstName;
   }
   
   /** @param newFirstName */
   public void setFirstName(String newFirstName)
   {
      firstName = newFirstName;
   }
   /*
   public java.util.Date getLastLoginDate()
   {
      return lastLoginDate;
   }
   
   public void setLastLoginDate(java.util.Date newLastLoginDate)
   {
      lastLoginDate = newLastLoginDate;
   }
   */
   public String getLastName()
   {
      return lastName;
   }
   
   /** @param newLastName */
   public void setLastName(String newLastName)
   {
      lastName = newLastName;
   }
   
   public java.lang.Boolean getActive()
   {
      return active;
   }
   
   /** @param newActive */
   public void setActive(java.lang.Boolean newActive)
   {
      active = newActive;
   }
   
   public UserVO()
   {
      // TODO: implement
   }
   
   /** @param newPassword */
   public void setPassword(String newPassword)
   {
      password = newPassword;
   }
   
   /** @param newRoleNum */
   public void setRoleNum(int newRoleNum)
   {
      roleNum = newRoleNum;
   }
   
   /** @param newRoleName */
   public void setRoleName(String newRoleName)
   {
      roleName = newRoleName;
   }
   /*
   public void setCreationDate(java.util.Date newCreationDate)
   {
      creationDate = newCreationDate;
   }
   */
   /** @param newLog */
   public void setLog(Secretary newLog)
   {
      log = newLog;
   }
   
   /** @param newEmpNum */
   public void setEmpNum(int newEmpNum)
   {
      empNum = newEmpNum;
   }
   
   public int getRoleNum()
   {
      return roleNum;
   }
   public String getSSN()
   {
      return this.ssn;
   }
   public void setSSN(String temp)
   {
      this.ssn = temp;
   }
   public void displayAllInfo(){
       log.write("exam", "name = " + firstName + " " + lastName);
       log.write("exam", "password = " + password);
       log.write("exam", "empNum = " + empNum);
       log.write("exam", "roleName = " + roleName);
       log.write("exam", "roleNum = " + roleNum);
       //log.write("exam", "creationDate = " + creationDate);
       //log.write("exam", "lastLoginDate = " + lastLoginDate);
       log.write("exam", "active = " + active);
   }

    private String psCourseId = "";

    private String psSessionNbr = "";

    public String getPsCourseId() {
        return psCourseId;
    }

    public void setPsCourseId(String psCourseId) {
        this.psCourseId = psCourseId;
    }

    public String getPsSessionNbr() {
        return psSessionNbr;
    }

    public void setPsSessionNbr(String psSessionNbr) {
        this.psSessionNbr = psSessionNbr;
    }
}