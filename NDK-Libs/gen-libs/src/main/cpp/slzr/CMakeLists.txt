# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)


add_library(slzr
        SHARED

        src/libposapplication.cpp
        )
# copy out the lib binary and remove generated files
set(distribution_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../../../../../distribution)
set_target_properties(slzr
        PROPERTIES
        LIBRARY_OUTPUT_DIRECTORY
        "${distribution_DIR}/slzr/lib/${ANDROID_ABI}")

add_custom_command(TARGET slzr POST_BUILD

        COMMAND "${CMAKE_COMMAND}" -E
        copy "${CMAKE_CURRENT_SOURCE_DIR}/src/libposapplication.h"
        "${distribution_DIR}/slzr/include/libposapplication.h"

        COMMAND "${CMAKE_COMMAND}" -E
        copy "${CMAKE_CURRENT_SOURCE_DIR}/src/libposapplication_global.h"
        "${distribution_DIR}/slzr/include/libposapplication_global.h"
        COMMENT "Copying native-lib to output directory")

find_library(log-lib
        log)

target_link_libraries(slzr
        ${log-lib})