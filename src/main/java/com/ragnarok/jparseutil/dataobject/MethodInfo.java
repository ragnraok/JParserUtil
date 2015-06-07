package com.ragnarok.jparseutil.dataobject;

import java.util.ArrayList;

/**
 * Created by ragnarok on 15/5/29.
 * represent information about a method
 */
public class MethodInfo {
    
    private String methodName;
    private String returnType; // fully qualified name
    private ArrayList<String> methodParamsType = new ArrayList<>(); // the parameters' type(fully qualified), in the order of declare in method
    private ArrayList<AnnotationModifier> annotationModifiers = new ArrayList<>();
    
    public void setMethodName(String name) {
        this.methodName = name;
    }
    
    public String getMethodName() {
        return this.methodName;
    }
    
    public void setReturnType(String type) {
        this.returnType = type;
    } 
    
    public String getReturnType() {
        return this.returnType;
    }
    
    public void addParamType(String type) {
        this.methodParamsType.add(type);
    }
    
    public void setParamType(int pos, String type) {
        if (pos < methodParamsType.size()) {
            methodParamsType.set(pos, type);
        }
    }
    
    public ArrayList<String> getParamType() {
        return this.methodParamsType;
    }
    
    public void addAnnotation(AnnotationModifier annotationModifier) {
        this.annotationModifiers.add(annotationModifier);
    }
    
    public ArrayList<AnnotationModifier> getAllAnnotationModifiers() {
        return this.annotationModifiers;
    }
}