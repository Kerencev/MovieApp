package com.kerencev.movieapp.model.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(messageRes: Int, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, messageRes, length).show()
}

