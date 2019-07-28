package com.example.sialarm.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
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

    fun getAddNumberDialog(context: Context,click:(String,String)->Unit, error:(String)->Unit): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_add_number)
        val window = dialog.window
        window!!.setGravity(Gravity.CENTER)
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        val inputEditText = dialog.findViewById<EditText>(R.id.etNumber)
        val userName = dialog.findViewById<EditText>(R.id.etName)
        val btnConfirm = dialog.findViewById<Button>(R.id.btnConfirm)
        val btnClose = dialog.findViewById<ImageView>(R.id.ivClose)
        inputEditText.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                btnConfirm.isEnabled = s.toString().isNotEmpty() && s.toString().length==10 && s?.first()=='9' && userName.text.toString().isNotEmpty()
            }

        })

        userName.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnConfirm.isEnabled = inputEditText.text.toString().isNotEmpty() && inputEditText.text.toString().length==10 && inputEditText.text.toString().first()=='9' && userName.text.toString().isNotEmpty()

            }

        })

        btnConfirm.setOnClickListener(View.OnClickListener {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(inputEditText.windowToken, 0)
           click(inputEditText.text.toString().trim(),userName.text.toString().trim())
            dialog.dismiss()
        })

        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        return dialog
    }

   enum class OPTION{
       ADDFROMCONTACT,ADDANOTHERCONTACT
   }
}