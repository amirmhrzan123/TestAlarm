package com.example.sialarm.utils

import android.view.View
import androidx.databinding.BindingAdapter

object BindingUtils {
    @BindingAdapter("isVisible")
    @JvmStatic
    fun setIsVisible(v: View, b: Boolean) {
        if (b) {
            v.visibility = View.VISIBLE
        } else {
            v.visibility = View.GONE
        }
    }
}