package com.rangarok.jparserutil.test;

import java.util.Date;

public class User {

    private int name = 1;
    
    private Date birth = new Date();
    
    private InnerUser innerUser; //TODO: not support now
    
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

public @interface UserAnnotation {
    String user() default "12";
}

