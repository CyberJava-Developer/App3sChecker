//
// Created by thinkpad on 20/06/2020.
//

#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_gsm_gsmnetindo_app_13s_1checker_internal_Secret_apiKey(JNIEnv *env, jobject thiz) {
    std::string api_key = "2FjxWC4kSoDqmCmJ";
    return env->NewStringUTF(api_key.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_gsm_gsmnetindo_app_13s_1checker_internal_Secret_apiVersion(JNIEnv *env, jobject thiz) {
    std::string api_version = "v1";
    return env->NewStringUTF(api_version.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_gsm_gsmnetindo_app_13s_1checker_internal_Secret_baseApi(JNIEnv *env, jobject thiz) {
//    std::string base_api = "http://api.my3s.co/checker/";
    std::string base_api = "http://192.168.0.103/api/checker/";
//    std::string base_api = "http://beta.my3s.co/checker/";
    return env->NewStringUTF(base_api.c_str());
}
