#include "libposapplication.h"
#include<stdio.h>
#include<iostream>
#include"stdint.h"

LibPosApplication::LibPosApplication()
{
 printf("测试lib");
}
LIBPOSAPPLICATIONSHARED_EXPORT void testapp()
{
     printf("*********test*********");
     std::cout<<"Hello World!"<<std::endl;
    LOGI("这是来自testapp");
}

extern "C"{
LIBPOSAPPLICATIONSHARED_EXPORT int call_back(IC_test *body)
{
    int ret;
    printf("%s\n",__FUNCTION__);
       std::cout<<"Hello call_back!"<<std::endl;
    ret=(body->call_test)("你好啊");
    return 1;
}
}
//回调函数实现
