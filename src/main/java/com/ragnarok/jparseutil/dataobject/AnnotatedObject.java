package com.ragnarok.jparseutil.dataobject;

/**
 * Created by ragnarok on 15/8/15.
 * the subclass of {@link AnnotatedObject} means can be annotated
 */
public abstract class AnnotatedObject {

    /**
     * Annotation target, Class, interface
     */
    public static final int TARGET_CLASS = 1;

    /**
     * Annotatoin target, method
     */
    public static final int TARGET_METHOD = 2;

    /**
     * Annotation target, variable
     */
    public static final int TARGET_VARIABLE = 3;
    
    public abstract int getTarget();
    
}
