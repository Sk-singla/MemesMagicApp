# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.samarth.memesmagic.data.local.coverters.** { *; }
-keep class com.samarth.memesmagic.data.local.dao.** { *; }
-keep class com.samarth.memesmagic.data.local.database.** { *; }
-keep class com.samarth.memesmagic.data.local.entities.relations.** { *; }


-keep class com.samarth.memesmagic.data.remote.models.** { *; }
-keep class com.samarth.memesmagic.data.remote.request.** { *; }
-keep class com.samarth.memesmagic.data.remote.response.** { *; }
-keep class com.samarth.memesmagic.data.remote.response.fcm_messages.** { *; }
-keep class com.samarth.memesmagic.data.remote.response.imageflip.** { *; }
-keep class com.samarth.memesmagic.data.remote.response.meme_api_github.** { *; }
-keep class com.samarth.memesmagic.data.remote.response.meme_maker.** { *; }

-keep class com.samarth.memesmagic.data.remote.ws.models.** { *; }
-keep class com.samarth.memesmagic.data.remote.ws.** { *; }


-keep class com.samarth.memesmagic.data.remote.** { *; }
-keep class com.samarth.memesmagic.util.** { *; }
-keep class com.samarth.memesmagic.services.** { *; }
