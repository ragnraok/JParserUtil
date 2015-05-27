# JParserUtil
A set of utilities to extract info from Java source file using JDK tools

JDK ``tools.java`` , where live in (Library/Java/JavaVirtualMachines/jdk_version/Contents/Home/lib) for OSX, provide a syntax paresr for Java, and we can use it to parse Java source files and get the AST(abstract syntax tree)

The purpose of this project is provide toolchains to build an [Annotation Processor Tool](http://docs.oracle.com/javase/7/docs/technotes/guides/apt/) without launching the compile process , just simply parse the source file, and do what we can do in APT, which makes our Java development more comfortable.

-------

This project is still under heavily developement, if you have any idea, please open an issue.


