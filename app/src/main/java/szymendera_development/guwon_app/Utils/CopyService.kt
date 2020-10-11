package szymendera_development.guwon_app.Utils

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.support.annotation.NonNull
import android.support.v4.app.JobIntentService
import android.support.v7.app.AppCompatActivity
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

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
class CopyService : JobIntentService() {

    private val JOB_ID = 3
    var src: File? = null
    var dst: File? = null

    fun enqueueWork(context: Context, intent: Intent) {
        JobIntentService.enqueueWork(context, CopyService::class.java!!, JOB_ID, intent)
    }

    override fun onHandleWork(@NonNull intent: Intent) {
        var src_string = intent?.extras!!.getString("src")
        var dst_string = intent?.extras!!.getString("dst")
        src = File(src_string)
        dst = File(dst_string)
        copy()
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("CopyService","onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("CopyService","onDestroy")

    }

    fun copy(){
        Log.d("CopyService","Copy")
        try {
            val inChannel = FileInputStream(src).getChannel()
            val outChannel = FileOutputStream(dst).getChannel()
            try {
                inChannel!!.transferTo(0, inChannel!!.size(), outChannel)
            } finally {
                if (inChannel != null)
                    inChannel!!.close()
                if (outChannel != null)
                    outChannel!!.close()
            }
            val file = src
            val deleted = file?.delete()
        } catch (e: Exception) {
            println(e)
        }
    }
}
