package com.ragnarok.jparseutil.visitor;

import com.ragnarok.jparseutil.dataobject.*;
import com.ragnarok.jparseutil.dataobject.Modifier;
import com.ragnarok.jparseutil.memberparser.*;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;

import javax.lang.model.element.*;
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
        Log.d(TAG, "inspectClassTree, name: %s, kind: %s", classTree.getSimpleName().toString(), classTree.getKind());
        if (classTree.getKind() == Tree.Kind.CLASS || classTree.getKind() == Tree.Kind.INTERFACE) {
            addClassInfo(classTree);
            inspectAllClassTreeMembers(classTree.getMembers());
        } else if (classTree.getKind() == Tree.Kind.ANNOTATION_TYPE) {
            AnnotationInfo annotationInfo = AnnotationParser.parseAnnotationInfo(currentHandleClassName, sourceInfo, classTree);
            this.sourceInfo.putAnnotaiotn(annotationInfo);
        }
    }
    
    private void addClassInfo(ClassTree classTree) {
        String simpleName = classTree.getSimpleName().toString();
        if (!this.sourceInfo.isContainClass(simpleName)) {
            ClassInfo classInfo = new ClassInfo();
            
            classInfo.setSimpleName(simpleName);
            
            if (classTree.getKind() == Tree.Kind.INTERFACE) {
                classInfo.setIsInterface(true);
            } else {
                classInfo.setIsInterface(false);
            }
            
            if (classTree.getModifiers().getFlags() != null && classTree.getModifiers().getFlags().size() > 0) {
                for (javax.lang.model.element.Modifier modifier : classTree.getModifiers().getFlags()) {
                    classInfo.addModifier(Modifier.convertFromToolsModifier(modifier));
                }
            }
            
//            Log.d(TAG, "extends: %s", classTree.getExtendsClause().getClass().getSimpleName());
//            Log.d(TAG, "implements: %s", classTree.getImplementsClause().get(0).getClass().getSimpleName());
            
            if (classTree.getExtendsClause() != null) {
                Type superClass = TypeParser.parseType(sourceInfo, (JCTree) classTree.getExtendsClause(), classTree.getExtendsClause().toString());
                classInfo.setSuperClass(superClass);
            }
            
            if (classTree.getImplementsClause() != null && classTree.getImplementsClause().size() > 0) {
                for (Tree implementTree : classTree.getImplementsClause()) {
                    Type implementType = TypeParser.parseType(sourceInfo, (JCTree) implementTree, implementTree.toString());
                    classInfo.addImplements(implementType);
                }
            }
            
            String qualifiedName = Util.buildClassName(sourceInfo.getPackageName(), simpleName);
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
        String qualifiedName = Util.buildClassName(this.currentHandleClassName, simpleName);
        
        Log.d(TAG, "inspectInnerClass, qualifiedName: %s, currentHandleClassName: %s", qualifiedName, currentHandleClassName);
        
        ClassInfo classInfo = new ClassInfo();
        classInfo.setSimpleName(simpleName);
        classInfo.setQualifiedName(qualifiedName);
        sourceInfo.addClassInfo(classInfo);
        
        currentHandleClassName = qualifiedName;

    }
}
