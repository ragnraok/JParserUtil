package com.ragnarok.jparseutil.dataobject;

/**
 * Created by ragnarok on 15/5/24.
 * represent information about a variable for a class
 */
public class VariableInfo {
    
    private ClassInfo clazz;
    
    private String variableName;
    private String variableQualifiedClassName;
    private Object variableValue;
    
    public void setContainedClass(ClassInfo clazz) {
        this.clazz = clazz;
    }
    
    public ClassInfo getContainedClass() {
        return this.clazz;
    }
    
    public void setVariableTypeClassName(String className) {
        this.variableQualifiedClassName = className;
    }
    
    public String getVariableTypeClassName() {
        return this.variableQualifiedClassName;
    }
    
    public void setVariableName(String name) {
        this.variableName = name;
    }
    
    public String getVariableName() {
        return this.variableName;
    }
    
    public void setVariableValue(String value) {
        this.variableValue = value;
    }
    
    public Object getVariableValue() {
        return this.variableValue;
    }
}
