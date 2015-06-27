package com.ragnarok.jparseutil.dataobject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ragnarok on 15/5/24.
 * represent information for a Java Class
 */
public class ClassInfo {
    
    private String qualifiedName;
    private String simpleName;
    
    private ArrayList<VariableInfo> variableInfos = new ArrayList<>();

    private ArrayList<AnnotationModifier> annotationModifiers = new ArrayList<>();
    
    private ArrayList<MethodInfo> methodInfos = new ArrayList<>();

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
    
    public void updateVariable(String variableName, VariableInfo variableInfo) {
        int index = -1;
        for (int i = 0; i < this.variableInfos.size(); i++) {
            if (this.variableInfos.get(i).getVariableName().equals(variableName)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            this.variableInfos.set(index, variableInfo);
        }
    }
    
    public ArrayList<VariableInfo> getAllVariables() {
        return this.variableInfos;
    }

    public void putAnnotation(AnnotationModifier annotationModifier) {
        this.annotationModifiers.add(annotationModifier);
    }
    
    public List<AnnotationModifier> getAllAnnotationModifiers() {
        return annotationModifiers;
    }
    
    public void putMethod(MethodInfo method) {
        this.methodInfos.add(method);
    }
    
    public List<MethodInfo> getAllMethods() {
        return methodInfos;
    }
    
    public void updateMethod(String methodName, MethodInfo newMethod) {
        int index = -1;
        for (int i = 0; i < this.methodInfos.size(); i++) {
            if (this.methodInfos.get(i).getMethodName().equals(methodName)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            this.methodInfos.set(index, newMethod);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Class: ");
        result.append(String.format("qualifiedName: %s, ", qualifiedName));
        result.append(String.format("simpleName: %s, ", simpleName));
        result.append("\n");
        if (variableInfos.size() > 0) {
            for (VariableInfo variableInfo : variableInfos) {
                result.append(variableInfo.toString() + ", ");
                result.append("\n");
            }
        }
        result.append("\n");
        if (annotationModifiers.size() > 0) {
            for (AnnotationModifier annotationModifier : annotationModifiers) {
                result.append(annotationModifier.toString() + ", ");
                result.append("\n");
            }
        }
        result.append("\n");
        if (methodInfos.size() > 0) {
            for (MethodInfo methodInfo : methodInfos) {
                result.append(methodInfo.toString() + ",");
                result.append("\n");
            }
        }
        return result.toString();
    }
}
