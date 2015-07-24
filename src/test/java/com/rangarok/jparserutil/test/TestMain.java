package com.rangarok.jparserutil.test;

import com.ragnarok.jparseutil.JavaFileScanner;
import com.ragnarok.jparseutil.SourceInfoExtracter;
import com.ragnarok.jparseutil.dataobject.CodeInfo;
import com.ragnarok.jparseutil.util.Log;

import java.io.FileNotFoundException;

/**
 * Created by ragnarok on 15/5/24.
 */
public class TestMain {
    
    private static final String TAG = "JParserUtil.TestMain";

    public static void main(String[] args) {
        String dir = "testsource";

        JavaFileScanner fileScanner = new JavaFileScanner(dir);
        try {
            CodeInfo codeInfo = fileScanner.scanAllJavaSources();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }
}
