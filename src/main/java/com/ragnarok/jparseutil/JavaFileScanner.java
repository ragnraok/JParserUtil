package com.ragnarok.jparseutil;

import com.ragnarok.jparseutil.dataobject.CodeInfo;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by ragnarok on 15/7/23.
 * scan all Java file from a directory
 */
public class JavaFileScanner {
    
    private static final String TAG = "JParserUtil.JavaFileScanner";
    
    private String dir;
    
    private ArrayList<String> allJavaSourcePaths = new ArrayList<>();
    
    private CodeInfo result = new CodeInfo();
    
    public JavaFileScanner(String dir) {
        this.dir = dir;
    } 
    
    public void scanAllJavaSources() throws FileNotFoundException {
        initJavaSourcePaths();
        result = new CodeInfo();
        if (allJavaSourcePaths.size() > 0) {
            for (String path : allJavaSourcePaths) {
                parseJavaSource(path);   
            }
        }
    }
    
    private void parseJavaSource(String filepath) {
        
    }
    
    private void initJavaSourcePaths() throws FileNotFoundException {
        File rootPath = new File(dir);
        if (!rootPath.exists()) {
            throw new FileNotFoundException(String.format("Directory %s not exist!", dir));
        }
        allJavaSourcePaths.clear();
        initJavaSourcePathsRecursive(rootPath);
    }
    
    private void initJavaSourcePathsRecursive(File rootPath) {
        File[] children = rootPath.listFiles();
        if (children != null && children.length > 0) {
            for (File child : children) {
                if (child.isFile() && child.getAbsolutePath().endsWith(Util.JAVA_FILE_SUFFIX)) {
                    String path = child.getAbsolutePath();
                    allJavaSourcePaths.add(path);
                    Log.d(TAG, "get java source file path: %s", path);
                }
                initJavaSourcePathsRecursive(child);
            }
        }
    }
}
