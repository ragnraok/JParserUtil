package com.ragnarok.jparseutil.dataobject;

import java.util.HashMap;

/**
 * Created by ragnarok on 15/7/22.
 * represent information for a codebase, which may contain many Java source files
 */
public class CodeInfo {
    
    public static final String TAG = "JParserUtil.CodeInfo";
    
    private HashMap<String, SourceInfo> javaSources = new HashMap<>();
    
    public void addSource(SourceInfo sourceInfo) {
        javaSources.put(sourceInfo.getFilename(), sourceInfo);
    }
    
    public HashMap<String, SourceInfo> getAllSources() {
        return javaSources;
    }
}
