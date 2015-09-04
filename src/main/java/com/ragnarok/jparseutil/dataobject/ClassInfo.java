package com.ragnarok.jparseutil.dataobject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ragnarok on 15/5/24.
 * represent information for a Java Class or Interface or Annotaiton
 */
public class ClassInfo extends AnnotatedObject {
    
    private String packageName;
    
    private String qualifiedName;
    private String simpleName;
    
    private boolean isInterface;
    private boolean isEnum;
    private boolean isAnnotaiton;
    
    private Set<Modifier> modifiers = new HashSet<>();
    
    private ArrayList<VariableInfo> variableInfos = new ArrayList<>();
    
    private ArrayList<MethodInfo> methodInfos = new ArrayList<>();
    
    private Type superType = null;
    
    private ArrayList<Type> implementsInterfaces = new ArrayList<>();

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
    
    public void addModifier(Modifier modifier) {
        this.modifiers.add(modifier);
    }
    
    public Set<Modifier> getModifiers() {
        return modifiers;
    }
    
    public void addAllModifiers(Set<Modifier> modifiers) {
        this.modifiers.addAll(modifiers);
    }
    
    public void setIsInterface(boolean isInterface) {
        this.isInterface = isInterface;
    }
    
    public boolean isInterface() {
        return this.isInterface;
    }
    
    public void setIsEnum(boolean isEnum) {
        this.isEnum = isEnum;
    }
    
    public boolean isEnum() {
        return this.isEnum;
    }
    
    public void setIsAnnotation(boolean isAnnotaiton) {
        this.isAnnotaiton = isAnnotaiton;
    }
    
    public boolean isAnnotaiton() {
        return isAnnotaiton;
    }
    
    public void setSuperClass(Type type) {
        this.superType = type;
    }
    
    public Type getSuperClass() {
        return this.superType;
    }
    
    public void addImplements(Type type) {
        this.implementsInterfaces.add(type);
    }
    
    public ArrayList<Type> getImplements() {
        return this.implementsInterfaces;
    }
    
    public String getPackageName() {
        return this.packageName;
    }
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("{Class, package: %s, ", packageName));
        result.append(String.format("qualifiedName: %s, ", qualifiedName));
        result.append(String.format("simpleName: %s, ", simpleName));
        result.append(String.format("isInterface: %b, ", isInterface));
        result.append(String.format("isEnum: %b, ", isEnum));
        if (superType != null) {
            result.append(String.format("superClass: %s", superType));
        }
        if (implementsInterfaces.size() > 0) {
            result.append("\n");
            result.append("{implements: ");
            for (Type type: implementsInterfaces) {
                result.append(type.toString() + ", ");
            }
            result.append("}");
        }
        if (modifiers.size() > 0) {
            result.append("\n");
            result.append("{modifiers: ");
            for (Modifier modifier : modifiers) {
                result.append(modifier.toString() + ", ");
            }
            result.append("}");
        }
        if (variableInfos.size() > 0) {
            result.append("\n");
            for (VariableInfo variableInfo : variableInfos) {
                result.append(variableInfo.toString() + ", ");
                result.append("\n");
            }
        }
        if (annotationModifiers.size() > 0) {
            result.append("\n");
            for (AnnotationModifier annotationModifier : annotationModifiers) {
                result.append(annotationModifier.toString() + ", ");
                result.append("\n");
            }
        }
        if (methodInfos.size() > 0) {
            result.append("\n");
            for (MethodInfo methodInfo : methodInfos) {
                result.append(methodInfo.toString() + ",");
                result.append("\n");
            }
        }
        
        result.append("}");
        return result.toString();
    }

    @Override
    public int getTarget() {
        return AnnotatedObject.TARGET_CLASS;
    }
}
