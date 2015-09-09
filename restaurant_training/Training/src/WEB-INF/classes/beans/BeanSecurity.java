package beans;

import beans.*;

public class BeanSecurity {
    /** Creates a new instance of BeanSecurity */
    public BeanSecurity() {
    }
   /*
    * processLogin processes the entered empID and pwd and loads the 
    *    appropriate UserVO.
    * If the login is successful, the appropriate bean is created and 
    *    assigned to the session.
    */
   public boolean processLogin(java.sql.Connection connPS, javax.servlet.http.HttpServletRequest request)
   {
      user.UserDAO userDAO = new user.UserDAO();
      //logging.Secretary.startFxn("exam", "BeanSecurity.processLogin(request)");
      if((request.getParameter("emp_num") == null) || (request.getParameter("pwd") == null))
      {
         logging.Secretary.endFxn("exam", "BeanSecurity.processLogin(request) => false");
         return false;
      }
      Integer id = new Integer(request.getParameter("emp_num"));
      String pwd = request.getParameter("pwd");
      //Integer pwd = new Integer(request.getParameter("pwd"));
      javax.servlet.http.HttpSession theSession = request.getSession();
      if(userDAO.dbLoadVOViaLogin(connPS, id.intValue(), pwd)){
         //user successfully logged in.
         //userDAO.dbUpdateLastLogin(connPS);
         user.UserVO vo = userDAO.getVO();
         String beanToCreate = "beans.Bean" + vo.getRoleName();
         try{
            Class x = Class.forName(beanToCreate);
            BeanUser user = (BeanUser)x.newInstance();
            user.setUserVO(vo);
            theSession.setAttribute(user.SESSION, user);
            theSession.setMaxInactiveInterval(7200);
         }catch(InstantiationException e){
            logging.Secretary.write("exam", "InstantiationException in BeanSecurity.processLogin => " + e.toString());
         }catch(ClassNotFoundException e){
            logging.Secretary.write("exam", "ClassNotFoundException in BeanSecurity.processLogin => " + e.toString());
         }catch(IllegalAccessException e){
            logging.Secretary.write("exam", "IllegalAccessException in BeanSecurity.processLogin => " + e.toString());
         }
         logging.Secretary.endFxn("exam", "BeanSecurity.processLogin => true");
         return true;
      }   
      else{
         logging.Secretary.endFxn("exam", "BeanSecurity.processLogin => false");
         theSession.setAttribute(user.UserVO.SESSION, null);
         return false;
      }
      
   }
      
   public boolean userRoleNumIsValid(javax.servlet.http.HttpServletRequest request, 
                                javax.servlet.http.HttpSession session,
                                int requiredRoleNum){
      logging.Secretary.startFxn("exam", "BeanSecurity.userRoleNumIsValid(request, session, " + requiredRoleNum + ")");
      if(((BeanUser)(session.getAttribute(BeanUser.SESSION))) == null){
          return false;
      }
      user.UserVO vo = ((BeanUser)(session.getAttribute(BeanUser.SESSION))).getUserVO();
      boolean toReturn = vo.getRoleNum() >= requiredRoleNum;
      logging.Secretary.endFxn("exam", "BeanSecurity.userRoleNumIsValid(request, session, " + requiredRoleNum + ") => " + toReturn);
      return toReturn;
   }
}
