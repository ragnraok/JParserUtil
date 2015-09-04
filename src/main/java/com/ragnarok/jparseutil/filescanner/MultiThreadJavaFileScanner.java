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
    
    private LinkedBlockingQueue<Runnable> workerQueue;
    
    public MultiThreadJavaFileScanner(String dir, int threadNumber) {
        super(dir);
        this.threadNumber = threadNumber;
        initThreadPool();
    }
    
    private void initThreadPool() {
        workerQueue = new LinkedBlockingQueue<>();
        executor = new ThreadPoolExecutor(threadNumber, threadNumber, 2L, TimeUnit.SECONDS, workerQueue);
    }
    
    private class ScanSubSetFileRunnable implements Runnable {

        private List<String> subSetFileList;
        private int threadNo;
        
        private CodeInfo subTaskResult;
        
        public ScanSubSetFileRunnable(List<String> subSetFileList, int threadNo) {
            this.subSetFileList = subSetFileList;
            this.threadNo = threadNo;
            subTaskResult = new CodeInfo();
        }
        
        @Override
        public void run() {
            for (String file : subSetFileList) {
                SourceInfoExtracter extracter = new SourceInfoExtracter(file);
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
            
            Log.d(TAG, "startIndex: %d, endIndex: %d", startIndex, endIndex);

            runnableList[i] = new ScanSubSetFileRunnable(allJavaSourcePaths.subList(startIndex, endIndex), i);
            futureList[i] = executor.submit(runnableList[i]);
            
            currentStartIndex = endIndex;
        }
        
        
        for (Future future : futureList) {
            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        
        for (ScanSubSetFileRunnable runnable : runnableList) {
            result.addCodeInfo(runnable.getSubTaskResult());
        }
        
        executor.shutdown();
        
        return result;
    }
}
