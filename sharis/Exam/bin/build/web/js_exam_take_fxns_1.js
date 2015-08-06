        function checkForErrors(takeForm){
            var toReturn = false;
            var LETTER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            //alert("length = " + document.forms[0].length);
            //alert("document.forms[0].length = " + document.forms[0].length);
            //for(elementNum=0;elementNum<document.forms[0].length;elementNum++){
            //    var fieldName = document.forms[0].elements[elementNum].name;
            //    alert(elementNum + " => " + fieldName);
                //var uScoreLoc = fieldName.indexOf("_");
                //var qNum = fieldName.substring(0, uScoreLoc);
                //var qType = fieldName.substring((uScoreLoc+1), uScoreLoc + 3);
                //alert("elementNum=" + elementNum + "\nfieldName="+fieldName+"\nqType = " + qType+"\nqNum = " + qNum);
                //alert(document.forms[0].elements[elementNum].name);
            //}
/*
            for(elementNum=0;elementNum<document.forms[0].length;elementNum++){
                var fieldName = document.forms[0].elements[elementNum].name;
                alert(elementNum + " => " + fieldName);
                var uScoreLoc = fieldName.indexOf("_");
                var qNum = fieldName.substring(0, uScoreLoc);
                var qType = fieldName.substring((uScoreLoc+1), uScoreLoc + 3);
                alert("elementNum=" + elementNum + "\nfieldName="+fieldName+"\nqType = " + qType+"\nqNum = " + qNum);
                alert(document.forms[0].elements[elementNum].name);
                switch(qType){
                    case "tf":
                        //True False
                        //alert("elementNum=" + elementNum + "\nfieldName="+fieldName+"\nqType = " + qType+"\nqNum = " + qNum);
                        if(!validateTF(elementNum, qNum, document.forms[0])) return(false);
                        elementNum++;
                        break;
                    case "mc":
                        //Multiple Choice
                        if(!validateMC(elementNum, qNum, document.forms[0])) return(false);
                        elementNum+=3;
                        break;
                    case "es":
                        //Essay
                        if(!validateES(elementNum, qNum, document.forms[0])) return(false);
                        break;
                    case "fb":
                        //Fill in the Blank
                        if(!validateFB(elementNum, qNum, document.forms[0], LETTER)) return(false);
                        while(fieldName.substring((fieldName.indexOf("_")+1), fieldName.indexOf("_")+3) == "fb"){
                            fieldName = document.forms[0].elements[++elementNum].name;
                        }
                        elementNum--;
                        break;
                    case "ma":
                        //Matching
                        if(!validateMA(elementNum, qNum, document.forms[0], LETTER)) return(false);
                        while(fieldName.substring((fieldName.indexOf("_")+1), fieldName.indexOf("_")+3) == "ma"){
                            fieldName = document.forms[0].elements[++elementNum].name;
                        }
                        elementNum--;
                        break;
                    case "wb":
                        //Matching
                        if(!validateWB(elementNum, qNum, document.forms[0], LETTER)) return(false);
                        while(fieldName.substring((fieldName.indexOf("_")+1), fieldName.indexOf("_")+3) == "wb"){
                            fieldName = document.forms[0].elements[++elementNum].name;
                        }
                        elementNum--;
                        break;
                }
            }
*/
            //return(true);
            return(false);
        }
        function validateTF(elementNum, qNum, document.forms[0]){
            if((!document.forms[0].elements[elementNum].checked) && (!document.forms[0].elements[elementNum+1].checked)){
                alert("Please answer question " + qNum);
                return(false);
            }
            return(true);
        }
        function validateES(elementNum, qNum, document.forms[0]){
            if(document.forms[0].elements[elementNum].value == ""){
                alert("Please answer question " + qNum);
                return(false);
            }
            return(true);
        }
        function validateMC(elementNum, qNum, document.forms[0]){
            for(x=0;x<4;x++){
                if((document.forms[0].elements[elementNum+x].checked)){
                    return(true);
                }
            }
            alert("Please answer question " + qNum);
            return(false);
        }
        function validateFB(elementNum, qNum, document.forms[0], LETTER){
            for(x=0;x < LETTER.length;x++, elementNum++){
                var fieldName = qNum + "_fb_solution_" + LETTER.charAt(x);
		if(fieldExists(fieldName)){
                   if(document.forms[0].elements[elementNum].value == ""){
                        alert("Please answer all blanks in question " + qNum);
                        return(false);
                    }
		}
            }
            return(true);
        }
        function validateMA(elementNum, qNum, document.forms[0], LETTER){
            for(x=0;x<LETTER.length;x++, elementNum++){
                var fieldName = qNum + "_ma_solution_" + LETTER.charAt(x);
                if(fieldExists(fieldName)){
                    if(!(aLetter(document.forms[0].elements[elementNum].value))){
                        alert("Please answer each of question " + qNum + " with a single letter.");
                        return(false);
                    }
                }
            }
            return(true);
        }
        function validateWB(elementNum, qNum, document.forms[0], LETTER){
            for(x=0;x<LETTER.length && elementNum < document.forms[0].length;x++, elementNum++){
                var fieldName = qNum + "_wb_solution_" + LETTER.charAt(x);
                if(fieldExists(fieldName)){
                    if(!(aLetter(document.forms[0].elements[elementNum].value))){
                        alert("Please answer each of question " + qNum + " with a single letter.");
                        return(false);
                    }
                }
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
