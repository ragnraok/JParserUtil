package com.ragnarok.jparseutil.util;

import com.github.javaparser.ast.expr.*;
import com.ragnarok.jparseutil.dataobject.AnnotationInfo;
import com.ragnarok.jparseutil.dataobject.ClassInfo;
import com.ragnarok.jparseutil.dataobject.ReferenceSourceMap;
import com.ragnarok.jparseutil.dataobject.SourceInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

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
        } else {
            return prefix + "." + simpleName;
        }
    }
    
    public static boolean isStringInFile(String filename, List<String> stringList) {
        String content = getFileContent(filename);
        if (content == null) {
            return false;
        }
        for (String str : stringList) {
            if (content.contains(str)) {
                return true;
            }
        }
        return false;
    }
    
    public static String getFileContent(String filename) {
        File file;
        FileChannel channel;
        MappedByteBuffer buffer;

        file = new File(filename);
        FileInputStream fin  = null;
        try {
            fin = new FileInputStream(file);
            channel = fin.getChannel();
            buffer  = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            byte[] contentBytes = new byte[buffer.remaining()];
            buffer.get(contentBytes);
            String content = new String(contentBytes);
            return content;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
