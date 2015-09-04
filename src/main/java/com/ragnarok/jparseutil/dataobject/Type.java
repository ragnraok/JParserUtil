package com.ragnarok.jparseutil.dataobject;

import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;
import com.sun.tools.javac.code.Source;

import java.util.HashMap;

/**
 * Created by ragnarok on 15/6/28.
 * represent a variable type, currently only support primitive type and array type
 */
public class Type {
    
    public static final String TAG = "JParserUtil.Type";
    
    private String typeName; // fully qualified
    private boolean isPrimitive = false;
    private boolean isArray = false;
    private Type arrayElementType = null; // not null if isArray is true
    
    private boolean isUpdatedToQualifiedTypeName = false;
    
    private SourceInfo containedSourceInfo;
    
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    public String getTypeName() {
        if (CodeInfo.isParseFinish() && 
                !isUpdatedToQualifiedTypeName && !Util.isPrimitive(typeName) && !Util.isVoidType(typeName) && !isArray) {
            if (this.typeName != null) {
                typeName = Util.parseTypeFromSourceInfo(containedSourceInfo, typeName);
                if (typeName != null && typeName.contains(".")) {
                    isUpdatedToQualifiedTypeName = true;
                } else {
                    if (ReferenceSourceMap.getInstance().isFinishParse()) { // not in import, sample package of this containedSourceInfo
                        typeName = containedSourceInfo.getPackageName() + "." + typeName;
                        isUpdatedToQualifiedTypeName = true;
                    }
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
    
    public void setArrayElementType(Type elemType) {
        this.arrayElementType = elemType;
    }
    
    public Type getArrayElementType() {
        return this.arrayElementType;
    }
    
    public void setContainedSourceInfo(SourceInfo sourceInfo) {
        this.containedSourceInfo = sourceInfo;
    }

    @Override
    public String toString() {
        if (!isArray) {
            return String.format("{type: %s, isPrimitive: %b, isArray: %b}", getTypeName(), isPrimitive(), isArray());
        } else {
            return String.format("{type: %s, isPrimitive: %b, isArray: %b, arrayElemType: %s}", getTypeName(), isPrimitive(), isArray(), arrayElementType);
        }
    }
    
    
}
