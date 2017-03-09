package com.utouu.easyjoke.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;


public class CircleProgressView extends View {

    private int mMaxProgress = 100;

    private int mProgress = 30;

    private int progressBarStorkeWidth = 8;

    private float progressTextSize = 24;//进度值的字体大小
    private float descTextSize = 15;//描述文字的大小

    private int startPoint = 90;//起始角度,以逆时针进行控制

    private boolean isClockwise = false;//是否是顺时针，默认为逆时针

    private int progressBackgroundColor = Color.parseColor("#FFBDB9BA");//进度条的背景
    private int progressBgColor = Color.parseColor("#FFBAB0B4");//进度条背景颜色
    private int progresscColor = Color.parseColor("#FF89A4D4");//进度条颜色
    private int progressTextColor = Color.parseColor("#FFFCF6F8");//进度值文字
    private int progressDescColor = Color.parseColor("#FFEFE8EA");//描述文字的颜色

    private int progressInterval = 8;//进度条和外圆之间的距离

    private int textMargin = 14;

    // 画圆所在的距形区域
    private final RectF mRectF;

    private final Paint mPaint;

    private final Context mContext;

    private String mTxtHint;//描述的文字
    private int width;//控件的宽
    private int height;//控件的高
    private int padding;

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mRectF = new RectF();
        mPaint = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = this.getWidth();
        height = this.getHeight();
        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }
        int paddingRight = getPaddingRight();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        padding = Math.min(Math.min(paddingRight, paddingLeft), Math.min(paddingTop, paddingBottom));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        // 位置
        mRectF.left = padding; // 左上角x
        mRectF.top = padding; // 左上角y
        mRectF.right = width - padding; // 左下角x
        mRectF.bottom = height - padding; // 右下角y

        mPaint.setColor(progressBackgroundColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        canvas.drawCircle((mRectF.left + mRectF.right) / 2, (mRectF.top + mRectF.bottom) / 2, (mRectF.bottom - mRectF.top) / 2, mPaint);

        mRectF.left += progressInterval << 1; // 左上角x
        mRectF.top += progressInterval << 1; // 左上角y
        mRectF.right -= progressInterval << 1; // 左下角x
        mRectF.bottom -= progressInterval << 1; // 右下角y

        resetPaint();
        // 绘制圆圈，进度条背景
        canvas.drawArc(mRectF, -90, 360, false, mPaint);
        mPaint.setColor(progresscColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(mRectF, -startPoint, ((float) mProgress / mMaxProgress) * 360 * (isClockwise ? 1 : -1), false, mPaint);

        // 绘制进度文案显示
        String text = mProgress + "%";
        mPaint.setTextSize(progressTextSize);
        int textWidth = (int) mPaint.measureText(text, 0, text.length());
        mPaint.setColor(progressTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        if (TextUtils.isEmpty(mTxtHint)) {
            canvas.drawText(text, width / 2 - textWidth / 2, height / 2 + progressTextSize / 2, mPaint);
        } else {
            canvas.drawText(text, width / 2 - textWidth / 2, height / 2 + progressTextSize / 2 - textMargin, mPaint);
        }

        if (!TextUtils.isEmpty(mTxtHint)) {
            text = mTxtHint;
            mPaint.setColor(progressDescColor);
            mPaint.setTextSize(descTextSize);
            textWidth = (int) mPaint.measureText(text, 0, text.length());
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(text, width / 2 - textWidth / 2, 3 * height / 4 + descTextSize / 2 - textMargin, mPaint);
        }
    }

    /**
     * 设置画笔的相关属性
     */
    private void resetPaint() {
        mPaint.setAntiAlias(true);
        mPaint.setColor(progressBgColor);
        mPaint.setStrokeWidth(progressBarStorkeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
    }


    public CircleProgressView setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
        return this;
    }

    public CircleProgressView setProgress(int progress) {
        this.mProgress = progress;
        this.invalidate();
        return this;
    }

    public CircleProgressView setProgressNotInUiThread(int progress) {
        this.mProgress = progress;
        this.postInvalidate();
        return this;
    }


    public CircleProgressView setmTxtHint(String mTxtHint) {
        this.mTxtHint = mTxtHint;
        return this;
    }

    public CircleProgressView setProgressBgColor(int progressBgColor) {
        this.progressBgColor = progressBgColor;
        return this;
    }

    public CircleProgressView setProgresscColor(int progresscColor) {
        this.progresscColor = progresscColor;
        return this;
    }

    public CircleProgressView setProgressInterval(int progressInterval) {
        this.progressInterval = progressInterval;
        return this;
    }

    public CircleProgressView setProgressTextSize(float progressTextSize) {
        this.progressTextSize = progressTextSize;
        return this;
    }

    public CircleProgressView setDescTextSize(float descTextSize) {
        this.descTextSize = descTextSize;
        return this;
    }

    public CircleProgressView setProgressDescColor(int progressDescColor) {
        this.progressDescColor = progressDescColor;
        return this;
    }

    public CircleProgressView setProgressTextColor(int progressTextColor) {
        this.progressTextColor = progressTextColor;
        return this;
    }

    public CircleProgressView setProgressBarStorkeWidth(int progressBarStorkeWidth) {
        this.progressBarStorkeWidth = progressBarStorkeWidth;
        return this;
    }

    public CircleProgressView setProgressBackgroundColor(int progressBackgroundColor) {
        this.progressBackgroundColor = progressBackgroundColor;
        return this;
    }

    public CircleProgressView setStartPoint(int startPoint) {
        this.startPoint = startPoint;
        return this;
    }

    public CircleProgressView setClockwise(boolean clockwise) {
        isClockwise = clockwise;
        return this;
    }
}