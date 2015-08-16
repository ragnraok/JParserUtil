package com.ragnarok.jparseutil.dataobject;

import java.util.ArrayList;
import java.util.List;

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

    protected ArrayList<AnnotationModifier> annotationModifiers = new ArrayList<>();

    public void putAnnotation(AnnotationModifier annotationModifier) {
        this.annotationModifiers.add(annotationModifier);
    }

    public List<AnnotationModifier> getAllAnnotationModifiers() {
        return annotationModifiers;
    }
    
    public boolean isContainedAnnotationModifier(String annotationClassQualifiedName) {
        for (AnnotationModifier annotationModifier : annotationModifiers) {
            if (annotationModifier.getAnnotationName().equals(annotationClassQualifiedName)) {
                return true;
            }
        }
        return false;
    }
    
}
