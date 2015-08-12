package com.rangnarok.testsouce;

import java.lang.Class;
import java.util.*;

import com.rangnarok.testsouce.annotation.PrintMe;
import com.rangnarok.testsouce.annotation.interfaces.Printable;

class User implements Printable {
    
    public static final String CLASS_NAME = "User";
    
    private String firstName = "Ragnarok";
    private String lastName;
    
    private String test = getClass().getSimpleName();
    
    private Class clazz = getClass();
    
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
    
    public Juno getJuno() {
        return new Juno();
    }
    
    public Damn getDamn() {
        return new Damn();
    }
    
    public static class Juno {
        private String name = "Juno";
    }
    
    public static class Damn {
        private String name = "Damn";
        
        class DamnDamn {
            
        }
    }
}