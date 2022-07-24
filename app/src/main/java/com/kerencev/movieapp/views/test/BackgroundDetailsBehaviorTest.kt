package com.kerencev.movieapp.views.test

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.MaterialToolbar

class BackgroundDetailsBehaviorTest(context: Context, attrs: AttributeSet?=null) : CoordinatorLayout.Behavior<View>(context, attrs) {

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
        }
        return super.onDependentViewChanged(parent, child, dependency)
    }
}