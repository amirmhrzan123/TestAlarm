package com.example.sialarm.utils

import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.example.sialarm.R

object CommonUtils {
    fun openChooserDialog(activity: Activity, callback: (option:OPTION)->Unit) {

        val view = LayoutInflater.from(activity).inflate(R.layout.layout_bottomsheet, null)

        val mBottomSheetDialog = Dialog(activity,
            R.style.MaterialDialogSheet)
        mBottomSheetDialog.setContentView(view)
        mBottomSheetDialog.setCancelable(true)
        mBottomSheetDialog.setCanceledOnTouchOutside(true)
        mBottomSheetDialog.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        mBottomSheetDialog.window!!.setGravity(Gravity.BOTTOM)
        mBottomSheetDialog.show()

        val btnOptionPhoto = mBottomSheetDialog.findViewById<View>(R.id.btn_bs_option_photo) as Button
        val btnOptionVideo = mBottomSheetDialog.findViewById<View>(R.id.btn_bs_option_video) as Button
        val btnOptionCancel = mBottomSheetDialog.findViewById<View>(R.id.btn_bs_cancel_photo_gallery) as Button

        btnOptionPhoto.setOnClickListener {
            callback(CommonUtils.OPTION.ADDANOTHERCONTACT)
            mBottomSheetDialog.dismiss()
        }

        btnOptionVideo.setOnClickListener {
            callback(CommonUtils.OPTION.ADDFROMCONTACT)
            mBottomSheetDialog.dismiss()
        }

        btnOptionCancel.setOnClickListener { mBottomSheetDialog.dismiss() }

    }

   enum class OPTION{
       ADDFROMCONTACT,ADDANOTHERCONTACT
   }
}