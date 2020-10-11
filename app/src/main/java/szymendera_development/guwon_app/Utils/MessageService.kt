package szymendera_development.guwon_app.Utils

import android.content.Intent
import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.concurrent.timerTask
import android.app.*
import android.content.SharedPreferences
import android.support.annotation.NonNull
import android.support.v4.app.JobIntentService
import android.support.v7.app.AppCompatActivity


// TODO: Rename actions, choose action names that describe tasks that this
// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
private const val ACTION_FOO = "szymendera_development.guwon_app.action.FOO"
private const val ACTION_BAZ = "szymendera_development.guwon_app.action.BAZ"

// TODO: Rename parameters
private const val EXTRA_PARAM1 = "szymendera_development.guwon_app.extra.PARAM1"
private const val EXTRA_PARAM2 = "szymendera_development.guwon_app.extra.PARAM2"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class MessageService : JobIntentService() {

    var KommList : List<Kommunikation>? = null
    val timer = Timer()
    var timestamp:Long?  = null
    var sp: SharedPreferences? = null

    private val TAG = "MyJobIntentService"
    /**
     * Unique job ID for this service.
     */
    private val JOB_ID = 2

    fun enqueueWork(context: Context, intent: Intent) {
        JobIntentService.enqueueWork(context, MessageService::class.java!!, JOB_ID, intent)
    }


    override fun onCreate() {
        super.onCreate()
        Log.d("MessageService","onCreate")
        sp = getSharedPreferences("com.szymendera_development.guwon_app", AppCompatActivity.MODE_PRIVATE)
    }

    override fun onDestroy() {
        Log.d("MessageService","onDestroy")
        super.onDestroy()

    }

    override fun onHandleWork(@NonNull intent: Intent) {
        Log.d("MessageService","OnHandleWork")
//        timestamp = intent?.extras!!.getLong("timestamp")
        getKomm()
    }

    // Get all Messages available on the server
//    fun getKomm(timestamp:Long){
    fun getKomm(){
        Log.d("MessageService","getKomm")
        val url = "https://marc-szymendera.de/Guwon/getKommunikation.php"
        val jsonObject = JSONObject()
        try {
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val JSON = MediaType.parse("application/json; charset=utf-8")
        val Jsonbody = RequestBody.create(JSON, jsonObject.toString())

        val request = Request.Builder().url(url).post(Jsonbody).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                println(body)

                val gson = GsonBuilder().create()

                KommList = gson.fromJson(body, Array<Kommunikation>::class.java).toList()
                checkKomm()
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute request")
            }
        })
    }

    // filter the messages for the ones the user is allowed to read
//    fun checkKomm(timestamp: Long){
    fun checkKomm(){
        Log.d("MessageService","checkKomm")
        var TempListe = mutableListOf<Kommunikation>()
        if(sp?.getInt("auth",0) == 1){
            for(item in KommList!!){
                if(item.id_empfaenger == 0 || item.id_empfaenger == sp?.getInt("id",0)){
                    TempListe.add(item)
                }
            }
        }else{
            for(item in KommList!!){
                if(item.id_empfaenger == sp?.getInt("id",0)){
                    TempListe.add(item)
                }
            }
        }
        for(item in TempListe!!){
            if(item.gelesen == 0){
                val service = Intent()
//                service.putExtra("timestamp", timestamp)
                JobIntentService.enqueueWork(baseContext, NotificationService::class.java, 3, service)
                break
            }
        }
        timer.schedule(timerTask {
            Log.d("MessageService", "Timer inside MessageService.")
            getKomm()
        }, 60000) // 3600000
    }

    companion object
}
