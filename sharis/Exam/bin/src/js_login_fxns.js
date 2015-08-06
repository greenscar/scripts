function validateLoginForm(loginForm){
        // only allow numbers to be entered
        var checkOK = "0123456789";
        var checkID = loginForm.emp_num.value;
        var checkpwd = loginForm.pwd.value;
        var allValid = true;
        var allNum = "";
        if (checkID.length == 0)
        {
            alert("You must enter your employee number.");
            loginForm.emp_num.focus();
            return (false);
        }    

        if (checkpwd.length == 0)
        {
            alert("You must enter a pwd.");
            loginForm.pwd.focus();
            return (false);
        }    
        for (i = 0;  i < checkID.length;  i++)
        {
            ch = checkID.charAt(i);
            for (j = 0;  j < checkOK.length;  j++)
                    if (ch == checkOK.charAt(j)) break;
            if (j == checkOK.length)
            {
                    allValid = false;
                    break;
            }
            if (ch != ",") allNum += ch;
        }
        if (!allValid)
        {
            alert("Your username should be numbers only (Your payroll ID).");
            loginForm.emp_num.focus();
            return (false);
        }
        for (i = 0;  i < checkpwd.length;  i++)
        {
            ch = checkpwd.charAt(i);
            for (j = 0;  j < checkOK.length;  j++)
                    if (ch == checkOK.charAt(j)) break;
            if (j == checkOK.length)
            {
                    allValid = false;
                    break;
            }
            if (ch != ",") allNum += ch;
        }
        if (!allValid)
        {
            alert("Your pwd should be numbers only.");
            loginForm.pwd.focus();
            return (false);
        }
}