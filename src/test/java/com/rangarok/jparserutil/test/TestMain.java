package com.rangarok.jparserutil.test;

import com.ragnarok.jparseutil.JavaSourceReader;
import com.ragnarok.jparseutil.SourceInfoExtracter;
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
        String filePath = "test/AnnotationModifierTest.java"; // this should be the path to User.java
        
        SourceInfoExtracter extracter = new SourceInfoExtracter(filePath);
        extracter.extract();
    }
}
