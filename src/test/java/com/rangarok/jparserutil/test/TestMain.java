package com.rangarok.jparserutil.test;

import com.ragnarok.jparseutil.dataobject.AnnotationModifier;
import com.ragnarok.jparseutil.dataobject.CodeInfo;
import com.ragnarok.jparseutil.dataobject.ReferenceSourceMap;
import com.ragnarok.jparseutil.filescanner.JavaFileScanner;
import com.ragnarok.jparseutil.filescanner.MultiThreadJavaFileScanner;
import com.ragnarok.jparseutil.filescanner.SimpleJavaFileScanner;
import com.ragnarok.jparseutil.memberparser.AnnotationModifierParser;
import com.ragnarok.jparseutil.util.Log;

import java.io.FileNotFoundException;

/**
 * Created by ragnarok on 15/5/24.
 */
public class TestMain {
    
    public static final String TAG = "JParserUtil.TestMain";

    public static void main(String[] args) {
        Log.setMaxLogLevel(Log.DEBUG);
        Log.addShowLogTAG(MultiThreadJavaFileScanner.TAG);
        Log.addShowLogTAG(TestMain.TAG);
//        Log.addShowLogTAG(SourceTreeVisitor.TAG);
//        Log.addShowLogTAG(ClassTreeVisitor.TAG);
//        Log.addShowLogTAG(JavaFileScanner.TAG);
//        Log.addShowLogTAG(AnnotationModifierParser.TAG);
//        Log.addShowLogTAG(AnnotationModifierParser.TAG);
//        Log.addShowLogTAG(AnnotationModifier.TAG);
//        Log.addShowLogTAG(ReferenceSourceMap.TAG);

        String dir = "testsource";
        String sourceMapFile = "testsource/android-22.txt";
        
        try {
            ReferenceSourceMap.getInstance().initWithSourceMapFile(sourceMapFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        long startTime = System.currentTimeMillis();
        try {
            CodeInfo.reset();
            JavaFileScanner fileScanner = new MultiThreadJavaFileScanner(dir, 4);
            fileScanner.addExcludePath("gen");
            fileScanner.addExcludePath("pre-compile-tools");
            CodeInfo codeInfo = fileScanner.scanAllJavaSources();
            CodeInfo.markParseFinish();
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
