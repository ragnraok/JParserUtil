package com.ragnarok.jparseutil.filescanner;

import com.ragnarok.jparseutil.SourceInfoExtracter;
import com.ragnarok.jparseutil.dataobject.CodeInfo;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.util.Log;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by ragnarok on 15/8/23.
 * this JavaFileScanner parallel scan all files with configurable multi thread support
 */
public class MultiThreadJavaFileScanner extends JavaFileScanner {
    
    public static final String TAG = "JParserUtil.MultiThreadJavaFileScanner";
    
    private int threadNumber;
    
    private ThreadPoolExecutor executor;
    
    private BlockingQueue<Runnable> workerQueue;
    
    public MultiThreadJavaFileScanner(String dir, int threadNumber) {
        super(dir);
        this.threadNumber = threadNumber;
        initThreadPool();
    }
    
    private void initThreadPool() {
        workerQueue = new ArrayBlockingQueue<>(threadNumber * 2);
        executor = new ThreadPoolExecutor(threadNumber, threadNumber * 2, 2L, TimeUnit.SECONDS, workerQueue);
        executor.setThreadFactory(new FileScannerThreadPoolFactory());
    }
    
    private class FileScannerThreadPoolFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setPriority(Thread.MAX_PRIORITY);
            return thread;
        }
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
                if (extracter == null) {
                    extracter = new SourceInfoExtracter(file);
                } else {
                    extracter.setFilePath(file);
                }
                SourceInfo sourceInfo = extracter.extract();
                if (sourceInfo != null) {
                    subTaskResult.addSource(sourceInfo);
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
        if (executor == null || executor.isShutdown()) {
            initThreadPool();
        }
        
        result = new CodeInfo();
        initJavaSourcePaths();
        Log.d(TAG, "filelist size: %d", allJavaSourcePaths.size());
        
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
            
//            Log.d(TAG, "startIndex: %d, endIndex: %d", startIndex, endIndex);

            runnableList[i] = new ScanSubSetFileRunnable(allJavaSourcePaths.subList(startIndex, endIndex), i);
            futureList[i] = executor.submit(runnableList[i]);
            
            currentStartIndex = endIndex;
        }
        
        
//        for (Future future : futureList) {
//            try {
//                future.get();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (ScanSubSetFileRunnable runnable : runnableList) {
            result.addCodeInfo(runnable.getSubTaskResult());
        }
        
        executor.shutdown();
        
        return result;
    }
}
