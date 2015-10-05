package com.ragnarok.testsource.tree;

import com.ragnarok.testsource.annotation.*;

public class Node {
    public static final String CLASS_NAME = "Node";

    public String getValue() {
        return "node";
    }
    
    public Node getSibling() {
        return this;
    }
    
    public Tree getContainedTree() {
        return new Tree();
    }
}