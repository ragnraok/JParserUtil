package com.rangarok.jparserutil.test;

import com.ragnarok.jparseutil.filescanner.JavaFileScanner;
import com.ragnarok.jparseutil.filescanner.MultiThreadJavaFileScanner;
import com.ragnarok.jparseutil.filescanner.SimpleJavaFileScanner;
import com.ragnarok.jparseutil.dataobject.CodeInfo;
import com.ragnarok.jparseutil.dataobject.ReferenceSourceMap;
import com.ragnarok.jparseutil.memberparser.ArrayParser;
import com.ragnarok.jparseutil.memberparser.MethodParser;
import com.ragnarok.jparseutil.memberparser.TypeParser;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.visitor.ClassTreeVisitor;

import java.io.FileNotFoundException;

/**
 * Created by ragnarok on 15/5/24.
 */
public class TestMain {
    
    public static final String TAG = "JParserUtil.TestMain";

    public static void main(String[] args) {
        Log.setMaxLogLevel(Log.DEBUG);
        Log.addShowLogTAG(SimpleJavaFileScanner.TAG);
        Log.addShowLogTAG(JavaFileScanner.TAG);
        Log.addShowLogTAG(MultiThreadJavaFileScanner.TAG);
        Log.addShowLogTAG(TestMain.TAG);

        String dir = "/Users/ragnarok/adt-bundle-mac-x86_64-20131030/sdk/sources/android-22";
        String sourceMapFile = "testsource/android-22.txt";
        
        long startTime = System.currentTimeMillis();
        try {
            JavaFileScanner fileScanner = new MultiThreadJavaFileScanner(dir, 4);
            ReferenceSourceMap.getInstance().initWithSourceMapFile(sourceMapFile);
            ReferenceSourceMap.getInstance().prepare();
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
