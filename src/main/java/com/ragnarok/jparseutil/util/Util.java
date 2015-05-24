package com.ragnarok.jparseutil.util;

/**
 * Created by ragnarok on 15/5/25.
 */
public class Util {
    
    private static String[] PrimitiveType = new String[] {"String", "int", "Integer", 
            "float", "Float", "Double", "double", "Number", "char", "Character"};
    
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
            value = value.substring(1, value.length() - 1); // remove \" at the begin and the end
            return value;
        }
        return value;
    }
}
