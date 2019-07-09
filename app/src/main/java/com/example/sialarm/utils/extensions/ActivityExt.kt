package com.example.sialarm.utils.extensions

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import android.util.DisplayMetrics
import android.widget.TextView
import com.example.sialarm.R


/**
Created by Prajeet on 1/16/2019, 9:32 AM
 **/



fun Activity.getDeviceId(): String {
    return Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
}



fun Activity.showAlertDialog(message: String, positiveText: String = "Ok", negativeText: String = "Cancel") {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setTitle(resources.getString(R.string.app_name))
    builder.setMessage(message)
    builder.setPositiveButton(positiveText) { dialog, _ ->
        dialog.dismiss()
    }

    builder.setNegativeButton(negativeText) { dialog, _ ->
        dialog.dismiss()
    }

    val alertDialog = builder.create()
    alertDialog.show()
}

fun Activity.showValidationDialog(title: String = resources.getString(R.string.app_name), message: String, positiveText: String = "Ok") {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton(positiveText) { dialog, _ ->
        dialog.dismiss()
    }

    val alertDialog = builder.create()
    alertDialog.show()
}

fun Activity.showConfirmationDialog(title: String = resources.getString(R.string.app_name), message: String, positiveText: String = "Ok", negativeText: String = "Cancel", ok: () -> Unit) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setCancelable(false)
    builder.setMessage(message)
    builder.setPositiveButton(positiveText) { dialog, _ ->
        dialog.dismiss()
        ok()
    }

    builder.setNegativeButton(negativeText) { dialog, _ ->
        dialog.dismiss()
    }

    val alertDialog = builder.create()
//    val negative = alertDialog.getActionButton(DialogAction.NEGATIVE)
//    val positive = builder.getActionButton(DialogAction.POSITIVE)
//    negative.setAllCaps(false)
//    positive.setAllCaps(false)
    alertDialog.show()
}





fun Activity.showSuccessDialog(title: String = resources.getString(R.string.app_name), message: String, positiveText: String = resources.getString(R.string.tv_ok), negativeText: String = "Cancel", ok: () -> Unit) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setCancelable(false)
    builder.setMessage(message)
    builder.setPositiveButton(positiveText) { dialog, _ ->
        dialog.dismiss()
        ok()
    }

    val alertDialog = builder.create()
    alertDialog.show()
}

fun Activity.showNotCancellableAlert(title: String, message: String, callback: AlerDialogCallback) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.tv_ok) { dialog, _ ->
                dialog.dismiss()
                callback.onPositiveButtonClicked()
            }

            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
                callback.onPositiveButtonClicked()
            }
    val alertDialog = builder.create()

    alertDialog.setCancelable(false)
    alertDialog.show()
}


/**
 * Method for removing the keyboard if touched outside the editview.
 *
 * @param view
 * @param activity
 */
fun Activity.setupUI(view: View, activity: Activity) {

    //Set up touch listener for non-text box views to hide keyboard.
    if (view !is EditText) {
        view.setOnTouchListener { v, event ->
            //                    baseActivity.closeKeyboard();
            hideSoftKeyboard(activity)
            view.clearFocus()
            false
        }
    }

    //If a layout container, iterate over children and seed recursion.
    if (view is ViewGroup) {

        for (i in 0 until view.childCount) {

            val innerView = view.getChildAt(i)

            setupUI(innerView, activity)
        }
    }
}

fun hideSoftKeyboard(activity: Activity) {
    // Check if no view has focus: and hide the soft keyboard
    val view = activity.currentFocus
    //Checking if view!=null
    // to prevent java.lang.NullPointerException: Attempt to invoke virtual method 'android.os.IBinder android.view.View.getWindowToken()' on a null object reference
    if (view != null) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

interface AlerDialogCallback {
    fun onPositiveButtonClicked()
    fun onNegativeButtonClicked()
}


/**
 * This method converts dp unit to equivalent pixels, depending on device density.
 *
 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent px equivalent to dp depending on device density
 */
fun Activity.convertDpToPixel(dp: Float): Float {
    return dp * (this.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

/**
 * This method converts device specific pixels to density independent pixels.
 *
 * @param px A value in px (pixels) unit. Which we need to convert into db
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent dp equivalent to px value
 */
fun Activity.convertPixelsToDp(px: Float): Float {
    return px / (this.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}



