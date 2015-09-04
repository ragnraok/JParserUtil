package com.ragnarok.jparseutil.dataobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ragnarok on 15/5/24.
 * represent information about a Java source file 
 */
public class SourceInfo {
    
    private String fileName;
    private ArrayList<String> importClassNames = new ArrayList<>();
    private String packageName = null;
    
    
    private HashMap<String, AnnotationInfo> annotationInfos = new HashMap<>();
    
    /**
     * all class informations
     */
    private HashMap<String, ClassInfo> classInfos = new HashMap<>();
    
    public void setFilename(String filename) {
        this.fileName = filename;
    }
    
    public String getFilename() {
        return this.fileName;
    }
    
    public void addImports(String importClass) {
        this.importClassNames.add(importClass);
    }
    
    public ArrayList<String> getImports() {
        return this.importClassNames;
    }
        
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    public String getPackageName() {
        return this.packageName;
    }
    
    public void addClassInfo(ClassInfo clazz) {
        if (clazz != null) {
//            Log.d(TAG, "addClassInfo, name: %s, size: %d", clazz.getSimpleName(), this.classInfos.size());
            this.classInfos.put(clazz.getQualifiedName(), clazz);
        }
    }
    
    public boolean isContainClass(String simpleClassName) {
        for (ClassInfo clazz : classInfos.values()) {
            if (clazz.getSimpleName().equals(simpleClassName)) {
                return true;
            }
        }
        return false;
    }
    
    public ClassInfo getClassInfoByQualifiedName(String qualifiedName) {
        return classInfos.get(qualifiedName);
    }
    
    public ClassInfo getClassInfoBySimpleName(String simpleName) {
        for (ClassInfo clazz : classInfos.values()) {
            if (clazz.getSimpleName().equals(simpleName)) {
                return clazz;
            }
        }
        return null;
    }
    
    public ClassInfo getClassInfoBySuffixName(String suffixName) {
        for (ClassInfo clazz : classInfos.values()) {
            if (clazz.getQualifiedName().endsWith("." + suffixName)) {
                return clazz;
            }
        }
        return null;
    }
    
    public void updateClassInfoByQualifiedName(String qualifiedName, ClassInfo newClazz) {
        if (classInfos.containsKey(qualifiedName)) {
            classInfos.put(qualifiedName, newClazz);
        }
    }
    
    public List<ClassInfo> getAllClass() {
        List<ClassInfo> result = new ArrayList<>(this.classInfos.size());
        result.addAll(classInfos.values());
        return result;
    }
    
    public String dumpClazz() {
        String result = "";
        for (ClassInfo clazz : classInfos.values()) {
           result += clazz.getSimpleName() + ", ";
        }
        return result; 
    }
    
    public void putAnnotaiotn(AnnotationInfo annotationInfo) {
        this.annotationInfos.put(annotationInfo.getQualifiedName(), annotationInfo);
    }
    
    public List<AnnotationInfo> getAllAnnotations() {
        List<AnnotationInfo> result = new ArrayList<>(this.annotationInfos.size());
        result.addAll(annotationInfos.values());
        return result;
    }
    
    public AnnotationInfo getAnnotationInfoByQualifiedName(String qualifiedName) {
        return annotationInfos.get(qualifiedName);
    }
    
    public void updateAnnotationByQualifiedName(String qualifiedName, AnnotationInfo annotationInfo) {
        if (annotationInfos.containsKey(qualifiedName)) {
            annotationInfos.put(qualifiedName, annotationInfo);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("{SourceInfo, filename: %s, packageName: %s", fileName, packageName));
        result.append("\n");
        if (importClassNames.size() > 0) {
            for (String className : importClassNames) {
                result.append(String.format("importClass: %s, ", className));
            }
        }
        result.append("\n");
        if (annotationInfos.size() > 0) {
            for (AnnotationInfo annotationInfo : annotationInfos.values()) {
                result.append(annotationInfo.toString());
                result.append("\n");
            }
        }
        if (classInfos.size() > 0) {
            for (ClassInfo classInfo : classInfos.values()) {
                result.append(classInfo.toString());
                result.append("\n");
            }
        }
        result.append("}");
        return result.toString();
    }
}
