package com.ragnarok.jparseutil.dataobject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ragnarok on 15/5/24.
 * represent information about a variable for a class
 */
public class VariableInfo {
    
    private ClassInfo clazz;
    
    private String variableName;
    private VariableType variableType;
    private Object variableValue;
    
    private ArrayList<AnnotationModifier> annotationModifiers = new ArrayList<>();
    
    public void setContainedClass(ClassInfo clazz) {
        this.clazz = clazz;
    }
    
    public ClassInfo getContainedClass() {
        return this.clazz;
    }
    
    public void setVariableType(VariableType variableType) {
        this.variableType = variableType;
    }
    
    public VariableType getVariableType() {
        return this.variableType;
    }
    
    public void setVariableName(String name) {
        this.variableName = name;
    }
    
    public String getVariableName() {
        return this.variableName;
    }
    
    public void setVariableValue(Object value) {
        this.variableValue = value;
    }
    
    public Object getVariableValue() {
        return this.variableValue;
    }
    
    public void putAnnotation(AnnotationModifier annotationModifier) {
        this.annotationModifiers.add(annotationModifier);
    }
    
    public List<AnnotationModifier> getAllAnnotationModifiers() {
        return annotationModifiers;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(String.format("Variable: %s, type: %s, value: %s", variableName, variableType, variableValue));
        result.append("\n");
        if (annotationModifiers.size() > 0) {
            for (AnnotationModifier modifier : annotationModifiers) {
                result.append(modifier.toString() + "\n");
            }
        }
        return result.toString();
    }
}
