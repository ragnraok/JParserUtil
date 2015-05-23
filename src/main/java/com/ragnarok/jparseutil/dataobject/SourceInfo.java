package com.ragnarok.jparseutil.dataobject;

import java.util.ArrayList;

/**
 * Created by ragnarok on 15/5/24.
 * represent information about a Java source file 
 */
public class SourceInfo {
    
    private ArrayList<String> importClassNames = new ArrayList<>();
    private String packageName = null;

    /**
     * all class informations
     */
    private ArrayList<ClassInfo> classInfos = new ArrayList<>();
    
    public void addImports(String importClass) {
        this.importClassNames.add(importClass);
    }
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
