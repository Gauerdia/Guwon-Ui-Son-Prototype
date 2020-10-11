package szymendera_development.guwon_app

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_qrview.*
import kotlinx.android.synthetic.main.app_bar_qrview.*
import szymendera_development.guwon_app.Utils.*
import java.nio.charset.Charset

class QRViewActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var sp : SharedPreferences? = null
    var pref : SharedPreferences? = null
    val data3 : String? = null
    var editText1 : EditText? = null
    var editText2 : EditText? = null
    val charset: Charset = Charsets.UTF_8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrview)
        setSupportActionBar(toolbar)
        var textView2 = findViewById<TextView>(R.id.textview_qr_value2)
        editText1 = findViewById<EditText>(R.id.qrview_vorname)
        editText2 = findViewById<EditText>(R.id.qrview_nachname)

        if(intent.getStringExtra("qr_value") != null){
            val ss:String = intent.getStringExtra("qr_value")
            textView2.text = ss
        }else{
            textView2.text = ""
        }


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        // Tried to get the value of the qr with shared preferences.
        // doing it with intent-extra now.
        var data = sp?.getString("test","")
        var data2 = pref?.getString("qr_value_new","")

//        textView.text = data

        // Making the Logo in the NavBar-Header clickable
        val navigationView = findViewById<NavigationView>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        val headerview = navigationView.getHeaderView(0)
        val navheaderlogo = headerview.findViewById<ImageView>(R.id.navHeaderLogo)
        navheaderlogo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(this@QRViewActivity, MainActivity::class.java)
                this@QRViewActivity.startActivity(intent)
            }
        })
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
                ProfilPressed(this, 0)
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


}
