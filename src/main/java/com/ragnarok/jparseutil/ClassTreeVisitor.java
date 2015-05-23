package com.ragnarok.jparseutil;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;

import java.util.List;

/**
 * Created by ragnarok on 15/5/24.
 * the ClassTreeVisitor, inspect {@link com.sun.source.tree.ClassTree} and
 * extract info from a class
 */
public class ClassTreeVisitor {
    
    private static final String TAG = "JParserUtil.ClassTreeVisitor";
    
    public void inspectClassTress(ClassTree classTree) {
        Log.d(TAG, "inspectClassTress, name: %s", classTree.getSimpleName().toString());
        inspectAllClassTreeMembers(classTree.getMembers());
    }
    
    private void inspectAllClassTreeMembers(List<? extends Tree> classMembers) {
        for (Tree member : classMembers) {
            Log.d(TAG, "member.class: %s", member.getClass().getSimpleName());
        }
    }
}
