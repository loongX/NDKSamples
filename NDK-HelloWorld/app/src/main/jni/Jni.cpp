#include <stdio.h>
#include <stdlib.h>
#define TAG "JNI" //自定义的变量
#include <android/log.h>
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, TAG ,__VA_ARGS__)
#include "com_example_jnitest_Jni.h"
JNIEXPORT jstring JNICALL Java_com_example_jnitest_Jni_say
        (JNIEnv *env, jobject, jstring str) {
           LOGD("This is JNI");
    return str;
}