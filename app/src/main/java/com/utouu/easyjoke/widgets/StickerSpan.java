package com.utouu.easyjoke.widgets;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * Created by llh on 2017/2/20 14:10.
 * Function: SpannableString 位文本替换图片
 * Desc: 可以控制图片在x，y轴上偏移量
 */

public class StickerSpan extends ImageSpan {
    /*x偏移*/
    private int xOffset;
    /*y偏移*/
    private int yOffset;

    public StickerSpan(Drawable b, int verticalAlignment, int xOffset, int yOffset) {
        super(b, verticalAlignment);
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text,
                     int start, int end, float x,
                     int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        canvas.save();
        canvas.translate(x + xOffset, bottom - b.getBounds().bottom + yOffset);
        b.draw(canvas);
        canvas.restore();
    }
}