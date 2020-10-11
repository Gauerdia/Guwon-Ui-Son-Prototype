package szymendera_development.guwon_app.Utils

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.support.annotation.NonNull
import android.support.v4.app.JobIntentService
import android.util.Log
import szymendera_development.guwon_app.R
import java.util.*
import szymendera_development.guwon_app.MainActivity
import szymendera_development.guwon_app.MessageOverviewActivity


// TODO: Rename actions, choose action names that describe tasks that this
// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
private const val ACTION_FOO = "szymendera_development.guwon_app.Utils.action.FOO"
private const val ACTION_BAZ = "szymendera_development.guwon_app.Utils.action.BAZ"

// TODO: Rename parameters
private const val EXTRA_PARAM1 = "szymendera_development.guwon_app.Utils.extra.PARAM1"
private const val EXTRA_PARAM2 = "szymendera_development.guwon_app.Utils.extra.PARAM2"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class NotificationService : JobIntentService() {
    private lateinit var mNotification: Notification
    private val mNotificationId: Int = 1000
    private val mNotificationTime = Calendar.getInstance().timeInMillis + 5000

    private val TAG = "NotificationService"
    /**
     * Unique job ID for this service.
     */
    private val JOB_ID = 3

    fun enqueueWork(context: Context, intent: Intent) {
        JobIntentService.enqueueWork(context, MessageService::class.java!!, JOB_ID, intent)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("NotiService","onCreate")
    }

    override fun onDestroy() {
        Log.d("NotiService","onDestroy")
        super.onDestroy()

    }

    @SuppressLint("NewApi")
    private fun createChannel() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library

            val context = this.applicationContext
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.parseColor("#e8334a")
            notificationChannel.description = getString(R.string.notification_channel_description)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

    companion object {

        const val CHANNEL_ID = "samples.notification.devdeeds.com.CHANNEL_ID"
        const val CHANNEL_NAME = "Sample Notification"
    }


    override fun onHandleWork(@NonNull intent: Intent) {

        //Create Channel
        createChannel()


//        var timestamp: Long = 0
        if (intent != null && intent.extras != null) {
//            timestamp = intent.extras!!.getLong("timestamp")
        }

//        if (timestamp > 0) {
            val context = this.applicationContext
            var notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notifyIntent = Intent(this, MessageOverviewActivity::class.java)

            val title = "Neue Nachricht"
            val message = "Es wurde eine neue Nachricht an den Verteiler geschrieben!"

            notifyIntent.putExtra("title", title)
            notifyIntent.putExtra("message", message)
            notifyIntent.putExtra("notification", true)

            notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

//            val calendar = Calendar.getInstance()
////            calendar.timeInMillis = timestamp


            val pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val res = this.resources
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


                mNotification = Notification.Builder(this, CHANNEL_ID)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.logo)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setStyle(Notification.BigTextStyle()
                        .bigText(message))
                    .setContentText(message).build()
            } else {

                mNotification = Notification.Builder(this)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.logo)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle(title)
                    .setStyle(Notification.BigTextStyle()
                        .bigText(message))
                    .setSound(uri)
                    .setContentText(message).build()

            }



            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // mNotificationId is a unique int for each notification that you must define
            notificationManager.notify(mNotificationId, mNotification)
//        }


    }
}