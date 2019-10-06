package com.si.sipanicalarm.utils

import android.view.View
import androidx.databinding.BindingAdapter
import com.si.sipanicalarm.R
import com.si.sipanicalarm.utils.extensions.loadImage
import de.hdodenhof.circleimageview.CircleImageView

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

    @BindingAdapter("url")
    @JvmStatic
    fun setImage(v:CircleImageView,url:String?){
        if(url!!.isEmpty()){
            v.loadImage("",R.drawable.ic_profile)
        }else{
            v.loadImage(url!!, R.drawable.ic_profile)
        }
    }
}