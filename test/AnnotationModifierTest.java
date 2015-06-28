package com.rangarok.jparserutil.test;

import java.lang.Deprecated;
import java.util.Date;

public class Foo {
   
//    @Deprecated("123123")
//    public String annotateVar = null;
    
//    @Deprecated("123123")
    @Annotation(new Date())
    public void annotateMethod() {
        
    }
}

public @interface Annotation {
    String[] value();
}