package com.ragnarok.jparseutil.memberparser;

import com.github.javaparser.ast.type.ReferenceType;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.dataobject.Type;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Primitive;
import com.ragnarok.jparseutil.util.Util;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;

/**
 * Created by ragnarok on 15/6/28.
 * parser for the variable type
 */
public class TypeParser {

    public static final String TAG = "JParserUtil.TypeParser";

    public static Type parseType(SourceInfo sourceInfo, com.github.javaparser.ast.type.Type typeElement, String typeName) {
        if (typeElement != null) {
//            Log.d(TAG, "parseTypeFromSourceInfo, typeElement class: %s", typeElement.getClass().getSimpleName());
        }
        boolean isArray = typeElement != null && typeElement instanceof ReferenceType && ((ReferenceType)typeElement).getArrayCount() > 0;
        Type result = new Type();
        result.setContainedSourceInfo(sourceInfo);
        if (Util.isPrimitive(typeName)) {
            result.setPrimitive(true);
            result.setTypeName(typeName);
        } else {
            String qualifiedName = Util.parseTypeFromSourceInfo(sourceInfo, typeName);
            if (qualifiedName != null) {
                result.setTypeName(qualifiedName);
            } else {
                result.setTypeName(typeName);
            }
        }
        if (isArray) {
            result.setArray(true);
            ReferenceType arrayType = (ReferenceType) typeElement;
            if (Util.isPrimitive(arrayType.getType().toString())) {
                result.setPrimitive(true);
            }
            Type arrayElemType = parseType(sourceInfo, arrayType.getType(), arrayType.getType().toString());
            result.setArrayElementType(arrayElemType);
        }
        return result;
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
            return Primitive.INT_TYPE;
        } else if (kind == Tree.Kind.LONG_LITERAL) {
            return Primitive.LONG_TYPE;
        } else if (kind == Tree.Kind.FLOAT_LITERAL) {
            return Primitive.FLOAT_TYPE;
        } else if (kind == Tree.Kind.DOUBLE_LITERAL) {
            return Primitive.DOUBLE_TYPE;
        } else if (kind == Tree.Kind.BOOLEAN_LITERAL) {
            return Primitive.BOOLEAN_TYPE;
        } else if (kind == Tree.Kind.CHAR_LITERAL) {
            return Primitive.CHAR_TYPE;
        } else if (kind == Tree.Kind.STRING_LITERAL) {
            return Primitive.STRING_TYPE;
        } else {
            return "";
        }
    }
}
