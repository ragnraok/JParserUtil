package com.rangarok.jparserutil.test.annotation;

import java.io.Closeable;
import java.sql.Date;
import java.util.Iterator;

public class Foo extends Date implements Iterator, Closeable, FooInterface {
    public static final String TAG = "Test";
    
    public InnerFoo newInstance() {
        
    }
    
    public class InnerFoo {
        
    }
    
    public interface FooInterface {
        
    }
}