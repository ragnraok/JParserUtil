package com.ragnarok.jparseutil.incremental.matcher;

import com.ragnarok.jparseutil.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ragnarok on 15/9/13.
 * a Matcher is used to search specific component in source file, such as Annotation Modifier,
 * and find all files contain this component
 */
public abstract class Matcher {
    
    protected List<String> inputFileList;
    
    protected String sourceDirectory;
    protected String[] excludePaths;
    
    public Matcher(String dir, String... excludePaths) {
        this.sourceDirectory = dir;
        this.excludePaths = excludePaths;
        initSourcePathList();
    }
    
    protected void initSourcePathList() {
        if (sourceDirectory == null) {
            return;
        }
        File rootPath = new File(sourceDirectory);
        if (!rootPath.exists()) {
           return;
        }
        inputFileList = new ArrayList<>();
        initJavaSourcePathsRecursive(rootPath);
    }

    private void initJavaSourcePathsRecursive(File rootPath) {
        File[] children = rootPath.listFiles();
        if (children != null && children.length > 0) {
            for (File child : children) {
                if (!isMatchExcludePathList(child.getName(), child.getAbsolutePath())) {
                    if (child.isFile() && child.getAbsolutePath().endsWith(Util.JAVA_FILE_SUFFIX)) {
                        String path = child.getAbsolutePath();
                        inputFileList.add(path);
                    }
                    initJavaSourcePathsRecursive(child);
                }

            }
        }
    }

    protected boolean isMatchExcludePathList(String currentPathName, String absolutePath) {
        if (currentPathName == null || absolutePath == null || excludePaths == null || excludePaths.length == 0) {
            return false;
        }
        for (String excludePath : excludePaths) {
            if (currentPathName.equals(excludePath) || absolutePath.equals(excludePath)) {
                return true;
            }
        }
        return false;
    }
    
    public abstract List<String> match();
}
