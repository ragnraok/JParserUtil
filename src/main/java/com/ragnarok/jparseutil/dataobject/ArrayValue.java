package com.ragnarok.jparseutil.dataobject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ragnarok on 15/6/28.
 * represent the array value
 */
public class ArrayValue {
    
    private int size;
    
    private List<Object> values = new ArrayList<>();
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public void addElement(Object elem) {
        this.values.add(elem);
    }
    
    public List<Object> getElements() {
        return this.values;
    }

    @Override
    public String toString() {
        return String.format("{array, size: %s, values: %s}", this.size, this.values);
    }
}
