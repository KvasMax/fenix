-keepattributes Annotation

-keepnames class * {
@javax.inject.Inject (...);
@javax.inject.Inject ;
@javax.annotation.Nullable ;
}

-keepnames @javax.inject.Singleton class *
-dontwarn javax.inject.**

-keep class **$$Factory{ *; }
-keep class **$$MemberInjector{ *; }