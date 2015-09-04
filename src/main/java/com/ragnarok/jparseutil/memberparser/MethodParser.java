package com.ragnarok.jparseutil.memberparser;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.ragnarok.jparseutil.dataobject.*;
import com.ragnarok.jparseutil.util.Log;
import com.sun.tools.javac.tree.JCTree;

/**
 * Created by ragnarok on 15/6/7.
 * parse the class instance method
 */
public class MethodParser {
    
    public static final String TAG = "JParserUtil.MethodParser";
    
    public static MethodInfo parseMethodInfo(ClassInfo containedClass, SourceInfo sourceInfo, MethodDeclaration methodDecl) {
//        Log.d(TAG, "method name: %s, returnType: %s", methodDecl.name, methodDecl.getReturnType());
        if (containedClass != null && methodDecl.getType() != null) {
            MethodInfo methodInfo = new MethodInfo();
            
            String methodName = methodDecl.getName();
            methodName = containedClass.getQualifiedName() + "." + methodName;
            Type returnType = TypeParser.parseType(sourceInfo, methodDecl.getType(), methodDecl.getType().toString());
            methodInfo.setMethodName(methodName);
            methodInfo.setReturnType(returnType);
            
            if (methodDecl.getModifiers() != 0) {
                methodInfo.addAllModifiers(Modifier.parseModifiersFromFlags(methodDecl.getModifiers()));
            }
            
            Log.d(TAG, "parseMethodInfo, methodName: %s, returnType: %s", methodName, returnType);
            
            // parse parameters
            if (methodDecl.getParameters() != null && methodDecl.getParameters().size() > 0) {
                for (Parameter parameter : methodDecl.getParameters()) {
                    Type paramType = TypeParser.parseType(sourceInfo, parameter.getType(), parameter.getType().toString());
                    methodInfo.addParamType(paramType);
                    Log.d(TAG, "parseMethodInfo, parameter type: %s", paramType);
                }
            }
            
            // parse annotaion
            if (methodDecl.getAnnotations() != null && methodDecl.getAnnotations().size() > 0) {
                for (AnnotationExpr annotation : methodDecl.getAnnotations()) {
                    AnnotationModifier annotationModifier = AnnotationModifierParser.parseAnnotation(sourceInfo, annotation);
                    methodInfo.putAnnotation(annotationModifier);
                }
            }
            
            return methodInfo;
        }
        
        return null;
    }
}
