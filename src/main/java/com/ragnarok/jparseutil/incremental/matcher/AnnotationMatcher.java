package com.ragnarok.jparseutil.incremental.matcher;

import com.ragnarok.jparseutil.util.Log;
import com.ragnarok.jparseutil.util.Util;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by ragnarok on 15/9/13.
 * an Annotation Modifier matcher search a specific 
 */
public class AnnotationMatcher extends Matcher {

    public static final String TAG = "JParserUtil.AnnotationMatcher";
    
    private List<String> matchAnnotationList;
    
    private int threadNumber;
    private ThreadPoolExecutor executor;

    public AnnotationMatcher(List<String> fileList, int threadNumber, String... annotationQualifiedNameList) {
        super(null);
        inputFileList = new ArrayList<>(fileList.size());
        inputFileList.addAll(fileList);
        initAnnotationNameList(annotationQualifiedNameList);
        this.threadNumber = threadNumber;
    }
    
    private void initAnnotationNameList(String[] annotationQualifiedNameList) {
        if (annotationQualifiedNameList != null) {
            matchAnnotationList = new ArrayList<>(annotationQualifiedNameList.length * 2);
            for (String annotation : annotationQualifiedNameList) {
                String qualifiedName = "@" + annotation;
                String simpleName = annotation;
                if (simpleName.contains(".")) {
                    simpleName = simpleName.substring(simpleName.lastIndexOf(".") + 1);   
                }
                simpleName = "@" + simpleName;
                matchAnnotationList.add(qualifiedName);
                matchAnnotationList.add(simpleName);
            }
        } else {
            matchAnnotationList = new ArrayList<>();
        }
    }
    
    private void initThreadPool() {
        executor = new ThreadPoolExecutor(threadNumber, threadNumber, 2L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(threadNumber));
    }
    
    private class MatchSubTask implements Runnable {

        private List<String> subTaskResult;
        private List<String> subTaskFiles;
        
        private int threadNo;
        
        public MatchSubTask(List<String> subTaskFiles, int threadNo) {
            this.subTaskFiles = subTaskFiles;
            this.subTaskResult = new ArrayList<>(subTaskFiles.size());
            this.threadNo = threadNo;
        }
        
        @Override
        public void run() {
            Log.i(TAG, "thread %d start, filelist size: %d, thread: %s", threadNo, subTaskFiles.size(), Thread.currentThread());
//            String annotationStr1 = "@" + matchAnnotationName;
//            String annotationStr2 = "@" + matchAnnotationSimpleName;
            
            for (String filePath : subTaskFiles) {
                if (Util.isStringInFile(filePath, matchAnnotationList)) {
                    subTaskResult.add(filePath);
                }
            }

            Log.i(TAG, "thread %d finished, %s", threadNo, Thread.currentThread());
        }
        
        public List<String> getSubTaskResult() {
            return subTaskResult;
        }
    }
 
    @Override
    public List<String> match() {
        if (this.matchAnnotationList == null) {
            return new ArrayList<>();
        }
        if (inputFileList == null || inputFileList.size() == 0) {
            return new ArrayList<>();
        }
        initThreadPool();
        List<String> result = new ArrayList<>();
        Future[] futureList = new Future[threadNumber];
        MatchSubTask[] subTaskList = new MatchSubTask[threadNumber];

        int sourceSetSize = inputFileList.size();
        int pieceSize = sourceSetSize / threadNumber;
        int currentStartIndex = 0;
        for (int i = 0; i < threadNumber; i++) {
            int startIndex = currentStartIndex > inputFileList.size() ? inputFileList.size() - 1 : currentStartIndex;
            int endIndex = startIndex + pieceSize > inputFileList.size() ? inputFileList.size() - 1 : startIndex + pieceSize;

            if (i == threadNumber -1 && endIndex < inputFileList.size()) {
                endIndex = inputFileList.size();
            }

            subTaskList[i] = new MatchSubTask(inputFileList.subList(startIndex, endIndex), i);
            futureList[i] = executor.submit(subTaskList[i]);

            currentStartIndex = endIndex;
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        for (MatchSubTask subTask : subTaskList) {
            result.addAll(subTask.getSubTaskResult());
        }
        
        return result;
    }
}
