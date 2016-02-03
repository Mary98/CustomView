package com.mary.customview_01.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.mary.customview_01.R;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 验证码
 * Created by Administrator on 2016/2/2.
 */
public class SecurityView extends View {
    /**显示文本*/
    private String mTitleText;
    /**文本颜色*/
    private int mTitleTextColor;
    /**文本大小*/
    private int mTitleTextSize;
    /**背景颜色*/
    private int mTitleTextBackground;
    /**绘制时控制文本绘制的范围 */
    private Rect mRect;
    /**画笔*/
    private Paint mPaint;

    public SecurityView(Context context) {
        this(context, null);
    }

    public SecurityView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SecurityView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 获取自定义属性
         */
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.SecurityView, defStyleAttr, 0);
        int n = typedArray.getIndexCount();// 该控件设置了多小个自定义属性
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.SecurityView_titleText1:
                    mTitleText = typedArray.getString(attr);
                    break;
                case R.styleable.SecurityView_titleTextColor1:
                    // 默认颜色设置为黑色(第二参数为默认值)
                    mTitleTextColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.SecurityView_titleTextSize1:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    mTitleTextSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.SecurityView_titleTextBackground1:
                    // 默认颜色设置为白色(第二参数为默认值)
                    mTitleTextBackground = typedArray.getColor(attr, Color.WHITE);
                    break;
            }
        }
        typedArray.recycle();// 回收资源

        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        mPaint.setColor(mTitleTextColor);

        mRect = new Rect();
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);

        // 添加事件
        addEvent();

    }

    /**
     * 添加事件
     */
    private void addEvent() {
        this.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                mTitleText = randomText(4);
                postInvalidate();
            }
        });
    }

    /**
     * 产生随机数
     * @param length 数字长度
     * @return
     */
    private String randomText(int length) {
        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() < length) {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : set) {
            sb.append(String.valueOf(i));
        }
        return sb.toString();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 背景
        mPaint.setColor(mTitleTextBackground);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        // 文字
        mPaint.setColor(mTitleTextColor);
        canvas.drawText(mTitleText, getWidth() / 2 - mRect.width() / 2,
                getHeight() / 2 + mRect.height() / 2, mPaint);

        // 噪点
        int[] points;
        mPaint.setColor(Color.BLACK);
        for (int i = 0; i < 30; i++) {
            points = getPoint(getMeasuredWidth(), getMeasuredHeight());
            canvas.drawCircle(points[0], points[1], 8, mPaint);
        }

        int[] lines;
        for (int i = 0; i < 8; i++) {
            lines = getLine(getMeasuredWidth(), getMeasuredHeight());
            canvas.drawLine(lines[0], lines[1], lines[2], lines[3], mPaint);
        }

    }

    /**
     * 获取小圆点
     * @param width
     * @param height
     * @return
     */
    private int[] getPoint(int width, int height) {
        int[] temp = {0, 0, 0, 0};
        temp[0] = (int) (Math.random() * width);
        temp[1] = (int) (Math.random() * height);
        return temp;
    }

    /**
     * 获取干扰线
     * @param width
     * @param height
     * @return
     */
    private int[] getLine(int width, int height) {
        int[] temp = {0, 0, 0, 0};
        for (int i = 0; i < 4; i+=2) {
            temp[i] = (int) (Math.random() * width);
            temp[i + 1] = (int) (Math.random() * height);
        }
        return temp;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        // 获取宽度
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);
            float textWidth = mRect.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        // 获取高度
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);
            float textHeight = mRect.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }
        setMeasuredDimension(width, height);
    }
}
