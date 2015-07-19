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
    
//    private String currentHandleClassName = null;
    
    private String outerClassName = null; // fully qualified
    
    private String currentClassName = null; // fully qualified
    
    public ClassTreeVisitor() {
    }
    
    public void inspectClassTress(SourceInfo sourceInfo, ClassTree classTree, String outerClassName, boolean ignoreSelf) {
        this.sourceInfo = sourceInfo;
        this.outerClassName = outerClassName;
        Log.d(TAG, "inspectClassTree, name: %s, kind: %s, outerClassName: %s", classTree.getSimpleName().toString(), classTree.getKind(), outerClassName);
        if (classTree.getKind() == Tree.Kind.CLASS || classTree.getKind() == Tree.Kind.INTERFACE) {
            if (!ignoreSelf) {
                addClassInfo(classTree);
            }
            inspectAllClassTreeMembers(classTree.getMembers());
        } else if (classTree.getKind() == Tree.Kind.ANNOTATION_TYPE) {
            AnnotationInfo annotationInfo = AnnotationParser.parseAnnotationInfo(this.outerClassName, sourceInfo, classTree);
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
            String qualifiedName;
            if (outerClassName != null) {
                qualifiedName = Util.buildClassName(outerClassName, simpleName);
            } else {
                qualifiedName = Util.buildClassName(sourceInfo.getPackageName(), simpleName);
            }
             
            classInfo.setQualifiedName(qualifiedName);
            
            currentClassName = qualifiedName;

            Log.d(TAG, "addClassInfo, simpleName: %s, qualifiedName: %s", simpleName, qualifiedName);
            
            classInfo = parseClassAnnotation(classTree, classInfo);

            sourceInfo.addClassInfo(classInfo);
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
        Log.d(TAG, "inspectVariable, class: %s", outerClassName);
        VariableInfo variableInfo = VariableParser.parseVariable(sourceInfo, variableDecl);
        ClassInfo classInfo = sourceInfo.getClassInfoByQualifiedName(outerClassName);
        if (classInfo != null) {
            variableInfo.setContainedClass(classInfo);
            classInfo.addVariable(variableInfo);
            
            sourceInfo.updateClassInfoByQualifiedName(outerClassName, classInfo);
        }
    }
    
    private void inspectMethod(JCTree.JCMethodDecl methodDecl) {
        ClassInfo classInfo = sourceInfo.getClassInfoByQualifiedName(outerClassName);
        MethodInfo methodInfo = MethodParser.parseMethodInfo(classInfo, sourceInfo, methodDecl);
        if (methodInfo != null) {
            classInfo.putMethod(methodInfo);
            
            sourceInfo.updateClassInfoByQualifiedName(outerClassName, classInfo);
        }
    }
    
    private void inspectInnerClass(JCTree.JCClassDecl classDecl) {
        String simpleName = classDecl.getSimpleName().toString();
        String qualifiedName = null;
        if (outerClassName != null) {
            qualifiedName  = Util.buildClassName(this.outerClassName, simpleName);
        } else {
            qualifiedName = Util.buildClassName(this.currentClassName, simpleName);
        }
        
        
        Log.d(TAG, "inspectInnerClass, qualifiedName: %s, outerClassName: %s, currentClassName: %s", qualifiedName, outerClassName, currentClassName);
        
        ClassInfo classInfo = new ClassInfo();
        classInfo.setSimpleName(simpleName);
        classInfo.setQualifiedName(qualifiedName);
        sourceInfo.addClassInfo(classInfo);
        
        // recursive parse
        new ClassTreeVisitor().inspectClassTress(sourceInfo, classDecl, qualifiedName, true);
    }
}
