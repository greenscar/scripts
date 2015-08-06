package vos;
import logging.*;

public class EntryTypeVO {
    
    //***************** PROPERTY VARS ******************//
    private String name;
    private String code;
    private String className;
    
    //***************** CLASSES VARS *******************//
    
    //****************** CONSTRUCTORS ******************//
    public EntryTypeVO() {
    }
    public void setCode(String id){
        this.code = id;
    }
    public void setName(String id){
        this.name = id;
    }
    public void setClassName(String id){
        this.className = id;
    }
    
    public String getName(){
        return this.name;
    }
    public String getCode(){
        return this.code;
    }
    public String getClassName(){
        return this.className;
    }
    
}
