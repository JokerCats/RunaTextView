package cc.runa.richtextview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cc.runa.richtextview.span.AlignImageSpan;

/**
 * 可以修改文本样式的TextView
 *
 * @author Runa..
 */
public class RichTextView extends AppCompatTextView {

    private int mIgnore = -1;
    // 用来被替换的占位符
    private String mIconStr = "[$]";
    private int mIconLength;
    // 需要计算的图标个数
    private int mIconNum = 0;
    // 记录图标插入位置的容器
    private List<Integer> mIconJar;

    /**
     * 装饰对象
     */
    private SpannableString mSpannableStr;

    private OnLinkClickListener mListener;

    /**
     * 需要装饰的字符串
     */
    private CharSequence mDecorateStr, mOriginStr;

    public RichTextView(Context context) {
        super(context);
        initView();
    }

    public RichTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RichTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mIconJar = new ArrayList<>();
        mIconLength = mIconStr.length();
    }

    public void setLinkListener(OnLinkClickListener listener) {
        mListener = listener;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        mDecorateStr = text;
        mOriginStr = text;
        mSpannableStr = new SpannableString(mDecorateStr);
    }

    /**
     * 装载源字符串
     *
     * @param text 字符串
     */
    public RichTextView loadOrigin(CharSequence text) {
        setText(text);
        return this;
    }

    /**
     * 还原文本样式
     */
    public void reset() {
        setText(mOriginStr);
    }

    /**
     * 添加标签
     *
     * @param isHeader 是否添加至起始位置
     * @param label    标签内容
     */
    public RichTextView setLabel(boolean isHeader, String label) {
        if (isHeader) {
            updateDataSource(0, label);
        } else {
            updateDataSource(mDecorateStr.length(), label);
        }
        return this;
    }

    /**
     * 插入富文本图片
     *
     * @param start 插入索引
     * @param res   Drawable资源
     * @param align 是否居中对齐
     */
    public RichTextView insertImage(int start, int res, boolean align) {
        updateDataSource(start, mIconStr);
        if (align) {
            setRich(PropertyInt.IMAGE_ALIGN, start, start + mIconStr.length(), mIgnore, mIgnore, mIgnore, res);
        } else {
            setRich(PropertyInt.IMAGE, start, start + mIconStr.length(), mIgnore, mIgnore, mIgnore, res);
        }
        mIconJar.add(start);
        return this;
    }

    /**
     * 更新装饰数据源
     *
     * @param index 插入索引
     * @param str   插入内容
     */
    private void updateDataSource(int index, String str) {
        SpannableStringBuilder builder = new SpannableStringBuilder(mSpannableStr);
        builder.insert(index, str);
        setText(builder);
        mDecorateStr = builder.toString();
        mSpannableStr = new SpannableString(builder);
    }

    /**
     * 检查参数顺序
     *
     * @return true 正常  false 倒置
     */
    private boolean checkParamOrder(int start, int end) {
        return start < end;
    }

    /**
     * 检查是否插入图标 & 图标索引
     *
     * @param predict 预插入索引
     * @return 是否需要重新计算索引
     */
    private boolean checkIconIndex(int predict) {
        mIconNum = 0;
        for (Integer index : mIconJar) {
            if (index <= predict) {
                mIconNum += 1;
            }
        }
        return mIconNum > 0;
    }

    /**
     * 计算正确的索引
     *
     * @param origin 原始索引
     * @return 计算结果
     */
    private int calculateIndex(int origin) {
        return origin + (mIconLength - 1) * mIconNum;
    }

    /**
     * 回退原始的索引
     *
     * @param current 当前索引
     * @return 还原结果
     */
    private int reductionIndex(int current) {
        return current - (mIconLength - 1) * mIconNum;
    }

    /**
     * 设置文字颜色
     *
     * @param start 起始索引
     * @param end   结束索引
     * @param color 颜色ID
     */
    public RichTextView setForeground(int start, int end, int color) {
        if (checkParamOrder(start, end)) {
            if (checkIconIndex(start)) {
                setRich(PropertyInt.FOREGROUND, calculateIndex(start), calculateIndex(end), color, mIgnore, mIgnore, mIgnore);
            } else {
                setRich(PropertyInt.FOREGROUND, start, end, color, mIgnore, mIgnore, mIgnore);
            }
        }
        return this;
    }

    /**
     * 设置文字背景颜色
     *
     * @param start 起始索引
     * @param end   结束索引
     * @param color 颜色ID
     */
    public RichTextView setBackground(int start, int end, int color) {
        if (checkParamOrder(start, end)) {
            if (checkIconIndex(start)) {
                setRich(PropertyInt.BACKGROUND, calculateIndex(start), calculateIndex(end), color, mIgnore, mIgnore, mIgnore);
            } else {
                setRich(PropertyInt.BACKGROUND, start, end, color, mIgnore, mIgnore, mIgnore);
            }
        }
        return this;
    }

    /**
     * 设置字体 - 粗体
     *
     * @param start 起始索引
     * @param end   结束索引
     */
    public RichTextView setBold(int start, int end) {
        if (checkParamOrder(start, end)) {
            if (checkIconIndex(start)) {
                setRich(PropertyInt.BOLD, calculateIndex(start), calculateIndex(end), mIgnore, mIgnore, mIgnore, mIgnore);
            } else {
                setRich(PropertyInt.BOLD, start, end, mIgnore, mIgnore, mIgnore, mIgnore);
            }
        }
        return this;
    }

    /**
     * 设置字体 - 斜体
     *
     * @param start 起始索引
     * @param end   结束索引
     */
    public RichTextView setItalic(int start, int end) {
        if (checkParamOrder(start, end)) {
            if (checkIconIndex(start)) {
                setRich(PropertyInt.ITALIC, calculateIndex(start), calculateIndex(end), mIgnore, mIgnore, mIgnore, mIgnore);
            } else {
                setRich(PropertyInt.ITALIC, start, end, mIgnore, mIgnore, mIgnore, mIgnore);
            }
        }
        return this;
    }

    /**
     * 设置字体 - 粗斜体
     *
     * @param start 起始索引
     * @param end   结束索引
     */
    public RichTextView setBoldItalic(int start, int end) {
        if (checkParamOrder(start, end)) {
            if (checkIconIndex(start)) {
                setRich(PropertyInt.BOLD_ITALIC, calculateIndex(start), calculateIndex(end), mIgnore, mIgnore, mIgnore, mIgnore);
            } else {
                setRich(PropertyInt.BOLD_ITALIC, start, end, mIgnore, mIgnore, mIgnore, mIgnore);
            }
        }
        return this;
    }

    /**
     * 设置字体 - 正常
     *
     * @param start 起始索引
     * @param end   结束索引
     */
    public RichTextView setNormal(int start, int end) {
        if (checkParamOrder(start, end)) {
            if (checkIconIndex(start)) {
                setRich(PropertyInt.NORMAL, calculateIndex(start), calculateIndex(end), mIgnore, mIgnore, mIgnore, mIgnore);
            } else {
                setRich(PropertyInt.NORMAL, start, end, mIgnore, mIgnore, mIgnore, mIgnore);
            }
        }
        return this;
    }

    /**
     * 设置样式 - 删除线
     *
     * @param start 起始索引
     * @param end   结束索引
     */
    public RichTextView setStrikeLine(int start, int end) {
        if (checkParamOrder(start, end)) {
            if (checkIconIndex(start)) {
                setRich(PropertyInt.STRIKE, calculateIndex(start), calculateIndex(end), mIgnore, mIgnore, mIgnore, mIgnore);
            } else {
                setRich(PropertyInt.STRIKE, start, end, mIgnore, mIgnore, mIgnore, mIgnore);
            }
        }
        return this;
    }

    /**
     * 设置样式 - 下滑线
     *
     * @param start 起始索引
     * @param end   结束索引
     */
    public RichTextView setUnderLine(int start, int end) {
        if (checkParamOrder(start, end)) {
            if (checkIconIndex(start)) {
                setRich(PropertyInt.UNDERLINE, calculateIndex(start), calculateIndex(end), mIgnore, mIgnore, mIgnore, mIgnore);
            } else {
                setRich(PropertyInt.UNDERLINE, start, end, mIgnore, mIgnore, mIgnore, mIgnore);
            }
        }
        return this;
    }

    /**
     * 设置样式 - 上标
     *
     * @param start 起始索引
     * @param end   结束索引
     */
    public RichTextView setSuperscript(int start, int end) {
        if (checkParamOrder(start, end)) {
            if (checkIconIndex(start)) {
                setRich(PropertyInt.SUPERSCRIPT, calculateIndex(start), calculateIndex(end), mIgnore, mIgnore, mIgnore, mIgnore);
            } else {
                setRich(PropertyInt.SUPERSCRIPT, start, end, mIgnore, mIgnore, mIgnore, mIgnore);
            }
        }
        return this;
    }

    /**
     * 设置样式 - 下标
     *
     * @param start 起始索引
     * @param end   结束索引
     */
    public RichTextView setSubscript(int start, int end) {
        if (checkParamOrder(start, end)) {
            if (checkIconIndex(start)) {
                setRich(PropertyInt.SUBSCRIPT, calculateIndex(start), calculateIndex(end), mIgnore, mIgnore, mIgnore, mIgnore);
            } else {
                setRich(PropertyInt.SUBSCRIPT, start, end, mIgnore, mIgnore, mIgnore, mIgnore);
            }
        }
        return this;
    }

    /**
     * 设置文字大小
     *
     * @param start 起始索引
     * @param end   结束索引
     * @param isPx  是否使用像素单位
     * @param size  文字大小
     */
    public RichTextView setFontSize(int start, int end, boolean isPx, int size) {
        if (checkParamOrder(start, end)) {
            if (checkIconIndex(start)) {
                if (isPx) {
                    setRich(PropertyInt.SIZE_PX, calculateIndex(start), calculateIndex(end), mIgnore, size, mIgnore, mIgnore);
                } else {
                    setRich(PropertyInt.SIZE_DP, calculateIndex(start), calculateIndex(end), mIgnore, size, mIgnore, mIgnore);
                }
            } else {
                if (isPx) {
                    setRich(PropertyInt.SIZE_PX, start, end, mIgnore, size, mIgnore, mIgnore);
                } else {
                    setRich(PropertyInt.SIZE_DP, start, end, mIgnore, size, mIgnore, mIgnore);
                }
            }
        }
        return this;
    }

    /**
     * 设置文字比例
     *
     * @param start 起始索引
     * @param end   结束索引
     * @param scale 文字比例
     */
    public RichTextView setFontScale(int start, int end, float scale) {
        if (checkParamOrder(start, end)) {
            if (checkIconIndex(start)) {
                setRich(PropertyInt.SIZE_SCALE, calculateIndex(start), calculateIndex(end), mIgnore, mIgnore, scale, mIgnore);
            } else {
                setRich(PropertyInt.SIZE_SCALE, start, end, mIgnore, mIgnore, scale, mIgnore);
            }
        }
        return this;
    }

    /**
     * 设置富文本链接
     *
     * @param start 起始索引
     * @param end   结束索引
     */
    public RichTextView setLink(int start, int end) {
        if (checkParamOrder(start, end)) {
            if (checkIconIndex(start)) {
                setRich(PropertyInt.LINK, calculateIndex(start), calculateIndex(end), mIgnore, mIgnore, mIgnore, mIgnore);
            } else {
                setRich(PropertyInt.LINK, start, end, mIgnore, mIgnore, mIgnore, mIgnore);
            }
        }
        return this;
    }

    private void setRich(@PropertyInt int property, int start, int end, @ColorInt int color,
                         int size, float scale, @DrawableRes int res) {
        switch (property) {
            case PropertyInt.FOREGROUND:
                mSpannableStr.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                setText(mSpannableStr);
                break;
            case PropertyInt.BACKGROUND:
                mSpannableStr.setSpan(new BackgroundColorSpan(color), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                setText(mSpannableStr);
                break;
            case PropertyInt.BOLD:
                mSpannableStr.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                setText(mSpannableStr);
                break;
            case PropertyInt.ITALIC:
                mSpannableStr.setSpan(new StyleSpan(Typeface.ITALIC), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                setText(mSpannableStr);
                break;
            case PropertyInt.BOLD_ITALIC:
                mSpannableStr.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                setText(mSpannableStr);
                break;
            case PropertyInt.NORMAL:
                mSpannableStr.setSpan(new StyleSpan(Typeface.NORMAL), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                setText(mSpannableStr);
                break;
            case PropertyInt.STRIKE:
                mSpannableStr.setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                setText(mSpannableStr);
                break;
            case PropertyInt.UNDERLINE:
                mSpannableStr.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                setText(mSpannableStr);
                break;
            case PropertyInt.SUPERSCRIPT:
                mSpannableStr.setSpan(new SuperscriptSpan(), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                setText(mSpannableStr);
                break;
            case PropertyInt.SUBSCRIPT:
                mSpannableStr.setSpan(new SubscriptSpan(), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                setText(mSpannableStr);
                break;
            case PropertyInt.SIZE_PX:
                mSpannableStr.setSpan(new AbsoluteSizeSpan(size), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                setText(mSpannableStr);
                break;
            case PropertyInt.SIZE_DP:
                mSpannableStr.setSpan(new AbsoluteSizeSpan(size, true), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                setText(mSpannableStr);
                break;
            case PropertyInt.SIZE_SCALE:
                mSpannableStr.setSpan(new RelativeSizeSpan(scale), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                setText(mSpannableStr);
                break;
            case PropertyInt.IMAGE:
                Drawable drawable = getResources().getDrawable(res);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                mSpannableStr.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                setText(mSpannableStr);
                break;
            case PropertyInt.IMAGE_ALIGN:
                Drawable alignDrawable = getResources().getDrawable(res);
                alignDrawable.setBounds(0, 0, alignDrawable.getIntrinsicWidth(), alignDrawable.getIntrinsicHeight());
                mSpannableStr.setSpan(new AlignImageSpan(alignDrawable, ImageSpan.ALIGN_BASELINE), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                setText(mSpannableStr);
                break;
            case PropertyInt.LINK:
                final int tempIndex = start;
                ClickableSpan clickSpan = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        if (mListener != null) {
                            if (checkIconIndex(tempIndex)) {
                                mListener.clickLink(reductionIndex(tempIndex));
                            } else {
                                mListener.clickLink(tempIndex);
                            }
                            mIconNum = 0;
                        }
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        // 设置链接颜色
                        ds.setColor(Color.parseColor("#1A7DC2"));
                        // 设置链接下划线
                        ds.setUnderlineText(true);
                    }
                };

                mSpannableStr.setSpan(clickSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                setText(mSpannableStr);
                setMovementMethod(LinkMovementMethod.getInstance());
                setHighlightColor(Color.TRANSPARENT);
                break;
        }
    }

    public interface OnLinkClickListener {
        // 点击链接
        void clickLink(int startIndex);
    }

}
