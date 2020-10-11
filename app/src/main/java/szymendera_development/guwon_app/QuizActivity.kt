package szymendera_development.guwon_app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.android.synthetic.main.app_bar_quiz.*
import szymendera_development.guwon_app.Fragments.QuizFragenFragment
import szymendera_development.guwon_app.Fragments.QuizFragment
import szymendera_development.guwon_app.Utils.*
import java.text.ParseException
import java.util.*
import java.text.SimpleDateFormat

class QuizActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
QuizFragenFragment.OnFragmentInteractionListener, QuizFragment.OnFragmentInteractionListener{

    var PUNKTE_RICHTIGE_ANTWORT = 10;

    var QuizList : List<Quiz>? = null
    var frage : TextView? = null
    var antwort_a : TextView? = null
    var antwort_b : TextView? = null
    var antwort_c : TextView? = null
    var FragenCounter : Int = 0
    var Punkte : Int = 0
    var LastQuiz : Date? = null
    var frag = QuizFragenFragment()
    var frag2 = QuizFragment()
    val ft = supportFragmentManager

    var sp: SharedPreferences? = null

    var testdate = TestDate(0, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_quiz)
        setSupportActionBar(toolbar)


        AsyncIsOnlineGetQuiz().execute()
//        AsyncGetTestDate().execute()

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


        ft.beginTransaction().replace(R.id.quiz_frame_layout, frag).commit()

        // Making the Logo in the NavBar-Header clickable
        val navigationView = findViewById<NavigationView>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        val headerview = navigationView.getHeaderView(0)
        val navheaderlogo = headerview.findViewById<ImageView>(R.id.navHeaderLogo)
        navheaderlogo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(this@QuizActivity, MainActivity::class.java)
                this@QuizActivity.startActivity(intent)
            }
        })
        sp = getSharedPreferences("com.szymendera_development.guwon_app",MODE_PRIVATE)
    }

    inner class AsyncIsOnlineGetQuiz : AsyncTask<String, String, String>() {

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
                AsyncGetQuiz().execute()
            }else{
                toast("Keine Internetverbindung möglich")
            }
        }
    }

    inner class AsyncIsOnlineUpdateLastQuiz(i_id: Int, i_date: String, i_context: Context, i_punkte: Int) : AsyncTask<String, String, String>() {

        var success : Boolean? = null
        var id = i_id
        var date = i_date
        var context = i_context
        var punkte = i_punkte

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
                AsyncUpdateLastQuiz(id, date,context,punkte).execute()
            }else{
                toast("Keine Internetverbindung möglich")
            }
        }
    }

    inner class AsyncGetQuiz : AsyncTask<String, String, String>() {
        // stub function
        override fun onPreExecute() { super.onPreExecute() }
        // main function
        override fun doInBackground(vararg p0: String?): String {
            var Result: String = ""
            try {
                QuizList = getQuiz(this@QuizActivity)
            } catch (e: Exception) {
                println(e)
            }
            return Result
        }
        // what to do after response arrives
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var TempList = mutableListOf<Quiz>()
            var iterator = 0
            while(iterator < 10){
                val min = 0
                val max = QuizList!!.size
                val r = Random()
                val randomIndex = r.nextInt(max - min) + min
                TempList?.add(iterator,QuizList?.get(randomIndex)!!)
                iterator++
            }
            QuizList = TempList
            iterator = 0
            while(iterator < 10){
                iterator++
            }
           frag.setFrage(QuizList?.get(FragenCounter)?.Frage!!)
            frag.setAntwortA(QuizList?.get(FragenCounter)?.Antwort_A!!)
            frag.setAntwortB(QuizList?.get(FragenCounter)?.Antwort_B!!)
            frag.setAntwortC(QuizList?.get(FragenCounter)?.Antwort_C!!)
        }
    }

    inner class AsyncUpdateLastQuiz(i_id: Int, i_date: String, i_context: Context, i_punkte: Int) : AsyncTask<String, String, String>() {

        var id = i_id
        var date = i_date
        var context = i_context
        var punkte = i_punkte
        // stub function
        override fun onPreExecute() { super.onPreExecute() }
        // main function
        override fun doInBackground(vararg p0: String?): String {
            var Result: String = ""
            try {
                updateLastQuiz(id, date, punkte, context)
            } catch (e: Exception) {
                println(e)
            }
            return Result
        }
        // what to do after response arrives
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
        }
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

    fun AntwortAPressed(view: View){
        // Checking if the answer has been coorect
        if(QuizList?.get(FragenCounter)?.Richtige_Antwort == "a"){
            Punkte += PUNKTE_RICHTIGE_ANTWORT
        }
        FragenCounter++
        // Checking if it's been the last question
        // else: Getting the next question
        if(FragenCounter >= QuizList?.size!!){
            QuizFinished()
        }else{
            frag.setFrage(QuizList?.get(FragenCounter)?.Frage!!)
            frag.setAntwortA(QuizList?.get(FragenCounter)?.Antwort_A!!)
            frag.setAntwortB(QuizList?.get(FragenCounter)?.Antwort_B!!)
            frag.setAntwortC(QuizList?.get(FragenCounter)?.Antwort_C!!)

        }
    }

    fun AntwortBPressed(view: View){
        if(QuizList?.get(FragenCounter)?.Richtige_Antwort == "b"){
            Punkte += PUNKTE_RICHTIGE_ANTWORT
        }
        FragenCounter++

        if(FragenCounter >= QuizList?.size!!){
            QuizFinished()
        }else{
            frag.setFrage(QuizList?.get(FragenCounter)?.Frage!!)
            frag.setAntwortA(QuizList?.get(FragenCounter)?.Antwort_A!!)
            frag.setAntwortB(QuizList?.get(FragenCounter)?.Antwort_B!!)
            frag.setAntwortC(QuizList?.get(FragenCounter)?.Antwort_C!!)
        }
    }

    fun AntwortCPressed(view: View){
        if(QuizList?.get(FragenCounter)?.Richtige_Antwort == "c"){
            Punkte = PUNKTE_RICHTIGE_ANTWORT
        }

        FragenCounter++

        if(FragenCounter >= QuizList?.size!!){
            QuizFinished()
        }else{
            frag.setFrage(QuizList?.get(FragenCounter)?.Frage!!)
            frag.setAntwortA(QuizList?.get(FragenCounter)?.Antwort_A!!)
            frag.setAntwortB(QuizList?.get(FragenCounter)?.Antwort_B!!)
            frag.setAntwortC(QuizList?.get(FragenCounter)?.Antwort_C!!)
        }
    }

    fun QuizFinished(){

        // Checking which day it is, to change the "last_quiz"-parameter of the
        // user so that he has to wait til monday to do the quiz again.

        try {
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            val c = Calendar.getInstance()
            val dayOfWeek = c.get(Calendar.DAY_OF_WEEK)

            if (dayOfWeek == 1) {
                c.add(Calendar.DATE, 7)
            } else if (dayOfWeek == 2) {
                c.add(Calendar.DATE, 6)
            } else if (dayOfWeek == 3) {
                c.add(Calendar.DATE, 5)
            } else if (dayOfWeek == 4) {
                c.add(Calendar.DATE, 4)
            } else if (dayOfWeek == 5) {
                c.add(Calendar.DATE, 3)
            } else if (dayOfWeek == 6) {
                c.add(Calendar.DATE, 2)
            } else if (dayOfWeek == 7) {
                c.add(Calendar.DATE, 1)
            }
            val resultdate = Date(c.timeInMillis)
            var dateInString = sdf.format(resultdate)
            AsyncIsOnlineUpdateLastQuiz(sp!!.getInt("id", 0), dateInString.toString(), this, Punkte).execute()
            ft.beginTransaction().replace(R.id.quiz_frame_layout,
                QuizFragment()
            ).addToBackStack(null).commit()
        }catch(e: java.lang.Exception){
            println(e.toString())
        }
    }

    override fun onFragmentInteraction(uri: Uri) {}

    override fun changeFragment(){}

}
