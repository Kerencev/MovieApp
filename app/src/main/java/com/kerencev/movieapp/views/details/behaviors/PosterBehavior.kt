package com.kerencev.movieapp.views.details.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView

class PosterBehavior(context: Context, attrs: AttributeSet?=null) : CoordinatorLayout.Behavior<View>(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is NestedScrollView
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        if (dependency is NestedScrollView) {
            child.y = dependency.y
        }
        return super.onDependentViewChanged(parent, child, dependency)
    }
}