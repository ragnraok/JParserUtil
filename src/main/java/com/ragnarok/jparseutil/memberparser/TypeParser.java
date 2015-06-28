package com.ragnarok.jparseutil.memberparser;

import com.ragnarok.jparseutil.dataobject.AnnotationInfo;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.dataobject.VariableType;
import com.ragnarok.jparseutil.util.Util;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;

/**
 * Created by ragnarok on 15/6/28.
 * parser for the variable type
 */
public class TypeParser {

    private static final String TAG = "JParserUtil.TypeParser";

    public static VariableType parseType(SourceInfo sourceInfo, JCTree typeElement, String typeName) {
        VariableType result = new VariableType();
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
}
