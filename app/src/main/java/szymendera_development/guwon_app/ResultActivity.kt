/*
package szymendera_development.guwon_app

import android.app.Activity
import android.content.SharedPreferences
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_result.*
import szymendera_development.guwon_app.Utils.*

class ResultActivity : AppCompatActivity() {

    var recyclerView: RecyclerView ? = null
    var sp: SharedPreferences ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        sp = getSharedPreferences("com.szymendera_development.guwon_app", MODE_PRIVATE)

        recyclerView = findViewById(R.id.recyclerView_notification_result) as RecyclerView

        AsyncIsOnlineGetKomm(this, recyclerView!!,sp?.getString("vorname","niemand")!!,sp?.getInt("id",0)!!).execute()

        if (intent.getBooleanExtra("notification", false)) { //Just for confirmation
//            txtTitleView.text = intent.getStringExtra("title")
//            txtMsgView.text = intent.getStringExtra("message")

        }
    }

    class AsyncGetKomm(activity: Activity, recyclerView: RecyclerView,
                       vorname: String,id_nutzer:Int) : AsyncTask<String, String, String>() {

        var KommListe: List<Kommunikation>? = null
        var activity = activity
        var recyclerview = recyclerView
        var vorname = vorname
        var id_nutzer = id_nutzer

        // stub function
        override fun onPreExecute() {
            super.onPreExecute()
        }

        // main function
        override fun doInBackground(vararg p0: String?): String {
            var Result: String = ""
            try {
                KommListe = getKommunikation(activity)
            } catch (e: Exception) {
                println(e)
            }
            return Result
        }

        // what to do after response arrives
        override fun onPostExecute(result: String?) {
        }
    }
    class AsyncIsOnlineGetKomm(activity: Activity, recyclerview: RecyclerView,
                               vorname:String,id_nutzer: Int) : AsyncTask<String, String, String>() {

        var success : Boolean? = null
        var activity = activity
        var recyclerView = recyclerview
        var vorname = vorname
        var id_nutzer = id_nutzer

        // stub function
        override fun onPreExecute() {
            super.onPreExecute()
        }

        // main function
        override fun doInBackground(vararg p0: String?): String {
            var Result: String = ""
            try {
                success = isOnline()
            } catch (e: Exception) {
                println(e)
            }
            return Result
        }

        // what to do after response arrives
        override fun onPostExecute(result: String?) {
            if(success!!){
                AsyncGetKomm(activity,recyclerView,vorname,id_nutzer).execute()
            }else{
                Toast.makeText(activity,"Keine Internetverbindung möglich", Toast.LENGTH_LONG)
            }
        }
    }
}*/
