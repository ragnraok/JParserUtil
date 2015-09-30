package com.ragnarok.jparseutil.dataobject;

import com.ragnarok.jparseutil.util.Log;
import com.sun.org.apache.xpath.internal.operations.Variable;
import com.sun.tools.classfile.Annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ragnarok on 15/7/22.
 * represent information for a codebase, which may contain many Java source files
 */
public class CodeInfo {
    
    private HashMap<String, SourceInfo> javaSources = new HashMap<>();
    
    private HashMap<String, List<ClassInfo>> packageClassList = new HashMap<>();
    
    private HashMap<String, List<AnnotationInfo>> packageAnnotationList = new HashMap<>();
    
    private ArrayList<String> classQualifiedNameLists = new ArrayList<>();
    
    public void addSource(SourceInfo sourceInfo) {
        javaSources.put(sourceInfo.getFilename(), sourceInfo);
    }
    
    public boolean isContainedSource(String path) {
        return javaSources.containsKey(path);
    }
    
    public void addCodeInfo(CodeInfo codeInfo) {
        javaSources.putAll(codeInfo.getAllSources());
    }
    
    public HashMap<String, SourceInfo> getAllSources() {
        return javaSources;
    }
    
    public ArrayList<AnnotatedObject> annotatedObjectList = new ArrayList<>();

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
    
    public void arrangeAnnotatedObjects() {
        // currently, the AnnotatedObject are ClassInfo, VariableInfo, MethodInfo
        annotatedObjectList.clear();
        for (SourceInfo sourceInfo : javaSources.values()) {
            for (ClassInfo classInfo : sourceInfo.getAllClass()) {
                annotatedObjectList.add(classInfo);
                for (VariableInfo variableInfo : classInfo.getAllVariables()) {
                    annotatedObjectList.add(variableInfo);
                }
                for (MethodInfo method : classInfo.getAllMethods()) {
                    annotatedObjectList.add(method);
                }
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
    
    private static boolean isParseFinished = false;
    
    public static void reset() {
        isParseFinished = false;
    }
    
    public static void markParseFinish() {
        isParseFinished = true;
    }
    
    public static boolean isParseFinish() {
        return isParseFinished;
    }
}
