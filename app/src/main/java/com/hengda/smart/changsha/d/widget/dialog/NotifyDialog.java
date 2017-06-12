package com.hengda.smart.changsha.d.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hengda.smart.changsha.d.R;

import org.w3c.dom.Text;


public class NotifyDialog extends Dialog {
    private BaseEffects baseEffects;
    private TextView swipFloor;
    private TextView mTitle;
    private Button sureDialog;
    WindowManager.LayoutParams params;

    public NotifyDialog(Context context) {
        super(context, R.style.hd_dialog_dim);
        init(context);
    }

    public NotifyDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        View dialogContainer = View.inflate(context, R.layout.notify_dialog, null);
        params = NotifyDialog.this.getWindow().getAttributes();
        params.gravity = Gravity.TOP;
        swipFloor = (TextView) dialogContainer.findViewById(R.id.content_notify);
        mTitle = (TextView) dialogContainer.findViewById(R.id.title_notify);
        sureDialog = (Button) dialogContainer.findViewById(R.id.btn_notify);

        setContentView(dialogContainer);
        setOnShowListener(dialogInterface -> {
            if (baseEffects != null) {
                baseEffects.setDuration(500);

            }
        });
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public NotifyDialog title(CharSequence title) {
        mTitle.setText(title);
        mTitle.setSelected(true);
        return this;
    }

    /**
     * 设置消息
     *
     * @param msg
     * @return
     */
    public NotifyDialog message(CharSequence msg) {
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
    public NotifyDialog message(int msg) {
        swipFloor.setVisibility(View.VISIBLE);
        swipFloor.setText(msg);
        return this;
    }


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
    public NotifyDialog pBtnText(CharSequence text) {
        sureDialog.setVisibility(View.VISIBLE);
        sureDialog.setText(text);
        return this;
    }

    /**
     * 确定按钮文字
     *
     * @param text
     * @return
     */
    public NotifyDialog pBtnText(int text) {
        sureDialog.setVisibility(View.VISIBLE);
        sureDialog.setText(text);
        return this;
    }


    /**
     * 确定按钮监听
     *
     * @param click
     * @return
     */
    public NotifyDialog pBtnClickListener(View.OnClickListener click) {
        sureDialog.setOnClickListener(click);
        return this;
    }


    /**
     * 设置Dlg出现动画
     * <p>
     * //     * @param baseEffects
     *
     * @return
     */
    public NotifyDialog baseEffects(BaseEffects baseEffects) {
        this.baseEffects = baseEffects;
        return this;
    }

    /**
     * 设置是否可以点击周围取消
     *
     * @param outsideCancelable
     * @return
     */
    public NotifyDialog outsideCancelable(boolean outsideCancelable) {
        setCanceledOnTouchOutside(outsideCancelable);
        return this;
    }

    /**
     * 设置字体
     *
     * @param typeface
     * @return
     */
    public NotifyDialog setTypeface(Typeface typeface) {
        swipFloor.setTypeface(typeface);
        sureDialog.setTypeface(typeface);
        mTitle.setTypeface(typeface);
        return this;
    }

}
