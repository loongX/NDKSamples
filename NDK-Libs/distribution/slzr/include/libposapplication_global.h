#ifndef LIBPOSAPPLICATION_GLOBAL_H
#define LIBPOSAPPLICATION_GLOBAL_H

#if defined(LIBPOSAPPLICATION_LIBRARY)

#include <QtCore/qglobal.h>
#define LIBPOSAPPLICATIONSHARED_EXPORT Q_DECL_EXPORT

#else

#include "jni.h"
#include"android/log.h"
#define LOG_TAG "System.out.c"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

#define LIBPOSAPPLICATIONSHARED_EXPORT JNIEXPORT

#endif

#endif // LIBPOSAPPLICATION_GLOBAL_H
