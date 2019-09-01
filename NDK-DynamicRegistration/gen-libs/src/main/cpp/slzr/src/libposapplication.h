#ifndef LIBPOSAPPLICATION_H
#define LIBPOSAPPLICATION_H

#include "libposapplication_global.h"

class LIBPOSAPPLICATIONSHARED_EXPORT LibPosApplication
{

public:
    LibPosApplication();
};

#ifdef __cplusplus
extern "C"{
    extern LIBPOSAPPLICATIONSHARED_EXPORT void testapp();

    typedef struct _IC_test
    {
         int (*call_test)(char *data);
    }IC_test;

    extern LIBPOSAPPLICATIONSHARED_EXPORT int call_back(IC_test *body);
}
#endif

#endif // LIBPOSAPPLICATION_H
