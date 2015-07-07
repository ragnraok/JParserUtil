package com.ragnarok.jparseutil.util;

import com.ragnarok.jparseutil.dataobject.AnnotationInfo;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;

/**
 * Created by ragnarok on 15/5/25.
 */
public class Util {
    
    public static String INT_TYPE = "int";
    public static String INTEGER_TYPE = "Integer";
    public static String STRING_TYPE = "String";
    public static String FLOAT_TYPE = "float";
    public static String FLOAT_PKG_TYPE = "Float";
    public static String DOUBLE_TYPE = "double";
    public static String DOUBLE_PKG_TYPE = "Double";
    public static String CHAR_TYPE = "char";
    public static String CHARACTER_TYPE = "Character";
    public static String LONG_TYPE = "long";
    public static String LONG_PKG_TYPE = "Long";
    public static String BOOLEAN_TYPE = "boolean";
    public static String BOOLEAN_PKG_TYPE = "Boolean";
    public static String NUMBER_TYPE = "Number";
    
    private static String[] PrimitiveType = new String[] {INT_TYPE, INTEGER_TYPE, STRING_TYPE,
            FLOAT_TYPE, FLOAT_PKG_TYPE, DOUBLE_TYPE, DOUBLE_PKG_TYPE, CHAR_TYPE, CHARACTER_TYPE,
            LONG_TYPE, LONG_PKG_TYPE, BOOLEAN_TYPE, BOOLEAN_PKG_TYPE, NUMBER_TYPE};
    
    private static String SYSTEM_DEFAULT_PACKAGE = "java.lang.";
    public static String STRING_NULL_LITERAL = "null";
    
    public static boolean isPrimitive(String variableTypeName) {
        if (variableTypeName == null) {
            return false;
        }
        for (String type : PrimitiveType) {
            if (type.equals(variableTypeName)) {
                return true;
            }
        }
        return false;
    }
    
    public static String trimPrimitiveValue(String type, String value) {
        if (type.equals("String")) { // currently we only process String type
            if (!value.equals(STRING_NULL_LITERAL)) {
                value = value.substring(1, value.length() - 1); // remove \" at the begin and the end
            }
            return value;
        }
        return value;
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
    
    public static String getValueFromLiteral(JCTree.JCLiteral literal) {
        // currently only support base type
        switch (literal.typetag) {
            case 2:
                return literal.value.toString();
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return literal.value.toString();
            case 10:
                return literal.value.toString();
            default:
                return null;
        }
    }
}
