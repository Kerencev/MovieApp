package com.kerencev.movieapp.ui.details.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.MaterialToolbar

class BackgroundDetailsBehavior(context: Context, attrs: AttributeSet?=null) : CoordinatorLayout.Behavior<View>(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is MaterialToolbar
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        if (dependency is MaterialToolbar) {
            child.y = dependency.y
//            child.y = dependency.y + dependency.height
//            val params = child.layoutParams as CoordinatorLayout.LayoutParams
//            params.height = parent.height - dependency.height
//            child.layoutParams = params
        }
        return super.onDependentViewChanged(parent, child, dependency)
    }
}