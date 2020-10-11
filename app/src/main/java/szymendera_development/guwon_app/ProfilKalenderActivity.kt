package szymendera_development.guwon_app

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_profil_kalender.*
import kotlinx.android.synthetic.main.app_bar_profil_kalender.*
import com.roomorama.caldroid.CaldroidFragment
import java.util.*
import java.text.SimpleDateFormat
import android.os.AsyncTask
import android.widget.Toast
import com.roomorama.caldroid.CaldroidListener
import szymendera_development.guwon_app.Utils.*
import kotlin.collections.HashMap


class ProfilKalenderActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var sp: SharedPreferences? = null
    var UserVorname: String? = null
    var UserNachname: String? = null
    private val undo = false
    private var caldroidFragment: CaldroidFragment? = null
    private val dialogCaldroidFragment: CaldroidFragment? = null
    var CustomDateList = mutableListOf<CustomDate>()
    var dateDescriptions = HashMap<String,String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_kalender)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        sp = getSharedPreferences("com.szymendera_development.guwon_app", MODE_PRIVATE)

        caldroidFragment = CaldroidFragment()
        val args = Bundle()
        val cal = Calendar.getInstance()
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1)
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR))
        caldroidFragment?.arguments = args

        val t = supportFragmentManager.beginTransaction()
        t.replace(R.id.calendar1, caldroidFragment)
        t.commit()

//        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
//        val data= simpleDateFormat.parse("15-01-2019")
//        var hm = HashMap<Date, Int>()
//
//    if (caldroidFragment != null) {
//            caldroidFragment!!.setTextColorForDate(R.color.GuwonPrimary,data)
//        }

        caldroidFragment!!.refreshView()
        AsyncIsOnlineGetCustomDate().execute()

        // Making the Logo in the NavBar-Header clickable
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        // Setting up the calendar

        navigationView.setNavigationItemSelectedListener(this)
        val headerview = navigationView.getHeaderView(0)
        val navheaderlogo = headerview.findViewById<ImageView>(R.id.navHeaderLogo)
        navheaderlogo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(this@ProfilKalenderActivity, MainActivity::class.java)
                this@ProfilKalenderActivity.startActivity(intent)
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
        menuInflater.inflate(R.menu.profil_kalender, menu)
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
            R.id.messages -> {
                showKommDialog(this,sp?.getString("vorname","niemand")!!,
                    sp?.getString("nachname","...")!!,sp?.getInt("id",0)!!,sp?.getInt("auth",0)!!)
                return true
            }
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

    inner class AsyncGetCustomDate : AsyncTask<String, String, String>() {
        // stub function
        override fun onPreExecute() { super.onPreExecute() }
        // main function
        override fun doInBackground(vararg p0: String?): String {
            var Result: String = ""
            try {
                CustomDateList =
                    getDate(this@ProfilKalenderActivity, sp!!.getInt("id", 0))
            } catch (e: Exception) {
                println(e)
            }
            return Result
        }
        // what to do after response arrives
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            for(date in CustomDateList){
                val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
                val data= simpleDateFormat.parse(date.date)
                if (caldroidFragment != null) {
                    caldroidFragment!!.setTextColorForDate(R.color.GuwonPrimary,data)
                    dateDescriptions.put(date.date,date.description)
                }
            }
            val listener = object : CaldroidListener() {

                override fun onSelectDate(date: Date, view: View) {

                    val simpleDate = SimpleDateFormat("dd-MM-yyyy")
                    val strDt = simpleDate.format(date)
                    var tempString:String? = null
                        if(dateDescriptions.containsKey(strDt)){
                            tempString = dateDescriptions.get(strDt).toString()
                        }
                    Toast.makeText(
                        applicationContext, tempString,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onChangeMonth(month: Int, year: Int) {
                    val text = "month: $month year: $year"
                    Toast.makeText(
                        applicationContext, text,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onLongClickDate(date: Date, view: View) {
                    Toast.makeText(
                        applicationContext,
                        "Long click " + "onlongclickdate",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onCaldroidViewCreated() {
                    Toast.makeText(
                        applicationContext,
                        "Caldroid view is created",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            caldroidFragment?.setCaldroidListener(listener)
            caldroidFragment?.refreshView()
        }
    }
    inner class AsyncIsOnlineGetCustomDate : AsyncTask<String, String, String>() {

        var success : Boolean? = null

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
                AsyncGetCustomDate().execute()
            }else{
                toast("Keine Internetverbindung möglich")
            }
        }
    }

}
