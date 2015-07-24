package com.ragnarok.jparseutil;

import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.dataobject.Type;
import com.ragnarok.jparseutil.memberparser.MethodParser;
import com.ragnarok.jparseutil.memberparser.VariableParser;
import com.ragnarok.jparseutil.visitor.SourceTreeVisitor;
import com.sun.source.tree.CompilationUnitTree;

import java.io.FileNotFoundException;

/**
 * Created by ragnarok on 15/5/17.
 * Extract information from a Java source file
 */
public class SourceInfoExtracter {
    
    private String filePath = null;
    
    private JavaSourceReader sourceReader = null;
    
    public SourceInfoExtracter(String filePath) {
        this.filePath = filePath;
        sourceReader = new JavaSourceReader(this.filePath);
    }
    
    public SourceInfo extract() {
        Iterable<? extends CompilationUnitTree> parseResult = null;
        try {
            parseResult = sourceReader.readSource();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        SourceTreeVisitor sourceTreeVisitor = new SourceTreeVisitor(sourceReader.getFilename());
        for (CompilationUnitTree compilationUnitTree : parseResult) {
            compilationUnitTree.accept(sourceTreeVisitor, null);
        }
        SourceInfo sourceInfo = sourceTreeVisitor.getParseResult();
        Type.setFinalParseResult(sourceInfo);
        
        System.out.println("\n\n");
        
        System.out.println(sourceInfo);
        
        System.out.println("\n\n");
        
        return sourceInfo;
    }
}
