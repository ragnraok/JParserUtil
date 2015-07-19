package com.ragnarok.jparseutil.visitor;

import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.util.Log;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.tree.JCTree;

/**
 * Created by ragnarok on 15/5/18.
 * The Source TreeVisitor, travel a CompilationUnit(a Java source file) and extract its info
 */
public class SourceTreeVisitor extends TreeScanner<Void, Void> {
    
    private static final String TAG = "JParserUtil.SourceTreeVisitor";
    
    private ClassTreeVisitor classVisitor;
    private SourceInfo sourceInfo;
    
    public SourceTreeVisitor() {
        this.sourceInfo = new SourceInfo();
        this.classVisitor = new ClassTreeVisitor();
    }
    
    @Override
    public Void visitImport(ImportTree node, Void aVoid) {
        String classname = node.getQualifiedIdentifier().toString();
        Log.d(TAG, "visitImport, name: %s", classname);
        this.sourceInfo.addImports(classname);
        
        return super.visitImport(node, aVoid);
    }

    @Override
    public Void visitClass(ClassTree node, Void aVoid) {
        Log.d(TAG, "visitClass, name: %s", node.getSimpleName());
        this.classVisitor.inspectClassTress(sourceInfo, node, null, false);
        return null;
    }

    @Override
    public Void visitCompilationUnit(CompilationUnitTree node, Void aVoid) {
        Log.d(TAG, "visitCompilationUnit, packagename: %s", node.getPackageName().toString());
        this.sourceInfo.setPackageName(node.getPackageName().toString());
        return super.visitCompilationUnit(node, aVoid);
    }

    @Override
    public Void visitAnnotation(AnnotationTree node, Void aVoid) {
        Log.d(TAG, "visitAnnotation, name: %s", node.getAnnotationType().toString());
        return super.visitAnnotation(node, aVoid);
    }

    
    public SourceInfo getParseResult() {
        return this.sourceInfo;
    }
    
    
}
