package com.ragnarok.jparseutil.memberparser;

import com.github.javaparser.ast.expr.*;
import com.ragnarok.jparseutil.dataobject.AnnotationModifier;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;
import com.sun.tools.javac.tree.JCTree;

/**
 * Created by ragnarok on 15/5/30.
 * parse annotation modifier
 */
public class AnnotationModifierParser {
    
    public static final String TAG = "JParserUtil.AnnotationModifierParser";
    
    private static final String ANNOTATION_DEFAULT_ARG_NAME = "value";
    
    public static AnnotationModifier parseAnnotation(SourceInfo sourceInfo, AnnotationExpr annotation) {
        AnnotationModifier result = new AnnotationModifier();
        
        String name = annotation.getName().getName();
        name = Util.parseTypeFromSourceInfo(sourceInfo, name);
        result.setAnnotationName(name);
        
        Log.d(TAG, "parseAnnotation, name: %s", name);
        
        if (annotation instanceof SingleMemberAnnotationExpr || annotation instanceof NormalAnnotationExpr) {
            if (annotation instanceof SingleMemberAnnotationExpr) {
                SingleMemberAnnotationExpr annotationExpr = (SingleMemberAnnotationExpr) annotation;
                Expression valueExpr = annotationExpr.getMemberValue();
                Object value = VariableInitParser.parseVariableInit(sourceInfo, null, null, valueExpr);
                Log.d(TAG, "parse SingleMemberAnnotationExpr, valueExpr: %s", valueExpr);
                result.putNameValue(ANNOTATION_DEFAULT_ARG_NAME, value);
            } else {
                NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr) annotation;
                for (MemberValuePair valuePair : normalAnnotationExpr.getPairs()) {
                    String argName = valuePair.getName();
                    Expression argValueExpr = valuePair.getValue();
                    Log.d(TAG, "parse NormalAnnotationExpr, argName: %s, argValueExpr: %s", argName, argValueExpr);
                    Object value = VariableInitParser.parseVariableInit(sourceInfo, null, null, argValueExpr);
                    result.putNameValue(argName, value);
                }
            }
        }
        
        return result;
    }
    
    
}
