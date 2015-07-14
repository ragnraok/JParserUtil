package com.ragnarok.jparseutil.memberparser;

import com.ragnarok.jparseutil.dataobject.*;
import com.ragnarok.jparseutil.util.Log;
import com.sun.tools.javac.tree.JCTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ragnarok on 15/6/7.
 * parse the class instance method
 */
public class MethodParser {
    
    private static final String TAG = "JParserUtil.MethodParser";
    
    private static final String VOID_TYPE = "void";
    
    public static MethodInfo parseMethodInfo(ClassInfo containedClass, SourceInfo sourceInfo, JCTree.JCMethodDecl methodDecl) {
        if (containedClass != null) {
            MethodInfo methodInfo = new MethodInfo();
            
            String methodName = methodDecl.name.toString();
            methodName = containedClass.getQualifiedName() + "." + methodName;
            Type returnType = TypeParser.parseType(sourceInfo, methodDecl.getReturnType(), methodDecl.getReturnType().toString());
            methodInfo.setMethodName(methodName);
            methodInfo.setReturnType(returnType);
            
            if (methodDecl.getModifiers().getFlags() != null && methodDecl.getModifiers().getFlags().size() > 0) {
                for (javax.lang.model.element.Modifier modifier : methodDecl.getModifiers().getFlags()) {
                    methodInfo.addModifier(Modifier.convertFromToolsModifier(modifier));
                }
            }
            
            Log.d(TAG, "parseMethodInfo, methodName: %s, returnType: %s", methodName, returnType);
            
            // parse parameters
            if (methodDecl.getParameters() != null && methodDecl.getParameters().size() > 0) {
                for (JCTree.JCVariableDecl variableDecl : methodDecl.getParameters()) {
                    Type paramType = TypeParser.parseType(sourceInfo, variableDecl.getType(), variableDecl.getType().toString());
                    methodInfo.addParamType(paramType);
                    
                    Log.d(TAG, "parseMethodInfo, parameter type: %s", paramType);
                }
            }
            
            // parse annotaion
            if (methodDecl.getModifiers().getAnnotations() != null && methodDecl.getModifiers().getAnnotations().size() > 0) {
                for (JCTree.JCAnnotation annotation : methodDecl.getModifiers().getAnnotations()) {
                    AnnotationModifier annotationModifier = AnnotationModifierParser.parseAnnotation(sourceInfo, annotation);
                    methodInfo.addAnnotation(annotationModifier);
                }
            }
            
            return methodInfo;
        }
        
        return null;
    }
    
    public static SourceInfo updateAllMethodReturnTypeAndParamsTypeForInnerClass(SourceInfo sourceInfo) {
        ArrayList<ClassInfo> classInfos = sourceInfo.getAllClass();
        for (ClassInfo classInfo : classInfos) {
            List<MethodInfo> classMethods = classInfo.getAllMethods();
            for (MethodInfo method : classMethods) {
                
                // update return type
                Type returnType = method.getReturnType();
                ClassInfo returnClassType = sourceInfo.getClassInfoBySuffixName(returnType.getTypeName());
                if (returnClassType != null) {
                    returnType.setTypeName(returnClassType.getQualifiedName());
                    method.setReturnType(returnType);
                    
                    Log.d(TAG, "update method, name: %s, new returnType: %s", method.getMethodName(), returnType);
                }
                
                // update parameter type
                List<Type> paramsType = method.getParamType();
                if (paramsType != null && paramsType.size() > 0) {
                    for (int i = 0; i < paramsType.size(); i++) {
                        Type paramType = paramsType.get(i);
                        ClassInfo paramClassType = sourceInfo.getClassInfoBySuffixName(paramType.getTypeName());
                        if (paramClassType != null) {
                            paramType.setTypeName(paramClassType.getQualifiedName());
                            method.setParamType(i, paramType);
                            Log.d(TAG, "update method, name: %s, new paramType: %s, pos: %d", method.getMethodName(), paramType, i);
                        }
                    }
                }
                
                classInfo.updateMethod(method.getMethodName(), method);
            }
            
            sourceInfo.updateClassInfoByQualifiedName(classInfo.getQualifiedName(), classInfo);
        }

        return sourceInfo;
    }
}
