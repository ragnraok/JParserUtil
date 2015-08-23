package com.ragnarok.jparseutil;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.util.Context;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by ragnarok on 15/5/17.
 * read the Java source file and return a serial of {@link CompilationUnitTree}
 */
public class JavaSourceReader {
    
    private String filePath = null;
    private String filename = null;
    private JavacFileManager fileManager;
    private JavacTool javacTool;
    
    public JavaSourceReader(String filepath) {
        this.filePath = filepath;

        Context context = new Context();
        fileManager = new JavacFileManager(context, true, Charset.forName("UTF-8"));
        javacTool = new JavacTool();
    }
    
    public Iterable<? extends CompilationUnitTree> readSource() throws FileNotFoundException {
        File file = new File(this.filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("try parse a not exist source file: " + this.filePath);
        }
        
        this.filename = file.getName();
        
        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects(this.filePath);
        JavaCompiler.CompilationTask compilationTask = javacTool.getTask(null, fileManager, null, null, null, files);
        JavacTask javacTask = (JavacTask) compilationTask;

        try {
            Iterable<? extends CompilationUnitTree> result = javacTask.parse();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String getFilename() {
        return this.filename;
    }
    
}
