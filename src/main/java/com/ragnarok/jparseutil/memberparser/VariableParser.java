package com.ragnarok.jparseutil.memberparser;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.ragnarok.jparseutil.dataobject.*;
import com.ragnarok.jparseutil.dataobject.Modifier;
import com.ragnarok.jparseutil.util.Log;
import com.sun.tools.javac.tree.JCTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ragnarok on 15/5/25.
 * retrieve variable information from a {@link JCTree.JCVariableDecl} object
 * Note: the variable type currently only support primitive type, exclude array!
 */
public class VariableParser {
    
    public static final String TAG = "JParserUtil.VariableParser";
    
    public static VariableInfo parseVariable(SourceInfo sourceInfo, FieldDeclaration variableDecl) {
        VariableInfo result = new VariableInfo();
        
        String name = variableDecl.name.toString();
        String type = variableDecl.vartype.toString();
        
        result.setVariableName(name);
        
        if (variableDecl.getModifiers().getFlags() != null && variableDecl.getModifiers().getFlags().size() > 0) {
            for (javax.lang.model.element.Modifier modifier : variableDecl.getModifiers().getFlags()) {
                result.addModifier(Modifier.convertFromToolsModifier(modifier));
            }
        }
        
        Log.d(TAG, "vartype class name: %s", variableDecl.vartype.getClass().getSimpleName());
        
        Object value = null;
        if (variableDecl.init != null) {
            Log.d(TAG, "varinit class name: %s", variableDecl.init.getClass().getSimpleName());
            value = VariableInitParser.parseVariableInit(sourceInfo, type, variableDecl.vartype, variableDecl.init);   
        }

        Type variableType = TypeParser.parseType(sourceInfo, variableDecl.vartype, type);
        Log.d(TAG, "variableType: %s", variableType);
        result.setType(variableType);
        
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
}
