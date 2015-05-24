package com.ragnarok.jparseutil.dataobject;

/**
 * Created by ragnarok on 15/5/24.
 * represent information about a variable for a class
 */
public class VariableInfo {
    
    private ClassInfo clazz;
    
    private String variableName;
    private String variableQualifiedClassName;
    private int variableModified;
    private Object variableValue;
    
    public void setContainClass(ClassInfo clazz) {
        this.clazz = clazz;
    }
    
    public void setVariableTypeClassName(String className) {
        this.variableQualifiedClassName = className;
    }
    
    public void setVariableName(String name) {
        this.variableName = name;
    }
    
    public void setVariableValue(String value) {
        this.variableValue = value;
    }
}
