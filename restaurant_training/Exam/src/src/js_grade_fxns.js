/***************************************************************************
 * sendValueToPoints computes the total point value earned on examLoc, 
 *     which is a fill in blank from the radio buttons. It then places
 *     this value in the points earned field.
 ***************************************************************************/
function sendValueToPoints(form, examLoc, numSolutions, pointValuePerAnswer, empCorrectField)
{
    var lookFor = empCorrectField + "_" + examLoc;
    var pointsEarned = 0;
    var pointsEarnedField = "points_earned_" + examLoc;
    for(elementNum=0; elementNum < form.length; elementNum++){
        if(form.elements[elementNum].name.indexOf(lookFor) == 0){
            if(form.elements[elementNum].value == "true"){
                if(form.elements[elementNum].checked == true){
                    pointsEarned += pointValuePerAnswer;
                }
            }
        }
    }
    document.getElementById(pointsEarnedField).value = pointsEarned;
    sumUpAllPoints(form);
}

/******************************************************************************
 * checkGrading calls checkPointEarnedValue to ensure proper point credits
 *      are used and checkFIBPointFields to ensure all FIB's are graded
 ******************************************************************************/
function checkGrading(form){
    var fieldName,examLoc,qNum, qNumField = "";
    var pointValPerAnsw;
    //displayAllFormVars(form);
    for(en = 0; en < form.length; en++){
        if(form.elements[en].name.indexOf("points_earned_") == 0){
            if(!(checkPointEarnedValue(form, en)))
                return(false);
        }
        else if(form.elements[en].name.indexOf("fibCorrect_") == 0){
            if(!(checkFIBPointFields(form, en)))
                return(false);
            en++;
        }
    }
    return(true);
}

/******************************************************************************
 * checkFIBPointFields ensures all fill in blank entries are marked correct 
 *     or incorrect.
 ******************************************************************************/
function checkFIBPointFields(form, en){
    var nameOne = form.elements[en].name;
    var trueVar = form.elements[en++].checked;
    var nameTwo = form.elements[en].name;
    var falseVar = form.elements[en].checked;
    if(!trueVar && !falseVar) 
    { 
        fieldName = form.elements[en].name;
        fieldName = fieldName.substring((fieldName.indexOf("_")+1), fieldName.length);
        fieldName = fieldName.substring(0, fieldName.indexOf("_"));
        qNumField = "qNum_" + fieldName;
        qNum = document.getElementById(qNumField).value;
        alert("Please grade all of question " + qNum);
        return(false);
    }
    else
        return(true);
}

/*************************************************************************
 * checkPointEarnedValue ensures the points earned fields have a valid value.
 * This means they are >= 0 and <= points possible for that question.
 * form => the form
 * en => element number on the form.elements array
 *************************************************************************/
function checkPointEarnedValue(form, en){
    // Get the exam loc of this entry.
    fieldName = form.elements[en].name;
    fieldName = fieldName.substring((fieldName.indexOf("_")+1), fieldName.length);
    examLoc = fieldName.substring((fieldName.indexOf("_")+1), fieldName.length);
    qNumField = "qNum_" + examLoc;
    qNum = document.getElementById(qNumField).value;
    fieldName = "points_possible_" + examLoc;
    pointValPerAnsw = document.getElementById(fieldName).value;
    pointsEarned = form.elements[en].value;
    
    //Ensure the points earned for this entry isn't empty.
    if(pointsEarned == ""){
        alert("Please fill in the point value for question " + qNum);
        return(false);
    }
    else if(!isFinite(pointsEarned)){
        alert("Please enter a valid point value for question " + qNum);
        return(false);
    }
    /*
     * For some reason, the pointsEarned > pointValPerAnswer comparison below
     *   treats the form value of a single digit as that number followed by a 0.
     *   For instance, 5 is treated as 50. Therefore, we subtract 0 to turn the
     *   string into an int. This compares correctly.
     */
    pointsEarned = pointsEarned - 0;
    //Ensure the points earned for this entry aren't above the value.
    if(pointsEarned > pointValPerAnsw){
        alert("The point value you entered for question " + qNum + " is too high.\n"
                + "You entered " + pointsEarned + ".\n"
                + "Please enter a value of " + pointValPerAnsw + " or lower.");
        return(false);
    }
    else if(pointsEarned < 0){
        alert("The point value you entered for question " + qNum + " is too low.\n"
                + "Please enter a value of 0 or greater.");
        return(false);
    }
    else{
        sumUpAllPoints(form);
        return(true);
    }
    //ENDEnsure the points earned for this entry aren't above the value.
}

function checkGradingThisField(form, fieldName){
    for(en = 0; en < form.length; en++){
        if(form.elements[en].name.indexOf(fieldName) == 0){
            checkPointEarnedValue(form, en);
        }
    }
}

function sumUpAllPoints(form){
    var lookFor = "points_earned_";
    var pointsEarned = 0;
    for(eLoc = 0; eLoc < form.length; eLoc++){
        if(form.elements[eLoc].name.indexOf(lookFor) == 0){
            pointsEarned += (form.elements[eLoc].value - 0);
        }
    }
    document.getElementById("finalGrade").value = pointsEarned;
}