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
    
    ArrayList<VariableInfo> variableInfos = new ArrayList<>();

    private ArrayList<AnnotationModifier> annotationModifiers = new ArrayList<>();

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
}
