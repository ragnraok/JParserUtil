package com.ragnarok.jparseutil;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.util.Context;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by ragnarok on 15/5/17.
 * read the Java source file and return a serial of {@link CompilationUnitTree}
 */
public class JavaSourceReader {
    
    private String filePath = null;
    private JavacFileManager fileManager;
    private JavacTool javacTool;
    
    public JavaSourceReader(String filepath) {
        this.filePath = filepath;

        Context context = new Context();
        fileManager = new JavacFileManager(context, true, Charset.defaultCharset());
        javacTool = new JavacTool();
    }
    
    public Iterable<? extends CompilationUnitTree> readSource() {
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
    
}
