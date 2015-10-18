package com.ragnarok.jparseutil.dataobject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ragnarok on 15/8/15.
 * the subclass of {@link AnnotatedObject} means can be annotated
 */
public abstract class AnnotatedObject {

    /**
     * Annotation target, Class, Interface, Enum
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

    /**
     * Annotation target, enum constant
     */
    public static final int TARGET_ENUM_CONSTANT = 4;
    
    public abstract int getTarget();

    protected ArrayList<AnnotationModifier> annotationModifiers = new ArrayList<>();

    protected Set<Modifier> modifiers = new HashSet<>();

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

    public void addModifier(Modifier modifier) {
        this.modifiers.add(modifier);
    }

    public void addAllModifiers(Set<Modifier> modifiers) {
        this.modifiers.addAll(modifiers);
    }

    public Set<Modifier> getModifiers() {
        return modifiers;
    }
    
    public boolean isContainedModifier(Modifier modifier) {
        return modifiers.contains(modifier);
    }
    
}
