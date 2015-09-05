package com.ragnarok.jparseutil.memberparser;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
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
        
        VariableDeclarator variableDeclarator = variableDecl.getVariables().get(0);
        
        String name = variableDeclarator.getId().getName();
        String type = variableDecl.getType().toString();
        
        result.setVariableName(name);
        
        if (variableDecl.getModifiers() != 0) {
            result.addAllModifiers(Modifier.parseModifiersFromFlags(variableDecl.getModifiers()));
        }
        
//        Log.d(TAG, "vartype class name: %s", variableDecl.getType().getClass().getSimpleName());
        
        Object value = null;
        if (variableDeclarator.getInit() != null) {
//            Log.d(TAG, "varinit class name: %s", variableDeclarator.getInit().getClass().getSimpleName());
            value = VariableInitParser.parseVariableInit(sourceInfo, type, variableDeclarator.getInit());   
        }

        Type variableType = TypeParser.parseType(sourceInfo, variableDecl.getType(), type);
        Log.d(TAG, "variableType: %s", variableType);
        result.setType(variableType);
        
        if (value != null) {
            result.setVariableValue(value);
        }
        
        if (variableDecl.getAnnotations() != null && variableDecl.getAnnotations().size() > 0) {
            List<AnnotationExpr> annotationList = variableDecl.getAnnotations();
            for (AnnotationExpr annotation : annotationList) {
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
