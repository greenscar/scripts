<%
logging.Secretary.startFxn("exam", "logout.jsp");
%>
<%@ page import="beans.*" errorPage="error.jsp" %>
<% 
BeanUser user;
user = (BeanUser)session.getAttribute(BeanUser.SESSION);
if(user != null)
   user.logout(request);
%>
<%
logging.Secretary.endFxn("exam", "logout.jsp");
%>
<jsp:forward page="/login" />
