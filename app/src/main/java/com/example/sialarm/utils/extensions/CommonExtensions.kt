package com.example.sialarm.utils.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.sialarm.R
import org.json.JSONObject
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
Created by Prajeet on 1/16/2019, 4:08 PM
 **/

fun Context.isNetworkConnected():Boolean{
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork:NetworkInfo?=cm.activeNetworkInfo
    return activeNetwork?.isConnected==true
}

fun ViewModel.getCurrentTimeStamp(): String {
    return System.currentTimeMillis().toString()
}

fun ImageView.loadImage(url: String, placeholder: Int) {

    GlideApp.with(this.context).load(url).placeholder(R.drawable.ic_profile).into(this)
}


fun String.getNumber():String{
    var replacenumber = ""
    if(contains("+977")){
        replacenumber = replace("+977","")
    }else{
        replacenumber = this
    }
    return replacenumber.replace(" ","").replace("-","").replace("(","").replace(")","")
}

fun View.hideViewWithFadeOutAnimation(viewVisibility: Int = View.INVISIBLE) {
    animate()
            .alpha(0.0f)
            .setDuration(400)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    visibility = viewVisibility
                }
            })
}

fun View.showViewWithFadeInAnimation() {
    animate()
            .alpha(1.0f)
            .setDuration(10)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    visibility = View.VISIBLE
                }
            })
}

private val SECOND_MILLIS = 1000
private val MINUTE_MILLIS = 60 * SECOND_MILLIS
private val HOUR_MILLIS = 60 * MINUTE_MILLIS
private val DAY_MILLIS = 24 * HOUR_MILLIS


fun Long.getMessageDateTime(isMilliseconds: Boolean = false): String {
    val messageTime = Calendar.getInstance()

        if (!isMilliseconds) {
            messageTime.timeInMillis = this
            Log.d("millif", this.toLong().toString())
        }
        else
            messageTime.timeInMillis = this
        // get Currunt time
        val now = Calendar.getInstance()

        val strTimeFormate = "h:mm aa"
        val strDateFormate = "dd/MM/yyyy h:mm aa"

        return if (now.get(Calendar.DATE) === messageTime.get(Calendar.DATE)
            &&
            now.get(Calendar.MONTH) === messageTime.get(Calendar.MONTH)
            &&
            now.get(Calendar.YEAR) === messageTime.get(Calendar.YEAR)) {


            val now = now.timeInMillis
            println("now $now")
            val millis2 = messageTime.timeInMillis
            println("secon $millis2")
            val diff = now - millis2
            when {
                diff < MINUTE_MILLIS -> "Just now"
                diff < 2 * MINUTE_MILLIS -> "A minute ago"
                diff < 50 * MINUTE_MILLIS -> "${diff / MINUTE_MILLIS}  minutes ago"
                diff < 90 * MINUTE_MILLIS -> "An hour ago"
                diff < 24 * HOUR_MILLIS -> "${diff / HOUR_MILLIS}  hours ago"
                diff < 48 * HOUR_MILLIS -> "Yesterday"
                else -> "${diff / DAY_MILLIS} days ago"
            }

//        "Today at " + DateFormat.format(strTimeFormate, messageTime)

        } else if (now.get(Calendar.DATE) - messageTime.get(Calendar.DATE) === 1
            &&
            now.get(Calendar.MONTH) === messageTime.get(Calendar.MONTH)
            &&
            now.get(Calendar.YEAR) === messageTime.get(Calendar.YEAR)) {
            "Yesterday at " + DateFormat.format(strTimeFormate, messageTime)
        } else {
            try{
                "" + DateFormat.format(strDateFormate, messageTime)

            }catch(e:Exception){
                Log.d("Exeption",e.message.toString())
                ""
            }
        }

}

fun Long.milliseconds(): Long {
    //String date_ = date;
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    try {
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val mDate = sdf.parse(this.toString())
        val timeInMilliseconds = mDate.time
        println("Date in milli :: $timeInMilliseconds")
        return timeInMilliseconds
    } catch (e: ParseException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    }

    return 0
}


data class ErrorResponse(val title: String, val message: String)


/**
 * TODO Handles Exceptions
 */
fun Throwable.handleException(title: String, error: (errorResponse: ErrorResponse) -> Unit) {
    when (this) {
        is UnknownHostException -> error(ErrorResponse(title, "No internet connection.Please check your network connection and try again."))
        is SocketTimeoutException -> error(ErrorResponse(title, "Time out"))
        is NoSuchElementException -> error(ErrorResponse(title, "Something went wrong"))

        is HttpException ->
            try {
                val responseBody = this.response().errorBody()
                val jsonObject = JSONObject(responseBody?.string())
                when (this.code()) {
                    HttpURLConnection.HTTP_FORBIDDEN -> {
                        error(ErrorResponse(title, jsonObject.optString("message")))
                    }
                    456 -> {
                        error(ErrorResponse(title, jsonObject.optString("error")))
                    }

                    else -> {
                        if (jsonObject.has("message")) {
                            error(ErrorResponse(title, jsonObject.optString("message")))
                        } else {
                            error(ErrorResponse(title, "Something went wrong"))
                        }
                    }
                }
            } catch (e: Exception) {
                error(ErrorResponse(title, "Something went wrong"))
            }

        else -> {
            error(ErrorResponse(title, "Something went wrong"))
        }
    }
}

