package com.ragnarok.jparseutil.util;

import com.ragnarok.jparseutil.dataobject.AnnotationInfo;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;

/**
 * Created by ragnarok on 15/5/25.
 */
public class Util {
    
    public static final String JAVA_FILE_SUFFIX = ".java";
    
    public static boolean isPrimitive(String variableTypeName) {
        if (variableTypeName == null) {
            return false;
        }
        for (String type : Primitive.PrimitiveTypes) {
            if (type.equals(variableTypeName)) {
                return true;
            }
        }
        return false;
    }

    // parse type from source imports
    public static String parseType(SourceInfo sourceInfo, String type) {
        if (isPrimitive(type)) {
            return type;
        }
        
        // currently we just support parse type from imports
        for (String className : sourceInfo.getImports()) {
            String simpleClassName = className.substring(className.lastIndexOf(".") + 1);
            if (simpleClassName.equals(type)) {
                return className;
            }
        }
        
        // parse for annotation type
        for (AnnotationInfo annotationInfo : sourceInfo.getAllAnnotations()) {
            String name = annotationInfo.getQualifiedName();
            if (name != null && name.endsWith(type)) {
                return name;
            }
        }

        // for inner class variable, currently may not add in sourceInfo, so we will
        // update type later

        return type; // is import from java.lang
    }
    
    public static Object getValueFromLiteral(JCTree.JCLiteral literal) {
        return literal.getValue();
    }

    public static String buildClassName(String prefix, String simpleName) {
        simpleName = simpleName.replace(".", "");
        if (prefix.endsWith(".")) {
            return prefix + simpleName;
        } else  {
            return prefix + "." + simpleName;
        }
    }
}
