package com.ragnarok.jparseutil.dataobject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by ragnarok on 15/6/9.
 * representation an Annotation declaration in Java source, currently not concern about the retention and target
 */
public class AnnotationInfo {
    
    private String name;
    private String qualifiedName;
    
    private HashMap<Type, String> typeParamsNameMap = new HashMap<>(); // full qualified name
    private HashMap<String, Object> paramsDefaultValueMap = new HashMap<>();

    private Set<Modifier> modifiers = new HashSet<>();
    
    public void setSimpleName(String name) {
        this.name = name;
    }
    
    public String getSimpleName() {
        return this.name;
    }
    
    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }
    
    public String getQualifiedName() {
        return this.qualifiedName;
    }
    
    // the type must fully qualified
    public void putParams(Type type, String name, Object defaultValue) {
        this.typeParamsNameMap.put(type, name);
        this.paramsDefaultValueMap.put(name, defaultValue);
    }

    public void addModifier(Modifier modifier) {
        this.modifiers.add(modifier);
    }

    public Set<Modifier> getModifiers() {
        return modifiers;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{Annotation, ");
        result.append(String.format("name: %s, ", name));
        result.append(String.format("qualifiedName: %s, \n", qualifiedName));
        if (modifiers.size() > 0) {
            result.append("\n");
            result.append("{modifiers: ");
            for (Modifier modifier : modifiers) {
                result.append(modifier.toString() + ", ");
            }
            result.append("}");
        }
        if (typeParamsNameMap.size() > 0) {
            result.append("\n");
            for (Map.Entry<Type, String> entry: typeParamsNameMap.entrySet()) {
                result.append(String.format("paramName: %s, paramType: %s, defaultValue: %s",
                        entry.getValue(), entry.getKey(), paramsDefaultValueMap.get(entry.getValue())));
                result.append("\n");
            }
        }
        result.append("}");
        return result.toString();
    }
}
