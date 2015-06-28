package com.ragnarok.jparseutil.dataobject;

/**
 * Created by ragnarok on 15/6/28.
 * represent a variable type, currently only support primitive type and array type
 */
public class VariableType {
    
    private static final String TAG = "JParserUtil.VariableType";
    
    private String typeName; // fully qualified
    private boolean isPrimitive = false;
    private boolean isArray = false;
    
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

    @Override
    public String toString() {
        return String.format("variableType: %s, isPrimitive: %b, isArray: %b", typeName, isPrimitive, isArray);
    }
}
