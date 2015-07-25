package com.rangarok.jparserutil.test;

import com.ragnarok.jparseutil.JavaFileScanner;
import com.ragnarok.jparseutil.SourceInfoExtracter;
import com.ragnarok.jparseutil.dataobject.AnnotationInfo;
import com.ragnarok.jparseutil.dataobject.ClassInfo;
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
            System.out.println(codeInfo);
            
            codeInfo.arrangeAnnotationByPackage();
            codeInfo.arrangeClassByPackage();

//            ClassInfo classInfo = codeInfo.getClassByQualifiedName("com.rangnarok.testsouce.User");
//            System.out.println(classInfo + "\n");
//
//            AnnotationInfo annotationInfo = codeInfo.getAnnotationByQualifiedName("com.rangnarok.testsouce.annotation.PrintMe");
//            System.out.println(annotationInfo + "\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }
}
