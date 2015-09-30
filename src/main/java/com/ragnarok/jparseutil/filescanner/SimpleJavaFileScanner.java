package com.ragnarok.jparseutil.filescanner;

import com.ragnarok.jparseutil.dataobject.CodeInfo;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.dataobject.Type;
import com.ragnarok.jparseutil.util.Log;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ragnarok on 15/7/23.
 * the simplest implementation of {@link JavaFileScanner} which sequential scan all files
 */
public class SimpleJavaFileScanner extends JavaFileScanner {
    
    public static final String TAG = "JParserUtil.SimpleJavaFileScanner";

    public SimpleJavaFileScanner(String dir) {
        super(dir);
    }

    public SimpleJavaFileScanner(List<String> paths) {
        super(paths);
    }

    @Override
    public CodeInfo scanAllJavaSources() throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        if (sourceDirectory != null) {
            initJavaSourcePaths();  
        }
        Log.d(TAG, "source paths: %s\n", allJavaSourcePaths.toString());
        result = new CodeInfo();
        if (allJavaSourcePaths.size() > 0) {
            for (String path : allJavaSourcePaths) {
                SourceInfo sourceInfo = parseJavaSource(path);
                if (sourceInfo != null) {
                    result.addSource(sourceInfo);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        Log.i(TAG, "parse finish, used: %dms", endTime - startTime);
        return result;
    }
}
