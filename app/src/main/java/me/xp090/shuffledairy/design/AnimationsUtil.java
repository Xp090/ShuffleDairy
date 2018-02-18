package me.xp090.shuffledairy.design;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

/**
 * Created by Xp090 on 24/01/2018.
 */

public class AnimationsUtil {

    public static AnimationSet slideDown(int offset, int duration) {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                0,
                0,
                offset,
                0);
        translateAnimation.setDuration(duration);
      //  translateAnimation.setFillAfter(true);

        AlphaAnimation alphaAnimation = fadeIn(duration);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        return animationSet;
    }

    public static AnimationSet slideUp(int offset, int duration) {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                0,
                0,
                0,
                offset);
        translateAnimation.setDuration(duration);
       // translateAnimation.setFillAfter(true);

        AlphaAnimation alphaAnimation = fadeOut(duration);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        return animationSet;
    }

    public static AlphaAnimation fadeIn(int duration) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(duration);
        return alphaAnimation;
    }
    public static AlphaAnimation fadeOut(int duration) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(duration);
        return alphaAnimation;
    }

    public static class NewNoteAnimListener implements Animation.AnimationListener {

        private View view;
        private int visibility;

        public NewNoteAnimListener(View view, int visibility) {
            this.view = view;
            this.visibility = visibility;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            if (visibility == View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (visibility == View.GONE) {
                view.setVisibility(View.GONE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

}
