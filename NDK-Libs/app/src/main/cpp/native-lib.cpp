#include <jni.h>
#include <string>
#include <stdio.h>
#include <iostream>
#include "stdint.h"
#include <libposapplication_global.h>

#include <libposapplication.h>
#include<android/log.h>
#define LOG_TAG "System.out.c"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
/**
 * 封装c/c++与java的接口
 */


int soso(char *data) {
    printf("*********test*********");
    std::cout << "Hello World!" << std::endl;
    LOGI("这是来自tsoso");
    return 1;
}




extern "C" JNIEXPORT jstring JNICALL
Java_com_example_ndktest2_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";

    LOGD("调用了testapp");
    testapp();
    LOGI("这是来自so里面的info信息");

    IC_test cal;
    cal.call_test = soso;
    int i = call_back(&cal);
    LOGI("这是来自so里面的info123");

    return env->NewStringUTF(hello.c_str());
}
