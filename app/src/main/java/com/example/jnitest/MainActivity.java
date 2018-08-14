package com.example.jnitest;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.StreamHandler;

/*
* https://blog.csdn.net/ccg_201216323/article/details/54563825
* */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView sample_text, add_text, append_text, increase_text, check_text;
    TextView empty_text, addFromC_text, stringFomeC_text, displayStringFromC_text;
    PressureView pressure_pv;

    EditText check_et;

    private JniKit jniKit;

    private String buff = "";

    private int pressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        sample_text = (TextView) findViewById(R.id.sample_text);
        add_text = findViewById(R.id.add_text);
        append_text = findViewById(R.id.append_text);
        increase_text = findViewById(R.id.increase_text);
        check_text = findViewById(R.id.check_text);
        check_et = findViewById(R.id.check_et);
        empty_text = findViewById(R.id.empty_text);
        addFromC_text = findViewById(R.id.addFromC_text);
        stringFomeC_text = findViewById(R.id.stringFomeC_text);
        displayStringFromC_text = findViewById(R.id.displayStringFromC_text);
        pressure_pv = findViewById(R.id.pressure_pv);

        add_text.setOnClickListener(this);
        append_text.setOnClickListener(this);
        increase_text.setOnClickListener(this);
        check_text.setOnClickListener(this);
        empty_text.setOnClickListener(this);
        addFromC_text.setOnClickListener(this);
        stringFomeC_text.setOnClickListener(this);
        displayStringFromC_text.setOnClickListener(this);

        jniKit = new JniKit(this);
        sample_text.setText(jniKit.sayHellow());

        buff = "onCreate";

        new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    SystemClock.sleep(500);
                    pressure = Math.abs(jniKit.getPressure());
                    pressure_pv.setPressure(pressure);
                }
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_text:
                addMethod();
                break;
            case R.id.append_text:
                appendString();
                break;
            case R.id.increase_text:
                increaseArray();
                break;
            case R.id.check_text:
                checkPwd();
                break;
            case R.id.empty_text:
                invokeVoidMethodFromC();
                break;
            case R.id.addFromC_text:
                invokeIntMethod();
                break;
            case R.id.stringFomeC_text:
                invokeStringMethod();
                break;
            case R.id.displayStringFromC_text:
                invokeStaticStringMethod();
                break;
            default:
                break;
        }
    }

    public void hellowFromJava() {
        Log.e("===MainA==", "=========" + buff);
    }

    private void checkPwd() {
        String trim = check_et.getText().toString().trim();
        check_text.setText("结果: " + jniKit.checkPwd(trim));
    }

    private void increaseArray() {
        int[] array = new int[]{0, 1, 2};
        int[] arrayEles = jniKit.increaseArrayEles(array);
        String buff = "";
        for (int i = 0; i < arrayEles.length; i++) {
            buff = buff + arrayEles[i] + ",";
        }
        increase_text.setText("改变结果为:" + buff);
    }

    private void appendString() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                final String hellow = jniKit.appendString("hellow");
                append_text.post(new Runnable() {
                    @Override
                    public void run() {
                        append_text.setText("拼接结果为: " + hellow);
                    }
                });
            }
        }.start();
    }

    private void addMethod() {
        add_text.setText("结果为:" + jniKit.sum(10, 6));
    }

    private void invokeVoidMethodFromC() {
        jniKit.callbackHellowFromJava();
    }

    /*
   * 回调带int参数的方法
   * */
    private void invokeIntMethod() {
        jniKit.callbackAdd();
    }

    /*
    * 回调带string参数的方法
    * */
    private void invokeStringMethod() {
        jniKit.callbackString();
    }

    /*
    * 回调带string参数的静态方法*/
    private void invokeStaticStringMethod() {
        jniKit.callbackSayHellow();
    }
}
