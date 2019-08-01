package cc.runa.richtextview.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.style.ImageSpan;

/**
 * 中心对齐图标
 *
 * @author Runa..
 */
public class AlignImageSpan extends ImageSpan {

    public AlignImageSpan(@NonNull Drawable drawable) {
        super(drawable);
    }

    public AlignImageSpan(@NonNull Drawable drawable, int verticalAlignment) {
        super(drawable, verticalAlignment);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom,
                     @NonNull Paint paint) {
        Drawable drawable = getDrawable();
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        // 计算y方向的位移
        int translationY = (y + fm.descent + y + fm.ascent) / 2 - drawable.getBounds().bottom / 2;
        canvas.save();
        // 绘制图片位移一段距离
        canvas.translate(x, translationY);
        drawable.draw(canvas);
        canvas.restore();
    }
}
