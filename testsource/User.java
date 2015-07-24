package com.rangnarok.testsouce;

import java.util.Date;

import com.rangnarok.testsouce.annotation.PrintMe;

class User {
    
    public static final String CLASS_NAME = "User";
    
    private String firstName = "Ragnarok";
    private String lastName;
    
    @PrintMe
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public Date getBirthDate() {
        return new Date();
    }
    
    public static class Juno {
        private String name = "Juno";
    }
    
    public static class Damn {
        private String name = "Damn";
    }
}