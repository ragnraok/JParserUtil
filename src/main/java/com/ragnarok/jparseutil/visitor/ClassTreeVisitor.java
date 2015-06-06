package com.ragnarok.jparseutil.visitor;

import com.ragnarok.jparseutil.dataobject.*;
import com.ragnarok.jparseutil.memberparser.AnnotationModifierParser;
import com.ragnarok.jparseutil.memberparser.MethodParser;
import com.ragnarok.jparseutil.memberparser.VariableParser;
import com.ragnarok.jparseutil.util.ClassNameUtil;
import com.ragnarok.jparseutil.util.Log;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ragnarok on 15/5/24.
 * the ClassTreeVisitor, inspect {@link com.sun.source.tree.ClassTree} and
 * extract info from a class
 */
public class ClassTreeVisitor {
    
    private static final String TAG = "JParserUtil.ClassTreeVisitor";
    
    private SourceInfo sourceInfo;
    
    private String currentHandleClassName = null;
    
    
    public ClassTreeVisitor() {
    }
    
    public void inspectClassTress(SourceInfo sourceInfo, ClassTree classTree) {
        this.sourceInfo = sourceInfo;
        Log.d(TAG, "inspectClassTree, name: %s", classTree.getSimpleName().toString());
        if (classTree.getKind() == Tree.Kind.CLASS) {
            addClassInfo(classTree);
            inspectAllClassTreeMembers(classTree.getMembers());
        }
    }
    
    private void addClassInfo(ClassTree classTree) {
        String simpleName = classTree.getSimpleName().toString();
        
        if (!this.sourceInfo.isContainClass(simpleName)) {
            ClassInfo classInfo = new ClassInfo();
            
            classInfo.setSimpleName(simpleName);
            
            String qualifiedName = ClassNameUtil.buildClassName(sourceInfo.getPackageName(), simpleName);
            classInfo.setQualifiedName(qualifiedName);

            Log.d(TAG, "addClassInfo, simpleName: %s, qualifiedName: %s", simpleName, qualifiedName);
            
            classInfo = parseClassAnnotation(classTree, classInfo);

            sourceInfo.addClassInfo(classInfo);
            
            currentHandleClassName = qualifiedName;
        }
    }
    
    private ClassInfo parseClassAnnotation(ClassTree classTree, ClassInfo classInfo) {
        if (classTree.getModifiers().getAnnotations() != null && classTree.getModifiers().getAnnotations().size() > 0) {
            for (AnnotationTree annotationTree : classTree.getModifiers().getAnnotations()) {
                if (annotationTree instanceof JCTree.JCAnnotation) {
                    JCTree.JCAnnotation annotation = (JCTree.JCAnnotation) annotationTree;
                    AnnotationModifier annotationModifier = AnnotationModifierParser.parseAnnotation(sourceInfo, annotation);
                    classInfo.putAnnotation(annotationModifier);

                }
            }
        }
        return classInfo;
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
        Log.d(TAG, "inspectVariable, class: %s", currentHandleClassName);
        VariableInfo variableInfo = VariableParser.parseVariable(sourceInfo, variableDecl);
        ClassInfo classInfo = sourceInfo.getClassInfoByQualifiedName(currentHandleClassName);
        if (classInfo != null) {
            variableInfo.setContainedClass(classInfo);
            classInfo.addVariable(variableInfo);
            
            sourceInfo.updateClassInfoByQualifiedName(currentHandleClassName, classInfo);
        }
    }
    
    private void inspectMethod(JCTree.JCMethodDecl methodDecl) {
//        Log.d(TAG, "inspectMethod, name: %s, currentHandleClass: %s", methodDecl.getName(), currentHandleClassName);
        ClassInfo classInfo = sourceInfo.getClassInfoByQualifiedName(currentHandleClassName);
        MethodInfo methodInfo = MethodParser.parseMethodInfo(classInfo, sourceInfo, methodDecl);
        if (methodInfo != null) {
            classInfo.putMethod(methodInfo);
            
            sourceInfo.updateClassInfoByQualifiedName(currentHandleClassName, classInfo);
        }
    }
    
    private void inspectInnerClass(JCTree.JCClassDecl classDecl) {
        String simpleName = classDecl.getSimpleName().toString();
        String qualifiedName = ClassNameUtil.buildClassName(this.currentHandleClassName, simpleName);
        
        Log.d(TAG, "inspectInnerClass, qualifiedName: %s, currentHandleClassName: %s", qualifiedName, currentHandleClassName);
        
        ClassInfo classInfo = new ClassInfo();
        classInfo.setSimpleName(simpleName);
        classInfo.setQualifiedName(qualifiedName);
        sourceInfo.addClassInfo(classInfo);
        
        currentHandleClassName = qualifiedName;

    }
}
