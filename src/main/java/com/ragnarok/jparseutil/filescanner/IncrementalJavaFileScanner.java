package com.ragnarok.jparseutil.filescanner;

import com.ragnarok.jparseutil.SourceInfoExtracter;
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
import java.util.concurrent.*;

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

    private int threadNumber;
    private ThreadPoolExecutor executor;
    private BlockingQueue<Runnable> workerQueue;

    public IncrementalJavaFileScanner(List<String> paths, String sourceStartDirectory, int threadNumber) {
        super(paths);
        this.sourceStartDirectory = sourceStartDirectory;
        this.threadNumber = threadNumber;
        initThreadPool();
    }

    private void initThreadPool() {
        workerQueue = new ArrayBlockingQueue<>(threadNumber * 2);
        executor = new ThreadPoolExecutor(threadNumber, threadNumber * 2, 2L, TimeUnit.SECONDS, workerQueue);
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

    private class ScanSubSetFileRunnable implements Runnable {

        private List<String> subSetFileList;
        private int threadNo;

        private CodeInfo subTaskResult;

        private SourceInfoExtracter extracter = null;

        public ScanSubSetFileRunnable(List<String> subSetFileList, int threadNo) {
            this.subSetFileList = subSetFileList;
            this.threadNo = threadNo;
            subTaskResult = new CodeInfo();
        }

        @Override
        public void run() {
            Log.i(TAG, "thread %d start, subSetFileList.size: %d", threadNo, subSetFileList.size());
            for (String file : subSetFileList) {
                SourceInfo sourceInfo = parseJavaSource(file);
                if (sourceInfo != null) {
                    subTaskResult.addSource(sourceInfo);
                }

                // get file list from 'import *'
                List<String> importAsteriskFiles = getFileListFromImportAsterisk(sourceInfo);
                if (importAsteriskFiles != null && importAsteriskFiles.size() > 0) {
                    Log.d(TAG, "importAsteriskFiles: %s", importAsteriskFiles);
                    for (String importAsteriskFilePath : importAsteriskFiles) {
                        if (!subTaskResult.isContainedSource(importAsteriskFilePath)) {
                            SourceInfo asteriskSourceInfo = parseJavaSource(importAsteriskFilePath);
                            if (asteriskSourceInfo != null) {
                                subTaskResult.addSource(asteriskSourceInfo);
                            }   
                        }
                    }
                }
            }
            Log.i(TAG, "thread %d parsing finished", threadNo);
        }

        public CodeInfo getSubTaskResult() {
            return subTaskResult;
        }
    }

    @Override
    public CodeInfo scanAllJavaSources() throws FileNotFoundException {
        initParsingJavaSourcePaths();
        Log.i(TAG, "final source path list size: %d", allJavaSourcePaths.size());
        CodeInfo result = new CodeInfo();
        if (executor == null || executor.isShutdown()) {
            initThreadPool();
        }

        int sourceSetSize = allJavaSourcePaths.size();
        int pieceSize = sourceSetSize / threadNumber;

        Log.i(TAG, "start scan, threadNumber: %d, sourceSetSize: %d, pieceSize: %d", threadNumber, sourceSetSize, pieceSize);

        int currentStartIndex = 0;

        Future[] futureList = new Future[threadNumber];
        ScanSubSetFileRunnable[] runnableList = new ScanSubSetFileRunnable[threadNumber];

        for (int i = 0; i < threadNumber; i++) {
            int startIndex = currentStartIndex > allJavaSourcePaths.size() ? allJavaSourcePaths.size() - 1 : currentStartIndex;
            int endIndex = startIndex + pieceSize > allJavaSourcePaths.size() ? allJavaSourcePaths.size() - 1 : startIndex + pieceSize;

            if (i == threadNumber -1 && endIndex < allJavaSourcePaths.size()) {
                endIndex = allJavaSourcePaths.size();
            }

            runnableList[i] = new ScanSubSetFileRunnable(allJavaSourcePaths.subList(startIndex, endIndex), i);
            futureList[i] = executor.submit(runnableList[i]);

            currentStartIndex = endIndex;
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (ScanSubSetFileRunnable runnable : runnableList) {
            result.addCodeInfo(runnable.getSubTaskResult());
        }
        
        return result;
    }
    
    private List<String> getFileListFromImportAsterisk(SourceInfo sourceInfo) {
        List<String> result = new ArrayList<>();
        if (sourceStartDirectory == null) {
            return result;
        }
        for (String importClass : sourceInfo.getAsteriskImports()) {
            String filePath = importClass.substring(0, importClass.lastIndexOf(".*"));
            filePath = filePath.replace(".", File.separator);
            List<String> paths = searchMatchFilePath(filePath);
            if (paths != null && paths.size() > 0) {
                result.addAll(paths);
            }
        }
        return result;
    }
    
    private List<String> searchMatchFilePath(String path) {
        List<String> result = new ArrayList<>();
        for (String filePath : parsingJavaSourcePaths) {
            if (filePath.toLowerCase().contains(path.toLowerCase())) {
                result.add(filePath);
            }
        }
        return result;
    }
}
