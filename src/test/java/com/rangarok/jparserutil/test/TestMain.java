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
        
        long initSourceMapStartTime = System.currentTimeMillis();
        String sourceMapFile = "testsource/android-22.txt";
        try {
            ReferenceSourceMap.getInstance().initWithSourceMapFile(sourceMapFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        long initSourceMapEndTime = System.currentTimeMillis();
        Log.d(TAG, "init source map used: %dms", initSourceMapEndTime - initSourceMapStartTime);
        
        String dir = "testsource";
        
        long initPathStartTime = System.currentTimeMillis();
        List<String> allSourceFiles = JavaFileScanner.getAllSourceFilePathFromDirectory(dir,
                "gen", "pre-compile-tools", "buck-out", "buck_gen");
        long initPathEndTime = System.currentTimeMillis();
        Log.d(TAG, "init path used: %dms, size: %d", initPathEndTime - initPathStartTime, allSourceFiles.size());
        
        long matchStartTime = System.currentTimeMillis();
        AnnotationMatcher annotationMatcher = new AnnotationMatcher("PrintMe", allSourceFiles, 4);
        List<String> result = annotationMatcher.match();
        long matchEndTime = System.currentTimeMillis();
        Log.d(TAG, "file list size: %d, match used %dms", result.size(), matchEndTime - matchStartTime);

        CodeInfo parseResult = null;
        CodeInfo.reset();
        IncrementalJavaFileScanner incrementalJavaFileScanner = new IncrementalJavaFileScanner(result, allSourceFiles, 4);
        incrementalJavaFileScanner.addExcludePath("buck_gen");
        incrementalJavaFileScanner.addExcludePath("buck-out");
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
