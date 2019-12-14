package com.liyuanjinglyj.csdnserachapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class CSDNSerachView extends ViewGroup {

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;//整体宽度
        int height = 0;//整体高度
        int lineWidth = 0;//记录每一行的宽度
        int lineHeight = 0;//记录每一行的高度
        int count = getChildCount();//获取子控件数量
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (lineWidth + childWidth > measureWidth) {
                //如果当前行占位Textview+新占位如果当前行占位Textview大于整体宽度，就需要换行设置控件
                width = Math.max(lineWidth, width);//获取最大值的宽度值
                height += lineHeight;//换行后直接加上宽度就是整体宽度
                //因为当前行放不下控件，所以宽高直接等于新换行的控件宽高
                lineWidth = childWidth;
                lineHeight = childHeight;
            } else {
                //否则
                lineWidth += childWidth;//如果控件没有换行，就是之前的宽度加上新TextView宽度
                lineHeight = Math.max(lineHeight, childHeight);//而如果之前高度高于当前子控件高度，那么该行还是之前的宽度，否则为新控件高度
            }
            //因为最后一行不管填不填满，高度都需要加上最后一行所以,没经过最后换行的操作，上面是不会计算加最后一行的高度的，所以必须单独写出来
            if (i == count - 1) {
                height += lineHeight;
                width = Math.max(width, lineWidth);
            }
        }

        setMeasuredDimension((measureWidthMode == MeasureSpec.EXACTLY) ? measureWidth : width,
                (measureHeightMode == MeasureSpec.EXACTLY) ? measureHeight : height);
    }

    public CSDNSerachView(Context context) {
        super(context);
    }

    public CSDNSerachView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CSDNSerachView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int lineWidth = 0;
        int lineHeight = 0;
        int top = 0, left = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if (childWidth + lineWidth > getMeasuredWidth()) {
                //如果换行,与onMeasure基本类同
                top += lineHeight;
                left = 0;
                lineHeight = childHeight;
                lineWidth = childWidth;
            } else {
                lineHeight = Math.max(lineHeight, childHeight);
                lineWidth += childWidth;
            }
            int lc = left + lp.leftMargin;
            int tc = top + lp.topMargin;
            int rc = lc + child.getMeasuredWidth();
            int bc = tc + child.getMeasuredHeight();
            child.layout(lc, tc, rc, bc);
            left += childWidth;
        }
    }
}
