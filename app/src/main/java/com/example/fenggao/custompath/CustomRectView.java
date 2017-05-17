package com.example.fenggao.custompath;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by feng.gao on 2017/5/3.
 */

public class CustomRectView extends View implements Runnable {
    private Context mContext;
    private Paint mPaint;
    public CustomRectView(Context context) {
        this(context , null);
    }

    public CustomRectView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public CustomRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    //  坐标轴 轴线 画笔：
    private Paint axisLinePaint;
    //  坐标轴水平内部 虚线画笔
    private Paint hLinePaint;
    //  绘制文本的画笔
    private Paint titlePaint;
    //  矩形画笔 柱状图的样式信息
    private Paint recPaint;

    private Paint mCirlcePaint;
    private Paint mGrayRectPaint;
    private Paint mLinePaint;
    private Handler handler = new Handler();
    List<Integer> listX = new LinkedList<>();
    List<Integer> listY = new LinkedList<>();
    private int rh;
    private int num;

    private void init(Context context, AttributeSet attrs)
    {
        listX.clear();
        listY.clear();
        axisLinePaint = new Paint();
        hLinePaint = new Paint();
        titlePaint = new Paint();
        recPaint = new Paint();
        mCirlcePaint = new Paint();
        mGrayRectPaint = new Paint();
        mLinePaint = new Paint();

        axisLinePaint.setColor(Color.DKGRAY);
        hLinePaint.setColor(Color.LTGRAY);
        titlePaint.setColor(Color.BLACK);
        mCirlcePaint.setColor(Color.WHITE);
        mCirlcePaint.setAntiAlias(true);
        mGrayRectPaint.setColor(Color.LTGRAY);
        mGrayRectPaint.setAntiAlias(true);
        mLinePaint.setColor(Color.DKGRAY);
        mLinePaint.setAntiAlias(true);


    }


    //7 条
    private int[] lastYear;


    //updata last year data
    public void updateLastData(int[] lastData)
    {
        lastYear = lastData;
        listY.clear();
        listX.clear();
        for (int i = 0; i < lastData.length; i++) {
            performAnimate(this , lastData[i] , i);
        }
//        this.postInvalidate();  //可以子线程 更新视图的方法调用。
    }


    private String[] yTitlesStrings =
            new String[]{"80000","60000","40000","20000","0"};

    private String[] xTitles =
            new String[]{"1","2","3"};


    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        listX.clear();
        listY.clear();
        int width = getWidth();
        int height = getHeight();

        // 1 绘制坐标线：
        canvas.drawLine(100, 10, 100, 520, axisLinePaint);

        canvas.drawLine(100, 520, width-10 , 520, axisLinePaint);

        // 2 绘制坐标内部的水平线

        int leftHeight = 500;// 左侧外周的 需要划分的高度：

        int hPerHeight = leftHeight/4;

        hLinePaint.setTextAlign(Paint.Align.CENTER);
//        for(int i=0;i<4;i++)
//        {
//            canvas.drawLine(100, 20+i*hPerHeight, width-10, 20+i*hPerHeight, hLinePaint);
//        }


        // 3 绘制 Y 周坐标

        Paint.FontMetrics metrics =titlePaint.getFontMetrics();
        int descent = (int)metrics.descent;
        titlePaint.setTextAlign(Paint.Align.RIGHT);
        for(int i=0;i<yTitlesStrings.length;i++)
        {
            canvas.drawText(yTitlesStrings[i], 80, 20+i*hPerHeight+descent, titlePaint);
        }

        // 4  绘制 X 周 做坐标

        int xAxisLength = width-300;
        int columCount = xTitles.length+1;
        int step = xAxisLength/columCount;

        if(lastYear != null && lastYear.length >0)
        {
            int thisCount = lastYear.length;


            for(int i=0;i<thisCount;i++)
            {
                int value = lastYear[i];

                 num = 8 - value / 10000 ;


                recPaint.setColor(0xFFAA1122);

                Rect rect = new Rect();
                Rect rectGray = new Rect();
                rectGray.left  = 100 + step * (i+1)  - 30;
                rectGray.right = 100 + step * (i+1)  + 30;


                rect.left  = 100 + step * (i+1)  - 30;
                rect.right = 100 + step * (i+1)  + 30;

//              当前的相对高度：
                int rh = (leftHeight * num) / 8 ;

                rect.top = rh + 20;
                rect.bottom = 520 ;
                rectGray.top = 20;
                rectGray.bottom = rh + 20 ;
                if (i == 0 || i == 3) {
                    recPaint.setShader(new LinearGradient(rect.left + 30, rect.top, rect.left + 30, rect.bottom, R.color.lightGreen, R.color.darkGreen, Shader.TileMode.CLAMP));

                } else {
                    recPaint.setShader(new LinearGradient(rect.left + 30, rect.top, rect.left + 30, rect.bottom, R.color.lightBlue, Color.DKGRAY, Shader.TileMode.CLAMP));

                }

                canvas.drawRect(rect, recPaint);
                canvas.drawRect(rectGray, mGrayRectPaint);

                canvas.drawCircle(rect.left + 30 , rect.top , 5 , mCirlcePaint);


                listX.add(i , rect.left + 30);
                listY.add(i ,  rect.top);
            }
            for (int i = 0; i < listX.size(); i++) {
                if (i == listX.size() - 1) {
                    return;
                } else {
                    canvas.drawLine(listX.get(i) , listY.get(i) , listX.get(i+1) , listY.get(i+1) , mLinePaint);
                }

            }
        }




    }

    public int getRh() {
        return rh;
    }

    public void setRh(int rh) {
        this.rh = rh;
    }

    @Override
    public void run() {
        rh += 1;
        if (rh > (300 * num) / 8) {
            return;
        } else {
            invalidate();
        }
    }

    private void performAnimate(final View target, final int end , final int i) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 1000);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            //持有一个IntEvaluator对象，方便下面估值的时候使用
            private IntEvaluator mEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                //获得当前动画的进度值，整型，1-100之间
                int currentValue = (Integer)animator.getAnimatedValue();

                //计算当前进度占整个动画过程的比例，浮点型，0-1之间
                float fraction = currentValue / 1000f;

                lastYear[i] = mEvaluator.evaluate(fraction, 1, end);
               target.postInvalidate();
            }
        });

        valueAnimator.setDuration(300).start();
    }
}
