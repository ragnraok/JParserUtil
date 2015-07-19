package com.ragnarok.jparseutil.dataobject;

import com.ragnarok.jparseutil.util.Log;

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
    
    private boolean isUpdatedToQualifiedTypeName = false;
    
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    public String getTypeName() {
        if (!isUpdatedToQualifiedTypeName) {
            // if this is a fully qualifed className, it must looks like "com.example.test.QualifiedClassName",
            // which must contained a '.'
            if (this.typeName != null && !this.typeName.contains(".")) {
                if (finalParseResult != null) {
                    ClassInfo classInfo = finalParseResult.getClassInfoBySuffixName(this.typeName);
                    if (classInfo != null) {
                        String updatedName = classInfo.getQualifiedName();
                        Log.d(TAG, "update type name, %s to %s", this.typeName, updatedName);
                        this.typeName = updatedName;
                    }
                    isUpdatedToQualifiedTypeName = true;
                }
            }
        }
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
            return String.format("{type: %s, isPrimitive: %b, isArray: %b}", getTypeName(), isPrimitive(), isArray());
        } else {
            return String.format("{type: %s, isPrimitive: %b, isArray: %b, arrayElemType: %s}", getTypeName(), isPrimitive(), isArray(), arrayElmentType);
        }
    }
    
    private static SourceInfo finalParseResult;
    
    public static void setFinalParseResult(SourceInfo sourceInfo) {
        finalParseResult = sourceInfo;
    }
}
