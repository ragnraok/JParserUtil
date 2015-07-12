package com.ragnarok.jparseutil.dataobject;

/**
 * Created by ragnarok on 15/7/12.
 * represent a new class object declaration value
 */
public class NewClassObjectInfo {
    
    private static final String TAG = "JParserUtil.NewClassObjectInfo";
    
    private VariableType objectType; // the ``new`` object type
    
    public void setObjectType(VariableType type) {
        this.objectType = type;
    }
    
    public VariableType getObjectType() {
        return this.objectType;
    }

    @Override
    public String toString() {
        return String.format("{newClass, type: %s}", this.objectType);
    }
}
