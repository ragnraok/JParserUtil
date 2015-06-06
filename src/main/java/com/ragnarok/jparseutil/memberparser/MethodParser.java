package com.ragnarok.jparseutil.memberparser;

import com.ragnarok.jparseutil.dataobject.ClassInfo;
import com.ragnarok.jparseutil.dataobject.MethodInfo;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;
import com.sun.tools.javac.tree.JCTree;

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
            String returnType = methodDecl.getReturnType().toString();
            if (!returnType.equals(VOID_TYPE)) {
                returnType = Util.parseType(sourceInfo, returnType);
            }
            methodInfo.setMethodName(methodName);
            methodInfo.setReturnType(returnType);

            Log.d(TAG, "parseMethodInfo, methodName: %s, returnType: %s", methodName, returnType);
            
            // parse parameters
            if (methodDecl.getParameters() != null && methodDecl.getParameters().size() > 0) {
                for (JCTree.JCVariableDecl variableDecl : methodDecl.getParameters()) {
                    String paramType = variableDecl.getType().toString();
                    paramType = Util.parseType(sourceInfo, paramType);
                    methodInfo.addParamType(paramType);
                    
                    Log.d(TAG, "parseMethodInfo, parameter type: %s", paramType);
                }
            }
            
            return methodInfo;
        }
        
        return null;
    }
}
