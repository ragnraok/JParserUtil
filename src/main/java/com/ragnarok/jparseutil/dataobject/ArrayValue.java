package com.ragnarok.jparseutil.dataobject;

/**
 * Created by ragnarok on 15/6/28.
 * represent the array value
 */
public class ArrayValue {
    private static final String TAG = "JParserUtil.ArrayValue";
    
    private int size;
    
    private ArrayDimension value;
    
    public class ArrayDimension {
        private static final String TAG = "JParserUtil.ArrayValue.ArrayDimension";
        
        private VariableType elemType; // the type of the array, may be another array type, which is the multi dimensions array
        
        private int size;

        // may be the value literal, or another ArrayDimension object, which is the multi dimensions array
        private Object value;

        @Override
        public String toString() {
            return String.format("{ArrayDimension, size: %d, elemType: %s, value: %s}", size, elemType, value);
        }
    }

    @Override
    public String toString() {
        return String.format("{Array, size: %s, value: %s}", size, value);
    }
}
