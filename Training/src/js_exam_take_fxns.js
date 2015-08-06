function checkForErrors(takeForm){
            var toReturn = false;
            var LETTER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            var uScoreLoc = 0;
            //var toAlert = "";
            for(elementNum=0; elementNum < takeForm.length; elementNum++){
                //alert("elementNum = " + elementNum + "\ntakeForm.length = " + takeForm.length);
                // GET Q TYPE
                var fieldName = takeForm.elements[elementNum].name;
                var fullName = fieldName;
                uScoreLoc = fieldName.indexOf("_"); 
                var qType = fieldName.substring(0, uScoreLoc);
                if(qType.length == 3)
                    qType = qType.substr(0, 2);
                // GET QUESTION NUM
                fieldName = fieldName.substring(uScoreLoc+1);
                uScoreLoc = fieldName.indexOf("_"); 
				    var qNum = fieldName.substring(0, uScoreLoc);
                if(uScoreLoc == -1)
					 qNum = fieldName;
                //toAlert += elementNum + " = '" + qType + "'\n";
                //if(qNum < 10)
                    //alert("elementNum=" + elementNum + "\nfieldName="+fullName+"\nqType = '" + qType+"'\nqNum = " + qNum);
                    //return(false);
                //alert(takeForm.elements[elementNum].name);
                //if(qType == "tf") alert(qNum + " => " + "tf");
                switch(qType){
                    case "tf":
                        //True False
                        //alert("validateTFAnswer(" + elementNum + ", " + qNum + ", " + takeForm + ")");
                        if(!validateTFAnswer(elementNum, qNum, takeForm)) return(false);
                        elementNum++;
                        break;
                    case "mc":
                        //Multiple Choice
                        if(!validateMCAnswer(elementNum, qNum, takeForm)) return(false);
                        elementNum+=3;
                        break;
                    case "es":
                        //Essay
                        if(!validateESAnswer(elementNum, qNum, takeForm)) return(false);
                        break;
                    case "fb":
                        //Fill in the Blank
                        if(!validateFBAnswer(elementNum, qNum, takeForm, LETTER)) return(false);
                        while(fieldName.substring(0, 2) == "fb"){
						//while(fieldName.substring((fieldName.indexOf("_")+1), fieldName.indexOf("_")+3) == "fb"){
                            fieldName = takeForm.elements[++elementNum].name;
                        }
                        //elementNum--;
                        break;
                    case "ma":
                        //Matching
                        if(!validateMAAnswer(elementNum, qNum, takeForm, LETTER)) return(false);
                        while(fieldName.substring(0, 2) == "ma"){
						//while(fieldName.substring((fieldName.indexOf("_")+1), fieldName.indexOf("_")+3) == "ma"){
                            fieldName = takeForm.elements[++elementNum].name;
                        }
                        //elementNum--;
                        break;
                    case "wb":
                        //WordBank
                        if(!validateWBAnswer(elementNum, qNum, takeForm, LETTER)) return(false);
                        while(fieldName.substring(0, 2) == "wb"){
                        //while(fieldName.substring((fieldName.indexOf("_")+1), fieldName.indexOf("_")+3) == "wb"){
                            fieldName = takeForm.elements[++elementNum].name;
                        }
                        //elementNum--;
                        break;
                }
            }
            //alert(toAlert); 
            return(true);
        }

        function validateMCAnswer(elementNum, qNum, takeForm){
            for(x=0;x<4;x++){
                if((takeForm.elements[elementNum+x].checked)){
                    return(true);
                }
            }
            alert("Please answer question " + qNum);
            return(false);
        }

        function validateTFAnswer(elementNum, qNum, takeForm){
            //alert("validateTF");
            if((!takeForm.elements[elementNum].checked) && (!takeForm.elements[elementNum+1].checked)){
                alert("Please answer question " + qNum);
                //alert("validateTF => false");
                return(false);
            }
            //alert("validateTF => true");
            return(true);
        }

        function validateESAnswer(elementNum, qNum, takeForm){
            if(takeForm.elements[elementNum].value == ""){
                alert("Please answer question " + qNum);
                return(false);
            }
            if(takeForm.elements[elementNum].length > 1400){
               alert("Please limit your essay answer to < 1400 characters including spaces.\n"
                     + "You currently have " + takeForm.elements[elementNum].length + " characters.");
               return(false);
            }
            return(true);
        }
        function validateFBAnswer(elementNum, qNum, takeForm, LETTER){
            var answer = takeForm.elements[elementNum].value;
			var fieldName = takeForm.elements[elementNum]
			if(takeForm.elements[elementNum].value == ""){
                alert("Please answer all blanks in question " + qNum);
                return(false);
            }
            return(true);
        }
        function validateMAAnswer(elementNum, qNum, takeForm, LETTER){
            var numChoices = "num_choices_" + qNum;
            numChoices = takeForm.elements[numChoices].value;
			var empAnsw = trimSpaces(takeForm.elements[elementNum].value);
			//Ensure each answer entered was a single character.
			if(!(aLetter(empAnsw))){
                if(empAnsw != ""){
					alert("You have entered an invalid answer, " + empAnsw + ", on question " 
							+ qNum + ".\nPlease correct this error.");
				}
				else{
					alert("You have not answered all of question " 
							+ qNum + ".\nPlease complete the question.");
				}
            	return(false);
            }
			//Ensure each answer entered was a valid choice.
			if(LETTER.indexOf(empAnsw.toUpperCase()) > (numChoices - 1)){
				alert("You have entered an invalid letter, " + empAnsw + ", on question " 
						+ qNum + ".\nPlease correct this error.");
				return(false);
			}
            return(true);
        }
        function validateWBAnswer(elementNum, qNum, takeForm, LETTER){
            var numChoices = "num_choices_" + qNum;
            numChoices = takeForm.elements[numChoices].value;
			var empAnsw = trimSpaces(takeForm.elements[elementNum].value);
			//Ensure each answer entered was a single character.
			if(!(aLetter(empAnsw))){
                if(empAnsw != ""){
					alert("You have entered an invalid answer, " + empAnsw + ", on question " 
							+ qNum + ".\nPlease correct this error.");
				}
				else{
					alert("You have not answered all of question " 
							+ qNum + ".\nPlease complete the question.");
				}
            	return(false);
            }
			//Ensure each answer entered was a valid choice.
			if(LETTER.indexOf(empAnsw.toUpperCase()) > (numChoices - 1)){
				alert("You have entered an invalid letter, " + empAnsw + ", on question " 
						+ qNum + ".\nPlease correct this error.");
				return(false);
			}
            return(true);
        }

	function fieldExists(theId) {
 		if (document.getElementById && document.getElementById(theId)) return(true);
		if (document.all && document.all(theId)) return(true);
		if (document.layers && document.layers[theId]) return(true);
		return(false);
	}
        function aLetter(box){
            var checkOK = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            var allValid = false;
            box = box.toUpperCase();
            if(box.length > 1){
                return(false);
            }
            for(j=0;j<checkOK.length;j++){
                if(box == checkOK.charAt(j)){
                    return(true);
                }
            }
            return(false);
        }

function trimSpaces(sString)
{
    while (sString.substring(0,1) == ' ')
    {
        sString = sString.substring(1, sString.length);
    }
    while (sString.substring(sString.length-1, sString.length) == ' ')
    {
        sString = sString.substring(0,sString.length-1);
    }
    return sString;
}