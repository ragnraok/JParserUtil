package com.ragnarok.jparseutil.memberparser;

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
    
    private static final String TAG = "JParserUtil.AnnotationModifierParser";
    
    private static final String ANNOTATION_DEFAULT_ARG_NAME = "value";
    
    public static AnnotationModifier parseAnnotation(SourceInfo sourceInfo, JCTree.JCAnnotation annotation) {
        AnnotationModifier result = new AnnotationModifier();
        
        String name = annotation.annotationType.toString();
        name = Util.parseType(sourceInfo, name);
        result.setAnnotationName(name);
        
        Log.d(TAG, "parseAnnotation, name: %s", name);
        
        if (annotation.args != null && annotation.args.size() > 0) {
            for (JCTree.JCExpression expression : annotation.args) {
                Log.d(TAG, "parseAnnotation, arg type: %s", expression.getClass().getSimpleName());
                
                // currently only support Literal and Assign
                if (expression instanceof JCTree.JCLiteral) {
                    String value = Util.getValueFromLiteral((JCTree.JCLiteral) expression);
                    Log.d(TAG, "parseAnnotation, value: %s", value);
                    if (value != null) {
                        result.putNameValue(ANNOTATION_DEFAULT_ARG_NAME, value);
                    }
                } else if (expression instanceof JCTree.JCAssign) {
                    JCTree.JCAssign assign = (JCTree.JCAssign) expression;
                    Log.d(TAG, "parseAnnotation, lhs: %s, rhs: %s", assign.lhs, assign.rhs);
                    // for assign, just simply store the expression string representation
                    result.putNameValue(assign.lhs.toString(), assign.rhs.toString());
                }
            }
        }
        
        return result;
    }
    
    
}
