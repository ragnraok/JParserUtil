package com.rangarok.jparserutil.test;


import java.util.Date;

public class User {

    private int name = 1;
    
    private Date birth = new Date();
    
    class InnerUser {
        
        private String innerName = "1";
        
        class InnerInnerUser {
            private float innerinnerName = 1.0;
        }
    }

}


