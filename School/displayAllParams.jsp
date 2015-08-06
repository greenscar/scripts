<%@page contentType="text/html"%>
<html>
<head><title>JSP Page</title></head>
<body>
<%!
    String ne;
    java.util.Enumeration names;
%>
<%
    names = request.getParameterNames();
    while(names.hasMoreElements()){
        ne = (String)names.nextElement();
        
        %>
        <%= ne %> = <%= (String)request.getParameter(ne) %><br>
        <%
     }
%>

</body>
</html>
<%
    logging.Secretary.write("------ END displayAllParams.jsp");
%>