package com.ragnarok.jparseutil.filescanner;

import com.ragnarok.jparseutil.SourceInfoExtracter;
import com.ragnarok.jparseutil.dataobject.CodeInfo;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        allJavaSourcePaths = getAllSourceFilePathFromDirectory(sourceDirectory);
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
        try {
            Files.walk(Paths.get(directory)).filter(path -> {
                if (path.getFileName().toString().endsWith(Util.JAVA_FILE_SUFFIX) &&
                        !isMathExcludePath(excludePaths, path.getFileName().toString(), path.toString())) {
                    return true;
                }
                return false;
            }).forEach(path -> result.add(path.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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
