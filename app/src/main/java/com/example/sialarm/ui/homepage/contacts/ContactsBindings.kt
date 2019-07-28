package com.example.sialarm.ui.homepage.contacts

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.sialarm.data.firebase.Friends
import de.hdodenhof.circleimageview.CircleImageView

object ContactsBindings {

    @BindingAdapter("initialLetter")
    @JvmStatic
    fun setFirstLetter(tvName: TextView, friend:Friends) {
       with(tvName){
           text = friend.name.substring(0,1).capitalize()
       }
    }

    @BindingAdapter("firstNameColor")
    @JvmStatic
    fun setFirstLetter(cv: CircleImageView, friend:Friends) {
        with(cv){

        }
    }


}