package com.ragnarok.jparseutil;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.util.TreeScanner;

/**
 * Created by ragnarok on 15/5/18.
 * The Source TreeVisitor
 */
public abstract class SourceTreeVisitor extends TreeScanner<Void, Void> {
    
    private static final String TAG = "JParserUtilBaseSourceTreeVisitor";

    protected String packageName = null;
    
    @Override
    public Void visitImport(ImportTree node, Void aVoid) {
        Log.d(TAG, "visitImport, name: %s", node.toString());
        onParseImport(node.getQualifiedIdentifier().toString());
        return null;
    }

    @Override
    public Void visitClass(ClassTree node, Void aVoid) {
        return super.visitClass(node, aVoid);
    }

    @Override
    public Void visitCompilationUnit(CompilationUnitTree node, Void aVoid) {
        this.packageName = node.getPackageName().toString();
        Log.d(TAG, "visitCompilationUnit, packagename: %s", this.packageName);
        return super.visitCompilationUnit(node, aVoid);
    }
    
    public String getPackageName() {
        return this.packageName;
    }
    
    protected abstract void onParseImport(String importClassName);
}
