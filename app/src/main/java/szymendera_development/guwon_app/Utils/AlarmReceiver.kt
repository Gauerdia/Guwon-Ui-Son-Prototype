package szymendera_development.guwon_app.Utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.JobIntentService
import android.util.Log


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

//        val service = Intent(context, NotificationService::class.java)
//        service.putExtra("reason", intent.getStringExtra("reason"))
//        service.putExtra("timestamp", intent.getLongExtra("timestamp", 0))
//        val service = Intent(context, MessageService::class.java)


        val service = Intent()
//        service.putExtra("timestamp", intent.getLongExtra("timestamp", 0))
        JobIntentService.enqueueWork(context, MessageService::class.java, 1, intent)
    }

}