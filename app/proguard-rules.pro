# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html

-dontusemixedcaseclassnames
-verbose

# Preserve line numbers for debugging stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Preserve all public classes and their public members
-keep public class * {
    public protected *;
}

# Preserve annotated classes
-keepattributes *Annotation*

# Preserve Kotlin metadata
-keep class kotlin.Metadata { *; }
