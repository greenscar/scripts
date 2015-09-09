<HTML>
    <HEAD>
        <meta http-equiv="Expires" CONTENT="0">
        <meta http-equiv="Cache-Control" CONTENT="no-cache">
        <meta http-equiv="Pragma" CONTENT="no-cache">
        <script language = "javascript" type="text/javascript">
            <%@ include file="js_misc_fxns.js" %>
            <%if(fxnsFile != ""){%>
            <jsp:include page="<%=fxnsFile %>" flush="true" />
            <%}%>
            //history.forward();
        </script>
        <TITLE><%= title %></TITLE>
        <link rel=stylesheet type="text/css" href="templates.css">
    </HEAD>
    <BODY onload="setFocus('textarea', 'text')">
    <div align="center">
