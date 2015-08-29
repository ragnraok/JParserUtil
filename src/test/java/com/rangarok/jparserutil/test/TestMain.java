package com.rangarok.jparserutil.test;

import com.ragnarok.jparseutil.dataobject.CodeInfo;
import com.ragnarok.jparseutil.dataobject.ReferenceSourceMap;
import com.ragnarok.jparseutil.filescanner.JavaFileScanner;
import com.ragnarok.jparseutil.filescanner.MultiThreadJavaFileScanner;
import com.ragnarok.jparseutil.util.Log;

import java.util.regex.Pattern;

/**
 * Created by ragnarok on 15/5/24.
 */
public class TestMain {
    
    public static final String TAG = "JParserUtil.TestMain";

    public static void main(String[] args) {
        Log.setMaxLogLevel(Log.DEBUG);
        Log.addShowLogTAG(MultiThreadJavaFileScanner.TAG);
        Log.addShowLogTAG(TestMain.TAG);

        String dir = "testsource";
        String sourceMapFile = "testsource/android-22.txt";
        
        long startTime = System.currentTimeMillis();
        try {
            JavaFileScanner fileScanner = new MultiThreadJavaFileScanner(dir, 4);
            fileScanner.addExcludePath("gen");
            fileScanner.addExcludePath("pre-compile-tools");
            ReferenceSourceMap.getInstance().initWithSourceMapFile(sourceMapFile);
            CodeInfo codeInfo = fileScanner.scanAllJavaSources();
//            System.out.println(codeInfo);
            
            codeInfo.arrangeAnnotationByPackage();
            codeInfo.arrangeClassByPackage();
            codeInfo.arrangeAnnotatedObjects();
            
            
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        long endTime = System.currentTimeMillis();
        Log.d(TAG, "totally used %dms", endTime - startTime);
    }
}
