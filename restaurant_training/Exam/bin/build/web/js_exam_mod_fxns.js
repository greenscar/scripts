
function ensurePointValueEntered(qForm){
    var qType = qForm.entryType.value;
    if((qType == "he") || (qType == "pb") || (document.getElementById("PointValue").value != "")){
        return(true);
    }
    else{
        alert("You must enter a point value for this entry.");
        qForm.PointValue.focus();
        return(false);
    }
}
function confirmDelete(){
   if (confirm("Are you sure you wish to delete this entry?"))
      return true ;
   else
      return false ;
}

function ensureEntryTypeSelected(qForm){
    var answerEntered = false;
    for(i = 0; i < qForm.nextEntryType.length; i++){
        if(form_mod_entry.nextEntryType[i].checked){
            answerEntered = true;
        }
    }
    if(!answerEntered){
        alert("Please select an entry type.");
    }
    return(answerEntered);
        
}
function validateHeaderData(qForm){
    var name = qForm.examName.value;
    if(name == ""){
        alert("You must enter a name for the exam.");
        qForm.examName.focus();
        return(false);
    }
    else if(qForm.examName.length > 30)
    {
        alert("Your exam name must be 30 characters or less");
        qForm.examName.focus();
        return(false);
    }
    else{
        return(true);
    }
}

function validateEntryMod(qForm){
    var qType = qForm.entryType.value;
    var toReturn = false;
    var LETTER = new Array('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T');//,'U','V','W','X','Y','Z');
    switch(qType){
        case "he":
            //Header
            toReturn = validateHEMod(qForm);
            break;
        case "tf":
            //True False
            toReturn = validateTFMod(qForm);
            break;
        case "mc":
            //Multiple Choice
            toReturn = validateMCMod(qForm);
            break;
        case "es":
            //Essay
            toReturn = validateESMod(qForm);
            break;
        case "fb":
            //Fill in the Blank
            toReturn = validateFBMod(qForm);
            break;
        case "ma":
            //Matching
            toReturn = validateMAMod(qForm, LETTER);
            break;
        case "wb":
            //Matching
            toReturn = validateWBMod(qForm, LETTER);
            break;
        case "pb":
            toReturn = true;
            break;
        default:
            toReturn = false;
    }
    return(toReturn && ensurePointValueEntered(qForm));
}
function validateHEMod(qForm){
    if(form_mod_entry.Header.value != ""){
        return(true);
    }
    else{
        alert("You must enter a header.");
        form_mod_entry.Header.focus();
        return(false);
    }
}
function validateTFMod(qForm){
    var question = form_mod_entry.TrueFalseQ.value;
    if(question == ""){
        alert("You must enter a question.");
        form_mod_entry.TrueFalseQ.focus();
        return(false);
    }
    if((form_mod_entry.TrueFalseS[0].checked != true) && (form_mod_entry.TrueFalseS[1].checked != true)){
        alert("You must select a solution.");
        return(false);
    }
    else{
        return(true);
    }
}
function validateESMod(qForm){
    var question = form_mod_entry.EssayQ.value;
    if(question == ""){
        alert("You must enter a question.");
        form_mod_entry.EssayQ.focus();
        return(false);
    }
	else{
		return(true);
	}
}
function validateMCMod(qForm){
    var question = form_mod_entry.MultChoiceQ.value;
    var choice=new Array(form_mod_entry.choiceA.value, form_mod_entry.choiceB.value, form_mod_entry.choiceC.value, form_mod_entry.choiceD.value);
    var noAnswer = true;
    var solution = form_mod_entry.MultChoiceS.value;
    if(question == ""){
        alert("You must enter a question.");
        return(false);
    }
    for(i = 0; i < 4; i++){
        if(form_mod_entry.MultChoiceS[i].checked){
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
function validateFBMod(qForm){
    var question = form_mod_entry.FillInBlankQ.value;
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
function validateMAMod(qForm, LETTER){
    var numEntries = 0;
    var description = form_mod_entry.MatchingDescription.value;
    if(description == ""){
        alert("You must enter a description.");
        return(false);
    }
    var solution, question, choice;
    if(qForm.elements["question_" + LETTER[0]].value == ""){
        alert("You must have at least one question, choice, and solution.");
        return(false);
    }
    if(qForm.elements["choice_" + LETTER[0]].value == ""){
        alert("You must have at least one question, choice, and solution.");
        return(false);
    }
    if(qForm.elements["solution_" + LETTER[0]].value == ""){
        alert("You must have at least one question, choice, and solution.");
        return(false);
    }
    else{
      for(i=0;i<LETTER.length;i++){
        solution = qForm.elements["solution_" + LETTER[i]].value;
        // 1) Ensure each solution is a letter.
        if((solution != "") && (!aLetter(solution))){
			alert("Come on now, your solutions must be letters.");
		    return(false);
        }
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
	return(true);
}
function validateWBMod(qForm, LETTER){
    var numEntries = 0;
    var description = form_mod_entry.WordBankDescription.value;
    if(description == ""){
        alert("You must enter a description.");
        return(false);
    }
    var solution, question, choice;
    if(qForm.elements["question_" + LETTER[0]].value == ""){
        alert("You must have at least one question, choice, and solution.");
        return(false);
    }
    if(qForm.elements["choice_" + LETTER[0]].value == ""){
        alert("You must have at least one question, choice, and solution.");
        return(false);
    }
    if(qForm.elements["solution_" + LETTER[0]].value == ""){
        alert("You must have at least one question, choice, and solution.");
        return(false);
    }
    else{
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
function confirmDelete(){
   if (confirm("Are you sure you wish to delete this entry?"))
      return true ;
   else
      return false ;
}