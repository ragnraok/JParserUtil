package com.ragnarok.jparseutil.dataobject;

/**
 * Created by ragnarok on 15/7/14.
 * copy from javax.lang.model.element.Modifier
 */
public enum Modifier {
    /** The modifier {@code public} */          
    PUBLIC,
    
    /** The modifier {@code protected} */       
    PROTECTED,
    
    /** The modifier {@code private} */         
    PRIVATE,
    
    /** The modifier {@code abstract} */        
    ABSTRACT,
    
    /** The modifier {@code static} */          
    STATIC,
    
    /** The modifier {@code final} */           
    FINAL,
    
    /** The modifier {@code transient} */       
    TRANSIENT,
    
    /** The modifier {@code volatile} */        
    VOLATILE,
    
    /** The modifier {@code synchronized} */    
    SYNCHRONIZED,
    
    /** The modifier {@code native} */          
    NATIVE,
    
    /** The modifier {@code strictfp} */        
    STRICTFP,

    /**
     * unknown modifier
     */
    UNKNOWN;


    private String lowercase = null;    // modifier name in lowercase

    /**
     * Returns this modifier's name in lowercase.
     */
    public String toString() {
        if (lowercase == null) {
            lowercase = name().toLowerCase(java.util.Locale.US);
        }
        return lowercase;
    }
    
    public static Modifier convertFromToolsModifier(javax.lang.model.element.Modifier toolsModifier) {
        if  (toolsModifier == javax.lang.model.element.Modifier.PUBLIC) {
            return PUBLIC;
        }
        if  (toolsModifier == javax.lang.model.element.Modifier.PROTECTED) {
            return PROTECTED;
        }
        if  (toolsModifier == javax.lang.model.element.Modifier.PRIVATE) {
            return PRIVATE;
        }
        if  (toolsModifier == javax.lang.model.element.Modifier.ABSTRACT) {
            return ABSTRACT;
        }
        if  (toolsModifier == javax.lang.model.element.Modifier.STATIC) {
            return STATIC;
        }
        if  (toolsModifier == javax.lang.model.element.Modifier.FINAL) {
            return FINAL;
        }
        if  (toolsModifier == javax.lang.model.element.Modifier.TRANSIENT) {
            return TRANSIENT;
        }
        if  (toolsModifier == javax.lang.model.element.Modifier.VOLATILE) {
            return VOLATILE;
        }
        if  (toolsModifier == javax.lang.model.element.Modifier.SYNCHRONIZED) {
            return SYNCHRONIZED;
        }
        if  (toolsModifier == javax.lang.model.element.Modifier.NATIVE) {
            return NATIVE;
        }
        if  (toolsModifier == javax.lang.model.element.Modifier.STRICTFP) {
            return STRICTFP;
        }
        return UNKNOWN;
    }
}
