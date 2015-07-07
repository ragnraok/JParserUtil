package com.rangarok.jparserutil.test.annotation;

public @interface Annotation {
//    String[] value() default {"123", "123123"};
//    String foo();
    String bar() default "123123";
}