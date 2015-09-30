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

    public IncrementalJavaFileScanner(List<String> paths, String sourceStartDirectory) {
        super(paths);
        this.sourceStartDirectory = sourceStartDirectory;
    }
    
    private void addPathListInSamePackage() {
        Set<String> addFileList = new HashSet<>();
        for (String filePath : allJavaSourcePaths) {
            File file = new File(filePath);
            File parentPath = file.getParentFile();
            if (parentPath != null && parentPath.listFiles() != null) {
                for (File path : parentPath.listFiles()) {
                    File p = path.getAbsoluteFile();
                    if (!isMatchExcludePathList(p.getName(), p.getAbsolutePath()) && 
                            p.getAbsolutePath().endsWith(Util.JAVA_FILE_SUFFIX) && !allJavaSourcePaths.contains(p.getAbsolutePath())) {
                        addFileList.add(p.getAbsolutePath());
                    }
                }
            }
        }
        for (String addFilePath : addFileList) {
            if (!allJavaSourcePaths.contains(addFilePath)) {
                Log.i(TAG, "addPathListInSamePackage, add: %s", addFilePath);
                allJavaSourcePaths.add(addFilePath);
            }
        }
    }

    @Override
    public CodeInfo scanAllJavaSources() throws FileNotFoundException {
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
                File asteriskDirectory = new File(sourceStartDirectory, filePath);
                if (asteriskDirectory.list() != null && asteriskDirectory.list().length > 0) {
                    for (String asteriskFilePath : asteriskDirectory.list()) {
                        if (asteriskFilePath.endsWith(Util.JAVA_FILE_SUFFIX)) {
                            Log.i(TAG, "add new file from import asterisk: %s", asteriskFilePath);
                            result.add(asteriskFilePath);
                        }
                    }
                }
            }
        }
        return result;
    }
}
