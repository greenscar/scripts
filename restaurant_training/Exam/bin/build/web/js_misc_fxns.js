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
function displayAllFormVars(form){
    var vars = "";
    for(elementNum=0; elementNum < form.length; elementNum++){
        vars += form.elements[elementNum].name + " = " + form.elements[elementNum].value + "\n";
    }
    alert(vars);
}
