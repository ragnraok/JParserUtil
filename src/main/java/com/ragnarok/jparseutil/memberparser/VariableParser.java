package com.ragnarok.jparseutil.memberparser;

import com.ragnarok.jparseutil.dataobject.AnnotationModifier;
import com.ragnarok.jparseutil.dataobject.ClassInfo;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.dataobject.VariableInfo;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;
import com.sun.tools.javac.tree.JCTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ragnarok on 15/5/25.
 * retrieve variable information from a {@link JCTree.JCVariableDecl} object
 */
public class VariableParser {
    
    private static final String TAG = "JParserUtil.VariableParser";
    
    public static VariableInfo parseVariable(SourceInfo sourceInfo, JCTree.JCVariableDecl variableDecl) {
        VariableInfo result = new VariableInfo();
        
        String name = variableDecl.name.toString();
        String type = variableDecl.vartype.toString();
        
        result.setVariableName(name);
        
        Log.d(TAG, "init class name: %s", variableDecl.init.getClass().getSimpleName());
 
        type = Util.parseType(sourceInfo, type);
        String value = VariableInitParser.parseVariableInit(sourceInfo, type, variableDecl.init);

        result.setVariableTypeClassName(type);
        
        if (value != null) {
            result.setVariableValue(value);
        }
        
        if (variableDecl.getModifiers().getAnnotations() != null && variableDecl.getModifiers().getAnnotations().size() > 0) {
            List<JCTree.JCAnnotation> annotationList = variableDecl.getModifiers().getAnnotations();
            for (JCTree.JCAnnotation annotation : annotationList) {
                AnnotationModifier annotationModifier = AnnotationModifierParser.parseAnnotation(sourceInfo, annotation);
                if (annotationModifier != null) {
                    result.putAnnotation(annotationModifier);
                }
            }
        }
        
        Log.d(TAG, "parseVariable, name: %s, type: %s, value: %s", name, type, value);
        
        return result;
    }
    
    // update inner class variables type
    public static SourceInfo updateAllVariableTypeForInnerClassAfterParse(SourceInfo sourceInfo) {
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
