package com.ragnarok.jparseutil.dataobject;

/**
 * Created by ragnarok on 15/7/12.
 * represent a new class object declaration value
 */
public class NewClassObjectInfo {
    
    private static final String TAG = "JParserUtil.NewClassObjectInfo";
    
    private Type objectType; // the ``new`` object type
    
    public void setObjectType(Type type) {
        this.objectType = type;
    }
    
    public Type getObjectType() {
        return this.objectType;
    }

    @Override
    public String toString() {
        return String.format("{newClass, type: %s}", this.objectType);
    }
}
