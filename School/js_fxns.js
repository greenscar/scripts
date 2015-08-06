function validateQuestion(qForm){
    var qType = qForm.qType.value;
    var LETTER = new Array('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
    switch(qType){
        case "he":
            //Header
            return(validateHE(qForm));
            break;
        case "tf":
            //True False
            return(validateTF(qForm));
            break;
        case "mc":
            //Multiple Choice
            return(validateMC(qForm));
            break;
        case "es":
            //Essay
            return(validateES(qForm));
            break;
        case "fb":
            //Fill in the Blank
            return(validateFB(qForm));
            break;
        case "ma":
            //Matching
            return(validateMA(qForm, LETTER));
            break;
        case "wb":
            //Matching
            return(validateWB(qForm, LETTER));
            break;
        default:
            return(false);
    }
}
function validateHE(qForm){
    if(getEntry.aNewHeader.value != ""){
        return(true);
    }
    else{
        alert("You must enter a header.");
        getEntry.aNewHeader.focus();
        return(false);
    }
}
function validateTF(qForm){
    var question = getEntry.newTrueFalseQ.value;
    if(question == ""){
        alert("You must enter a question.");
        getEntry.newTrueFalseQ.focus();
        return(false);
    }
    if((getEntry.newTrueFalseS[0].checked != true) && (getEntry.newTrueFalseS[1].checked != true)){
        alert("You must select a solution.");
        return(false);
    }
    else{
        return(true);
    }
}
function validateES(qForm){
    var question = getEntry.newEssayQ.value;
    if(question == ""){
        alert("You must enter a question.");
        getEntry.newEssayQ.focus();
        return(false);
    }
}
function validateMC(qForm){
    var question = getEntry.newMultChoiceQ.value;
    var choice=new Array(getEntry.choiceA.value, getEntry.choiceB.value, getEntry.choiceC.value, getEntry.choiceD.value);
    var noAnswer = true;
    var solution = getEntry.newMultChoiceS.value;
    if(question == ""){
        alert("You must enter a question.");
        return(false);
    }
    for(i = 0; i < 4; i++){
        if(getEntry.newMultChoiceS[i].checked){
            noAnswer = false;
        }
    }
    if(noAnswer){
        alert("You must select a solution.");
        return(false);
    }
    for(i = 0; i < choice.length; i++){
        if(choice[i] == ""){
            alert("You must enter 4 choices.");
            return(false);
        }
    }
    return(true);
}
function validateFB(qForm){
    var question = getEntry.newFillInBlankQ.value;
    if(question == ""){
        alert("You must enter a question.");
        return(false);
    }
    var elNum=0, el, els = qForm.elements; 
    while (el = els[elNum++]) { 
        if((el.name.match("solution")) && (el.value != ""))
            return(true);
    } 
    alert("You must enter at least one answer.");
    return(false);       
}
function validateMA(qForm, LETTER){
    var numEntries = 0;
    var description = getEntry.newMatchingDescription.value;
    if(description == ""){
        alert("You must enter a description.");
        return(false);
    }
    var solution, question, choice;
    for(i=0;i<LETTER.length;i++){
        solution = qForm.elements["solution_" + LETTER[i]].value;
        // 1) Ensure each solution is a letter.
        if((solution != "") && (!aLetter(solution)))  return(false);
        // 2) Ensure each solution has a corresponding question.
        question = qForm.elements["question_" + LETTER[i]].value;
        if(question == "" && solution != ""){
            alert("There must be a question to each solution.");
            return(false);
        }
        if(question != "" && solution == ""){
            alert("There must be a solution to each question.");
            return(false);
        }
        // 3) Ensure each solution's letter has a choice entry.
        if(question != ""){
            //Check to ensure this choice exists.
            choice = qForm.elements["choice_" + solution.toUpperCase()].value;
            if(choice == ""){
                alert("You have " + solution + " as a solution, but there is not choice for that letter.");
                return(false);
            }
            else numEntries++;
        }
    }
}
function validateWB(qForm, LETTER){
    var numEntries = 0;
    var description = getEntry.newWordBankDescription.value;
    if(description == ""){
        alert("You must enter a description.");
        return(false);
    }
    var solution, question, choice;
    for(i=0;i<LETTER.length;i++){
        solution = qForm.elements["solution_" + LETTER[i]].value;
        // 1) Ensure each solution is a letter.
        if((solution != "") && (!aLetter(solution)))  return(false);
        // 2) Ensure each solution has a corresponding question.
        question = qForm.elements["question_" + LETTER[i]].value;
         if(question == "" && solution != ""){
            alert("There must be a question to each solution.");
            return(false);
        }
        if(question != "" && solution == ""){
            alert("There must be a solution to each question.");
            return(false);
        }
        // 3) Ensure each solution's letter has a choice entry.
        if(question != ""){
            //Check to ensure this choice exists.
            choice = qForm.elements["choice_" + solution.toUpperCase()].value;
            if(choice == ""){
                alert("You have " + solution + " as a solution, but there is not choice for that letter.");
                return(false);
            }
            else numEntries++;
        }
    }
}

function aLetter(box){
    var checkOK = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    var allValid = false;
    box = box.toUpperCase();
    if(box.length > 1){
        alert("Each solution must be a single letter.");
        return(false);
    }
    for(j=0;j<checkOK.length;j++){
        if(box == checkOK.charAt(j)){
            return(true);
        }
    }
    alert("Each solution must be a single letter.");
    return(false);
}
function validateLoginForm(loginForm){
    // only allow numbers to be entered
    var checkOK = "0123456789";
    var checkID = loginForm.emp_num.value;
    var checkPwd = loginForm.pwd.value;
    var allValid = true;
    var allNum = "";
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
    for (i = 0;  i < checkPwd.length;  i++)
    {
        ch = checkPwd.charAt(i);
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
        alert("Your password should be numbers only.");
        loginForm.pwd.focus();
        return (false);
    }
}
function setFocus() { 
    if (document.forms.length > 0) { 
        var el, type, i = 0, j, els = document.forms[0].elements; 
        while (el = els[i++]) { 
            j = 0; 
            while (type = arguments[j++]){
                if (el.type == type){
                    el.focus(); 
                    return(true);
                }
            }
        } 
    } 
} 

 
