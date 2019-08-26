package com.example.sialarm.data.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.sialarm.R
import com.example.sialarm.data.prefs.PrefsManager
import com.example.sialarm.data.room.NotificationCountTable
import com.example.sialarm.data.room.dao.NotificationDao
import com.example.sialarm.ui.homepage.HomeActivity
import com.example.sialarm.utils.Extras
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import android.media.AudioAttributes



class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val prefs: PrefsManager by inject()


    private val viewModelScoper: CoroutineScope by inject()

    private val notificationDao: NotificationDao by inject()


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage?.from}")

        viewModelScoper.launch {
            withContext(Dispatchers.IO){

                try{
                    var databaseCount = notificationDao.getBadgeCount("1")

                    databaseCount += 1
                    val notificationCountTable = NotificationCountTable(
                        id = "1",
                        count = databaseCount
                    )
                    notificationDao.updateNotificationCount(notificationCountTable)
                }catch(e:Exception){
                    println("Error"+ e.message)
                }

            }
        }


        // Check if message contains a notification payload.
        remoteMessage?.notification?.let {
            var notificationTypeId = 0
            remoteMessage.data?.isNotEmpty()?.let {
                for(key in remoteMessage.data.entries){
                    //Log.d(key,intent.extras!!.getString(key))
                    if(key.key.contains("notification_type_id")) {
                        notificationTypeId = key.value.toString().toInt()
                    }
                    }
            }



            sendNotification(it.title!!,it.body!!,notificationTypeId)
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    // [START on_new_token]
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String?) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private fun scheduleJob() {
        // [START dispatch_job]
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
        WorkManager.getInstance().beginWith(work).enqueue()
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.


    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */


    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }

    private fun sendNotification(title:String,message:String,notificationTypeId:Int) {

        var intent : Intent? = null

                intent = Intent(this, HomeActivity::class.java)
                intent.putExtra(Extras.FROMNOTIFICATION, true)

        val uniqueInt = (System.currentTimeMillis() and 0xff).toInt()


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        var channelId = ""
        var notificationBuilder:NotificationCompat.Builder?=null

        var defaultSoundUri:Uri?=null
        if(notificationTypeId==2){
            defaultSoundUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.alert)
            channelId = "AlertMessage"

        }else{
            defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            channelId = getString(R.string.default_notification_channel_id)


        }
         notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            val channel = NotificationChannel(channelId,
                "SIALARM",
                NotificationManager.IMPORTANCE_HIGH)

            channel.enableVibration(true)
            channel.vibrationPattern=longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(defaultSoundUri, attributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

}