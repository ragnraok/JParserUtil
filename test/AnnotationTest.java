package com.rangarok.jparserutil.test.annotation;

public @interface Annotation {
    String[] value() default {"123", "123123"};
    String foo();
}