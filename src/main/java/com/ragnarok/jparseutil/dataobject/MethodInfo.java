package com.ragnarok.jparseutil.dataobject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ragnarok on 15/5/29.
 * represent information about a method
 */
public class MethodInfo extends AnnotatedObject {
    
    private String methodName;
    private Type returnType; // fully qualified name
    private ArrayList<Type> methodParamsType = new ArrayList<>(); // the parameters' type(fully qualified), in the order of declare in method

    private Set<Modifier> modifiers = new HashSet<>();
    
    public void setMethodName(String name) {
        this.methodName = name;
    }
    
    public String getMethodName() {
        return this.methodName;
    }
    
    public void setReturnType(Type type) {
        this.returnType = type;
    } 
    
    public Type getReturnType() {
        return this.returnType;
    }
    
    public void addParamType(Type type) {
        this.methodParamsType.add(type);
    }
    
    public void setParamType(int pos, Type type) {
        if (pos < methodParamsType.size()) {
            methodParamsType.set(pos, type);
        }
    }
    
    public ArrayList<Type> getParamType() {
        return this.methodParamsType;
    }
    
    public void addModifier(Modifier modifier) {
        this.modifiers.add(modifier);
    }

    public Set<Modifier> getModifiers() {
        return modifiers;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("{Method, ");
        result.append(String.format("name: %s, ", methodName));
        result.append(String.format("returnType: %s, ", returnType));
        if (modifiers.size() > 0) {
            result.append("\n");
            result.append("{modifiers: ");
            for (Modifier modifier : modifiers) {
                result.append(modifier.toString() + ", ");
            }
            result.append("}");
        }
        if (methodParamsType.size() > 0) {
            result.append("\n");
            for (Type params : methodParamsType) {
                result.append(String.format("paramsType: %s, ", params));
            }
        }
        if (annotationModifiers.size() > 0) {
            result.append("\n");
            for (AnnotationModifier annotationModifier : annotationModifiers) {
                result.append(annotationModifier.toString());
                result.append("\n");
            }
        }
        result.append("}");
        return result.toString();
    }

    @Override
    public int getTarget() {
        return AnnotatedObject.TARGET_METHOD;
    }
}
