package com.ragnarok.jparseutil.dataobject;

import com.sun.tools.classfile.Annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ragnarok on 15/7/22.
 * represent information for a codebase, which may contain many Java source files
 */
public class CodeInfo {
    
    public static final String TAG = "JParserUtil.CodeInfo";
    
    private HashMap<String, SourceInfo> javaSources = new HashMap<>();
    
    private HashMap<String, List<ClassInfo>> packageClassList = new HashMap<>();
    
    private HashMap<String, List<AnnotationInfo>> packageAnnotationList = new HashMap<>();
    
    private ArrayList<String> classQualifiedNameLists = new ArrayList<>();
    
    public void addSource(SourceInfo sourceInfo) {
        javaSources.put(sourceInfo.getFilename(), sourceInfo);
    }
    
    public HashMap<String, SourceInfo> getAllSources() {
        return javaSources;
    }

    /**
     * arrange {@link ClassInfo} by package name
     */
    public void arrangeClassByPackage() {
        packageClassList.clear();
        for (SourceInfo sourceInfo : javaSources.values()) {
            for (ClassInfo classInfo : sourceInfo.getAllClass()) {
                String packageName = classInfo.getPackageName();
                if (packageClassList.get(packageName) == null || packageClassList.get(packageName).size() == 0) {
                    packageClassList.put(packageName, new ArrayList<ClassInfo>());
                }
                packageClassList.get(packageName).add(classInfo);
            }
        }
    }

    /**
     * arrange {@link AnnotationInfo} by package name
     */
    public void arrangeAnnotationByPackage() {
        packageAnnotationList.clear();
        for (SourceInfo sourceInfo : javaSources.values()) {
            for (AnnotationInfo annotationInfo : sourceInfo.getAllAnnotations()) {
                String packageName = annotationInfo.getPackageName();
                if (packageAnnotationList.get(packageName) == null || packageAnnotationList.get(packageName).size() == 0) {
                    packageAnnotationList.put(packageName, new ArrayList<AnnotationInfo>());
                }
                packageAnnotationList.get(packageName).add(annotationInfo);
            }
        }
    }
    
    public HashMap<String, List<ClassInfo>> getPackageClassList() {
        return packageClassList;
    }
    
    public HashMap<String, List<AnnotationInfo>> getPackageAnnotationList() {
        return packageAnnotationList;
    }

    /**
     * get {@link ClassInfo} by qualified name 
     * @param qualifiedName
     * @return
     */
    public ClassInfo getClassByQualifiedName(String qualifiedName) {
        for (SourceInfo sourceInfo : javaSources.values()) {
            ClassInfo classInfo = sourceInfo.getClassInfoByQualifiedName(qualifiedName);
            if (classInfo != null) {
                return classInfo;
            }
        }
        return null;
    }
    
    public AnnotationInfo getAnnotationByQualifiedName(String qualifiedName) {
        for (SourceInfo sourceInfo : javaSources.values()) {
            AnnotationInfo annotationInfo = sourceInfo.getAnnotationInfoByQualifiedName(qualifiedName);
            if (annotationInfo != null) {
                return annotationInfo;
            }
        }
        return null;
    }
    
    public void arrangeAllClassNameList() {
        classQualifiedNameLists.clear();
        for (SourceInfo sourceInfo : javaSources.values()) {
            for (ClassInfo classInfo : sourceInfo.getAllClass()) {
                classQualifiedNameLists.add(classInfo.getQualifiedName());
            }
        }
    }
    
    public String getClassQualifiedNameBySimpleName(String simpleName) {
        if (classQualifiedNameLists.size() == 0) {
            arrangeAllClassNameList();
        }
        for (String qualifiedName : classQualifiedNameLists) {
            if (qualifiedName.endsWith("." + simpleName)) {
                return qualifiedName;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("");
        for (SourceInfo sourceInfo : javaSources.values()) {
            result.append(sourceInfo.toString());
            result.append("\n");
        }
        return result.toString();
    }
}
