package com.ragnarok.jparseutil.incremental.matcher;

import com.ragnarok.jparseutil.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ragnarok on 15/9/13.
 * an Annotation Modifier matcher search a specific 
 */
public class AnnotationMatcher extends Matcher {

    public static final String TAG = "JParserUtil.AnnotationMatcher";
    
    private String matchAnnotationName = null;
    private String matchAnnotationSimpleName = null;

    public AnnotationMatcher(String annotationQualifiedName, String dir, String... excludePaths) {
        super(dir, excludePaths);
        this.matchAnnotationName = annotationQualifiedName;
        if (this.matchAnnotationName.contains(".")) {
            this.matchAnnotationSimpleName = this.matchAnnotationName.substring(this.matchAnnotationName.lastIndexOf(".") + 1);   
        }
    }

    @Override
    public List<String> match() {
        if (this.matchAnnotationName == null) {
            return new ArrayList<>();
        }
        if (inputFileList == null || inputFileList.size() == 0) {
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>();
        String annotationStr1 = "@" + matchAnnotationName;
        String annotationStr2 = "@" + matchAnnotationSimpleName;
        for (String file : inputFileList) {
            if (Util.isStringInFile(file, annotationStr1, annotationStr2)) {
                result.add(file);
                if (matchFileCallback != null) {
                    matchFileCallback.onMatchFile(file);
                }
            }
        }
        
        return result;
    }
}
