package com.ragnarok.jparseutil.util;

import com.github.javaparser.ast.expr.*;
import com.ragnarok.jparseutil.dataobject.AnnotationInfo;
import com.ragnarok.jparseutil.dataobject.ClassInfo;
import com.ragnarok.jparseutil.dataobject.ReferenceSourceMap;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;

import java.io.*;
import java.util.Scanner;

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
    
    public static boolean isVoidType(String type) {
        if (type == null) {
            return false;
        }
        return type.equalsIgnoreCase(Primitive.VOID_TYPE);
    }

    // parse type from source imports
    public static String parseTypeFromSourceInfo(SourceInfo sourceInfo, String type) {
        if (isPrimitive(type)) {
            return type;
        }
        

        for (String className : sourceInfo.getImports()) {
//            String simpleClassName = className.substring(className.lastIndexOf(".") + 1);
//            if (simpleClassName.equals(type)) {
//                return className;
//            }
            if (!className.endsWith("*")) {
                String simpleClassName = className.substring(className.lastIndexOf(".") + 1);
                if (simpleClassName.equals(type)) {
                    return className;
                }
            } else {
                // import *
                String fullQualifiedClassName = ReferenceSourceMap.getInstance().searchClassNameByPrefixAndSimpleClassName(className, type);
                if (fullQualifiedClassName != null) {
                    return fullQualifiedClassName;
                }
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
        ClassInfo classInfo = sourceInfo.getClassInfoBySuffixName(type);
        if (classInfo != null) {
            return classInfo.getQualifiedName();
        }

        return type; // is import from java.lang
    }
    
    public static Object getValueFromLiteral(LiteralExpr literal) {
        if (literal instanceof BooleanLiteralExpr) {
            return ((BooleanLiteralExpr) literal).getValue();
        } else if (literal instanceof IntegerLiteralExpr) {
            return ((IntegerLiteralExpr) literal).getValue();
        } else if (literal instanceof LongLiteralExpr) {
            return ((LongLiteralExpr) literal).getValue();
        } else if (literal instanceof DoubleLiteralExpr) {
            return ((DoubleLiteralExpr) literal).getValue();
        } else if (literal instanceof CharLiteralExpr) {
            return ((CharLiteralExpr) literal).getValue();
        } else if (literal instanceof NullLiteralExpr) {
            return null;
        } else if (literal instanceof StringLiteralExpr) {
            return ((StringLiteralExpr) literal).getValue();
        } else {
            return literal.toString();
        }
    }

    public static String buildClassName(String prefix, String simpleName) {
        simpleName = simpleName.replace(".", "");
        if (prefix.endsWith(".")) {
            return prefix + simpleName;
        } else  {
            return prefix + "." + simpleName;
        }
    }
    
    public static String getFileContent(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return null;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            String result = "";
            String line = null;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();   
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static boolean isStringInFile(String filename, String... strings) {
        File file = new File(filename);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                for (String str : strings) {
                    if (line.contains(str)) {
                        return true;
                    }
                }
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return false;
    }
}
