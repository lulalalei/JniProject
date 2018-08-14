package com.example.jnitest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2018/6/6.
 */

public class PressureView extends View{

    private int pressure;
    private Paint mPaint;
    private String warnString="压力以达最大值";

    public PressureView(Context context) {
        this(context,null);
    }

    public PressureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PressureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(50);
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //如果压力值大于220,就绘制文本
        if (pressure>220){
            mPaint.setColor(Color.RED);
            canvas.drawText(warnString,10,getHeight()/2,mPaint);
        }else {
            mPaint.setColor(Color.GRAY);
            canvas.drawRect(10,10,60,260,mPaint);
            canvas.drawText("pressure="+pressure,getWidth()/2,getHeight()/2,mPaint);
            //如果是小于200,正常显示并且设置画笔颜色为绿色
            if (pressure<200){
                mPaint.setColor(Color.GREEN);
                canvas.drawRect(10,260-pressure,60,260,mPaint);
            }else{
                mPaint.setColor(Color.YELLOW);
                canvas.drawRect(10,260-pressure,60,260,mPaint);
            }
            //canvas.drawRect(10,260-pressure,60,260,mPaint);
        }
    }
}
