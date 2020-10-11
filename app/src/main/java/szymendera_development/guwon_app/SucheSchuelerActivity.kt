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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_suche_schueler.*
import kotlinx.android.synthetic.main.app_bar_suche_schueler.*
import kotlinx.android.synthetic.main.content_suche_schueler.*
import szymendera_development.guwon_app.Utils.*


class SucheSchuelerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var namen_suche_ET: EditText? = null
    var spinner: Spinner? = null

    var schuelerListe: List<Schueler>? = null

    var sp: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suche_schueler)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        sp = getSharedPreferences("com.szymendera_development.guwon_app", MODE_PRIVATE)

        recyclerView_main.layoutManager = LinearLayoutManager(this)

        namen_suche_ET = findViewById(R.id.suche_schueler_edittext)
        spinner = findViewById(R.id.suche_schueler_spinner_sportart)
        val SportartItems = arrayOf("Alle Sportarten", "Taekwon-Do", "Kickboxen", "Pilates", "Yoga")
        val Sportartadapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, SportartItems)
        spinner!!.setAdapter(Sportartadapter)

        // Making the nav_view clickable
        nav_view.setNavigationItemSelectedListener(this)

        // Making the Logo in the NavBar-Header clickable
        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        val headerview = navigationView.getHeaderView(0)
        val navheaderlogo = headerview.findViewById<ImageView>(R.id.navHeaderLogo)
        navheaderlogo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(this@SucheSchuelerActivity, MainActivity::class.java)
                this@SucheSchuelerActivity.startActivity(intent)
            }
        })
        AsyncIsOnlineGetSchueler().execute()
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
                return true
            }
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
        // taking the array of all schueler and sorting it now
        // for the purpose we aim for.
        val permitsModulated = mutableListOf<Schueler>()
        if(schuelerListe != null) {
            for (schueler in schuelerListe!!) {
                val VornameUppercase = StringBuilder(schueler.vorname)
                VornameUppercase.setCharAt(0, Character.toUpperCase(VornameUppercase[0]))
                val VornameLowercase = StringBuilder(schueler.vorname)
                VornameLowercase.setCharAt(0, Character.toLowerCase(VornameLowercase[0]))

                val NachnameUppercase = StringBuilder(schueler.nachname)
                NachnameUppercase.setCharAt(0, Character.toUpperCase(NachnameUppercase[0]))
                val NachnameLowercase = StringBuilder(schueler.nachname)
                NachnameLowercase.setCharAt(0, Character.toLowerCase(NachnameLowercase[0]))

                if (schueler.vorname == namen_suche_ET?.text?.toString() || VornameUppercase.toString() == namen_suche_ET?.text?.toString() ||
                    VornameLowercase.toString() == namen_suche_ET?.text?.toString() || schueler.nachname == namen_suche_ET?.text?.toString() ||
                    NachnameLowercase.toString() == namen_suche_ET?.text?.toString() || NachnameUppercase.toString() == namen_suche_ET?.text?.toString()
                ) {
                    permitsModulated.add(schueler)
                }
            }
        }else{
            Toast.makeText(this,"Internet not available",Toast.LENGTH_SHORT)
        }
        recyclerView_main.adapter = GsonAdapterMutable(
            permitsModulated,
            { partItem: Schueler -> partItemClicked(partItem, this) })
    }

    fun SearchSportartButton(v: View) {
        val permitsModulated = mutableListOf<Schueler>()
        if (spinner?.selectedItem.toString() == "Alle Sportarten") {
            for (permit in schuelerListe!!) {
                permitsModulated.add(permit)
            }
        } else {
            for (permit in schuelerListe!!) {
                if (permit.TaekwonDo == 1) {
                    permitsModulated.add(permit)
                } else if (permit.Kickboxen == 1) {
                    permitsModulated.add(permit)
                } else if (permit.Yoga == 1) {
                    permitsModulated.add(permit)
                } else if (permit.Pilates == 1) {
                    permitsModulated.add(permit)
                }
            }
        }
        recyclerView_main.adapter = GsonAdapterMutable(
            permitsModulated,
            { partItem: Schueler -> partItemClicked(partItem, this) })
    }

    inner class AsyncGetSchueler : AsyncTask<String, String, String>() {
        // stub function
        override fun onPreExecute() {
            super.onPreExecute()
        }

        // main function
        override fun doInBackground(vararg p0: String?): String {
            var Result: String = ""
            try {
                schuelerListe = getAllSchueler(this@SucheSchuelerActivity)
            } catch (e: Exception) {
                println(e)
            }
            return Result
        }

        // what to do after response arrives
        override fun onPostExecute(result: String?) {
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
                Toast.makeText(this@SucheSchuelerActivity,"Keine Internetverbindung möglich", Toast.LENGTH_LONG)
            }
        }
    }
}

