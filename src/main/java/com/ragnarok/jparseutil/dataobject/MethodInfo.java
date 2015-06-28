package com.ragnarok.jparseutil.dataobject;

import java.util.ArrayList;

/**
 * Created by ragnarok on 15/5/29.
 * represent information about a method
 */
public class MethodInfo {
    
    private String methodName;
    private VariableType returnType; // fully qualified name
    private ArrayList<VariableType> methodParamsType = new ArrayList<>(); // the parameters' type(fully qualified), in the order of declare in method
    private ArrayList<AnnotationModifier> annotationModifiers = new ArrayList<>();
    
    public void setMethodName(String name) {
        this.methodName = name;
    }
    
    public String getMethodName() {
        return this.methodName;
    }
    
    public void setReturnType(VariableType type) {
        this.returnType = type;
    } 
    
    public VariableType getReturnType() {
        return this.returnType;
    }
    
    public void addParamType(VariableType type) {
        this.methodParamsType.add(type);
    }
    
    public void setParamType(int pos, VariableType type) {
        if (pos < methodParamsType.size()) {
            methodParamsType.set(pos, type);
        }
    }
    
    public ArrayList<VariableType> getParamType() {
        return this.methodParamsType;
    }
    
    public void addAnnotation(AnnotationModifier annotationModifier) {
        this.annotationModifiers.add(annotationModifier);
    }
    
    public ArrayList<AnnotationModifier> getAllAnnotationModifiers() {
        return this.annotationModifiers;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Method: ");
        result.append(String.format("name: %s, ", methodName));
        result.append(String.format("returnType: %s, ", returnType));
        result.append("\n");
        if (methodParamsType.size() > 0) {
            for (VariableType params : methodParamsType) {
                result.append(String.format("paramsType: %s, ", params));
            }
        }
        result.append("\n");
        if (annotationModifiers.size() > 0) {
            for (AnnotationModifier annotationModifier : annotationModifiers) {
                result.append(annotationModifier.toString());
                result.append("\n");
            }
        }
        return result.toString();
    }
}
