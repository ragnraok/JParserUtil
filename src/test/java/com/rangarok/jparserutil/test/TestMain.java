package com.rangarok.jparserutil.test;

import com.ragnarok.jparseutil.SourceInfoExtracter;

/**
 * Created by ragnarok on 15/5/24.
 */
public class TestMain {
    
    private static final String TAG = "JParserUtil.TestMain";

    public static void main(String[] args) {
        String filePath = "test/VariableParseTest.java"; // this should be the path to User.java
        
        SourceInfoExtracter extracter = new SourceInfoExtracter(filePath);
        extracter.extract();
    }
}
