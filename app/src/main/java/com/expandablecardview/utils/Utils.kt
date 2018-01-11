package com.expandablecardview.utils

import android.animation.ValueAnimator
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.AbsListView
import com.expandablecardview.base.ScrolledParent

/**
 * Created by SilenceDut on 16/8/21.
 */

internal object Utils {
    fun getScrolledParent(child: ViewGroup): ScrolledParent? {
        var parent: ViewParent? = child.parent
        var childBetweenParentCount = 0
        while (parent != null) {
            if (parent is RecyclerView || parent is AbsListView) {
                val scrolledParent = ScrolledParent()
                scrolledParent.scrolledView = parent as ViewGroup
                scrolledParent.childBetweenParentCount = childBetweenParentCount

                return scrolledParent
            }
            childBetweenParentCount++
            parent = parent.parent
        }
        return null
    }

    fun createParentAnimator(parent: View, distance: Int): ValueAnimator {

        val parentAnimator = ValueAnimator.ofInt(0, distance)

        parentAnimator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            internal var lastDy: Int = 0
            internal var dy: Int = 0
            override fun onAnimationUpdate(animation: ValueAnimator) {
                dy = animation.animatedValue as Int - lastDy
                lastDy = animation.animatedValue as Int
                parent.scrollBy(0, dy)
            }
        })

        return parentAnimator
    }


}
