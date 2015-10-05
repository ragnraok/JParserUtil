package com.ragnarok.jparseutil.filescanner;

import com.ragnarok.jparseutil.SourceInfoExtracter;
import com.ragnarok.jparseutil.dataobject.CodeInfo;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ragnarok on 15/8/23.
 * the base Java file scanner, which provide basic interface to 
 * parse a set of Java source files
 */
public abstract class JavaFileScanner {
    
    public static final String TAG = "JParserUtil.JavaFileScanner";
    
    protected String sourceDirectory;

    protected List<String> allJavaSourcePaths = new ArrayList<>();

    protected CodeInfo result = new CodeInfo();
    
    private List<String> excludePathList = new ArrayList<>();

    public JavaFileScanner(String dir) {
        this.sourceDirectory = dir;
    }
    
    public JavaFileScanner(List<String> paths) {
        allJavaSourcePaths = new ArrayList<>();
        allJavaSourcePaths.addAll(paths);
    }
    
    public void addExcludePath(String path) {
        excludePathList.add(path);
    }

    /**
     * parse all Java source files contains in {@link #sourceDirectory}
     * @return a CodeInfo object contain the information of these Java source files
     * @throws FileNotFoundException
     */
    public abstract CodeInfo scanAllJavaSources() throws FileNotFoundException;

    /**
     * parse a single Java source file
     * @param filepath the path of this Java source file
     */
    protected SourceInfo parseJavaSource(String filepath) {
        Log.i(TAG, "parsing source: %s", filepath);
        SourceInfoExtracter extracter = new SourceInfoExtracter(filepath);
        SourceInfo sourceInfo = extracter.extract();
        return sourceInfo;
    }

    /**
     * collect all Java source files path into {@link #allJavaSourcePaths},
     * must be call before parsing all Java source files
     * @throws FileNotFoundException
     */
    protected void initJavaSourcePaths() throws FileNotFoundException {
        File rootPath = new File(sourceDirectory);
        if (!rootPath.exists()) {
            throw new FileNotFoundException(String.format("Directory %s not exist!", sourceDirectory));
        }
        allJavaSourcePaths.clear();
        initJavaSourcePathsRecursive(rootPath);
    }

    private void initJavaSourcePathsRecursive(File rootPath) {
        File[] children = rootPath.listFiles();
        if (children != null && children.length > 0) {
            for (File child : children) {
                if (!isMatchExcludePathList(child.getName(), child.getAbsolutePath())) {
                    if (child.isFile() && child.getAbsolutePath().endsWith(Util.JAVA_FILE_SUFFIX)) {
                        String path = child.getAbsolutePath();
                        allJavaSourcePaths.add(path);
                    }
                    initJavaSourcePathsRecursive(child);   
                }
                
            }
        }
    }
    
    protected boolean isMatchExcludePathList(String currentPathName, String absolutePath) {
        if (currentPathName == null || absolutePath == null) {
            return false;
        }
        for (String excludePath : excludePathList) {
            if (currentPathName.equals(excludePath) || absolutePath.equals(excludePath)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getAllSourceFilePathFromDirectory(String directory, String... excludePaths) {
        List<String> result = new ArrayList<>();
        File rootPath = new File(directory);
        if (!rootPath.exists()) {
            return result;
        }
        initSourceFilePathListRecursive(result, rootPath, excludePaths);
        return result;
    }
    
    private static void initSourceFilePathListRecursive(List<String> result, File rootPath, String[] excludePath) {
        File[] children = rootPath.listFiles();
        if (children != null && children.length > 0) {
            for (File child : children) {
                if (!isMathExcludePath(excludePath, child.getName(), child.getAbsolutePath())
                        && child.isFile() && child.getAbsolutePath().endsWith(Util.JAVA_FILE_SUFFIX)) {
                    String path = child.getAbsolutePath();
                    result.add(path);
                }
                initSourceFilePathListRecursive(result, child, excludePath);
            }
        }
    }
    
    private static boolean isMathExcludePath(String[] excludePathList, String currentPathName, String absolutePath) {
        if (currentPathName == null || absolutePath == null) {
            return false;
        }
        for (String excludePath : excludePathList) {
            if (currentPathName.equals(excludePath) || absolutePath.equals(excludePath)) {
                return true;
            }
        }
        return false;
    }
}
