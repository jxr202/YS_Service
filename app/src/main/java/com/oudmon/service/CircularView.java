package com.oudmon.service;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by jxr20 on 2017/6/9
 */

public class CircularView extends View {

    private float degree = 0;
    private Paint mPaint;
    private Paint mPaintGray1;
    private Paint mPaintGray2;
    private Paint mPaintWhite;
    private RectF mRect1;
    private RectF mRect2;
    private int bold = 30;
    private boolean isPlaying = false;
    private ObjectAnimator circleAnimator;

    public CircularView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mRect1 = new RectF(15, 15, 585, 585);
        mRect2 = new RectF(23, 23, 577, 577);
        mPaint = new Paint() {{
            setAntiAlias(true);
            setStyle(Style.STROKE);
            setStrokeWidth(bold);
            setColor(0xffFFBF42);
        }};
        mPaintGray1 = new Paint() {{
            setAntiAlias(true);
            setStyle(Style.STROKE);
            setStrokeWidth(30);
            setColor(0xffD0D0D0);
        }};
        mPaintGray2 = new Paint() {{
            setAntiAlias(true);
            setStyle(Style.STROKE);
            setStrokeWidth(45);
            setColor(0xffEEEEEE);
        }};
        mPaintWhite = new Paint() {{
            setAntiAlias(true);
            setColor(0xffFFFFFF);
        }};
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(300, 300, 300, mPaintWhite);
        canvas.drawArc(mRect2, 0, 360, false, mPaintGray2);
        canvas.drawArc(mRect1, 0, 360, false, mPaintGray1);
        if (mRect1 != null) {
            canvas.drawArc(mRect1, 0, degree, false, mPaint);
        }
    }

    public void startAnimator() {
        if (isPlaying) {
            return;
        }
        isPlaying = true;

        circleAnimator = ObjectAnimator.ofFloat(this, "degree", 0, 360);
        circleAnimator.setDuration(10000);
        circleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                EventBus.getDefault().post(new ClickCallback(true));
                isPlaying = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                EventBus.getDefault().post(new ClickCallback(false));
                isPlaying = false;
                setDegree(0f);
            }
        });
        circleAnimator.start();
    }

    public void stopAnimator() {
        if (isPlaying && circleAnimator != null) {
            circleAnimator.cancel();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 处理接受消息的方法  “subscriber methods”
     * 也可以使用注释
     * @link http://greenrobot.org/eventbus/documentation/how-to-get-started/
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Click event) {
        if (event.start) {
            startAnimator();
        } else {
            stopAnimator();
        }
    }

    public void setDegree(float degree) {
        this.degree = degree;
        invalidate();
    }

}
