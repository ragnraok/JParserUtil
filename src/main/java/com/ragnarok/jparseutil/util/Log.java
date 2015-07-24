package com.ragnarok.jparseutil.util;

import com.ragnarok.jparseutil.JavaFileScanner;
import com.ragnarok.jparseutil.dataobject.CodeInfo;
import com.ragnarok.jparseutil.dataobject.SourceInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ragnarok on 15/5/17.
 * a simple logger implementation like android.util.Log in Android Development
 */
public class Log {

    /**
     * log level definitions
     */
    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int WARNING = 4;
    private static final int ERROR = 5;
    
    private static final String LOG_FORMAT = "[%s/%s]:[%s] %s"; // time/level: TAG content
    
    public static final int MAX_SHOW_LOG_LEVEL = DEBUG;
    
    public static Set<String> SHOW_LOG_TAG = new HashSet<>();
    
    static {
        SHOW_LOG_TAG.add(SourceInfo.TAG);
        SHOW_LOG_TAG.add(CodeInfo.TAG);
        SHOW_LOG_TAG.add(JavaFileScanner.TAG);
    }
    
    public static void v(String TAG, String format, String args) {
        println(VERBOSE, TAG, String.format(format, args));
    }
    
    public static void d(String TAG, String format, Object... args) {
        println(DEBUG, TAG, String.format(format, args));
    }
    
    public static void i(String TAG, String format, Object... args) {
        println(INFO, TAG, String.format(format, args));
    }
    
    public static void w(String TAG, String format, Object... args) {
        println(WARNING, TAG, String.format(format, args));
    }
    
    public static void e(String TAG, String format, Object... args) {
        println(ERROR, TAG, String.format(format, args));
    } 
    
    private static String getCurrentLogTime() {
        return new SimpleDateFormat("K:mm:ss:SSS").format(new Date());
    }
    
    private static String logLevelToString(int level) {
        switch (level) {
            case VERBOSE:
                return "V";
            case DEBUG:
                return "D";
            case INFO:
                return "I";
            case WARNING:
                return "W";
            case ERROR:
                return "E";
        }
        return "";
    }
    
    private static void println(int level, String tag, String content) {
        if (MAX_SHOW_LOG_LEVEL >= level && SHOW_LOG_TAG.contains(tag)) {
            System.out.println(String.format(LOG_FORMAT, getCurrentLogTime(), logLevelToString(level), tag, content));
        }
    }
}
