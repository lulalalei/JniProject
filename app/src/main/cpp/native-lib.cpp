#include <jni.h>
#include <string>
#include <stdio.h>
#include<malloc.h>

char *jstring2CStr(JNIEnv *pEnv, jstring pJstring);

int getPressure();

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_example_jnitest_JniKit_sayHellow(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "jni say hellow";
    return env->NewStringUTF(hello.c_str());
}

/*
 * 求两个数字的和*/
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_jnitest_JniKit_sum(JNIEnv *env, jobject instance, jint x, jint y) {

    //jint可以直接进行算术运算
    int sum=x+y;
    //可直接将int类型数据作为jint返回
    return sum;
}

/**
 * 工具函数
 * 把一个jstring转换成一个c语言的char* 类型.
 */
char* _JString2CStr(JNIEnv* env, jstring jstr) {

    char* rtn;
    jclass clsstring = env->FindClass("java/lang/String");
    jstring strencode = env->NewStringUTF("GB2312");
    jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray barr = (jbyteArray)env->CallObjectMethod(jstr, mid, strencode); // String .getByte("GB2312");
    jsize alen = env->GetArrayLength(barr);
    jbyte* ba = env->GetByteArrayElements(barr, JNI_FALSE);
    if(alen > 0) {
        rtn = (char*)malloc(alen+1); //"\0"
        memcpy(rtn, ba, alen);
        rtn[alen]=0;
    }
    env->ReleaseByteArrayElements(barr, ba,0);
    return rtn;
}

/*
 * 将字符串拼接后返回
 * */
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_jnitest_JniKit_appendString(JNIEnv *env, jobject instance, jstring js) {
    //将jstring类型的js转换为char*类型数据
    char * fromJava = _JString2CStr(env,js);
    //c
    char * fromC = "add I am SWJ";
    //将拼接两个char*类型字符串拼接在第一个上
    strcat(fromJava, fromC);
    //将结果转换为jstring类型返回
    return env->NewStringUTF(fromJava);
}

/*
 * 将数组的每个元素增加10
 * */
extern "C"
JNIEXPORT jintArray JNICALL
Java_com_example_jnitest_JniKit_increaseArrayEles(JNIEnv *env, jobject instance,
                                                  jintArray intArray_) {
    //得到数组
    jint *intArray = env->GetIntArrayElements(intArray_, NULL);
    //得到数组长度
    jsize length=env->GetArrayLength(intArray_);

    //遍历数组,并将每个元素+10
    int i=0;
    for (i = 0; i < length; ++i) {
        *(intArray+i)+=10;
    }

    env->ReleaseIntArrayElements(intArray_, intArray, 0);
    //返回数组
    return intArray_;
}

/*
 * 检查密码是否正确,如果正确返回200,否则返回400
 * */
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_jnitest_JniKit_checkPwd(JNIEnv *env, jobject instance, jstring pwd_) {
    const char *pwd = env->GetStringUTFChars(pwd_, 0);
    char * key="123456";
    //比较两个字符串是否相等
    int result=strcmp(key,pwd);
    env->ReleaseStringUTFChars(pwd_, pwd);
    if (result==0){
        return 200;
    }
    return 400;
}

//////////////////////////////////////////////////C回调java中的方法

extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnitest_JniKit_callbackHellowFromJava(JNIEnv *env, jobject instance) {

    //加载得到jclass对象
    jclass jc=env->FindClass("com/example/jnitest/JniKit");
    //得到对应方法的Method对象
    jmethodID method=env->GetMethodID(jc,"hellowFromJava","()V");
    //创建类对象
    jobject jobj=env->AllocObject(jc);
    //调用方法
    //env->CallVoidMethod(jobj,method);
    env->CallVoidMethod(instance,method);
}

/*
 * 回调带int参数的方法
 * */
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnitest_JniKit_callbackAdd(JNIEnv *env, jobject instance) {

    //加载类得到class对象
    jclass jc=env->FindClass("com/example/jnitest/JniKit");
    //得到对应方法的method对象
    jmethodID method=env->GetMethodID(jc,"add","(II)I");
    //创建类对象
    //jobject jobj=env->AllocObject(jc);
    //env->CallIntMethod(jobj,method,3,4);
    //调用方法
    env->CallIntMethod(instance,method,3,4);
}

/*
 * 回调带String参数的方法
 * */
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnitest_JniKit_callbackString(JNIEnv *env, jobject instance) {

    //加载类得到class对象
    jclass jc=env->FindClass("com/example/jnitest/JniKit");
    //得到对应方法的method对象
    jmethodID method=env->GetMethodID(jc,"displayString","(Ljava/lang/String;)V");
    jstring js=env->NewStringUTF("I am from C");
    env->CallVoidMethod(instance,method,js);
}

/*
 * 回调静态方法
 * */
extern "C"
JNIEXPORT void JNICALL
Java_com_example_jnitest_JniKit_callbackSayHellow(JNIEnv *env, jobject instance) {

    //加载类得到class对象
    jclass jc=env->FindClass("com/example/jnitest/JniKit");
    //得到对应方法的method对象
    jmethodID method=env->GetStaticMethodID(jc,"sayHellowStaic","(Ljava/lang/String;)V");
    //调用方法
    jstring js=env->NewStringUTF("I am from C");
    env->CallStaticVoidMethod(jc,method,js);


}

///////////////////////////////////////////////////////////

int pressure=20;

//得到锅炉的压力值
int getPressure() {
    pressure+=20;
    if (pressure>260){
        pressure=20;
    }
    return pressure;
}

/*
 * 锅炉压力系统
 * */

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_jnitest_JniKit_getPressure(JNIEnv *env, jobject instance) {

    int pressure=getPressure();
    return pressure;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_jnitest_JniKit_resetPressure(JNIEnv *env, jobject instance,jint process_) {
    pressure=process_;
}