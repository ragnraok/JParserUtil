package com.ragnarok.jparseutil.memberparser;

import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;
import com.sun.tools.javac.tree.JCTree;

/**
 * Created by ragnarok on 15/6/21.
 * parser for variable initialization, currently only support for primitive, and exclude array!
 */
public class VariableInitParser {
    
    private static final String TAG = "JParserUtil.VariableInitParser";
    
    public static String parseVariableInit(SourceInfo sourceInfo, String fullQualifiedTypeName,
                                           JCTree.JCExpression type, JCTree.JCExpression expression) {
        Log.d(TAG, "parseVariableInit, express class: %s", expression.getClass().getSimpleName());
        if (expression instanceof JCTree.JCLiteral && Util.isPrimitive(fullQualifiedTypeName)) {
            return Util.trimPrimitiveValue(fullQualifiedTypeName, expression.toString());
        } else if (expression instanceof JCTree.JCIdent) {
            return expression.toString();
        } else if (expression instanceof JCTree.JCAssign) {
            return expression.toString();
        } else if (expression instanceof JCTree.JCNewClass) {
            
        } else if (expression instanceof JCTree.JCNewArray) {
            JCTree.JCNewArray newArray = (JCTree.JCNewArray) expression;
            Log.d(TAG, "parseVariableInit, newArray elemType: %s, elemType class: %s", newArray.elemtype, newArray.elemtype.getClass().getSimpleName());
        }
        
        return null;
    }
}
