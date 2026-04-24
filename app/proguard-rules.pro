# WristPet ProGuard Rules

# Room
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
-keep class com.wristpet.data.** { *; }

# Protobuf (used by Health Services)
-keep class * extends com.google.protobuf.GeneratedMessageLite { *; }
-keep class * extends com.google.protobuf.GeneratedMessageLite$Builder { *; }
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite {
    <fields>;
}

# Health Services
-keep class androidx.health.services.client.** { *; }
-keep class androidx.health.services.client.proto.** { *; }
