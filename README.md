# JParserUtil
A set of utilities to extract info from Java source file using JDK tools

JDK ``tools.java`` , where live in (Library/Java/JavaVirtualMachines/jdk_version/Contents/Home/lib) for OSX, provide a syntax paresr for Java, and we can use it to parse Java source files and get the AST(abstract syntax tree)

The purpose of this project is provide toolchains to build an [Annotation Processor Tool](http://docs.oracle.com/javase/7/docs/technotes/guides/apt/) without launching the compile process , just simply parse the source file, and do what we can do in APT, which makes our Java development more comfortable.

-------

This project is still under heavily developement, if you have any idea, please open an issue.

-----
Currently support:

1. Extract source information, like imports, packge name
2.  Extract class info, include class name, inner class name, annotation modifiers, and retrive the fully qualified name from imports and package declaration(include parse from 'import *')
3. Extract variable info, include variable name, variable type and the fully qualified from imports and package declaration, variable value(currently only support parse primitive type), and annotations. 
4. Extract methods info for classes, include method name, return type, and parameter type.
5. For annotaions annotated with variables or methods, now can extract its name, and arguments, but currently only support simple primitive simple value. 
6. Extract Annotation declaration.
7. Parse value from Array declaration, currently only support parse primitive type
8. Support parse multiple Java source files, just need to give a input directory
9. Configurable multi thread support for speed up parsing


---

Todo: 

- [x] add modifiers information for class, variable, method
- [x] add enum parse support
- [x] add interface parse support
- [x] add super class information
- [x] multiple files parsing support
- [x] add Java source map to support retrieve qualified class name from  ``import *`` 
- [ ] simple mathematical operation parsing support for primitive type variable initialization
- [ ] improve the pasring speed

