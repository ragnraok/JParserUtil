package com.ragnarok.jparseutil.memberparser;

import com.ragnarok.jparseutil.dataobject.ClassInfo;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.dataobject.VariableInfo;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;
import com.sun.tools.javac.tree.JCTree;

import java.util.ArrayList;

/**
 * Created by ragnarok on 15/5/25.
 * retrieve variable information from a {@link JCTree.JCVariableDecl} object
 */
public class VariableParser {
    
    private static final String TAG = "JParserUtil.VariableParser";
    
    public static VariableInfo parseVariable(SourceInfo sourceinfo, JCTree.JCVariableDecl variableDecl) {
        VariableInfo result = new VariableInfo();
        
        String name = variableDecl.name.toString();
        String type = variableDecl.vartype.toString();
        String value = null;
        
        result.setVariableName(name);
        
        if (Util.isPrimitive(type)) {
            value = variableDecl.init.toString();
            value = Util.trimPrimitiveValue(type, value);
        } else {
            type = parseType(sourceinfo, type);
        }
        result.setVariableTypeClassName(type);
        
        if (value != null) {
            result.setVariableValue(value);
        }
        
        Log.d(TAG, "parseVariable, name: %s, type: %s, value: %s", name, type, value);
        
        return result;
    }
    
    private static String parseType(SourceInfo sourceInfo, String type) {
        // currently we just support parse type from imports
        for (String className : sourceInfo.getImports()) {
            String simpleClassName = className.substring(className.lastIndexOf(".") + 1);
            if (simpleClassName.equals(type)) {
                return className;
            }
        }
        
        // for inner class variable, currently may not add in sourceInfo, so we will
        // update type later
        
        return type; // is import from java.lang
    }
    
    // update inner class variables type
    public static SourceInfo updateAllVariableTypeAfterParse(SourceInfo sourceInfo) {
        ArrayList<ClassInfo> classInfos = sourceInfo.getAllClass();
        for (ClassInfo classInfo : classInfos) {
            ArrayList<VariableInfo> variableInfos = classInfo.getAllVariables();
            for (VariableInfo variableInfo : variableInfos) {
                String type = variableInfo.getVariableTypeClassName();
                ClassInfo classType = sourceInfo.getClassInfoBySuffixName(type);
                if (classType != null) {
                    type = classType.getQualifiedName();
                    variableInfo.setVariableTypeClassName(type);
                    classInfo.updateVariable(variableInfo.getVariableName(), variableInfo);
                    Log.d(TAG, "update variable type: %s, name: %s", type, variableInfo.getVariableName());
                    sourceInfo.updateClassInfoByQualifiedName(classInfo.getQualifiedName(), classInfo);
                }
            }
        }
        
        return sourceInfo;
    }
}
