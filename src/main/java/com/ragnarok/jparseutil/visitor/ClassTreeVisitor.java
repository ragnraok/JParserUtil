package com.ragnarok.jparseutil.visitor;

import com.ragnarok.jparseutil.dataobject.ClassInfo;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.util.Log;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;

import java.util.List;

/**
 * Created by ragnarok on 15/5/24.
 * the ClassTreeVisitor, inspect {@link com.sun.source.tree.ClassTree} and
 * extract info from a class
 */
public class ClassTreeVisitor {
    
    private static final String TAG = "JParserUtil.ClassTreeVisitor";
    
    private SourceInfo sourceInfo;
    
    public ClassTreeVisitor() {
    }
    
    public ClassInfo inspectClassTress(SourceInfo sourceInfo, ClassTree classTree) {
        this.sourceInfo = sourceInfo;
        Log.d(TAG, "inspectClassTress, name: %s", classTree.getSimpleName().toString());
        Log.d(TAG, classTree.getKind().toString());
        inspectAllClassTreeMembers(classTree.getMembers());
        
        return new ClassInfo();
    }
    
    private void inspectAllClassTreeMembers(List<? extends Tree> classMembers) {
        for (Tree member : classMembers) {
            Log.d(TAG, "member.class: %s", member.getClass().getSimpleName());
            
            if (member instanceof JCTree.JCVariableDecl) {
                inspectVariable((JCTree.JCVariableDecl) member);
            } else if (member instanceof JCTree.JCMethodDecl) {
                inspectMethod((JCTree.JCMethodDecl) member);
            } else if (member instanceof JCTree.JCClassDecl) {
                inspectInnerClass((JCTree.JCClassDecl) member);
            }
        }
    }
    
    private void inspectVariable(JCTree.JCVariableDecl variableDecl) {
//        Log.d(TAG, "inspectVariable, name:%s, type: %s, init: %s, annotation: %d", 
//                variableDecl.name, variableDecl.vartype, variableDecl.init, variableDecl.getModifiers().annotations.size());
    }
    
    private void inspectMethod(JCTree.JCMethodDecl methodDecl) {
//        Log.d(TAG, "inspectMethod, name: %s,  ");
    }
    
    private void inspectInnerClass(JCTree.JCClassDecl classDecl) {
        
    }
}
