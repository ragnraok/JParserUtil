package com.ragnarok.jparseutil.memberparser;

import com.github.javaparser.ast.body.*;
import com.ragnarok.jparseutil.dataobject.AnnotationInfo;
import com.ragnarok.jparseutil.dataobject.Modifier;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.dataobject.Type;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;

import java.util.List;

/**
 * Created by ragnarok on 15/6/21.
 * parse the annotation declaration 
 */
public class AnnotationParser {
   
    public static final String TAG = "JParserUtil.AnnotationParser";
    
//    public static AnnotationInfo parseAnnotationInfo(String containedClassName, SourceInfo sourceInfo, AnnotationDeclaration typeDeclaration) {
//        String simpleName = typeDeclaration.getName();
//        String qualifiedName = null;
//        if (containedClassName != null && containedClassName.length() > 0) {
//            qualifiedName = Util.buildClassName(containedClassName, simpleName);
//        } else {
//            qualifiedName = Util.buildClassName(sourceInfo.getPackageName(), simpleName);
//        }
//
//        Log.d(TAG, "parseAnnotationInfo, simpleName: %s, qualifiedName: %s", simpleName, qualifiedName);
//        
//        AnnotationInfo result = new AnnotationInfo();
//        result.setPackageName(sourceInfo.getPackageName());
//        result.setSimpleName(simpleName);
//        result.setQualifiedName(qualifiedName);
//
//        if (typeDeclaration.getModifiers() != 0) {
//            result.addAllModifiers(Modifier.parseModifiersFromFlags(typeDeclaration.getModifiers()));
//        }
//        
//        List<BodyDeclaration> members = typeDeclaration.getMembers();
//        if (members != null && members.size() > 0) {
//            for (BodyDeclaration member : typeDeclaration.getMembers()) {
//                Log.d(TAG, "parseAnnotationInfo, annotation member class: %s", member.getClass().getSimpleName());
//                
//                if (member instanceof AnnotationMemberDeclaration) {
//                    AnnotationMemberDeclaration annotationMember = (AnnotationMemberDeclaration) member;
//                    Type type = TypeParser.parseType(sourceInfo, annotationMember.getType(), annotationMember.getType().toString());
//                    String name = annotationMember.getName().toString();
//                    Log.d(TAG, "parseAnnotationInfo, paramName: %s, paramType: %s, paramTypeKind: %s",
//                            type, name, annotationMember.getType().getClass().getName());
//                    if (annotationMember.getDefaultValue() != null) {
//                        String defaultValueLiteral = annotationMember.getDefaultValue().toString();
//                        Log.d(TAG, "defaultValueLiteral: %s, defaultValueClass: %s", defaultValueLiteral,
//                                annotationMember.getDefaultValue().getClass().getSimpleName());
//                        Object defaultValue = VariableInitParser.parseVariableInit(sourceInfo, null, null, annotationMember.getDefaultValue());
//                        result.putParams(type, name, defaultValue);
//                    } else {
//                        result.putParams(type, name, null);
//                    }
//                }
//            }
//        }
//        
//        return result;
//    }
}