package com.rangarok.jparserutil.test.annotation;

import java.io.Closeable;
import java.sql.*;
import java.util.Iterator;

public class Foo extends Date implements Iterator, Closeable, FooInterface, Test {
    public static final String TAG = "Test";
    
    public Bar newInstance() {
        
    }
    
    public class Bar {
        public class Shit {
            
        }
    }
    
    public interface FooInterface {
        
    }
}

interface Test {
    
}