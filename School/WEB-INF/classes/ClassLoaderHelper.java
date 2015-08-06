interface VMClassLoader{
    public Class loadClass(String cls) throws ClassNotFoundException;
}
public class ClassLoaderHelper {
    private static VMClassLoader vmClassLoader;
    static{
        vmClassLoader = new VMClassLoader(){
            public Class loadClass(String cls) throws ClassNotFoundException{
                Thread t = Thread.currentThread();
                ClassLoader c1 = t.getContextClassLoader();
                return c1.loadClass(cls);
            }
        };
    }
    public static Class loadClass(String cls) throws ClassNotFoundException{        
        return vmClassLoader.loadClass(cls);
    }
    private static String[] scrubArgs(String[] args){
        String[] toReturn = new String[args.length-1];
        for(int i=1; i<args.length; i++){
            toReturn[i-1] = args[i].toLowerCase();
        }
        return toReturn;
    }
    public static java.lang.reflect.Method findMethod(Class clazz, String methodName) throws Exception{
            java.lang.reflect.Method[] methods = clazz.getMethods();
            for(int i = 0; i < methods.length; i++){
                if (methods[i].getName().equals(methodName))
                    return methods[i];
            }
            return null;
    }          
}
