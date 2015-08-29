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
    
    public static final String TAG = "JParserUtil.AnnotationModifierParser";
    
    private static final String ANNOTATION_DEFAULT_ARG_NAME = "value";
    
    public static AnnotationModifier parseAnnotation(SourceInfo sourceInfo, JCTree.JCAnnotation annotation) {
        AnnotationModifier result = new AnnotationModifier();
        
        String name = annotation.annotationType.toString();
        name = Util.parseTypeFromSourceInfo(sourceInfo, name);
        result.setAnnotationName(name);
        
        Log.d(TAG, "parseAnnotation, name: %s", name);
        
        if (annotation.args != null && annotation.args.size() > 0) {
            for (JCTree.JCExpression expression : annotation.args) {
                Log.d(TAG, "parseAnnotation, arg type: %s", expression.getClass().getSimpleName());
                
                String argName = ANNOTATION_DEFAULT_ARG_NAME;
                JCTree.JCExpression valueExpression = expression;
                if (expression instanceof JCTree.JCAssign) {
                    JCTree.JCAssign assign = (JCTree.JCAssign) expression;
                    argName = assign.lhs.toString();
                    valueExpression = assign.rhs;
                }
                
                Object value;
                if (expression instanceof JCTree.JCLiteral) { // for primitive type
                    value = Util.getValueFromLiteral((JCTree.JCLiteral) expression);
                    if (value != null) {
                        result.putNameValue(argName, value);
                    }
                } else {
                    value = VariableInitParser.parseVariableInit(sourceInfo, null, null, valueExpression);
                    if (value != null) {
                        result.putNameValue(argName, value);
                    }
                }
                
                Log.d(TAG, "parseAnnotation, argName: %s, value: %s", argName, value);
            }
        }
        
        return result;
    }
    
    
}
