package com.ragnarok.jparseutil.visitor;

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.ragnarok.jparseutil.dataobject.*;
import com.ragnarok.jparseutil.memberparser.*;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;
import sun.org.mozilla.javascript.internal.ast.VariableDeclaration;

import java.util.List;

/**
 * Created by ragnarok on 15/5/24.
 * the ClassTreeVisitor, inspect {@link com.sun.source.tree.ClassTree} and
 * extract info from a class
 */
public class ClassTreeVisitor {
    
    public static final String TAG = "JParserUtil.ClassTreeVisitor";
    
    private SourceInfo sourceInfo;
    
//    private String currentHandleClassName = null;
    
    private String outerClassName = null; // fully qualified
    
    private String currentClassName = null; // fully qualified
    
    public ClassTreeVisitor() {
    }
    
    public void inspectTypeDeclaration(SourceInfo sourceInfo, TypeDeclaration typeDeclaration, String outerClassName, boolean ignoreSelf) {
        if (typeDeclaration == null) {
            return;
        }
        this.sourceInfo = sourceInfo;
        this.outerClassName = outerClassName;
        Log.d(TAG, "inspectClassTree, name: %s, kind: %s, outerClassName: %s", 
                typeDeclaration.getName(), typeDeclaration.getClass().getName(), outerClassName);
        if (typeDeclaration instanceof ClassOrInterfaceDeclaration || typeDeclaration instanceof EnumDeclaration
                || typeDeclaration instanceof AnnotationDeclaration) {
            if (!ignoreSelf) {
                addClassInfo(typeDeclaration);
            } else if (outerClassName != null) {
                currentClassName = Util.buildClassName(outerClassName, typeDeclaration.getName());
            }
            inspectAllClassTreeMembers(typeDeclaration.getMembers());
        } 
//        else if (typeDeclaration instanceof AnnotationDeclaration) {
//            AnnotationDeclaration annotationDeclaration = (AnnotationDeclaration) typeDeclaration;
//            AnnotationInfo annotationInfo = AnnotationParser.parseAnnotationInfo(this.outerClassName, sourceInfo, annotationDeclaration);
//            ReferenceSourceMap.getInstance().addClassNameToSourceMap(annotationInfo.getQualifiedName());
//            this.sourceInfo.putAnnotaiotn(annotationInfo);
//        }
    }
    
    private void addClassInfo(TypeDeclaration typeDeclaration) {
        if (typeDeclaration != null) {
            String simpleName = typeDeclaration.getName();
            ClassInfo classInfo = null;
            if (typeDeclaration instanceof AnnotationDeclaration) {
                classInfo = new ClassInfo();
            } else {
                classInfo = new AnnotationInfo();
            }
            classInfo.setPackageName(this.sourceInfo.getPackageName());
            classInfo.setSimpleName(simpleName);
            
            if (typeDeclaration instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration classOrInterfaceDeclaration = (ClassOrInterfaceDeclaration) typeDeclaration;
                if (classOrInterfaceDeclaration.isInterface()) {
                    classInfo.setIsInterface(true);
                    classInfo.setIsEnum(false);
                    classInfo.setIsAnnotation(false);
                }
            } else if (typeDeclaration instanceof EnumDeclaration) {
                classInfo.setIsInterface(false);
                classInfo.setIsEnum(true);
                classInfo.setIsAnnotation(false);
            } else if (typeDeclaration instanceof AnnotationDeclaration) {
                classInfo.setIsEnum(false);
                classInfo.setIsInterface(false);
                classInfo.setIsAnnotation(true);
            } else {
                classInfo.setIsEnum(false);
                classInfo.setIsInterface(false);
                classInfo.setIsAnnotation(false); 
            }
            
            if (typeDeclaration.getModifiers() != 0) {
                classInfo.addAllModifiers(Modifier.parseModifiersFromFlags(typeDeclaration.getModifiers()));
            }
            
            if (typeDeclaration instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration classDeclaration = (ClassOrInterfaceDeclaration) typeDeclaration;
                if (classDeclaration.getExtends() != null && classDeclaration.getExtends().size() > 0) {
                    Type superClass = TypeParser.parseType(sourceInfo, 
                           classDeclaration.getExtends().get(0), classDeclaration.getExtends().get(0).getName());
                    classInfo.setSuperClass(superClass);
                }

                if (classDeclaration.getImplements() != null && classDeclaration.getImplements().size() > 0) {
                    for (ClassOrInterfaceType type : classDeclaration.getImplements()) {
                        Type implementType = TypeParser.parseType(sourceInfo, type, type.getName());
                        classInfo.addImplements(implementType);
                    }
                }
            }
           
            String qualifiedName;
            if (outerClassName != null) {
                qualifiedName = Util.buildClassName(outerClassName, simpleName);
            } else {
                qualifiedName = Util.buildClassName(sourceInfo.getPackageName(), simpleName);
            }
             
            classInfo.setQualifiedName(qualifiedName);
            ReferenceSourceMap.getInstance().addClassNameToSourceMap(classInfo.getQualifiedName());
            
            currentClassName = qualifiedName;

            Log.d(TAG, "addClassInfo, simpleName: %s, qualifiedName: %s", simpleName, qualifiedName);

            classInfo = parseClassAnnotation(typeDeclaration, classInfo);

            sourceInfo.addClassInfo(classInfo);
            
            if (classInfo.isAnnotaiton() && classInfo instanceof AnnotationInfo) {
                sourceInfo.putAnnotaiotn((AnnotationInfo) classInfo);
            }
        }
    }
    
