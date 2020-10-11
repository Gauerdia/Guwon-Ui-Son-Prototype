package szymendera_development.guwon_app

import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_profil_anwesenheit.*
import kotlinx.android.synthetic.main.app_bar_profil_anwesenheit.*
import kotlinx.android.synthetic.main.content_profil_anwesenheit.*
import szymendera_development.guwon_app.Utils.*

class ProfilAnwesenheitActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var sp: SharedPreferences? = null
    var edittext: EditText? = null
    var spinner : Spinner? = null
    var stunden_spinner : Spinner? = null
    var username:String? = null
    var PUNKTE_ANWESENHEIT_STUNDE = 10

    var schuelerListe: List<SchuelerTemp>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_anwesenheit)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        sp = getSharedPreferences("com.szymendera_development.guwon_app",MODE_PRIVATE)
        username = sp?.getString("vorname","someone")!!

        AsyncIsOnlineGetSchueler().execute()

        // Setting the interface up
        profil_anwesenheit_recyclerView_main.layoutManager = LinearLayoutManager(this)
        edittext = findViewById(R.id.suche_schueler_edittext)
        spinner = findViewById(R.id.profil_anwesenheit_spinner_sportart)
        stunden_spinner = findViewById(R.id.profil_anwesenheit_spinner_stunden)
        val SportartItems = arrayOf("Alle Sportarten","Taekwon-Do", "Kickboxen", "Pilates", "Yoga")
        val StundenItems = arrayOf("1","1.5","2","2.5","3","3.5","4","4.5","5")
        val Sportartadapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, SportartItems)
        val Stundenadapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, StundenItems)
        spinner!!.setAdapter(Sportartadapter)
        stunden_spinner!!.setAdapter(Stundenadapter)

        // Making the Logo in the NavBar-Header clickable
        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        val headerview = navigationView.getHeaderView(0)
        val navheaderlogo = headerview.findViewById<ImageView>(R.id.navHeaderLogo)
        navheaderlogo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(this@ProfilAnwesenheitActivity, MainActivity::class.java)
                this@ProfilAnwesenheitActivity.startActivity(intent)
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
                ProfilPressed(this, sp?.getInt("auth", 0)!!)
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

    fun SearchPersonButton(v: View) {
        val permitsModulated = mutableListOf<SchuelerTemp>()
        for (schueler in schuelerListe!!) {
            val VornameUppercase = StringBuilder(schueler.vorname)
            VornameUppercase.setCharAt(0, Character.toUpperCase(VornameUppercase[0]))
            val VornameLowercase = StringBuilder(schueler.vorname)
            VornameLowercase.setCharAt(0, Character.toLowerCase(VornameLowercase[0]))

            val NachnameUppercase = StringBuilder(schueler.nachname)
            NachnameUppercase.setCharAt(0, Character.toUpperCase(NachnameUppercase[0]))
            val NachnameLowercase = StringBuilder(schueler.nachname)
            NachnameLowercase.setCharAt(0, Character.toLowerCase(NachnameLowercase[0]))

            if (schueler.vorname == edittext?.text.toString() || VornameUppercase.toString() == edittext?.text.toString() ||
                VornameLowercase.toString() == edittext?.text.toString() ||
                schueler.nachname == edittext?.text.toString() || NachnameLowercase.toString() == edittext?.text.toString() ||
                NachnameUppercase.toString() == edittext?.text.toString()
            ) {
                permitsModulated.add(schueler)
            }
            profil_anwesenheit_recyclerView_main.adapter =
                GsonAdapterAnwesenheit(
                    permitsModulated,
                    { partItem: SchuelerTemp -> AnwesenheitClicked(partItem) },
                    this,
                    sp?.getString("vorname", "jemand")!!
                )

        }
    }

    fun SearchSportartButton(v: View) {
        val permitsModulated = mutableListOf<SchuelerTemp>()
        if(spinner?.selectedItem.toString() == "Alle Sportarten"){
            for (schueler in schuelerListe!!) {
                permitsModulated.add(schueler)
            }
        }else{
            for (schueler in schuelerListe!!) {
                    if (spinner?.selectedItem.toString() == "Taekwon-Do" && schueler.TaekwonDo == 1) {
                        permitsModulated.add(schueler)
                    } else if (spinner?.selectedItem.toString() == "Kickboxen" && schueler.Kickboxen == 1) {
                        permitsModulated.add(schueler)
                    } else if (spinner?.selectedItem.toString() == "Yoga" && schueler.Yoga == 1) {
                        permitsModulated.add(schueler)
                    } else if (spinner?.selectedItem.toString() == "Pilates" && schueler.Pilates == 1) {
                        permitsModulated.add(schueler)
                    }
            }
        }
        profil_anwesenheit_recyclerView_main.adapter = GsonAdapterAnwesenheit(
            permitsModulated,
            { partItem: SchuelerTemp -> AnwesenheitClicked(partItem) },
            this,
            sp?.getString("vorname", "jemand")!!
        )
    }

    fun FertigButton(v: View){
        enterAnwesenheit(
            username!!,
            stunden_spinner?.selectedItem.toString(),
            PUNKTE_ANWESENHEIT_STUNDE,
            this
        )
    }

    inner class AsyncGetSchueler : AsyncTask<String, String, String>() {
        // stub function
        override fun onPreExecute() { super.onPreExecute() }
        // main function
        override fun doInBackground(vararg p0: String?): String {
            var Result: String = ""
            try {
                Log.d("ProfilAnwesenheit",username)
                schuelerListe = getAllSchuelerAnwesenheit(
                    this@ProfilAnwesenheitActivity,
                    username!!
                )
            } catch (e: Exception) {
                println(e)
            }
            return Result
        }
        // what to do after response arrives
        override fun onPostExecute(result: String?) {
            for(schueler in schuelerListe!!){
            }
        }
    }

    inner class AsyncIsOnlineGetSchueler : AsyncTask<String, String, String>() {

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
                AsyncGetSchueler().execute()
            }else{
                toast("Keine Internetverbindung möglich")
            }
        }
    }
}

