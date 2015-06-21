package com.rangarok.jparserutil.test;

import java.lang.Deprecated;
import java.lang.Override;
import java.lang.SuppressWarnings;
import java.util.Date;

@SuppressWarnings(value="123123")
public class User {

    private int name = 1;
    
    @TestAnnotation(user=new User())
    @TestAnnotation2("123123")
    private Date birth = new Date();
    
//    private InnerUser innerUser; 
    
    @Deprecated("123123")
    public String foo(InnerUser a) {
        int fooVar = 1;
        return null;
    }
    
    class InnerUser {
        
        private String innerName = "1";
        
        public InnerUser foo(User u) {
            
        }
                
        class InnerInnerUser {
            private float innerinnerName = 1.0;
            
            public void foo() {
                
            }
        }
    }
}

public @interface TestAnnotation {
    User user();
}

public @interface TestAnnotation2 {
    Sttring value() default "123123";
}
