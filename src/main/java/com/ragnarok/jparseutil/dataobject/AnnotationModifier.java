package com.ragnarok.jparseutil.dataobject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ragnarok on 15/5/29.
 * represent an annotation modifier, may be set in class, variable and method
 */
public class AnnotationModifier {
    
    private String annotationName; // fully qualified
    
    private HashMap<String, Object> nameValues = new HashMap<>(); // if value isn't primitive type, it may be null
    
    public void setAnnotationName(String name) {
        this.annotationName = name;
    }
    
    public String getAnnotationName() {
        return this.annotationName;
    }
    
    public void putNameValue(String name, Object value) {
        nameValues.put(name, value);
    }
    
    public HashMap<String, Object> getNameValues() {
        return nameValues;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("");
        result.append("{AnnotationModifier,");
        result.append(String.format("annotationName: %s, ", annotationName));
        result.append("\n");
        if (nameValues.size() > 0) {
            for (Map.Entry<String, Object> entry : nameValues.entrySet()) {
                result.append(String.format("paramName: %s, paramValue: %s, ", entry.getKey(), entry.getValue()));
            }
        }
        result.append("}");
        return result.toString();
    }
}
