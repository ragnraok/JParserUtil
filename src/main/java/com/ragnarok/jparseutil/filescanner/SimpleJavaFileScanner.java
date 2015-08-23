package com.ragnarok.jparseutil.filescanner;

import com.ragnarok.jparseutil.dataobject.CodeInfo;
import com.ragnarok.jparseutil.dataobject.Type;
import com.ragnarok.jparseutil.util.Log;

import java.io.FileNotFoundException;

/**
 * Created by ragnarok on 15/7/23.
 * the simplest implementation of {@link JavaFileScanner} which sequential scan all files
 */
public class SimpleJavaFileScanner extends JavaFileScanner {
    
    public static final String TAG = "JParserUtil.SimpleJavaFileScanner";

    public SimpleJavaFileScanner(String dir) {
        super(dir);
    }


    @Override
    public CodeInfo scanAllJavaSources() throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        initJavaSourcePaths();
        Log.d(TAG, "source paths: %s\n", allJavaSourcePaths.toString());
        result = new CodeInfo();
        if (allJavaSourcePaths.size() > 0) {
            for (String path : allJavaSourcePaths) {
                parseJavaSource(path);
            }
        }
        Type.setFinalParseResult(result);
        long endTime = System.currentTimeMillis();
        Log.i(TAG, "parse finish, used: %dms", endTime - startTime);
        return result;
    }
}
