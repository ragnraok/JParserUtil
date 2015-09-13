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
    private String matchAnnotaionSimpleName = null;

    public AnnotationMatcher(String annotationQualifiedName, String dir, String... excludePaths) {
        super(dir, excludePaths);
        this.matchAnnotationName = annotationQualifiedName;
        if (this.matchAnnotationName.contains(".")) {
            this.matchAnnotaionSimpleName = this.matchAnnotationName.substring(this.matchAnnotationName.lastIndexOf(".") + 1);   
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
        for (String file : inputFileList) {
            String fileContent = Util.getFileContent(file);
            if (fileContent != null && isContainedAnnotation(fileContent)) {
                result.add(file);
            }
        }
        
        return result;
    }
    
    private boolean isContainedAnnotation(String fileContent) {
        String annotationStr1 = "@" + matchAnnotationName;
        String annotationStr2 = "@" + matchAnnotaionSimpleName;
        if (fileContent.contains(annotationStr2) || fileContent.contains(annotationStr1)) {
            return true;
        }
        return false;
    }
}
