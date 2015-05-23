package com.ragnarok.jparseutil;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.code.Source;

import java.util.ArrayList;

/**
 * Created by ragnarok on 15/5/18.
 * The Source TreeVisitor, travel a CompilationUnit(a Java source file) and extract its info
 */
public class SourceTreeVisitor extends TreeScanner<Void, Void> {
    
    private static final String TAG = "JParserUtil.SourceTreeVisitor";

    private String packageName = null;
    private ArrayList<String> importClassNames = new ArrayList<String>();
    
    private ClassTreeVisitor classVisitor;
    
    public SourceTreeVisitor() {
        this.classVisitor = new ClassTreeVisitor();
    }
    
    @Override
    public Void visitImport(ImportTree node, Void aVoid) {
        String classname = node.getQualifiedIdentifier().toString();
        Log.d(TAG, "visitImport, name: %s", classname);

        importClassNames.add(classname);
        return super.visitImport(node, aVoid);
    }

    @Override
    public Void visitClass(ClassTree node, Void aVoid) {
        this.classVisitor.inspectClassTress(node);
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
    
    
    
}
