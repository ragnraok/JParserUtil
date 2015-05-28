package com.ragnarok.jparseutil.dataobject;

import java.util.HashMap;

/**
 * Created by ragnarok on 15/5/29.
 * represent an annotation modifier, may be set in class, variable and method
 */
public class AnntationModifier {
    
    private String annotationName; // fully qualified
    
    private HashMap<String, String> nameValues = new HashMap<>(); // if value isn't primitive type, it may be null
    
    public void setAnnotationName(String name) {
        this.annotationName = name;
    }
    
    public String getAnnotationName() {
        return this.annotationName;
    }
    
    public void putNameValue(String name, String value) {
        nameValues.put(name, value);
    }
    
    public HashMap<String, String> getNameValues() {
        return nameValues;
    }
}
