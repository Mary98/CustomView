package com.mary.customview_02.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.mary.customview_02.R;

/**
 * Created by Administrator on 2016/2/3.
 */
public class CustomImageView extends View {
    /**控件的宽度*/
    private int mWidth;
    /**控件的高度*/
    private int mHeight;
    /**图片的介绍*/
    private String mTitleText;
    /**文本大小*/
    private int mTitleTextSize;
    /**文本颜色*/
    private int mTitleTextColor;
    /**图片*/
    private Bitmap mImage;
    /**图片的缩放模式*/
    private int mImageScaleType;
    private static final int IMAGE_SCALE_FILXY = 0;
    private static final int IMAGE_SCALE_CENTER = 1;
    /**画笔*/
    private Paint mPaint;
    /**对文本的约束*/
    private Rect mRectBound;
    /**控制整体布局*/
    private Rect mRect;

    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化所持有自定义属性
        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.CustomImageView, defStyleAttr, 0);
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.CustomImageView_titleText2:
                    mTitleText = array.getString(attr);
                    break;
                case R.styleable.CustomImageView_titleTextSize2:
                    mTitleTextSize = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                       TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomImageView_titleTextColor2:
                    mTitleTextColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomImageView_image2:
                    mImage = BitmapFactory.decodeResource(getResources(), array.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomImageView_imageScaleType2:
                    mImageScaleType = array.getInt(attr, 0);
                    break;
            }

        }
        array.recycle();// 回收资源

        mRect = new Rect();
        mRectBound = new Rect();
        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        // 计算了描绘字体需要的范围
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRectBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 设置高度
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {// match_parent , accurate
            mWidth = specSize;
        } else {
            // 有图片决定的宽度
            int desireByImg = getPaddingLeft() + getPaddingRight() + mImage.getWidth();
            // 由字体决定的宽度
            int desireByTitle = getPaddingLeft() + getPaddingRight() + mRectBound.width();
            if (specMode == MeasureSpec.AT_MOST) {// wrap_content
                int desire = Math.max(desireByImg, desireByTitle);
                mWidth = Math.min(desire, specSize);
            }
        }

        // 设置高度
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {// match_parent , accurate
            mHeight = specSize;
        } else {
            int desire = getPaddingTop() + getPaddingBottom() + mImage.getHeight() + mRectBound.height();
            if (specMode == MeasureSpec.AT_MOST) {// wrap_content
                mHeight = Math.min(desire, specSize);
            }
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        /**边框*/
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.CYAN);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mRect.left   = getPaddingLeft();
        mRect.right  = mWidth - getPaddingRight();
        mRect.top    = getPaddingTop();
        mRect.bottom = mHeight - getPaddingBottom();

        mPaint.setColor(mTitleTextColor);
        mPaint.setStyle(Paint.Style.FILL);

        // 如果当前设置的宽度小于字体需要的宽度，将字体改为xxx...
        if (mRectBound.width() > mWidth) {
            TextPaint paint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(mTitleText, paint,
                    (float) mWidth - getPaddingLeft() - getPaddingRight(), TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg, getPaddingLeft(), mHeight - getPaddingBottom(), mPaint);
        } else {
            //正常情况，将字体居中
            canvas.drawText(mTitleText, mWidth / 2 - mRectBound.width() * 1.0f / 2,
                    mHeight - getPaddingBottom(), mPaint);
        }
        // 取消使用掉的模块
        mRect.bottom -= mRectBound.height();

        if (mImageScaleType == IMAGE_SCALE_FILXY) {
            canvas.drawBitmap(mImage, null, mRect, mPaint);
        } else {
            // 计算居中的矩形范围
            mRect.left = mWidth / 2 - mImage.getWidth() / 2;
            mRect.right = mWidth / 2 + mImage.getWidth() / 2;
            mRect.top = (mHeight - mRectBound.height()) / 2 - mImage.getHeight() / 2;
            mRect.bottom = (mHeight - mRectBound.height()) / 2 + mImage.getHeight() / 2;

            canvas.drawBitmap(mImage, null, mRect, mPaint);
        }
    }
}
