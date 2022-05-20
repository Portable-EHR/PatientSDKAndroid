Portable EHR Patient SDK
=========================

This is the root project for Patient SDK.

Following are the main modules/folders in this project folder:
1. App - A sample app containing the basic usage of the Patient SDK (This is WIP)
2. PatientSDK - SDK module created as a library in the project. This is being built into AAR file to be used by other apps.

Install SDK in local maven repo
================================
`./gradelw -p PatientSDK installLocal`

* Note : This works with java 8, it doesn't work with java 17. Some were between this versions something got broken : `java.lang.IllegalAccessError: class org.gradle.internal.compiler.java.ClassNameCollector (in unnamed module @0x84139ff) cannot access class com.sun.tools.javac.code.Symbol$TypeSymbol (in module jdk.compiler) because module jdk.compiler does not export com.sun.tools.javac.code to unnamed module @0x84139ff`