package app.divarinterview.android.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

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

    this.setOnTouchListener { _, event ->
        when (event.action) {
            android.view.MotionEvent.ACTION_DOWN -> {
                scaleDown.start()
            }

            android.view.MotionEvent.ACTION_UP -> {
                scaleUp.start()
            }
        }
        false
    }
}