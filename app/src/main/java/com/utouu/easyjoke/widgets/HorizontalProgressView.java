package com.utouu.easyjoke.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
/**
 * Create by 黄思程 on 2017/3/20  13:23
 * Function：
 * Desc：自定义带动画的水平进度条
 */
public class HorizontalProgressView extends View {
    private String descText = "";
    private int height;
    private int width;
    private int progress = 70;
    private float maxProgress = 100;
    private int progressHight = dip2px(5);
    private int textColor = Color.parseColor("#FF4959B9");
    private int bgColor = Color.parseColor("#FFC3C1C2");
    private RectF mRectF;
    private float textSize = 18;
    private Paint mPaint;
    private float textDis = dip2px(3);

    public HorizontalProgressView(Context context) {
        this(context,null);
    }

    public HorizontalProgressView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HorizontalProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        mRectF = new RectF();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(bgColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(mRectF,mPaint);
        mPaint.setColor(textColor);
        mRectF.right = mRectF.left + (progress/maxProgress)* width;
        canvas.drawRect(mRectF,mPaint);
        int textWidth = (int) mPaint.measureText(descText, 0, descText.length());
        mPaint.setTextSize(textSize);
        float x = mRectF.right - textWidth / 2;
        float y = mRectF.top - textSize/2 - textDis;
        if (x < getPaddingLeft()){
            x = getPaddingLeft();
        }else if (x >= width - textWidth){
            x = width- textWidth;
        }
        canvas.drawText(descText, x, y,mPaint);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = getHeight();
        width = getWidth();
        mRectF.left = getPaddingLeft();
        mRectF.top = getHeight()-progressHight;
        mRectF.right = getWidth();
        mRectF.bottom = getBottom();
    }

    /**
     * 设置描述文字
     * @param descText
     */
    public void setDescText(String descText) {
        this.descText = descText;
        invalidate();
    }

    /**
     * 设置当前的进度
     * @param progress
     */
    public void setCurrentProgress(int progress){
        this.progress = progress;
        this.descText = "已融资" + progress + "%";
        invalidate();
    }

    /**
     * 设置进度条的宽度
     * @param dp
     */
    public void setProgressHight(int dp){
        progressHight = dip2px(dp);
        invalidate();
    }

    private int dip2px( float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 设置文字和进度条之间的距离
     * @param dp
     */
    public void setTextDis(int dp){
        this.textDis = dip2px(dp);
        invalidate();
    }

}
