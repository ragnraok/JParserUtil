package com.ragnarok.jparseutil.util;

import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;

/**
 * Created by ragnarok on 15/5/25.
 */
public class Util {
    
    private static String[] PrimitiveType = new String[] {"String", "int", "Integer", 
            "float", "Float", "Double", "double", "Number", "char", "Character"};
    
    private static String SYSTEM_DEFAULT_PACKAGE = "java.lang.";
    public static String STRING_NULL_LITERAL = "null";
    
    public static boolean isPrimitive(String variableTypeName) {
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
                return literal.value.toString().substring(1, literal.value.toString().length() - 1);
            default:
                return null;
        }
    }
}
