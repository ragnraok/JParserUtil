package com.ragnarok.jparseutil.dataobject;

import com.ragnarok.jparseutil.util.Log;

import java.util.ArrayList;

/**
 * Created by ragnarok on 15/5/24.
 * represent information about a Java source file 
 */
public class SourceInfo {
    
    private static final String TAG = "JParserUtil.SourceInfo";
    
    private ArrayList<String> importClassNames = new ArrayList<>();
    private String packageName = null;
    
    /**
     * all class informations
     */
    private ArrayList<ClassInfo> classInfos = new ArrayList<>();
    
    public void addImports(String importClass) {
        this.importClassNames.add(importClass);
    }
    
    public ArrayList<String> getImports() {
        return this.importClassNames;
    }
        
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    public String getPackageName() {
        return this.packageName;
    }
    
    public void addClassInfo(ClassInfo clazz) {
        if (clazz != null && !isContainClass(clazz.getSimpleName())) {
//            Log.d(TAG, "addClassInfo, name: %s, size: %d", clazz.getSimpleName(), this.classInfos.size());
            this.classInfos.add(clazz);
        }
    }
    
    public boolean isContainClass(String simpleClassName) {
        for (ClassInfo clazz : classInfos) {
            if (clazz.getSimpleName().equals(simpleClassName)) {
                return true;
            }
        }
        return false;
    }
    
    public String dumpClazz() {
        String result = "";
        for (ClassInfo clazz : classInfos) {
           result += clazz.getSimpleName() + ", ";
        }
        return result; 
    }
}
