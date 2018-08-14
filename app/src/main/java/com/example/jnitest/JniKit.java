package com.example.jnitest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/6/4.
 */

public class JniKit {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private Context mContext;
    private String buff;

    public JniKit(Context mContext) {
        this.mContext = mContext;
    }

    /*
        * 定义native方法
        * 调用C代码对应方法*/
    public native String sayHellow();

    /*
    * 求两个数字的和*/
    public native int sum(int x,int y);

    /*
    * 将字符串拼接后返回
    * */
    public native String appendString(String content);

    /*
    * 将数组中的每个元素增加10
    * */
    public native int[] increaseArrayEles(int[] intArray);

    /*
    * 检查密码是否正确,如果正确返回200,否则返回400*/
    public native int checkPwd(String pwd);

    /////////////////////////////////////////////////////////////////////////C回调Java中的方法

    /*
    * 回调一般方法,无参无返回
    * */
    public native void callbackHellowFromJava();

    public void hellowFromJava(){
        buff="不为空";
        Toast.makeText(mContext,"java中的方法",Toast.LENGTH_LONG).show();
    }

    /*
    * 回调带int参数的方法
    * */
    public native void callbackAdd();

    public int add(int x,int y){
        int i = x + y;
        Toast.makeText(mContext,"计算结果为: "+i+buff,Toast.LENGTH_LONG).show();
        return i;
    }

    /*
    * 回调带String参数的方法
    * */
    public native void callbackString();

    public void displayString(String str){
        Toast.makeText(mContext,str,Toast.LENGTH_LONG).show();
    }

    /*
    * 回调静态方法
    * */
    public native void callbackSayHellow();

    public static void sayHellowStaic(String string){
        Log.e("===JniKit==","========="+"静态方法"+string);
    }

    ////////////////////////////////////////////////////锅炉压力系统

    /*
    * 获取压力值
    * */
    public native int getPressure();

    /*
    * 重置压力值
    * */
    public native int resetPressure(int process);
}
