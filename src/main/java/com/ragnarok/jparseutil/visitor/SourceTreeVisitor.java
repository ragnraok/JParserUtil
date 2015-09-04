package com.ragnarok.jparseutil.visitor;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.util.Log;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.tree.JCTree;

import java.util.Objects;

/**
 * Created by ragnarok on 15/5/18.
 * The Source TreeVisitor, travel a CompilationUnit(a Java source file) and extract its info
 */
public class SourceTreeVisitor extends VoidVisitorAdapter<Object> {
    
    public static final String TAG = "JParserUtil.SourceTreeVisitor";
    
    private ClassTreeVisitor classVisitor;
    private SourceInfo sourceInfo;
    
    public SourceTreeVisitor(String filename) {
        Log.d(TAG, "create SourceTreeVisitor, filename: %s", filename);
        this.sourceInfo = new SourceInfo();
        this.sourceInfo.setFilename(filename);
        this.classVisitor = new ClassTreeVisitor();
    }

    @Override
    public void visit(CompilationUnit node, Object arg) {
        if (node == null || node.getPackage() == null || node.getPackage().getName() == null) {
            return;
        }
        Log.d(TAG, "visit CompilationUnit, packageName: %s", node.getPackage().getName());
        sourceInfo.setPackageName(node.getPackage().getName().toString());
        if (node.getImports() == null) {
            super.visit(node, arg);
        }
        for (ImportDeclaration importDeclaration : node.getImports()) {
            Log.d(TAG, "visit CompilationUnit, import: %s", importDeclaration.getName());
            sourceInfo.addImports(importDeclaration.getName().toString());
        }
        super.visit(node, arg);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration node, Object arg) {
        if (node == null) {
            return;
        }
        ensurePackageName(node);
        Log.d(TAG, "visit ClassOrInterfaceDeclaration, name: %s", node.getName());
    }

    @Override
    public void visit(EnumDeclaration node, Object arg) {
        if (node == null) {
            return;
        }
        ensurePackageName(node);
        Log.d(TAG, "visit EnumDeclaration, name: %s", node.getName());
    }

    @Override
    public void visit(AnnotationDeclaration node, Object arg) {
        if (node == null) {
            return;
        }
        ensurePackageName(node);
        Log.d(TAG, "visit AnnotationDeclaration, name: %s", node.getName());
    }
    
    private void ensurePackageName(TypeDeclaration node) {
        if (node == null) {
            return;
        }
        if (sourceInfo.getPackageName() == null && node.getParentNode() instanceof CompilationUnit) {
            CompilationUnit compilationUnit = (CompilationUnit) node.getParentNode();
            sourceInfo.setPackageName(compilationUnit.getPackage().getName().getName());
        }
    }

    public SourceInfo getParseResult() {
        return this.sourceInfo;
    }
    
    
}
