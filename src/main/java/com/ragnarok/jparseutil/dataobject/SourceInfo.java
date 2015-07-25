package com.ragnarok.jparseutil.dataobject;

import com.sun.tools.classfile.Annotation;

import java.util.ArrayList;

/**
 * Created by ragnarok on 15/5/24.
 * represent information about a Java source file 
 */
public class SourceInfo {
    
    public static final String TAG = "JParserUtil.SourceInfo";
    
    private String fileName;
    private ArrayList<String> importClassNames = new ArrayList<>();
    private String packageName = null;
    private ArrayList<AnnotationInfo> annotationInfos = new ArrayList<>();
    
    /**
     * all class informations
     */
    private ArrayList<ClassInfo> classInfos = new ArrayList<>();
    
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
        if (clazz != null && !isContainClass(clazz.getSimpleName())) {
//            Log.d(TAG, "addClassInfo, name: %s, size: %d", clazz.getSimpleName(), this.classInfos.size());
            this.classInfos.add(clazz);
        }
    }
    
    public boolean isContainClass(String simpleClassName) {
        for (ClassInfo clazz : classInfos) {
            if (clazz.getSimpleName().equals(simpleClassName)) {
                return true;
            }
        }
        return false;
    }
    
    public ClassInfo getClassInfoByQualifiedName(String qualifiedName) {
        for (ClassInfo clazz : classInfos) {
            if (clazz.getQualifiedName().equals(qualifiedName)) {
                return clazz;
            }
        }
        return null;
    }
    
    public ClassInfo getClassInfoBySimpleName(String simpleName) {
        for (ClassInfo clazz : classInfos) {
            if (clazz.getSimpleName().equals(simpleName)) {
                return clazz;
            }
        }
        return null;
    }
    
    public ClassInfo getClassInfoBySuffixName(String suffixName) {
        for (ClassInfo clazz : classInfos) {
            if (clazz.getQualifiedName().endsWith("." + suffixName)) {
                return clazz;
            }
        }
        return null;
    }
    
    public void updateClassInfoByQualifiedName(String qualifiedName, ClassInfo newClazz) {
        int index = -1;
        for (int i = 0; i < classInfos.size(); i++) {
            if (classInfos.get(i).getQualifiedName().equals(qualifiedName)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            classInfos.set(index, newClazz);
        }
    }
    
    public ArrayList<ClassInfo> getAllClass() {
        return this.classInfos;
    }
    
    public String dumpClazz() {
        String result = "";
        for (ClassInfo clazz : classInfos) {
           result += clazz.getSimpleName() + ", ";
        }
        return result; 
    }
    
    public void putAnnotaiotn(AnnotationInfo annotationInfo) {
        this.annotationInfos.add(annotationInfo);
    }
    
    public ArrayList<AnnotationInfo> getAllAnnotations() {
        return this.annotationInfos;
    }
    
    public AnnotationInfo getAnnotationInfoByQualifiedName(String qualifiedName) {
        for (AnnotationInfo annotationInfo : annotationInfos) {
            if (annotationInfo.getQualifiedName().equals(qualifiedName)) {
                return annotationInfo;
            }
        }
        return null;
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
            for (AnnotationInfo annotationInfo : annotationInfos) {
                result.append(annotationInfo.toString());
                result.append("\n");
            }
        }
        if (classInfos.size() > 0) {
            for (ClassInfo classInfo : classInfos) {
                result.append(classInfo.toString());
                result.append("\n");
            }
        }
        result.append("}");
        return result.toString();
    }
}
