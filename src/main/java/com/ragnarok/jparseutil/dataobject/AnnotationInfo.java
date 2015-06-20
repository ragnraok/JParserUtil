package com.ragnarok.jparseutil.dataobject;

import java.util.HashMap;

/**
 * Created by ragnarok on 15/6/9.
 * representation an Annotation declaration in Java source, currently not concern about the retention and target
 */
public class AnnotationInfo {
    
    private String name;
    private String qualifiedName;
    
    private HashMap<String, String> typeParamsNameMap = new HashMap<>();
    private HashMap<String, String> paramsDefaultValueMap = new HashMap<>();
    
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
    public void putParams(String type, String name, String defaultValue) {
        this.typeParamsNameMap.put(type, name);
        
    }
}