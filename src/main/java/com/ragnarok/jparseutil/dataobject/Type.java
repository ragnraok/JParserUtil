package com.ragnarok.jparseutil.dataobject;

/**
 * Created by ragnarok on 15/6/28.
 * represent a variable type, currently only support primitive type and array type
 */
public class Type {
    
    private static final String TAG = "JParserUtil.VariableType";
    
    private String typeName; // fully qualified
    private boolean isPrimitive = false;
    private boolean isArray = false;
    private Type arrayElmentType = null; // not null if isArray is true
    
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    public String getTypeName() {
        return this.typeName;
    }
    
    public void setPrimitive(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }
    
    public boolean isPrimitive() {
        return this.isPrimitive;
    }
    
    public void setArray(boolean isArray) {
        this.isArray = isArray;
    }
    
    public boolean isArray() {
        return this.isArray;
    }
    
    public void setArrayElmentType(Type elemType) {
        this.arrayElmentType = elemType;
    }
    
    public Type getArrayElmentType() {
        return this.arrayElmentType;
    }

    @Override
    public String toString() {
        if (!isArray) {
            return String.format("{type: %s, isPrimitive: %b, isArray: %b}", typeName, isPrimitive, isArray);
        } else {
            return String.format("{type: %s, isPrimitive: %b, isArray: %b, arrayElemType: %s}", typeName, isPrimitive, isArray, arrayElmentType);
        }
    }
}
