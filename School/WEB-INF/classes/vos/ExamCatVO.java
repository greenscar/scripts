package vos;

import java.beans.*;

public class ExamCatVO extends Object implements java.io.Serializable {
    private String name;
    private String code;
    
    public ExamCatVO(){
    }
    public ExamCatVO(String code, String name){
        this.code = code;
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    public String getCode(){
        return this.code;
    }
    public void setName(String n){
        this.name = n;
    }
    public void setCode(String n){
        this.code = n;
    }
    
}
