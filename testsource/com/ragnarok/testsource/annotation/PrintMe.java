package com.ragnarok.testsource.annotation;

import com.rangnarok.testsource.tree.*;

public @interface PrintMe {
    public String value() default "123123";
    public String me() default ";lasdflajsdf";
    public String[] manyMe() default {"123", "!23"};
    public Node getNode();
}