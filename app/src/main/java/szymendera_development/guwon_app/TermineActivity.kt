package szymendera_development.guwon_app

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_termine.*
import kotlinx.android.synthetic.main.app_bar_termine.*
import android.content.SharedPreferences
import android.graphics.Color.BLACK
import android.graphics.Color.GRAY
import android.os.AsyncTask
import android.util.TypedValue
import android.view.View
import android.widget.*
import szymendera_development.guwon_app.Utils.*
import java.text.SimpleDateFormat
import java.util.*


class TermineActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var sp: SharedPreferences? = null

    var terminListe: List<Termine>? = null

    var linearLayout: LinearLayout? = null

    var PERMISSION_REQUEST = 1000

    var MonatAlreadyDisplayed: IntArray = intArrayOf(0,0,0,0,0,0,0,0,0,0,0,0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_termine)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        sp = getSharedPreferences("com.szymendera_development.guwon_app",MODE_PRIVATE)

        AsyncIsOnlineGetTermine().execute()

        linearLayout = findViewById(R.id.linear_layout_termine)

        // Making the Logo in the NavBar-Header clickable
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val headerview = navigationView.getHeaderView(0)
        val navheaderlogo = headerview.findViewById<ImageView>(R.id.navHeaderLogo)
        navheaderlogo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(this@TermineActivity, MainActivity::class.java)
                this@TermineActivity.startActivity(intent)
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
        if(sp?.getInt("auth",0) == 1){
            menuInflater.inflate(R.menu.termine_auth_menu, menu)
        }else{
            menuInflater.inflate(R.menu.template, menu)
        }
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
            R.id.add_termine -> {
                showAddTerminDialog(this)
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

    inner class AsyncGetTermine : AsyncTask<String, String, String>() {
        // stub function
        override fun onPreExecute() { super.onPreExecute() }
        // main function
        override fun doInBackground(vararg p0: String?): String {
            var Result: String = ""
            try {
                terminListe = getTermine(this@TermineActivity)
            } catch (e: Exception) {
                println(e)
            }
            return Result
        }
        // what to do after response arrives
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var mutTerminListe = mutableListOf<Termine>()
            for(termin in terminListe!!){
                val sdf = SimpleDateFormat("dd-MM-yyyy")
                val tempDate = sdf.parse((termin.tag+"-"+termin.monat+"-"+termin.jahr))
                if(Date().after(tempDate)){
                    deleteTermin(this@TermineActivity, termin.id)
                }else{
                    mutTerminListe.add(termin)
                }
            }
            QuickSort(mutTerminListe,0,(mutTerminListe?.size!!-1))
            terminListe = mutTerminListe
            checkData()
            }
        }

    inner class AsyncIsOnlineGetTermine : AsyncTask<String, String, String>() {

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
                AsyncGetTermine().execute()
            }else{
                toast("Keine Internetverbindung möglich")
            }
        }
    }

    fun checkData(){
        var List2019 = mutableListOf<Termine>()
        var List2020 = mutableListOf<Termine>()
        var List2021 = mutableListOf<Termine>()
        for(termin in terminListe!!){
            if(termin.jahr == "2019"){
                List2019.add(termin)
            }else if(termin.jahr == "2020"){
                List2020.add(termin)
            }else if(termin.jahr == "2021"){
                List2021.add(termin)
            }
        }
        if(List2019.isNotEmpty()){

            // Setting the large "2019" above the dates
            val valueTV = TextView(this)
            valueTV.text = "2019"
            valueTV.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,32f)

            val view = View(this)
            view.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2
            )
            view.setBackgroundColor(BLACK)

            val spaceTV = TextView(this)
            spaceTV.text = "    "
            spaceTV.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            (linearLayout as LinearLayout).addView(valueTV)
            (linearLayout as LinearLayout).addView(view)
            (linearLayout as LinearLayout).addView(spaceTV)

            for(termin in List2019!!){
                when(termin.monat){
                    "01" -> { if(MonatAlreadyDisplayed.get(0) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Januar"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )


                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[0] = 1

                    }}
                    "02" -> { if(MonatAlreadyDisplayed.get(1) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Februar"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[1] = 1
                        }}
                    "03" -> { if(MonatAlreadyDisplayed.get(2) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "März"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[2] = 1
                            }}
                    "04" -> { if(MonatAlreadyDisplayed.get(3) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "April"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[3] = 1
                    }}
                    "05" -> { if(MonatAlreadyDisplayed.get(4) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Mai"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[4] = 1
                    }}
                    "06" -> { if(MonatAlreadyDisplayed.get(5) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Juni"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[5] = 1
                    }}
                    "07" -> { if(MonatAlreadyDisplayed.get(6) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Juli"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[6] = 1
                    }}
                    "08" -> { if(MonatAlreadyDisplayed.get(7) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "August"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[7] = 1
                    }}
                    "09" -> { if(MonatAlreadyDisplayed.get(8) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "September"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[8] = 1
                    }}
                    "10" -> { if(MonatAlreadyDisplayed.get(9) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Oktober"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[9] = 1
                    }}
                    "11" -> { if(MonatAlreadyDisplayed.get(10) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "November"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[10] = 1
                    }}
                    "12" -> { if(MonatAlreadyDisplayed.get(11) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Dezember"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[11] = 1
                    }}
                }
                val tv_temp = TextView(this)
                tv_temp.text = termin.tag+"."+termin.monat+"."+termin.jahr+"    "+termin.uhrzeit+" Uhr"
                val tv_temp2 = TextView(this)
                tv_temp2.text = "                         " + termin.anlass
                val tv_temp3 = TextView(this)
                tv_temp3.text = "                         " + termin.ort

                tv_temp.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                tv_temp2.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                tv_temp3.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                val spaceTV_month = TextView(this)
                spaceTV_month.text = "    "
                spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                (linearLayout as LinearLayout).addView(tv_temp)
                (linearLayout as LinearLayout).addView(tv_temp2)
                (linearLayout as LinearLayout).addView(tv_temp3)
                (linearLayout as LinearLayout).addView(spaceTV_month)
            }
        }
        // Resetting the array for the following year
        var i: Int = 0
        for(item in MonatAlreadyDisplayed){
            MonatAlreadyDisplayed[i] = 0
            i++
        }
        if(List2020.isNotEmpty()){
            // Setting the large "2019" above the dates
            val valueTV = TextView(this)
            valueTV.text = "2020"
            valueTV.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,32f)

            val view = View(this)
            view.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2
            )
            view.setBackgroundColor(BLACK)

            val spaceTV = TextView(this)
            spaceTV.text = "    "
            spaceTV.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            (linearLayout as LinearLayout).addView(valueTV)
            (linearLayout as LinearLayout).addView(view)
            (linearLayout as LinearLayout).addView(spaceTV)

            for(termin in List2020!!){
                when(termin.monat){
                    "01" -> { if(MonatAlreadyDisplayed.get(0) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Januar"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )


                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[0] = 1

                    }}
                    "02" -> { if(MonatAlreadyDisplayed.get(1) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Februar"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[1] = 1
                    }}
                    "03" -> { if(MonatAlreadyDisplayed.get(2) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "März"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[2] = 1
                    }}
                    "04" -> { if(MonatAlreadyDisplayed.get(3) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "April"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[3] = 1
                    }}
                    "05" -> { if(MonatAlreadyDisplayed.get(4) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Mai"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[4] = 1
                    }}
                    "06" -> { if(MonatAlreadyDisplayed.get(5) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Juni"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[5] = 1
                    }}
                    "07" -> { if(MonatAlreadyDisplayed.get(6) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Juli"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[6] = 1
                    }}
                    "08" -> { if(MonatAlreadyDisplayed.get(7) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "August"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[7] = 1
                    }}
                    "09" -> { if(MonatAlreadyDisplayed.get(8) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "September"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[8] = 1
                    }}
                    "10" -> { if(MonatAlreadyDisplayed.get(9) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Oktober"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[9] = 1
                    }}
                    "11" -> { if(MonatAlreadyDisplayed.get(10) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "November"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[10] = 1
                    }}
                    "12" -> { if(MonatAlreadyDisplayed.get(11) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Dezember"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[11] = 1
                    }}
                }
                val tv_temp = TextView(this)
                tv_temp.text = termin.tag+"."+termin.monat+"."+termin.jahr+"    "+termin.uhrzeit+" Uhr"
                val tv_temp2 = TextView(this)
                tv_temp2.text = "                         " + termin.anlass
                val tv_temp3 = TextView(this)
                tv_temp3.text = "                         " + termin.ort

                tv_temp.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                tv_temp2.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                tv_temp3.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                val spaceTV_month = TextView(this)
                spaceTV_month.text = "    "
                spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                (linearLayout as LinearLayout).addView(tv_temp)
                (linearLayout as LinearLayout).addView(tv_temp2)
                (linearLayout as LinearLayout).addView(tv_temp3)
                (linearLayout as LinearLayout).addView(spaceTV_month)
            }
        }
        // Resetting the array for the following year
        i = 0
        for(item in MonatAlreadyDisplayed){
            MonatAlreadyDisplayed[i] = 0
            i++
        }
        if(List2021.isNotEmpty()){
            // Setting the large "2021" above the dates
            val valueTV = TextView(this)
            valueTV.text = "2021"
            valueTV.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,32f)

            val view = View(this)
            view.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2
            )
            view.setBackgroundColor(BLACK)

            val spaceTV = TextView(this)
            spaceTV.text = "    "
            spaceTV.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            (linearLayout as LinearLayout).addView(valueTV)
            (linearLayout as LinearLayout).addView(view)
            (linearLayout as LinearLayout).addView(spaceTV)

            for(termin in List2021!!){
                when(termin.monat){
                    "01" -> { if(MonatAlreadyDisplayed.get(0) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Januar"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )


                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[0] = 1

                    }}
                    "02" -> { if(MonatAlreadyDisplayed.get(1) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Februar"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[1] = 1
                    }}
                    "03" -> { if(MonatAlreadyDisplayed.get(2) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "März"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[2] = 1
                    }}
                    "04" -> { if(MonatAlreadyDisplayed.get(3) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "April"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[3] = 1
                    }}
                    "05" -> { if(MonatAlreadyDisplayed.get(4) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Mai"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[4] = 1
                    }}
                    "06" -> { if(MonatAlreadyDisplayed.get(5) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Juni"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[5] = 1
                    }}
                    "07" -> { if(MonatAlreadyDisplayed.get(6) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Juli"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[6] = 1
                    }}
                    "08" -> { if(MonatAlreadyDisplayed.get(7) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "August"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[7] = 1
                    }}
                    "09" -> { if(MonatAlreadyDisplayed.get(8) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "September"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[8] = 1
                    }}
                    "10" -> { if(MonatAlreadyDisplayed.get(9) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Oktober"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[9] = 1
                    }}
                    "11" -> { if(MonatAlreadyDisplayed.get(10) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "November"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[10] = 1
                    }}
                    "12" -> { if(MonatAlreadyDisplayed.get(11) == 0){
                        val valueTV = TextView(this)
                        valueTV.text = "Dezember"
                        valueTV.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20f)

                        val view = View(this)
                        view.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2
                        )
                        view.setBackgroundColor(GRAY)

                        val spaceTV_month = TextView(this)
                        spaceTV_month.text = "    "
                        spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        (linearLayout as LinearLayout).addView(valueTV)
                        (linearLayout as LinearLayout).addView(view)
                        (linearLayout as LinearLayout).addView(spaceTV_month)
                        MonatAlreadyDisplayed[11] = 1
                    }}
                }
                val tv_temp = TextView(this)
                tv_temp.text = termin.tag+"."+termin.monat+"."+termin.jahr+"    "+termin.uhrzeit+" Uhr"
                val tv_temp2 = TextView(this)
                tv_temp2.text = "                         " + termin.anlass
                val tv_temp3 = TextView(this)
                tv_temp3.text = "                         " + termin.ort

                tv_temp.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                tv_temp2.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                tv_temp3.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                val spaceTV_month = TextView(this)
                spaceTV_month.text = "    "
                spaceTV_month.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                (linearLayout as LinearLayout).addView(tv_temp)
                (linearLayout as LinearLayout).addView(tv_temp2)
                (linearLayout as LinearLayout).addView(tv_temp3)
                (linearLayout as LinearLayout).addView(spaceTV_month)
            }
        }
    }

    fun QuickSort(terminListe: MutableList<Termine>, links: Int, rechts: Int) {
        if (links < rechts) {
            var pivot = terminListe[rechts].tag + "-" + terminListe[rechts].monat + "-" +terminListe[rechts].jahr
            var i = links
            var j = rechts - 1

            val sdf = SimpleDateFormat("dd-MM-yyyy")
            val pivotDate = sdf.parse(pivot)

            while (i <= j) {
                var tempi = terminListe[i].tag + "-" + terminListe[i].monat + "-" +terminListe[i].jahr
                var tempiDate = sdf.parse(tempi)
                if (tempiDate.after(pivotDate)) {
                    var temp: Termine = terminListe[i]
                    terminListe[i] = terminListe[j]
                    terminListe[j] = temp
                    j--
                } else {
                    i++
                }
            }

            var temp = terminListe[i]
            terminListe[i] = terminListe[rechts]
            terminListe[rechts] = temp

            QuickSort(terminListe, links, i - 1)
            QuickSort(terminListe, i + 1, rechts)
        }
    }

}
