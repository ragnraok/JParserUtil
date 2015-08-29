package com.ragnarok.jparseutil.dataobject;

import com.ragnarok.jparseutil.util.Log;

import java.io.*;
import java.util.*;

/**
 * Created by ragnarok on 15/8/8.
 * Source Map read from external library reference, which is used in parse import classes from the "import *"
 */
public class ReferenceSourceMap {
    
    public static final String TAG = "JParserUtil.ReferenceSourceMap";
    
//    private List<String> classesNameList = new ArrayList<>();

    // simpleClassName -> qualifiedClassName list
    // because simpleClassName may have multiple correspond qualified name, 
    // like android.view.SurfaceView and android.view.mock.SurfaceView
    private HashMap<String, List<String>> simpleNameQualifiedNameMap = new HashMap<>(); 
    
    private static ReferenceSourceMap INSTANCE;
    
    private boolean isPrepare = false;
    
    private ReferenceSourceMap() {}
    
    public static ReferenceSourceMap getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ReferenceSourceMap();
        }
        return INSTANCE;
    }
    
    public void clearSourceMap() {
        simpleNameQualifiedNameMap.clear();
    }

    /**
     * initialize the ReferenceSourceMap from a list of source map file
     * @param sourceMapFileList an array contains all source map filenames
     * @throws FileNotFoundException
     */
    public void initWithSourceMapFile(String... sourceMapFileList) throws FileNotFoundException {
        simpleNameQualifiedNameMap.clear();
        for (String filename : sourceMapFileList) {
            addClassesListFromSourceMapFile(filename);
        }
        isPrepare = false;
    }

    /**
     * add classes from the {@param sourceMapFile}
     * @param sourceMapFile
     * @throws FileNotFoundException
     */
    public void addClassesListFromSourceMapFile(String sourceMapFile) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(sourceMapFile);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader reader = new BufferedReader(isr);
        
        String className = null;
        try {
            while ((className = reader.readLine()) != null) {
                addClassNameToSourceMap(className);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "initFromSourceMapFile error: %s", e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        isPrepare = false;
    }
    
    public synchronized void addClassNameToSourceMap(String className) {
        if (className == null) {
            return;
        }
        String simpleClassName = null;
        if (className.lastIndexOf(".") != -1) {
            int lastDotIndex = className.lastIndexOf(".");
            if (lastDotIndex < className.length() - 1) {
                simpleClassName = className.substring(lastDotIndex + 1);
            } else {
                simpleClassName = className;
            }
        } else {
            simpleClassName = className;
        }
        
        if (simpleNameQualifiedNameMap.containsKey(simpleClassName)) {
            List<String> qualifiedNameList = simpleNameQualifiedNameMap.get(simpleClassName);
            qualifiedNameList.add(className);
            simpleNameQualifiedNameMap.put(simpleClassName, qualifiedNameList);
        } else {
            List<String> qualifiedNameList = new ArrayList<>();
            qualifiedNameList.add(className);
            simpleNameQualifiedNameMap.put(simpleClassName, qualifiedNameList);
        }
    }

    /**
     * search a class qualifed name by prefix and simple class name(not qualified)
     * @param prefix
     * @throws IllegalStateException if not prepare to search
     * @return
     */
    public String searchClassNameByPrefixAndSimpleClassName(String prefix, final String simpleClassName) {
        if (prefix == null || simpleClassName == null) {
            return null;
        }
        if (prefix.endsWith(".*")) { // remove the 'import *' note
            prefix = prefix.substring(0, prefix.lastIndexOf(".*"));
        }
        List<String> qualifiedNameList = simpleNameQualifiedNameMap.get(simpleClassName);
        if (qualifiedNameList != null && qualifiedNameList.size() > 0) {
            for (String className : qualifiedNameList) {
                String classNamePrefix = className.substring(0, className.lastIndexOf("."));
                if (classNamePrefix.equals(prefix)) {
                    return className;
                }
            }
        }
        return null;
    }
}
