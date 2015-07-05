package com.rangarok.jparserutil.test;

import java.lang.Deprecated;
import java.util.Date;

public class Foo {
   
//    @Deprecated("123123")
//    public String annotateVar = null;
    
//    @Deprecated("123123")
    @Annotation(foo=new String[]{"123", "!23"})
//    @Annotation({"123", "123", "!23"})
    public void annotateMethod() {
        
    }
}

public @interface Annotation {
    String[] value();
}