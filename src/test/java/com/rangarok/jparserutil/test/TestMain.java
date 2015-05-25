package com.rangarok.jparserutil.test;

import com.ragnarok.jparseutil.JavaSourceReader;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.memberparser.VariableParser;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.visitor.SourceTreeVisitor;
import com.sun.source.tree.CompilationUnitTree;

/**
 * Created by ragnarok on 15/5/24.
 */
public class TestMain {
    
    private static final String TAG = "JParserUtil.TestMain";

    public static void main(String[] args) {
        String filePath = "test/User.java"; // this should be the path to User.java
        
        JavaSourceReader sourceReader = new JavaSourceReader(filePath);
        Iterable<? extends CompilationUnitTree> parseResult = sourceReader.readSource();
        
        Log.d(TAG, "currentDir: %s", System.getProperty("user.dir"));

        SourceTreeVisitor sourceTreeVisitor = new SourceTreeVisitor();
        for (CompilationUnitTree compilationUnitTree : parseResult) {
            compilationUnitTree.accept(sourceTreeVisitor, null);
        }
        SourceInfo sourceInfo = sourceTreeVisitor.getParseResult();
        sourceInfo = VariableParser.updateAllVariableTypeAfterParse(sourceInfo);
        
    }
}
