    
    <br>
    <br>
    <a href="menuStudent.jsp">HOME</a>&nbsp&nbsp<a href="/exam/logout">LOGOUT</a>
    </div>
    <script language="JavaScript" type="text/javascript">
    if (document.getElementsByTagName) 
    {
        var inputElements = document.getElementsByTagName("input");
        for (i=0; inputElements[i]; i++) 
        {
            if (inputElements[i].className && (inputElements[i].className.indexOf("disableAutoComplete") != -1)) 
            {
                inputElements[i].setAttribute("autocomplete","off");
            }//if current input element has the disableAutoComplete class set.
        }//loop thru input elements
    }//basic DOM-happiness-check
    </script>
  </body>
</html>