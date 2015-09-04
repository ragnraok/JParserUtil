package com.ragnarok.jparseutil.dataobject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by ragnarok on 15/6/9.
 * representation an Annotation declaration in Java source, currently not concern about the retention and target
 */
public class AnnotationInfo extends ClassInfo {
    
    private HashMap<Type, String> typeParamsNameMap = new HashMap<>(); // full qualified name
    private HashMap<String, Object> paramsDefaultValueMap = new HashMap<>();

    private Set<Modifier> modifiers = new HashSet<>();
    
    // the type must fully qualified
    public void putParams(Type type, String name, Object defaultValue) {
        this.typeParamsNameMap.put(type, name);
        this.paramsDefaultValueMap.put(name, defaultValue);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(String.format("{Annotation, %s", super.toString()));
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
