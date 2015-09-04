package com.ragnarok.jparseutil.memberparser;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LiteralExpr;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;
import com.sun.tools.javac.tree.JCTree;

/**
 * Created by ragnarok on 15/6/21.
 * parser for variable initialization, currently only support for primitive, and exclude array!
 */
public class VariableInitParser {
    
    public static final String TAG = "JParserUtil.VariableInitParser";
    
    public static Object parseVariableInit(SourceInfo sourceInfo, String fullQualifiedTypeName, Expression expression) {
        Log.d(TAG, "parseVariableInit, express class: %s", expression.getClass().getSimpleName());
        if (expression instanceof LiteralExpr) {
            return Util.getValueFromLiteral((LiteralExpr) expression);
        } 
//        else if (expression instanceof JCTree.JCIdent) {
//            return expression.toString();
//        } else if (expression instanceof JCTree.JCAssign) {
//            return expression.toString();
//        }
//        else if (expression instanceof JCTree.JCNewClass) {
//            JCTree.JCNewClass newClass = (JCTree.JCNewClass) expression;
//            return NewClassObjectParser.parseNewClass(sourceInfo, newClass);
//        } 
//        else if (expression instanceof JCTree.JCNewArray) {
//            JCTree.JCNewArray newArray = (JCTree.JCNewArray) expression;
//            return ArrayParser.parseArray(sourceInfo, newArray);
//        }
        
        return expression.toString();
    }
}
