package security;
import daos.*;
import beans.*;
import logging.Secretary;
public class Guard {
    private Secretary log;
    /** Creates a new instance of Guard */
    public Guard() {
        log = new Secretary();
    }
    public void processLogin(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response){
        log.write("Guard.processLogin");
        boolean loggedIn = false;
        Integer id = new Integer(request.getParameter("emp_num"));
        String pwd = request.getParameter("pwd");
        //Attempt to create this userDAO
        UserDAO u = new UserDAO(id.intValue(), pwd);
        if(u.isLoggedIn()){
            String userType = u.getRoleName();
            String classToCreate = "beans."+userType + "Bean";
            
            try{
                Class x = Class.forName(classToCreate);
                UserBean ub = (UserBean)x.newInstance();
                log.write("u.getFullName() = " + u.getFullName());
                ub.setDAO(u);
                String sessionName = null;
                if(ub instanceof AdminBean){
                    sessionName = AdminBean.SESSION;
                    ub.updateLastLogin();
                }
                else if(ub instanceof StudentBean){
                    sessionName = StudentBean.SESSION;
                    ub.updateLastLogin();
                }
                else if(ub instanceof GodBean){
                    sessionName = GodBean.SESSION;
                    ub.updateLastLogin();
                }
                javax.servlet.http.HttpSession theSession = request.getSession();
                theSession.setAttribute(sessionName, ub);
            }catch(InstantiationException e){
                log.write("Guard.processLogin threw " + e.toString());
            }catch(ClassNotFoundException e){
                log.write("Guard.processLogin threw " + e.toString());
            }catch(IllegalAccessException e){
                log.write("Guard.processLogin threw " + e.toString());
            }
            //loggedIn = true;
        }
        //return loggedIn;
    }
}
