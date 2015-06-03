package com.rangarok.jparserutil.test;

import java.lang.Deprecated;
import java.lang.SuppressWarnings;
import java.util.Date;

@SuppressWarnings(new String("123123"))
public class User {

    private int name = 1;
    
    @TestAnnotation(user=new User())
    @TestAnnotation2("123123")
    private Date birth = new Date();
    
//    private InnerUser innerUser; 
    
    public String foo() {
        int fooVar = 1;
        return null;
    }
    
    class InnerUser {
        
        private String innerName = "1";
        
        class InnerInnerUser {
            private float innerinnerName = 1.0;
        }
    }
}

public @interface TestAnnotation {
    User user();
}

public @interface TestAnnotation2 {
    Sttring value();
}
