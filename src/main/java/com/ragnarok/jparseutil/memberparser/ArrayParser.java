package com.ragnarok.jparseutil.memberparser;

import com.ragnarok.jparseutil.dataobject.ArrayValue;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.dataobject.VariableType;
import com.ragnarok.jparseutil.util.Log;
import com.sun.tools.javac.tree.JCTree;

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
                
            }
            
//            int dimens = newArray.dims.size();
//            Log.d(TAG, "parseArray, dimens: %d", dimens);
//            
//            for (JCTree.JCExpression dimenExpression : newArray.dims) {
//                Log.d(TAG, "dimen class: %s", dimenExpression.getClass().getSimpleName());
//            }
//            
//            if (newArray.elems != null) {
//                for (JCTree.JCExpression elemExpression : newArray.elems) {
//                    Log.d(TAG, "elem class: %s", elemExpression.getClass().getSimpleName());
//                }
//            }
        }
        
        return null;
    }
    
    private static ArrayValue parseArrayForWithoutInitialization(SourceInfo sourceInfo, JCTree.JCNewArray newArray) {
        ArrayValue result = new ArrayValue();
        int dimensions = newArray.dims.size();
        Log.d(TAG, "parseArrayForWithoutInitialization, dimensions: %d", dimensions);
        result.setDimensions(dimensions);

        // for array without initialization, the elemType does not contained the inner array type
        VariableType elemType = TypeParser.parseType(sourceInfo, newArray.elemtype, newArray.elemtype.toString());
        Log.d(TAG, "parseArrayForWithoutInitialization, elemType: %s", elemType);

        for (JCTree.JCExpression dimExpression : newArray.dims) {
            ArrayValue.ArrayDimension arrayDimension = new ArrayValue.ArrayDimension();
            Log.d(TAG, "parseArrayForWithoutInitialization, dimen class: %s", dimExpression.getClass().getSimpleName());
            if (dimExpression instanceof JCTree.JCLiteral) {
                JCTree.JCLiteral literal = (JCTree.JCLiteral) dimExpression;
                int dimSize = (int) literal.getValue();
                Log.d(TAG, "parseArrayForWithoutInitialization, dim size: %d", dimSize);
                arrayDimension.setSize(dimSize);
                arrayDimension.setElemType(elemType);
            }
        }
        
        return result;
    }
}
