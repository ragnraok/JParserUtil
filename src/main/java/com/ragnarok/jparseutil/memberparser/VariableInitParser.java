package com.ragnarok.jparseutil.memberparser;

import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;
import com.sun.tools.javac.tree.JCTree;

/**
 * Created by ragnarok on 15/6/21.
 * parser for variable initialization, currently only support for primitive
 */
public class VariableInitParser {
    
    private static final String TAG = "JParserUtil.VariableInitParser";
    
    public static String parseVariableInit(SourceInfo sourceInfo, String type, JCTree.JCExpression expression) {
        Log.d(TAG, "parseVariableInit, express class: %s", expression.getClass().getSimpleName());
        if (expression instanceof JCTree.JCLiteral && Util.isPrimitive(type)) {
            return Util.trimPrimitiveValue(type, expression.toString());
        } else if (expression instanceof JCTree.JCIdent) {
            return expression.toString();
        } else if (expression instanceof JCTree.JCNewClass) {
            
        } else if (expression instanceof JCTree.JCNewArray) {
            
        }
        
        return null;
    }
}
