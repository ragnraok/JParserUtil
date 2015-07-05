package com.ragnarok.jparseutil.memberparser;

import com.ragnarok.jparseutil.dataobject.ArrayValue;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.dataobject.VariableType;
import com.ragnarok.jparseutil.util.Log;
import com.sun.tools.javac.tree.JCTree;

import javax.lang.model.type.ArrayType;

/**
 * Created by ragnarok on 15/7/2.
 * parser for parse array variable
 */
public class ArrayParser {
    
    private static final String TAG = "JParserUtil.ArrayParser";
    
    // for array with initialization, each dimension int the array is a JCNewArray object, and newArray.dims is 0
    // otherwise the newArray.elems will be null and newArray.dims.size() is equal to the number of dimensions for this array
    
    public static ArrayValue parseArray(SourceInfo sourceInfo, JCTree.JCExpression expression) {
        if (expression instanceof JCTree.JCNewArray) {
            JCTree.JCNewArray newArray = (JCTree.JCNewArray) expression;
            
            if (newArray.dims.size() != 0 && newArray.elems == null) {
                return parseArrayForWithoutInitialization(sourceInfo, newArray);
            } else {
                return parseArrayForWithInitialization(sourceInfo, newArray);
            }
        }
        
        return null;
    }
    
    private static ArrayValue parseArrayForWithInitialization(SourceInfo sourceInfo, JCTree.JCNewArray newArray) {
        ArrayValue result = new ArrayValue();
                
        parseArrayForWithInitializationRecur(sourceInfo, newArray, null, result);
        
        return result;
    }
    
    private static void parseArrayForWithInitializationRecur(SourceInfo sourceInfo, JCTree.JCExpression expr, ArrayValue.ArrayDimension currentDimenValue, ArrayValue arrayValue) {
        Log.d(TAG, "parseArrayForWithInitializationRecur, exprClass: %s", expr.getClass().getSimpleName());
        if (expr instanceof JCTree.JCNewArray) {
            JCTree.JCNewArray newArray = (JCTree.JCNewArray) expr;
            
            Log.d(TAG, "parseArrayForWithInitializationRecur, elemType: %s", newArray.elemtype);

            ArrayValue.ArrayDimension innerDimension = new ArrayValue.ArrayDimension();
            innerDimension.setSize(newArray.elems.size());
            
            
            if (currentDimenValue == null) { // the most outer array
                currentDimenValue = innerDimension;
                arrayValue.setValue(currentDimenValue);
            } else {
                currentDimenValue.addValue(innerDimension);
            }
            
            
            for (JCTree.JCExpression arrayExpr : newArray.elems) {
                if (!(arrayExpr instanceof JCTree.JCNewArray) && arrayValue.getElemType() == null && newArray.elemtype != null) {
                    VariableType type = TypeParser.parseType(sourceInfo, newArray.elemtype, newArray.elemtype.toString());
                    arrayValue.setElemType(type);    
                }
                
                parseArrayForWithInitializationRecur(sourceInfo, arrayExpr, innerDimension, arrayValue);
            }
            
        } else {
            if (expr instanceof JCTree.JCLiteral) {
                JCTree.JCLiteral literal = (JCTree.JCLiteral) expr;
                currentDimenValue.addValue(literal.getValue());
                
                if (arrayValue.getElemType() == null) {
                    arrayValue.setElemType(TypeParser.parseTypeFromJCLiteral(literal));
                }
            }
        }
    }
    
    private static ArrayValue parseArrayForWithoutInitialization(SourceInfo sourceInfo, JCTree.JCNewArray newArray) {
        ArrayValue result = new ArrayValue();
        int dimensions = newArray.dims.size();
        Log.d(TAG, "parseArrayForWithoutInitialization, dimensions: %d", dimensions);
        result.setDimensions(dimensions);

        // for array without initialization, the elemType does not contained the inner array type
        VariableType elemType = TypeParser.parseType(sourceInfo, newArray.elemtype, newArray.elemtype.toString());
        Log.d(TAG, "parseArrayForWithoutInitialization, elemType: %s", elemType);
        result.setElemType(elemType);

        int[] eachDimSizes = new int[newArray.dims.size()];
        int index = 0;
        
        for (JCTree.JCExpression dimExpression : newArray.dims) {
            if (dimExpression instanceof JCTree.JCLiteral) {
                JCTree.JCLiteral literal = (JCTree.JCLiteral) dimExpression;
                eachDimSizes[index] = (int) literal.getValue();
                Log.d(TAG, "dimen %d size: %d", index, eachDimSizes[index]);
                index++;
            }
        }

        ArrayValue.ArrayDimension arrayValue = new ArrayValue.ArrayDimension();
        arrayValue.setSize(eachDimSizes[0]);
        result.setValue(arrayValue);
        
        if (eachDimSizes.length == 1) {
            return result;
        }
        
        ArrayValue.ArrayDimension currentDimens = result.getValue();
        for (int i = 0; i < eachDimSizes.length; i++) {
            if (i == 0) {
                currentDimens = parseOneLevelDimensionForWithoutInitialization(currentDimens, eachDimSizes[i]);
            } else {
                for (Object dimenValue : currentDimens.getValues()) {
                    if (dimenValue instanceof ArrayValue.ArrayDimension) {
                        currentDimens = (ArrayValue.ArrayDimension) dimenValue;
                        currentDimens = parseOneLevelDimensionForWithoutInitialization(currentDimens, eachDimSizes[0]);
                    }
                }
            }
            
        }
        
        return result;
    }
    
    private static ArrayValue.ArrayDimension parseOneLevelDimensionForWithoutInitialization(ArrayValue.ArrayDimension currentLevelValue, int dimenSize) {
        for (int i = 0; i < dimenSize; i++) {
            ArrayValue.ArrayDimension innerDimension = new ArrayValue.ArrayDimension();
            innerDimension.setSize(dimenSize);
            currentLevelValue.addValue(innerDimension);
        }
        return currentLevelValue;
    }
}
