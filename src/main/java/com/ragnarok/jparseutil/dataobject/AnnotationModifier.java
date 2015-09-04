package com.ragnarok.jparseutil.dataobject;

import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ragnarok on 15/5/29.
 * represent an annotation modifier, may be set in class, variable and method
 */
public class AnnotationModifier {
    
    public static final String TAG = "JParserUtil.AnnotationModifier";
    
    private String annotationName; // fully qualified
    
    private boolean isUpdatedToQualifiedTypeName = false;
    
    private HashMap<String, Object> nameValues = new HashMap<>(); // if value isn't primitive type, it may be null

    private SourceInfo containedSourceInfo;
    
    public void setAnnotationName(String name) {
        this.annotationName = name;
    }
    
    public String getAnnotationName() {
        if (!isUpdatedToQualifiedTypeName && CodeInfo.isParseFinish()) {
            String origin = annotationName;
            if (annotationName != null) {
                annotationName = Util.parseTypeFromSourceInfo(containedSourceInfo, annotationName);
                if (annotationName != null && annotationName.contains(".")) {
                    isUpdatedToQualifiedTypeName = true;
                } else {
                    if (!isUpdatedToQualifiedTypeName) { // not in import, sample package of this containedSourceInfo
                        annotationName = containedSourceInfo.getPackageName() + "." + annotationName;
                        isUpdatedToQualifiedTypeName = true;
                    }
                }
            }
            Log.d(TAG, "update annotationName, from: %s, to: %s", origin, annotationName);
        }
        
        return this.annotationName;
    }
    
    public void putNameValue(String name, Object value) {
        nameValues.put(name, value);
    }
    
    public HashMap<String, Object> getNameValues() {
        return nameValues;
    }

    public void setContainedSourceInfo(SourceInfo sourceInfo) {
        this.containedSourceInfo = sourceInfo;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("");
        result.append("{AnnotationModifier,");
        result.append(String.format("annotationName: %s, ", getAnnotationName()));
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
