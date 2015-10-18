package com.ragnarok.jparseutil.dataobject;

/**
 * Created by ragnarok on 15/10/18.
 * an object represent 'XXX.class' literal
 */
public class ClassLiteralValue {
    
    private Type type;
    
    public void setType(Type type) {
        this.type = type;
    }
    
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("{Class Literal: %s.class}", type.getTypeName());
    }
}
