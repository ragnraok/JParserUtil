package com.rangnarok.testsouce.tree;

public class Node {
    public static final String CLASS_NAME = "Node";

    public String getValue() {
        return "node";
    }
    
    public Node getSibling() {
        return this;
    }
}