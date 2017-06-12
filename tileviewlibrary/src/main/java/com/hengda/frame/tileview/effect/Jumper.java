package com.hengda.frame.tileview.effect;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by baishiwei on 2015/07/08.
 */
public class Jumper {
    private Animation mAniDown;
    private Animation mAniUp;
    private AnimationSet mAniShadowDown;
    private AnimationSet mAniShadowUp;
    private SwitchAnimationListener mSwitchListener;
    private View mView;
    private View mShadow;

    /**
     * @param duration 每次跳跃和落下的动画持续时间
     * @param offset   动画跳起的高度(像素)
     */
    public Jumper(int duration, int offset) {
        mAniDown = new TranslateAnimation(0, 0, -offset, 0);
        mAniUp = new TranslateAnimation(0, 0, 0, -offset);
        mAniDown.setInterpolator(new AccelerateInterpolator());
        mAniUp.setInterpolator(new DecelerateInterpolator());
        mAniDown.setDuration(duration);
        mAniUp.setDuration(duration);
        Animation aniToRight = new TranslateAnimation(0, offset, 0, -offset / 2);
        Animation aniToLeft = new TranslateAnimation(offset, 0, -offset / 2, 0);
        Animation aniToSmall = new ScaleAnimation(1.0f, 0.7f, 1.0f, 0.7f);
        Animation aniToBig = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f);
        mAniShadowDown = new AnimationSet(false);
        mAniShadowDown.addAnimation(aniToLeft);
        mAniShadowDown.addAnimation(aniToBig);
        mAniShadowDown.setDuration(duration);
        mAniShadowUp = new AnimationSet(false);
        mAniShadowUp.addAnimation(aniToRight);
        mAniShadowUp.addAnimation(aniToSmall);
        mAniShadowUp.setDuration(duration);
        mSwitchListener = new SwitchAnimationListener();
        mAniDown.setAnimationListener(mSwitchListener);
        mAniUp.setAnimationListener(mSwitchListener);
        mAniShadowDown.setAnimationListener(mSwitchListener);
        mAniShadowUp.setAnimationListener(mSwitchListener);

    }

    /**
     * 将跳跃动画附到一个View
     *
     * @param view   准备显示跳跃动画的View
     * @param shadow 动画阴影(一般是半透明阴影图像的ImageView)，因为需要将阴影放于动画后面，因此View的上级Layout类型应选用允许相互覆盖的如RelativeLayout
     */
    public void attachToView(View view, View shadow) {
        mView = view;
        mShadow = shadow;
        if (mView != null)
            mView.startAnimation(mAniDown);
        if (mShadow != null)
            mShadow.startAnimation(mAniShadowDown);
    }

    /**
     * 动画切换侦听器
     */

    private class SwitchAnimationListener implements Animation.AnimationListener {
        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            if (mView == null)
                return;
            if (animation == mAniUp) {
                if (mView != null)
                    mView.startAnimation(mAniDown);
            } else if (animation == mAniDown) {
                if (mView != null)
                    mView.startAnimation(mAniUp);
            } else if (animation == mAniShadowUp) {
                if (mShadow != null)
                    mShadow.startAnimation(mAniShadowDown);
            } else if (animation == mAniShadowDown) {
                if (mShadow != null)
                    mShadow.startAnimation(mAniShadowUp);
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }
}