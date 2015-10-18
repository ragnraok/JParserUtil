package com.ragnarok.jparseutil.dataobject;

/**
 * Created by ragnarok on 15/10/18.
 * represent an object's field access, like objA.a;
 */
public class ObjectFieldRef {
    
    private Type type;
    
    private String fieldName;
    
    public void setType(Type type) {
        this.type = type;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    public String getFieldName() {
        return this.fieldName;
    }

    @Override
    public String toString() {
        return String.format("{%s.%s}", type.getTypeName(), this.fieldName);
    }
}
