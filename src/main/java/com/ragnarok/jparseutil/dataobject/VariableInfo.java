package com.ragnarok.jparseutil.dataobject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ragnarok on 15/5/24.
 * represent information about a variable for a class
 */
public class VariableInfo extends AnnotatedObject {
    
    private ClassInfo clazz;
    
    private String variableName;
    private Type type;
    private Object variableValue;

    private Set<Modifier> modifiers = new HashSet<>();
    
    public void setContainedClass(ClassInfo clazz) {
        this.clazz = clazz;
    }
    
    public ClassInfo getContainedClass() {
        return this.clazz;
    }
    
    public void setType(Type type) {
        this.type = type;
    }
    
    public Type getType() {
        return this.type;
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

    public void addModifier(Modifier modifier) {
        this.modifiers.add(modifier);
    }

    public void addAllModifiers(Set<Modifier> modifiers) {
        this.modifiers.addAll(modifiers);
    }

    public Set<Modifier> getModifiers() {
        return modifiers;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(String.format("{Variable: %s, type: %s, value: %s", variableName, type, variableValue));
        if (modifiers.size() > 0) {
            result.append("\n");
            result.append("{modifiers: ");
            for (Modifier modifier : modifiers) {
                result.append(modifier.toString() + ", ");
            }
            result.append("}");
        }
        if (annotationModifiers.size() > 0) {
            result.append("\n");
            for (AnnotationModifier modifier : annotationModifiers) {
                result.append(modifier.toString() + "\n");
            }
        }
        result.append("}");
        return result.toString();
    }

    @Override
    public int getTarget() {
        return AnnotatedObject.TARGET_VARIABLE;
    }
}
