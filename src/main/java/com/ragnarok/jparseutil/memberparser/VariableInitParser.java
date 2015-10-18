package com.ragnarok.jparseutil.memberparser;

import com.github.javaparser.ast.expr.*;
import com.ragnarok.jparseutil.dataobject.ClassLiteralValue;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.dataobject.Type;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;
import com.sun.tools.internal.jxc.gen.config.Classes;
import com.sun.tools.javac.tree.JCTree;

import java.util.List;

/**
 * Created by ragnarok on 15/6/21.
 * parser for variable initialization, currently only support these value:
 * 1. primitive type
 * 2. String
 * 3. Class literal
 * 4. Enum item
 * 5. one dimension array of them
 */
public class VariableInitParser {
    
    public static final String TAG = "JParserUtil.VariableInitParser";
    
    public static Object parseVariableInit(SourceInfo sourceInfo, String fullQualifiedTypeName, Expression expression) {
//        Log.d(TAG, "parseVariableInit, express class: %s", expression.getClass().getSimpleName());
        if (expression instanceof LiteralExpr) {
            return Util.getValueFromLiteral((LiteralExpr) expression);
        } else if (expression instanceof ClassExpr) {
            ClassExpr classExpr = (ClassExpr) expression;
            Type type = TypeParser.parseType(sourceInfo, classExpr.getType(), classExpr.getType().toString());
            ClassLiteralValue classLiteralValue = new ClassLiteralValue();
            classLiteralValue.setType(type);
            return classLiteralValue;
        } else if (expression instanceof FieldAccessExpr) { // Enum
            FieldAccessExpr fieldAccessExpr = (FieldAccessExpr) expression;
        } else if (expression instanceof ArrayInitializerExpr) {
            ArrayInitializerExpr arrayInitializerExpr = (ArrayInitializerExpr) expression;
            List<Expression> values = arrayInitializerExpr.getValues();
            for (Expression arrayElement : values) {
                
            }
        }
        
        return expression.toString();
    }
}
