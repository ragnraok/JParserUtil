package com.ragnarok.jparseutil.dataobject;

/**
 * Created by ragnarok on 15/9/6.
 */
public class EnumConstant extends AnnotatedObject {
    
    private String name;
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public int getTarget() {
        return AnnotatedObject.TARGET_ENUM_CONSTANT;
    }

    @Override
    public String toString() {
        return String.format("EnumConstant, name: %s", getName());
    }
}
