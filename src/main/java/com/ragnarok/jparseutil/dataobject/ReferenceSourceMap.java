package com.ragnarok.jparseutil.dataobject;

import com.ragnarok.jparseutil.util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ragnarok on 15/8/8.
 * Source Map read from external library reference, which is used in parse import classes from the "import *"
 */
public class ReferenceSourceMap {
    
    public static final String TAG = "JParserUtil.ReferenceSourceMap";
    
    public List<String> classesNameList = new ArrayList<>();
   
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
        classesNameList.clear();
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
                classesNameList.add(className);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "initFromSourceMapFile error: %s", e.getMessage());
        }
        isPrepare = false;
    }

    /**
     * prepare for the class name search, must be call before doing any class name search
     */
    public void prepare() {
        Collections.sort(classesNameList);
        isPrepare = true;
    }

    /**
     * search a class qualifed name by prefix and simple class name(not qualified)
     * @param prefix
     * @throws IllegalStateException if not prepare to search
     * @return
     */
    public String searchClassNameByPrefixAndSimpleClassName(String prefix, final String simpleClassName) {
        if (!isPrepare) {
            throw new IllegalStateException("must call prepare before search!");
        }
        if (prefix.endsWith(".*")) { // remove the 'import *' note
            prefix = prefix.substring(0, prefix.lastIndexOf(".*"));
        }
        int index = Collections.binarySearch(classesNameList, prefix, new Comparator<String>() {
            @Override
            public int compare(String currentItem, String key) {
                if (currentItem.startsWith(key) && currentItem.endsWith(simpleClassName)) {
                    return 0;
                }
                return currentItem.compareTo(key);
            }
        });
        if (index >= 0) {
            return classesNameList.get(index);
        }
        return null;
    }
}
