#include <jni.h>
#include <string>
#include <stdio.h>
#include <iostream>
#include "stdint.h"
#include <libposapplication_global.h>

#include <libposapplication.h>
#include<android/log.h>

#define LOG_TAG "System.out.c"
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL, LOG_TAG, __VA_ARGS__)




/**
 * 封装c/c++与java的接口
 */

// The Data Callback
extern JavaVM *theJvm;              // Need this for allocating data buffer for...
extern jobject dataCallbackObj;     // This is the (Java) object that implements...
extern jmethodID midDataCallback;   // ...this callback routine

int soso(char *data) {
    printf("*********test*********");
    std::cout << "Hello World!" << std::endl;
    LOGI("这是来自tsoso");

    JNIEnv *env;
    theJvm->AttachCurrentThread(&env, NULL);
    if (env == NULL) {
        LOGI("Error retrieving JNI Env");
    }

    // Allocate the Java array and fill with received data
    jbyteArray ret = env->NewByteArray(strlen(data));
    env->SetByteArrayRegion(ret, 0, strlen(data), (jbyte *) data);

    // send it to the (Java) callback
    env->CallVoidMethod(dataCallbackObj, midDataCallback, ret);


    return 1;
}


extern "C" JNIEXPORT jstring JNICALL
Java_com_slzr_ndk_NDKManager_stringFromJNI(
        JNIEnv *env,
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


/**
 * 动态注册
 */
extern "C" {

jstring stringFromJNI2(JNIEnv *env, jobject instance) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

jint add(JNIEnv *env, jclass clazz, jint a, jint b) {
    LOGE("ADD!!");
    return a + b;

}

#define JNIREG_CLASS "com/slzr/ndk/NDKManager"//指定要注册的类
/**
* 方法对应表
*/
static JNINativeMethod gMethods[] = {
//        {"stringFromJNI2", "()Ljava/lang/String;", (void *) stringFromJNI2},
        {"add",            "(II)I",                (void *) add}
};

/*
* 为某一个类注册本地方法
*/
static int registerNativeMethods(JNIEnv *env, const char *className, JNINativeMethod *gMethods,
                                 int numMethods) {
    jclass clazz;
    clazz = env->FindClass(className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        return JNI_FALSE;
    }

    return JNI_TRUE;
}


/*
* 为所有类注册本地方法
*/
static int registerNatives(JNIEnv *env) {
    return registerNativeMethods(env, JNIREG_CLASS, gMethods,
                                 sizeof(gMethods) / sizeof(gMethods[0]));
}

/*
* System.loadLibrary("lib")时调用
* 如果成功返回JNI版本, 失败返回-1
*/
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    jint result = -1;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }
    assert(env != NULL);
    if (!registerNatives(env)) {//注册
        return -1;
    }
    //成功
    result = JNI_VERSION_1_6;
    return result;
}
}

