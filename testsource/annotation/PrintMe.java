package com.rangnarok.testsouce.annotation;

public @interface PrintMe {
    public String value() default "123123";
    public String me() default ";lasdflajsdf";
    public String[] manyMe() default {"123", "!23"};
}