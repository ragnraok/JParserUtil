package com.ragnarok.jparseutil.memberparser;

import com.ragnarok.jparseutil.dataobject.AnnotationInfo;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.dataobject.Type;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;

/**
 * Created by ragnarok on 15/6/28.
 * parser for the variable type
 */
public class TypeParser {

    private static final String TAG = "JParserUtil.TypeParser";

    public static Type parseType(SourceInfo sourceInfo, JCTree typeElement, String typeName) {
        Log.d(TAG, "parseType, typeElement class: %s", typeElement.getClass().getSimpleName());
        Type result = new Type();
        if (Util.isPrimitive(typeName)) {
            result.setPrimitive(true);
            result.setTypeName(typeName);
            return result;
        } else {
            String qualifiedName = parseTypeNameFromSourceInfo(sourceInfo, typeName);
            if (qualifiedName != null) {
                result.setTypeName(typeName);
                return result;
            }
        }
        result.setTypeName(typeName);
        if (typeElement.getKind() == Tree.Kind.ARRAY_TYPE) {
            result.setArray(true);
            if (typeElement instanceof JCTree.JCArrayTypeTree) {
                JCTree.JCArrayTypeTree arrayTypeTree = (JCTree.JCArrayTypeTree) typeElement;
                if (Util.isPrimitive(arrayTypeTree.elemtype.toString())) {
                    result.setPrimitive(true);
                }
                Type arrayElemType = parseType(sourceInfo, arrayTypeTree.elemtype, arrayTypeTree.elemtype.toString());
                result.setArrayElmentType(arrayElemType);
            }
        }
        return result;
    }

    private static String parseTypeNameFromSourceInfo(SourceInfo sourceInfo, String type) {
        if (type == null) {
            return null;
        }
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
        return null;
    }
    
    public static Type parseTypeFromJCLiteral(JCTree.JCLiteral literal) {
        Type result = new Type();
        result.setArray(false);
        result.setPrimitive(true);
        Tree.Kind kind = literal.getKind();
        result.setTypeName(getTypeNameFromKind(kind));
        return result;
    }
    
    private static String getTypeNameFromKind(Tree.Kind kind) {
        if (kind == Tree.Kind.INT_LITERAL) {
            return Util.INT_TYPE;
        } else if (kind == Tree.Kind.LONG_LITERAL) {
            return Util.LONG_TYPE;
        } else if (kind == Tree.Kind.FLOAT_LITERAL) {
            return Util.FLOAT_TYPE;
        } else if (kind == Tree.Kind.DOUBLE_LITERAL) {
            return Util.DOUBLE_TYPE;
        } else if (kind == Tree.Kind.BOOLEAN_LITERAL) {
            return Util.BOOLEAN_TYPE;
        } else if (kind == Tree.Kind.CHAR_LITERAL) {
            return Util.CHAR_TYPE;
        } else if (kind == Tree.Kind.STRING_LITERAL) {
            return Util.STRING_TYPE;
        } else {
            return "";
        }
    }
}
