package com.ragnarok.jparseutil.filescanner;

import com.ragnarok.jparseutil.dataobject.CodeInfo;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ragnarok on 15/9/30.
 * The incremental file scanner implementation, it will start parsing from a small set of 
 * Java files, and will increase the file set in the whole parsing process according to the 
 * imports or the reference relationship in the source file
 */
public class IncrementalJavaFileScanner extends JavaFileScanner {
    
    public static final String TAG = "JParserUtil.IncrementalJavaFileScanner";
    
    private String sourceStartDirectory = null;
    private List<String> parsingJavaSourcePaths = new ArrayList<>();

    public IncrementalJavaFileScanner(List<String> paths, String sourceStartDirectory) {
        super(paths);
        this.sourceStartDirectory = sourceStartDirectory;
    }

    private void initParsingJavaSourcePaths() throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        File rootPath = new File(sourceStartDirectory);
        if (!rootPath.exists()) {
            throw new FileNotFoundException(String.format("Directory %s not exist!", sourceDirectory));
        }
        parsingJavaSourcePaths.clear();
        initJavaSourcePathsRecursive(rootPath);
        long endTime = System.currentTimeMillis();
        Log.d(TAG, "initParsingJavaSourcePaths used: %dms", endTime - startTime);
    }

    private void initJavaSourcePathsRecursive(File rootPath) {
        File[] children = rootPath.listFiles();
        if (children != null && children.length > 0) {
            for (File child : children) {
                if (!isMatchExcludePathList(child.getName(), child.getAbsolutePath())) {
                    if (child.isFile() && child.getAbsolutePath().endsWith(Util.JAVA_FILE_SUFFIX)) {
                        String path = child.getAbsolutePath();
                        parsingJavaSourcePaths.add(path);
                    }
                    initJavaSourcePathsRecursive(child);
                }

            }
        }
    }
    
    private void addPathListInSamePackage() {
        long startTime = System.currentTimeMillis();
        Set<String> addFileList = new HashSet<>();
        for (String filePath : allJavaSourcePaths) {
            File file = new File(filePath);
            File parentPath = file.getParentFile();
            if (parentPath != null && parentPath.listFiles() != null) {
                for (File path : parentPath.listFiles()) {
                    String fileName = path.getName();
                    String absolutePath = path.getAbsolutePath();
                    if (!isMatchExcludePathList(fileName, absolutePath) && 
                            absolutePath.endsWith(Util.JAVA_FILE_SUFFIX) && !allJavaSourcePaths.contains(absolutePath)) {
                        addFileList.add(absolutePath);
                    }
                }
            }
        }
        allJavaSourcePaths.addAll(addFileList);
        long endTime = System.currentTimeMillis();
        Log.d(TAG, "addPathListInSamePackage used: %dms", endTime - startTime);
    }

    @Override
    public CodeInfo scanAllJavaSources() throws FileNotFoundException {
        addPathListInSamePackage();
        initParsingJavaSourcePaths();
        Log.i(TAG, "final source path list size: %d", allJavaSourcePaths.size());
        CodeInfo result = new CodeInfo();
        for (String file : allJavaSourcePaths) {
            SourceInfo sourceInfo = parseJavaSource(file);
            if (sourceInfo != null) {
                result.addSource(sourceInfo);
            }
            
            // get file list from 'import *'
            List<String> importAsteriskFiles = getFileListFromImportAsterisk(sourceInfo);
            if (importAsteriskFiles != null && importAsteriskFiles.size() > 0) {
                for (String importAsteriskFilePath : importAsteriskFiles) {
//                        Log.d(TAG, "parsing asterisk file: %s", importAsteriskFilePath);
                    SourceInfo asteriskSourceInfo = parseJavaSource(importAsteriskFilePath);
                    if (asteriskSourceInfo != null) {
                        result.addSource(asteriskSourceInfo);
                    }
                }
            }
        }
        
        return result;
    }
    
    private List<String> getFileListFromImportAsterisk(SourceInfo sourceInfo) {
        List<String> result = new ArrayList<>();
        if (sourceStartDirectory == null) {
            return result;
        }
        for (String importClass : sourceInfo.getImports()) {
            if (importClass.endsWith(".*")) { // the goddamn import *
                String filePath = importClass.substring(0, importClass.lastIndexOf(".*"));
                filePath = filePath.replace(".", "/");
                List<String> paths = searchMatchFilePath(filePath);
                if (paths != null && paths.size() > 0) {
                    result.addAll(paths);
                }
            }
        }
        return result;
    }
    
    private List<String> searchMatchFilePath(String path) {
        List<String> result = new ArrayList<>();
        for (String filePath : parsingJavaSourcePaths) {
            if (filePath.contains(path)) {
                result.add(filePath);
            }
        }
        return result;
    }
}
