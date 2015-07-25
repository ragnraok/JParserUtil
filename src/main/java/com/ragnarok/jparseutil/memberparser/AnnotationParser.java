package com.ragnarok.jparseutil.memberparser;

import com.ragnarok.jparseutil.dataobject.AnnotationInfo;
import com.ragnarok.jparseutil.dataobject.Modifier;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.dataobject.Type;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;

import java.util.List;

/**
 * Created by ragnarok on 15/6/21.
 * parse the annotation declaration 
 */
public class AnnotationParser {
    private static final String TAG = "JParserUtil.AnnotationParser";
    
    public static AnnotationInfo parseAnnotationInfo(String containedClassName, SourceInfo sourceInfo, ClassTree classTree) {
        String simpleName = classTree.getSimpleName().toString();
        String qualifiedName = null;
        if (containedClassName != null && containedClassName.length() > 0) {
            qualifiedName = Util.buildClassName(containedClassName, simpleName);
        } else {
            qualifiedName = Util.buildClassName(sourceInfo.getPackageName(), simpleName);
        }

        Log.d(TAG, "parseAnnotationInfo, simpleName: %s, qualifiedName: %s", simpleName, qualifiedName);
        
        AnnotationInfo result = new AnnotationInfo();
        result.setPackageName(sourceInfo.getPackageName());
        result.setSimpleName(classTree.getSimpleName().toString());
        result.setQualifiedName(qualifiedName);
        
        if (classTree.getModifiers().getFlags() != null && classTree.getModifiers().getFlags().size() > 0) {
            for (javax.lang.model.element.Modifier modifier : classTree.getModifiers().getFlags()) {
                result.addModifier(Modifier.convertFromToolsModifier(modifier));
            }
        }
        
        List<? extends Tree> members = classTree.getMembers();
        if (members != null && members.size() > 0) {
            for (Tree member : classTree.getMembers()) {
                Log.d(TAG, "parseAnnotationInfo, annotation member class: %s", member.getClass().getSimpleName());
                
                if (member instanceof JCTree.JCMethodDecl) {
                    JCTree.JCMethodDecl annotationMember = (JCTree.JCMethodDecl) member;
                    Type type = TypeParser.parseType(sourceInfo, annotationMember.getReturnType(), annotationMember.getReturnType().toString());
                    String name = annotationMember.getName().toString();
                    Log.d(TAG, "parseAnnotationInfo, paramName: %s, paramType: %s, paramTypeKind: %s", type, name, annotationMember.getReturnType().getKind());
                    if (annotationMember.getDefaultValue() != null) {
                        String defaultValueLiteral = annotationMember.getDefaultValue().toString();
                        Log.d(TAG, "defaultValueLiteral: %s, defaultValueClass: %s", defaultValueLiteral,
                                annotationMember.getDefaultValue().getClass().getSimpleName());
                        Object defaultValue = VariableInitParser.parseVariableInit(sourceInfo, null, null, (JCTree.JCExpression)annotationMember.getDefaultValue());
                        result.putParams(type, name, defaultValue);
                    } else {
                        result.putParams(type, name, null);
                    }
                    
                }
            }
        }
        
        return result;
    }
}