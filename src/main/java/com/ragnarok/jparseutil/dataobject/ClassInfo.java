package com.ragnarok.jparseutil.dataobject;

import java.util.ArrayList;

/**
 * Created by ragnarok on 15/5/24.
 * represent information for a Java Class
 */
public class ClassInfo {
    
    private String qualifiedName;
    private String simpleName;
    
    ArrayList<VariableInfo> variableInfos = new ArrayList<>();
    
    public void setQualifiedName(String className) {
        this.qualifiedName = className;
    }
    
    public void setSimpleName(String className) {
        this.simpleName = className;
    }
    
    public String getQualifiedName() {
        return this.qualifiedName;
    }
    
    public String getSimpleName() {
        return this.simpleName;
    }
    
    public void addVariable(VariableInfo variableInfo) {
        this.variableInfos.add(variableInfo);
    }
}
