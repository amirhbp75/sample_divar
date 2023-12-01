package app.divarinterview.android.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

@SuppressLint("ClickableViewAccessibility")
fun View.clickButtonAnimation() {
    val scaleDownX = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 0.9f)
    val scaleDownY = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 0.9f)
    val scaleUpX = ObjectAnimator.ofFloat(this, "scaleX", 0.9f, 1.0f)
    val scaleUpY = ObjectAnimator.ofFloat(this, "scaleY", 0.9f, 1.0f)

    val scaleDown = AnimatorSet().apply {
        play(scaleDownX).with(scaleDownY)
        duration = 100
        interpolator = android.view.animation.AccelerateDecelerateInterpolator()
    }

    val scaleUp = AnimatorSet().apply {
        play(scaleUpX).with(scaleUpY)
        duration = 100
        interpolator = android.view.animation.AccelerateDecelerateInterpolator()
    }

    setOnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                scaleDown.start()
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                scaleUp.start()
            }
        }
        false
    }
}