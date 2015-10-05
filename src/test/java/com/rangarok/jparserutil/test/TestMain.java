package com.rangarok.jparserutil.test;

import com.ragnarok.jparseutil.dataobject.CodeInfo;
import com.ragnarok.jparseutil.dataobject.ReferenceSourceMap;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.filescanner.IncrementalJavaFileScanner;
import com.ragnarok.jparseutil.filescanner.JavaFileScanner;
import com.ragnarok.jparseutil.filescanner.MultiThreadJavaFileScanner;
import com.ragnarok.jparseutil.incremental.matcher.AnnotationMatcher;
import com.ragnarok.jparseutil.incremental.matcher.Matcher;
import com.ragnarok.jparseutil.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by ragnarok on 15/5/24.
 */
public class TestMain {
    
    public static final String TAG = "JParserUtil.TestMain";

    public static void main(String[] args) {
        Log.setMaxLogLevel(Log.DEBUG);
        Log.addShowLogTAG(TestMain.TAG);
        Log.addShowLogTAG(IncrementalJavaFileScanner.TAG);
        Log.addShowLogTAG(SourceInfo.TAG);

        long startTime = System.currentTimeMillis();

        String sourceMapFile = "testsource/android-22.txt";

        try {
            ReferenceSourceMap.getInstance().initWithSourceMapFile(sourceMapFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        String dir = "testsource";
        
        AnnotationMatcher annotationMatcher = new AnnotationMatcher("PrintMe", dir, 
                "gen", "pre-compile-tools", "buck-out");
        List<String> result = annotationMatcher.match();
        Log.d(TAG, "file list size: %d", result.size());

        CodeInfo parseResult = null;
        CodeInfo.reset();
        IncrementalJavaFileScanner incrementalJavaFileScanner = new IncrementalJavaFileScanner(result, dir, 4);
        incrementalJavaFileScanner.addExcludePath("buck_gen");
        incrementalJavaFileScanner.addExcludePath("gen");
        incrementalJavaFileScanner.addExcludePath("pre-compile-tools");
        try {
            parseResult = incrementalJavaFileScanner.scanAllJavaSources();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        CodeInfo.markParseFinish();

        long endTime = System.currentTimeMillis();
        Log.d(TAG, "totally used %dms", endTime - startTime);
        
        System.out.println(parseResult);
    }
}
