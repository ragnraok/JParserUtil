package com.rangarok.jparserutil.test;


import java.lang.Deprecated;
import java.util.Date;

public class User {

    private int name = 1;
    
    @Deprecated
    private Date birth = new Date();
    
    public String foo() {
        return null;
    }
    
    class InnerUser {
        
        private String innerName = "1";
        
        class InnerInnerUser {
            private float innerinnerName = 1.0;
        }
    }
}

