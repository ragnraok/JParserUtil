package com.ragnarok.jparseutil.dataobject;

import java.util.ArrayList;

/**
 * Created by ragnarok on 15/6/28.
 * represent the array value
 */
public class ArrayValue {
    private static final String TAG = "JParserUtil.ArrayValue";
    
    private int dimensions = 0;
    
    private ArrayDimension value;
    
    public void setDimensions(int dimen) {
        this.dimensions = dimen;
    }
    
    public int getDimensions() {
        return this.dimensions;
    }
    
    public void setValue(ArrayDimension value) {
        this.value = value;
    }
    
    public ArrayDimension getValue() {
        return this.value;
    }

    /**
     * represent a dimension of an array
     */
    public static class ArrayDimension {
        private static final String TAG = "JParserUtil.ArrayValue.ArrayDimension";
        
        private VariableType elemType; // the type of the array, may be another array type, which is the multi dimensions array
        
        private int size;

        // may be the value literal, or another ArrayDimension object, which is the multi dimensions array
        // empty if the array does not initialize when decleare
        private ArrayList<Object> values = new ArrayList<Object>();
       
        
        public void setElemType(VariableType type) {
            this.elemType = type;
        }
        
        public VariableType getElemType() {
            return this.elemType;
        }
        
        public void addValue(Object value) {
            this.values.add(value);
        }
        
        public ArrayList<Object> getValues() {
            return this.values;
        }
        
        public void setSize(int size) {
            this.size = size;
        }
        
        public int getSize() {
            return this.size;
        }

        @Override
        public String toString() {
            return String.format("{ArrayDimension, size: %d, elemType: %s, value: %s}", size, elemType, values);
        }
    }

    @Override
    public String toString() {
        return String.format("{Array, dimensions: %d, value: %s}", dimensions, value);
    }
}
