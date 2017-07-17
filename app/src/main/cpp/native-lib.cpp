#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_oudmon_service_JNI_1Utils_getValue(JNIEnv *env, jobject instance) {

    // TODO


    return env->NewStringUTF("Hello");
}

