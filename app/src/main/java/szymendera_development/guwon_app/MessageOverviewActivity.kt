package szymendera_development.guwon_app

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_message_overview.*
import kotlinx.android.synthetic.main.app_bar_message_overview.*
import szymendera_development.guwon_app.Utils.*

class MessageOverviewActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var sp: SharedPreferences? = null
    var recyclerView: RecyclerView? = null
    var PERMISSION_REQUEST = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_overview)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recyclerView_message_overview)
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        sp = getSharedPreferences("com.szymendera_development.guwon_app",MODE_PRIVATE)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        val headerview = navigationView.getHeaderView(0)
        val navheaderlogo = headerview.findViewById<ImageView>(R.id.navHeaderLogo)
        navheaderlogo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(this@MessageOverviewActivity, MainActivity::class.java)
                this@MessageOverviewActivity.startActivity(intent)
            }
        })

        nav_view.setNavigationItemSelectedListener(this)
        AsyncIsOnlineGetKomm(this, recyclerView!!,sp?.getString("vorname","niemand")!!,
            sp?.getInt("id",0)!!,sp?.getInt("auth",0)!!).execute()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.template, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.profil -> {
                checkIfLoggedIn(this,sp?.getBoolean("loggedIn", false)!!,sp?.getInt("auth",0)!!,PERMISSION_REQUEST)
                return true}

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.aktuelles -> {
                AktuellesPressed(this)
            }
            R.id.termine -> {
                TerminePressed(this)
            }
            R.id.kurse -> {
                KursePressed(this)
            }
            R.id.team -> {
                TeamPressed(this)
            }
            R.id.kontakt -> {
                KontaktPressed(this,sp?.getInt("id",0)!!)
            }
            R.id.taekwondo -> {
                TaekwondoPressed(this)
            }
            R.id.kickboxen -> {
                KickboxenPressed(this)
            }
            R.id.pilatis -> {
                PilatisPressed(this)
            }
            R.id.yoga -> {
                YogaPressed(this)
            }
            R.id.nordic -> {
                NordicWalkingPressed(this)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    class AsyncGetKomm(activity: Activity, recyclerView: RecyclerView,
                       vorname: String, id_nutzer:Int, auth:Int) : AsyncTask<String, String, String>() {

        var KommListe: List<Kommunikation>? = null
        var activity = activity
        var recyclerview = recyclerView
        var vorname = vorname
        var id_nutzer = id_nutzer
        var auth = auth

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
            recyclerview.adapter = GsonAdapterKommDialog(
                KommListe!!,
                { partItem: Kommunikation -> MessageClicked(activity,partItem,vorname,id_nutzer,auth) })
        }
    }

    class AsyncIsOnlineGetKomm(activity: Activity, recyclerview: RecyclerView,
                               vorname:String, id_nutzer: Int,auth: Int) : AsyncTask<String, String, String>() {

        var success : Boolean? = null
        var activity = activity
        var recyclerView = recyclerview
        var vorname = vorname
        var id_nutzer = id_nutzer
        var auth = auth

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
                AsyncGetKomm(activity,recyclerView,vorname,id_nutzer,auth).execute()
            }else{
                Toast.makeText(activity,"Keine Internetverbindung möglich", Toast.LENGTH_LONG)
            }
        }
    }
}
