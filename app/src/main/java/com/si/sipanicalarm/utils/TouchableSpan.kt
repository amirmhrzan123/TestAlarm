package com.si.sipanicalarm.utils

import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.ClickableSpan

abstract class TouchableSpan(private val normalTextColor: Int,
                             private val pressedTextColor: Int,
                             private val underline: Boolean = false,
                             private val style: Typeface = Typeface.DEFAULT_BOLD) : ClickableSpan() {
    private var mIsPressed: Boolean = false

    fun setPressed(isSelected: Boolean) {
        mIsPressed = isSelected
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = if (mIsPressed) pressedTextColor else normalTextColor
        ds.isUnderlineText = underline
        ds.typeface = style
    }
}