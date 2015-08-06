<HTML> 
<HEAD> 
	<!-- Example2 -->
	<TITLE> JSP loop</TITLE> 
</HEAD> 
<BODY> 
	<font face=verdana color=darkblue>
		JSP loop
		<BR>
		<BR> 
		<%!
			public String writeThis(int x){
				String myText="";
				for (int i = 1; i < x; i++)
					myText = myText + "<font size=" + i + " color=darkred face=verdana>VisualBuilder JSP Tutorial</font><br>" ;
				return myText;
			}
		%> 
		This is a loop example from the 
		<br>
		<%= writeThis(8) %> 
	</font>
</BODY> 
</HTML>
