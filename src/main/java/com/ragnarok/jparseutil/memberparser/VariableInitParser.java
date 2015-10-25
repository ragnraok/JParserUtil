package com.ragnarok.jparseutil.memberparser;

import com.github.javaparser.ast.expr.*;
import com.ragnarok.jparseutil.dataobject.*;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;

/**
 * Created by ragnarok on 15/6/21.
 * parser for variable initialization, currently only support these value:
 * 1. primitive type
 * 2. String
 * 3. Class literal
 * 4. Field access
 * 5. one dimension array of them
 * if cannot parse this variable initialization, just return its string representation
 */
public class VariableInitParser {
    
    public static final String TAG = "JParserUtil.VariableInitParser";
    
    public static Object parseVariableInit(SourceInfo sourceInfo, Expression expression) {
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
            Expression type = fieldAccessExpr.getScope();
            if (type != null) {
                ObjectFieldRef objectFieldRef = new ObjectFieldRef();
                Type objectType = TypeParser.parseType(sourceInfo, null, type.toString());
                String field = fieldAccessExpr.getField();
                objectFieldRef.setType(objectType);
                objectFieldRef.setFieldName(field);
                return objectFieldRef;
            }
        } else if (expression instanceof ArrayInitializerExpr) {
            ArrayInitializerExpr arrayInitializerExpr = (ArrayInitializerExpr) expression;
            return parseArray(sourceInfo, arrayInitializerExpr);
        } else if (expression instanceof ArrayCreationExpr) {
            ArrayCreationExpr arrayCreationExpr = (ArrayCreationExpr) expression;
            return parseArray(sourceInfo, arrayCreationExpr);
        } else if (expression instanceof BinaryExpr) {
            BinaryExpr binaryExpr = (BinaryExpr) expression;
            return parseBinaryExpr(binaryExpr);
        }
        
        return expression.toString();
    }
    
    private static ArrayValue parseArray(SourceInfo sourceInfo, ArrayInitializerExpr arrayInitializerExpr) {
        if (arrayInitializerExpr.getValues() != null && arrayInitializerExpr.getValues().size() > 0) {
            ArrayValue result = new ArrayValue();
            result.setSize(arrayInitializerExpr.getValues().size());
            for (Expression expression : arrayInitializerExpr.getValues()) {
                Object value = parseVariableInit(sourceInfo, expression);
                result.addElement(value);
            }
            return result;
        }
        return null;
    }
    
    private static ArrayValue parseArray(SourceInfo sourceInfo, ArrayCreationExpr arrayCreationExpr) {
        ArrayInitializerExpr arrayInitializerExpr = arrayCreationExpr.getInitializer();
        if (arrayInitializerExpr != null && arrayInitializerExpr.getValues() != null 
                && arrayInitializerExpr.getValues().size() > 0) {
            return parseArray(sourceInfo, arrayInitializerExpr);
        }
        return null;
    }
    
    private static Object parseBinaryExpr(BinaryExpr binaryExpr) {
        Expression leftExpr = binaryExpr.getLeft();
        Expression rightExpr = binaryExpr.getRight();
        BinaryExpr.Operator op = binaryExpr.getOperator();
        Object leftResult = null, rightResult = null;
        if (leftExpr instanceof BinaryExpr) {
            leftResult = parseBinaryExpr((BinaryExpr) leftExpr);
        } else if (leftExpr instanceof LiteralExpr) {
            leftResult = Util.getValueFromLiteral((LiteralExpr) leftExpr);
        } else if (leftExpr instanceof EnclosedExpr) {
            EnclosedExpr enclosedExpr = (EnclosedExpr) leftExpr;
            if (enclosedExpr.getInner() instanceof BinaryExpr) {
                leftResult = parseBinaryExpr((BinaryExpr) enclosedExpr.getInner());
            } else if (enclosedExpr.getInner() instanceof LiteralExpr) {
                leftResult = Util.getValueFromLiteral((LiteralExpr) enclosedExpr.getInner());
            }
        } else {
            leftResult = null;
        }
        Log.d(TAG, "leftResult: %s", leftResult);

        if (rightExpr instanceof BinaryExpr) {
            rightResult = parseBinaryExpr((BinaryExpr) rightExpr);
        } else if (rightExpr instanceof LiteralExpr) {
            rightResult = Util.getValueFromLiteral((LiteralExpr) rightExpr);
        } else if (rightExpr instanceof EnclosedExpr) {
            EnclosedExpr enclosedExpr = (EnclosedExpr) rightExpr;
            if (enclosedExpr.getInner() instanceof BinaryExpr) {
                rightResult = parseBinaryExpr((BinaryExpr) enclosedExpr.getInner());
            } else if (enclosedExpr.getInner() instanceof LiteralExpr) {
                rightResult = Util.getValueFromLiteral((LiteralExpr) enclosedExpr.getInner());
            }
        } else {
            rightResult = null;
        }
        Log.d(TAG, "rightResult: %s", rightResult);

        if (leftResult != null && rightResult != null && (leftExpr instanceof LiteralExpr || rightExpr instanceof LiteralExpr)) {
            return Util.opWithTwoLiteralValue(leftResult, leftExpr, rightResult, rightExpr, op);
        }
        
        return null;
    }
}
