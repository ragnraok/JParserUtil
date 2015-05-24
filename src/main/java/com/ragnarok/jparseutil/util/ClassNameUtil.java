package com.ragnarok.jparseutil.util;

/**
 * Created by ragnarok on 15/5/24.
 */
public class ClassNameUtil {
    
    public static String buildClassName(String prefix, String simpleName) {
        simpleName = simpleName.replace(".", "");
        if (prefix.endsWith(".")) {
            return prefix + simpleName;
        } else  {
            return prefix + "." + simpleName;
        }
    }
}
