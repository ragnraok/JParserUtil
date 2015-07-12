package com.ragnarok.jparseutil.memberparser;

import com.ragnarok.jparseutil.dataobject.NewClassObjectInfo;
import com.ragnarok.jparseutil.dataobject.SourceInfo;
import com.ragnarok.jparseutil.dataobject.VariableType;
import com.ragnarok.jparseutil.util.Log;
import com.sun.tools.javac.tree.JCTree;

/**
 * Created by ragnarok on 15/7/12.
 */
public class NewClassObjectParser {
    
    private static final String TAG = "JParserUtil.NewClassObjectParser";
    
    public static NewClassObjectInfo parseNewClass(SourceInfo sourceInfo, JCTree.JCNewClass newClass) {
        Log.d(TAG, "parseNewClass, class: %s, args: %s, constructor: %s, constructorType: %s", 
                newClass.clazz, newClass.args, newClass.constructor, newClass.constructorType);
        NewClassObjectInfo newClassObjectInfo = new NewClassObjectInfo();
        VariableType classType = TypeParser.parseType(sourceInfo, newClass.clazz, newClass.clazz.toString());
        newClassObjectInfo.setObjectType(classType);
        return newClassObjectInfo;
    }
}
