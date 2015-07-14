package com.rangarok.jparserutil.test;

import java.lang.Deprecated;
import java.util.Date;

public class Foo {
    
    private String[] c = "123";
   
//    @Deprecated("123123")
//    public String annotateVar = null;
    
//    @Deprecated("123123")
    @Annotation(foo=c)
//    @Annotation({"123", "123", "!23"})
    public void annotateMethod() {
        
    }
}

public @interface Annotation {
    String[] value();
}