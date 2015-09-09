package exam;
public class EntryType implements java.io.Serializable
{
   

   private String code;
   private String name;
   private String className;
   private boolean selfGrading;
   
   public EntryType()
   {
   }
   
   public void setCode(String c){ this.code = c; }
   public void setName(String n){ this.name = n; }
   public void setClassName(String cn){ this.className = cn; }
   public void setSelfGrading(boolean b){ this.selfGrading = b; }
   public void setSelfGrading(int i)
   {
      if(i == 0) this.selfGrading = false;
      else this.selfGrading = true;
   }
   
   public String getCode(){ return this.code; }
   public String getName(){ return this.name; }
   public String getClassName(){ return this.className; }
   public boolean isSelfGrading(){ return this.selfGrading; }
}
