package cc.runa.richtextview;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 具体修改文本属性
 *
 * @author Runa..
 */
@IntDef({

        PropertyInt.FOREGROUND,
        PropertyInt.BACKGROUND,
        PropertyInt.BOLD,
        PropertyInt.ITALIC,
        PropertyInt.BOLD_ITALIC,
        PropertyInt.NORMAL,
        PropertyInt.STRIKE,
        PropertyInt.UNDERLINE,
        PropertyInt.SUPERSCRIPT,
        PropertyInt.SUBSCRIPT,
        PropertyInt.SIZE_PX,
        PropertyInt.SIZE_DP,
        PropertyInt.SIZE_SCALE,
        PropertyInt.IMAGE,
        PropertyInt.LINK,
        PropertyInt.IMAGE_ALIGN
})

@Retention(RetentionPolicy.SOURCE)
public @interface PropertyInt {

    // 文本 - 颜色
    int FOREGROUND = 0;
    // 文本 - 背景颜色
    int BACKGROUND = 1;

    // 字体 - 粗体
    int BOLD = 2;
    // 字体 - 斜体
    int ITALIC = 3;
    // 字体 - 粗斜体
    int BOLD_ITALIC = 4;
    // 字体 - 正常
    int NORMAL = 5;

    // 样式 - 删除线
    int STRIKE = 6;
    // 样式 - 下滑线
    int UNDERLINE = 7;
    // 样式 - 上标
    int SUPERSCRIPT = 8;
    // 样式 - 下标
    int SUBSCRIPT = 9;

    // 文字 - 大小 (px)
    int SIZE_PX = 10;
    // 文字 - 大小 (dp)
    int SIZE_DP = 11;
    // 文字 - 大小 (scale)
    int SIZE_SCALE = 12;

    // 富文本 - 图片
    int IMAGE = 13;
    // 富文本 - 链接
    int LINK = 14;
    // 富文本 - 图片（中心对齐）
    int IMAGE_ALIGN = 15;

}
