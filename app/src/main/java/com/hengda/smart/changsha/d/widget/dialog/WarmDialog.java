package com.hengda.smart.changsha.d.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hengda.smart.changsha.d.R;


public class WarmDialog extends Dialog {


    private BaseEffects baseEffects;
private LinearLayout customPanel;
    private TextView swipFloor;


    public WarmDialog(Context context) {
        super(context,R.style.hd_dialog_dim);
        init(context);
    }

    public WarmDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        View dialogContainer = View.inflate(context, R.layout.tip_layout, null);

        swipFloor= (TextView) dialogContainer.findViewById(R.id.warm_tip);



        setContentView(dialogContainer);
        setOnShowListener(dialogInterface -> {
            if (baseEffects != null) {
                baseEffects.setDuration(500);
                baseEffects.start(customPanel);
            }
        });
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
//    public ListDialog title(CharSequence title) {
//        topPanel.setVisibility(View.VISIBLE);
//        mTitle.setText(title);
//        return this;
//    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
//    public ListDialog title(int title) {
//        topPanel.setVisibility(View.VISIBLE);
//        mTitle.setText(title);
//        return this;
//    }

    /**
     * 设置标题文字颜色
     *
     * @param color
     * @return
     */
//    public ListDialog titleColor(int color) {
//        mDivider.setVisibility(View.VISIBLE);
//        mTitle.setTextColor(color);
//        return this;
//    }

    /**
     * 设置Divider颜色
     *
     * @param color
     * @return
     */
//    public ListDialog dividerColor(int color) {
//        mDivider.setVisibility(View.VISIBLE);
//        mDivider.setBackgroundColor(color);
//        return this;
//    }

    /**
     * 设置消息
     *
     * @param msg
     * @return
     */
    public WarmDialog message(CharSequence msg) {
        swipFloor.setVisibility(View.VISIBLE);
        swipFloor.setText(msg);
        return this;
    }

    /**
     * 设置消息
     *
     * @param msg
     * @return
     */
    public WarmDialog message(int msg) {
        swipFloor.setVisibility(View.VISIBLE);
        swipFloor.setText(msg);
        return this;
    }

    /**
     * 设置消息文字颜色
     *
     * @param color
     * @return
     */
//    public ListDialog msgColor(int color) {
//        swipFloor.setTextColor(color);
//        return this;
//    }

    /**
     * 设置Dialog背景颜色
     *
     * @param color
     * @return
     */
//    public ListDialog dialogColor(int color) {
//        if (color == Color.TRANSPARENT) {
//            rootPanel.setBackgroundColor(color);
//        } else {
//            rootPanel.getBackground().setColorFilter(ColorUtils.getColorFilter(color));
//        }
//        return this;
//    }

    /**
     * 设置Dialog背景
     *
     * @param bgResId
     * @return
     */
//    public ListDialog dialogBg(int bgResId) {
//        rootPanel.setBackgroundResource(bgResId);
//        return this;
//    }
//
//    /**
//     * 设置Icon
//     *
//     * @param drawableResId
//     * @return
//     */
//    public ListDialog withIcon(int drawableResId) {
//        mIcon.setImageResource(drawableResId);
//        return this;
//    }

//    /**
//     * 设置按钮背景（自定义XML）
//     *
//     * @param resId
//     * @return
//     */
//    public ListDialog btnBg(int resId) {
//        mBtnP.setBackgroundResource(resId);
//        mBtnN.setBackgroundResource(resId);
//        return this;
//    }

    /**
     * 确定按钮文字
     *
     * @param text
     * @return
     */
    public WarmDialog pBtnText(CharSequence text) {

        return this;
    }

    /**
     * 确定按钮文字
     *
     * @param text
     * @return
     */
    public WarmDialog pBtnText(int text) {

        return this;
    }

    /**
     * 取消按钮文字
     *
     * @param text
     * @return
     */
    public WarmDialog nBtnText(CharSequence text) {

        return this;
    }

    /**
     * 取消按钮文字
     *
     * @param text
     * @return
     */
    public WarmDialog nBtnText(int text) {

        return this;
    }

    /**
     * 确定按钮监听
     *
     * @param click
     * @return
     */
    public WarmDialog pBtnClickListener(View.OnClickListener click) {

        return this;
    }

    /**
     * 取消按钮监听
     *
     * @param click
     * @return
     */
    public WarmDialog nBtnClickListener(View.OnClickListener click) {

        return this;
    }

    /**
     * 设置Dlg出现动画
     *
//     * @param baseEffects
     * @return
     */
    public WarmDialog baseEffects(BaseEffects baseEffects) {
        this.baseEffects = baseEffects;
        return this;
    }

//    /**
//     * 自定义Dialog主体部分
//     *
//     * @param view
//     * @return
//     */
//    public ListDialog setCustomView(View view) {
//        if (customPanel.getChildCount() > 0) {
//            customPanel.removeAllViews();
//        }
//        customPanel.addView(view);
//        customPanel.setVisibility(View.VISIBLE);
//        return this;
//    }

//    /**
//     * 自定义Dialog宽度
//     *
//     * @param width
//     * @return
//     */
    public WarmDialog setDlgWidth(int width) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = width;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return this;
    }


    public WarmDialog setDlgHeight() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return this;
    }

    /**
     * 设置是否可以取消
     *
     * @param cancelable
     * @return
     */
    public WarmDialog cancelable(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    /**
     * 设置是否可以点击周围取消
     *
     * @param outsideCancelable
     * @return
     */
    public WarmDialog outsideCancelable(boolean outsideCancelable) {
        setCanceledOnTouchOutside(outsideCancelable);
        return this;
    }

    /**
     * 设置字体
     *
     * @param typeface
     * @return
     */
    public WarmDialog setTypeface(Typeface typeface) {
        swipFloor.setTypeface(typeface);

        return this;
    }

}
