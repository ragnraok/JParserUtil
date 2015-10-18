package com.ragnarok.testsource;

import java.lang.Class;
import java.util.*;

import com.ragnarok.testsource.annotation.*;
import com.ragnarok.testsource.interfaces.Printable;

class User implements Printable {
    
    public static final String CLASS_NAME = "User";
    
    private String firstName = "Ragnarok" + "123123";
    private String lastName;
    
    private String[][][] usernameList = null;
    
    private Date[][] birthDateArray = null;
    
    private String test = getClass().getSimpleName();
    
    @PrintMe
    private Class clazz = User.class;
    
    private Class clazz2 = getClass();
    
    private UserType userType = UserType.MAN;
    
    public User() {
        
    }
    
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
    
    public void voidMethod() {
        
    }
    
    public static class Juno {
        private String name = "Juno";
        
        class Damn {
            
        }
    }
    
    public static class Damn {
        private String name = "Damn";
        
        class DamnDamn {
            public void intDman() {
                
            }
        }
    }
}

class MyUser extends User {
    
}