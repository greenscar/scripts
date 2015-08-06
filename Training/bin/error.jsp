<%@ page isErrorPage="true" %>
<HTML>
<HEAD>
<TITLE> Error Page </TITLE>
</HEAD>
<BODY BGCOLOR=Black TEXT="#FFFFFF" LINK="#FFFFFF" VLINK="#FFFFFF">
<H1>Exception Information:</H1>
<h3>Please leave this screen open & contact the help desk</h3>
<table>
   <tr>
      <td>Exception Class:</td>
      <td><%= exception.getClass() %></td>
   </tr>

   <tr>
      <td>Message:</td>
      <td><%= exception.getMessage() %></td>
   </tr>

   <tr>
      <td>To String:</td>
      <td><%= exception.toString() %></td>
   </tr>
</TABLE>
</BODY>
</HTML>