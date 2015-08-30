package com.rangnarok.testsouce.tree;

import com.rangnarok.testsouce.annotation.*;

public class Node {
    public static final String CLASS_NAME = "Node";

    public String getValue() {
        return "node";
    }
    
    @PrintMe
    public Node getSibling() {
        return this;
    }
}