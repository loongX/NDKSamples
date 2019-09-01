# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# 这里设置外部代码根目录
set(distribution_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../../../../distribution)

#调用第三方so文件
add_library(slzr-lib SHARED IMPORTED)
set_target_properties(slzr-lib PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/slzr/lib/${ANDROID_ABI}/libslzr.so)

#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")
#主so文件配置
add_library(native-lib
        SHARED
        #这里设置需要引入的c文件
#        ${distribution_DIR}/slzr/src/libposapplication.cpp
        NDKManager.cpp
        native-lib.cpp)

#这里设置生成so文件的输出目录，可以不设置
set_target_properties(native-lib
        PROPERTIES
        LIBRARY_OUTPUT_DIRECTORY
        "${distribution_DIR}/native-lib/lib/${ANDROID_ABI}")

#这里设置外部源码位置
target_include_directories(native-lib PRIVATE
#        ${distribution_DIR}/slzr/src
        ${distribution_DIR}/slzr/include)

find_library(log-lib
        log)


target_link_libraries(native-lib
        slzr-lib   #第三方so文件

        ${log-lib})