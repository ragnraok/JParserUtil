package com.ragnarok.jparseutil.dataobject;

import java.util.ArrayList;

/**
 * Created by ragnarok on 15/6/28.
 * represent the array value
 */
public class ArrayValue {
    private static final String TAG = "JParserUtil.ArrayValue";
    
    private int dimensions = 0; // how many dimensions for this array
    
    private Type elemType = null;
    
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
    
    public void setElemType(Type type) {
        this.elemType = type;
    }
    
    public Type getElemType() {
        return this.elemType;
    }

    /**
     * represent a dimension of an array
     */
    public static class ArrayDimension {
        private static final String TAG = "JParserUtil.ArrayValue.ArrayDimension";
   
        private int size;

        // may be the value literal, or another ArrayDimension object, which is the multi dimensions array
        // empty if the array does not initialize when decleare
        private ArrayList<Object> values = new ArrayList<Object>();
        
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
            return String.format("{ArrayDimension, size: %d, value: %s}", size, values);
        }
    }

    @Override
    public String toString() {
        return String.format("{Array, dimensions: %d, elemType: %s, value: %s}", dimensions, elemType, value);
    }
}
