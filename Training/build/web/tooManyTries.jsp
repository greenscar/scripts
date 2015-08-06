<%@page contentType="text/html"%>
<html>
<head><title>Login Failed</title></head>
<body>
<H1 align="center">Login Failed</H1>

<h3>You have failed to enter a correct ID and password 5 times.
Please contact the help desk to get a proper login.</h3>
<%-- <jsp:useBean id="beanInstanceName" scope="session" class="package.class" /> --%>
<%-- <jsp:getProperty name="beanInstanceName"  property="propertyName" /> --%>

</body>
</html>
<%
    logging.Secretary.write("exam", "------ END tooManyTries.jsp");
%>