fun isConnectingToInternet(context: Context): Boolean {
    val connectivity = context.getSystemService(
        Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivity != null) {
        val info = connectivity.allNetworkInfo
        if (info != null)
            for (i in info)
                if (i.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
    }
    return false
}


fun getDistrictsKeyValue():MutableList<District>{
    val listDistricts : MutableList<District> = mutableListOf()
    listDistricts.add(0,District(1,"Bhojpur"))
    listDistricts.add(1,District(2,"Dhankuta"))
    listDistricts.add(2,District(3,"Ilam"))
    listDistricts.add(3,District(4,"Jhapa"))
    listDistricts.add(4,District(5,"Khotang"))
    listDistricts.add(5,District(6,"Morang"))
    listDistricts.add(6,District(7,"Okhaldhunga"))
    listDistricts.add(7,District(8,"Panchthar"))
    listDistricts.add(8,District(9,"Sankhuwasabha"))
    listDistricts.add(9,District(10,"Solukhumbu"))
    listDistricts.add(10,District(11,"Sunsari"))
    listDistricts.add(11,District(12,"Taplejung"))
    listDistricts.add(12,District(13,"Saptari"))
    listDistricts.add(13,District(14,"Siraha"))
    listDistricts.add(14,District(15,"Dhanusa"))
    listDistricts.add(15,District(16,"Mahottari"))
    listDistricts.add(16,District(17,"Sarlahi"))
    listDistricts.add(17,District(18,"Bara"))
    listDistricts.add(18,District(19,"Parsa"))
    listDistricts.add(19,District(20,"Rautahat"))
    listDistricts.add(20,District(21,"Sindhuli"))
    listDistricts.add(21,District(22,"Ramechhap"))
    listDistricts.add(22,District(23,"Dolakha"))
    listDistricts.add(23,District(24,"Ramechhap"))
    listDistricts.add(24,District(25,"Dolakha"))
    listDistricts.add(25,District(26,"Bhaktapur"))
    listDistricts.add(26,District(27,"Dhading"))
    listDistricts.add(27,District(28,"Kathmandu"))
    listDistricts.add(28,District(29,"Kavrepalanchok"))
    listDistricts.add(29,District(30,"Lalitpur"))
    listDistricts.add(30,District(31,"Nuwakot"))
    listDistricts.add(31,District(32,"Rasuwa"))
    listDistricts.add(32,District(33,"Sindhupalchok"))
    listDistricts.add(33,District(34,"Chitwan"))
    listDistricts.add(34,District(35,"Makwanpur"))
    listDistricts.add(35,District(36,"Baglung"))
    listDistricts.add(36,District(37,"Gorkha"))
    listDistricts.add(37,District(38,"Kaski"))
    listDistricts.add(38,District(39,"Lamjung"))
    listDistricts.add(39,District(40,"Manang"))
    listDistricts.add(40,District(41,"Mustang"))
    listDistricts.add(41,District(42,"Myagdi"))
    listDistricts.add(42,District(43,"Nawalpur"))
    listDistricts.add(43,District(44,"Parbat"))
    listDistricts.add(44,District(45,"Syangja"))
    listDistricts.add(45,District(46,"Tanahun"))
    listDistricts.add(46,District(47,"Kapilvastu"))
    listDistricts.add(47,District(48,"Parasi"))
    listDistricts.add(48,District(49,"Runpandehi"))
    listDistricts.add(49,District(50,"Arghakhanchi"))
    listDistricts.add(50,District(51,"Gulmi"))
    listDistricts.add(51,District(52,"Palpa"))
    listDistricts.add(52,District(53,"Dang"))
    listDistricts.add(53,District(54,"Pyuthan"))
    listDistricts.add(54,District(55,"Rolpa"))
    listDistricts.add(55,District(56,"Eastern Rukum"))
    listDistricts.add(56,District(57,"Banke"))
    listDistricts.add(57,District(58,"Bardiya"))
    listDistricts.add(58,District(59,"Western Rukum"))
    listDistricts.add(59,District(60,"Salyan"))
    listDistricts.add(60,District(61,"Humla"))
    listDistricts.add(61,District(62,"Jumla"))
    listDistricts.add(62,District(63,"Kalikot"))
    listDistricts.add(63,District(64,"Mugu"))
    listDistricts.add(64,District(65,"Surkhet"))
    listDistricts.add(65,District(66,"Dailekh"))
    listDistricts.add(66,District(67,"Jajarkot"))
    listDistricts.add(67,District(68,"Kailali"))
    listDistricts.add(68,District(68,"Accham"))
    listDistricts.add(69,District(70,"Doti"))
    listDistricts.add(70,District(71,"Bajhang"))
    listDistricts.add(71,District(72,"Bajura"))
    listDistricts.add(72,District(73,"Kachanpur"))
    listDistricts.add(73,District(74,"Dadeldhura"))
    listDistricts.add(74,District(75,"Baitedi"))
    listDistricts.add(75,District(76,"Darchula"))
    return listDistricts

}


data class District(val id: Int,
                    val name:String)

data class State(val id:Int,
                 val name:String)