    private ClassInfo parseClassAnnotation(TypeDeclaration typeDeclaration, ClassInfo classInfo) {
        if (typeDeclaration.getAnnotations() != null && typeDeclaration.getAnnotations().size() > 0) {
            for (AnnotationExpr annotation : typeDeclaration.getAnnotations()) {
                AnnotationModifier annotationModifier = AnnotationModifierParser.parseAnnotation(sourceInfo, annotation);
                classInfo.putAnnotation(annotationModifier);
            }
        }
        return classInfo;
    }
   
    private void inspectAllClassTreeMembers(List<BodyDeclaration> classMembers) {
        for (BodyDeclaration member : classMembers) {
            Log.d(TAG, "member.class: %s", member.getClass().getSimpleName());
            
            if (member instanceof FieldDeclaration) {
                inspectVariable((FieldDeclaration) member);
            } else if (member instanceof MethodDeclaration) {
                inspectMethod((MethodDeclaration) member);
            } else if (member instanceof TypeDeclaration) {
                inspectInnerClass((TypeDeclaration) member);
            } else if (member instanceof AnnotationMemberDeclaration) {
                inspectAnnotationMember((AnnotationMemberDeclaration) member);
            }
        }
    }
    
    private void inspectVariable(FieldDeclaration variableDecl) {
        Log.d(TAG, "inspectVariable, class: %s", currentClassName);
        VariableInfo variableInfo = VariableParser.parseVariable(sourceInfo, variableDecl);
        ClassInfo classInfo = sourceInfo.getClassInfoByQualifiedName(currentClassName);
        if (classInfo != null) {
            variableInfo.setContainedClass(classInfo);
            classInfo.addVariable(variableInfo);
            
            sourceInfo.updateClassInfoByQualifiedName(currentClassName, classInfo);
        }
    }
    
    private void inspectMethod(MethodDeclaration methodDecl) {
        ClassInfo classInfo = sourceInfo.getClassInfoByQualifiedName(currentClassName);
        MethodInfo methodInfo = MethodParser.parseMethodInfo(classInfo, sourceInfo, methodDecl);
        if (methodInfo != null) {
            classInfo.putMethod(methodInfo);
            
            sourceInfo.updateClassInfoByQualifiedName(currentClassName, classInfo);
        }
    }
    
    private void inspectInnerClass(TypeDeclaration classDecl) {
        String simpleName = classDecl.getName();
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
        classInfo.setPackageName(sourceInfo.getPackageName());
        sourceInfo.addClassInfo(classInfo);
        
        // recursive parse
        new ClassTreeVisitor().inspectTypeDeclaration(sourceInfo, classDecl, qualifiedName, true);
    }
    
    private void inspectAnnotationMember(AnnotationMemberDeclaration annotationMember) {
        AnnotationInfo annotationInfo = sourceInfo.getAnnotationInfoByQualifiedName(currentClassName);
        Type type = TypeParser.parseType(sourceInfo, annotationMember.getType(), annotationMember.getType().toString());
        String name = annotationMember.getName();
        Log.d(TAG, "inspectAnnotationMember, paramName: %s, paramType: %s");
        if (annotationMember.getDefaultValue() != null) {
            String defaultValueLiteral = annotationMember.getDefaultValue().toString();
            Log.d(TAG, "defaultValueLiteral: %s, defaultValueClass: %s", defaultValueLiteral,
                    annotationMember.getDefaultValue().getClass().getSimpleName());
            Object defaultValue = VariableInitParser.parseVariableInit(sourceInfo, null, null, annotationMember.getDefaultValue());
            annotationInfo.putParams(type, name, defaultValue);
        } else {
            annotationInfo.putParams(type, name, null);
        }
        sourceInfo.updateClassInfoByQualifiedName(currentClassName, annotationInfo);
        sourceInfo.updateAnnotationByQualifiedName(currentClassName, annotationInfo);
    }
}
