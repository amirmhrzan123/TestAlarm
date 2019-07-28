package com.example.sialarm.utils.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import org.json.JSONObject
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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






