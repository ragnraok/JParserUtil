package com.ragnarok.jparseutil.visitor;

import com.ragnarok.jparseutil.dataobject.ClassInfo;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.util.ClassNameUtil;
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
    
    private SourceTreeVisitor sourceTreeVisitor;
    
    private String currentHandleClassName = null;
    
    public ClassTreeVisitor() {
    }
    
    public void inspectClassTress(SourceTreeVisitor sourceTreeVisitor, ClassTree classTree) {
        this.sourceTreeVisitor = sourceTreeVisitor;
        Log.d(TAG, "inspectClassTree, name: %s", classTree.getSimpleName().toString());
        if (classTree.getKind() == Tree.Kind.CLASS) {
            addClassInfo(classTree.getSimpleName().toString());
            inspectAllClassTreeMembers(classTree.getMembers());
        }
    }
    
    private void addClassInfo(String simpleName) {
        if (!this.sourceTreeVisitor.getParseResult().isContainClass(simpleName)) {
            ClassInfo classInfo = new ClassInfo();
            
            classInfo.setSimpleName(simpleName);
            
            String qualifiedName = ClassNameUtil.buildClassName(sourceTreeVisitor.getParseResult().getPackageName(), simpleName);
            classInfo.setQualifiedName(qualifiedName);

            Log.d(TAG, "addClassInfo, simpleName: %s, qualifiedName: %s", simpleName, qualifiedName);

            sourceTreeVisitor.getParseResult().addClassInfo(classInfo);
            
            currentHandleClassName = qualifiedName;
        }
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
        String simpleName = classDecl.getSimpleName().toString();
        String qualifiedName = ClassNameUtil.buildClassName(this.currentHandleClassName, simpleName);
        
        Log.d(TAG, "inspectInnerClass, qualifiedName: %s, currentHandleClassName: %s", qualifiedName, currentHandleClassName);
        
        ClassInfo classInfo = new ClassInfo();
        classInfo.setSimpleName(simpleName);
        classInfo.setQualifiedName(qualifiedName);
        sourceTreeVisitor.getParseResult().addClassInfo(classInfo);
        
        currentHandleClassName = qualifiedName;

    }
}
