package szymendera_development.guwon_app

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.text.method.ScrollingMovementMethod
import android.content.SharedPreferences
import android.util.Log
import android.os.Handler
import android.widget.*
import java.util.*
import kotlin.concurrent.timerTask
import ImageModel
import android.app.*
import android.support.v4.view.ViewPager
import com.viewpagerindicator.CirclePageIndicator
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import szymendera_development.guwon_app.Utils.*
import kotlin.collections.ArrayList


/*
layout:
Activity_Name  -> Init of the Nav-Drawer on the left
App_Bar_Name   -> Init of Toolbar
Content_Name   -> Basic Content-Layout
Nav_Header_Name-> Image on the top of the Nav-Drawer
menu:
Activity_Name_Drawer -> The Items of the Nav-Drawer
Name                 -> The items of the button on the right

neue Activity:
layout:
activity_name : app:headerLayout="@layout/nav_header_template"
                app:menu="@menu/activity_template_drawer"

    nameActivity -> activity_name(layout) -> nav_header_name
                                          -> activity_name_drawer
                 -> activity(menu)
                 -> app_bar_name(floating_button) -> content_name
In der Activity die Funktion OnCreateOptionsMenu anpassen.
 */


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // Array of the ImageSlide on the HomeScreen
    private var imageModelArrayList: ArrayList<ImageModel>? = null
    private val myImageList = intArrayOf(
        R.drawable.homescreen_1,
        R.drawable.homescreen_2,
        R.drawable.homescreen_3,
        R.drawable.homescreen_4,
        R.drawable.homescreen_5,
        R.drawable.tobi_1
    )
    // Defining two SharedPreferences Instances to test some value-movements
    var sp: SharedPreferences? = null
    // Originally used to time the Pictures on the first activity. Now we take another
    // approach. Maybe we need it later?
    val timer = Timer()

    // const values to identify the permissions
    var PERMISSION_REQUEST = 1000
    var SERVICE_PERMISSION_REQUEST = 1001

    // variables to deal the with the message-service
    private val mNotificationTime = Calendar.getInstance().timeInMillis + 5000
    //Set after 5 seconds from the current time.
    private var mNotified = false

    // Lists to save the information we want to display
    var schuelerListe: List<Schueler>? = null
    var photoUpdateListe: List<PhotoUpdate>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Saving if the user is logged in, SP-Approach
        sp = getSharedPreferences("com.szymendera_development.guwon_app", MODE_PRIVATE)
        // For testing purposes
//        sp?.edit()?.putBoolean("loggedIn", false)?.apply()

        setSupportActionBar(toolbar)
        // We need this for the actionbardrawer
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        // To enable the scrolling, when the text is longer than the screen
        main_textView.movementMethod = ScrollingMovementMethod()
        // Getting the nav_view on the left right hot to use it to navigate.
        nav_view.setNavigationItemSelectedListener(this)

        // Array for the ImageSlider
        imageModelArrayList = ArrayList()
        imageModelArrayList = populateList(myImageList)
        initSlider()

        AsyncIsOnlineToday().execute()
        AsyncIsOnlineGetPhotoUpdate().execute()
        AsyncIsOnlineGetSchueler().execute()

        val service = Intent(applicationContext, MessageService::class.java)
        applicationContext.startService(service)


        // Making the Logo in the NavBar-Header clickable
        val navigationView = findViewById<NavigationView>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        val headerview = navigationView.getHeaderView(0)
        val navheaderlogo = headerview.findViewById<ImageView>(R.id.navHeaderLogo)
        navheaderlogo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(this@MainActivity, MainActivity::class.java)
                this@MainActivity.startActivity(intent)
            }
        })

        // When user is authorized, he will get a notification
        if(sp?.getInt("auth",0) == 1){
            if (!mNotified) {
                // first argument is when to display the notification
                NotificationUtils().setNotification(mNotificationTime, this@MainActivity)
            }
        }
        if(sp?.getInt("id",0) == 0 || sp?.getInt("id",0) == null){
            AsyncIsOnlineAddTempProfile().execute()
        }
    }

    // The Timer which can be used to make regularly online-requests
    override fun onResume() {
        super.onResume()
        try {
            timer.schedule(timerTask {
                Log.d("Test", "Timer Works.")
            }, 3000, 3000)
        } catch (e: Exception) {
            Log.d("Timer", "Timer is off")
        }
    }

    // Terminate the timer from onResume()
    override fun onPause() {
        super.onPause()
        timer.cancel()
    }

    // Getting the Back-Button ready
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (sp?.getBoolean("logged", false) == false) {
            menuInflater.inflate(R.menu.main, menu)
        } else {
            menuInflater.inflate(R.menu.main_logged_in, menu)
        }

        return true
    }

    // Handle action bar, right upper corner
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.actionbar_profil -> {
                Log.d("test",sp?.getBoolean("loggedIn", false)!!.toString() + " " + sp?.getInt("auth",0)!!.toString())
                checkIfLoggedIn(this,sp?.getBoolean("loggedIn", false)!!,sp?.getInt("auth",0)!!,PERMISSION_REQUEST)
                return true
            }
            R.id.messages -> {
                showKommDialog(this@MainActivity,sp?.getString("vorname","niemand")!!,
                    sp?.getString("nachname","...")!!,sp?.getInt("id",0)!!,sp?.getInt("auth",0)!!)
                return true
            }
            // ENABLE WHEN THERE IS A NEED FOR IT
//            R.id.actionbar_qr_reader -> {
//                Log.d("ItemClick:", "QR")
//                QRPressed(this)
//                return true
//            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    // Handle nav-bar on the left
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

    // Diashow Slider
    private fun initSlider() {

        mPager = findViewById<ViewPager>(R.id.pager) as ViewPager
        mPager!!.adapter =
            SlidingImage_Adapter(this@MainActivity, this.imageModelArrayList!!)

        val indicator = findViewById<CirclePageIndicator>(R.id.indicator) as CirclePageIndicator

        indicator.setViewPager(mPager)

        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicator.setRadius(5 * density)

        NUM_PAGES = imageModelArrayList!!.size

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            mPager!!.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)

        // Pager listener over indicator
        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                currentPage = position

            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(pos: Int) {

            }
        })

    }

    // Belonging to the Dia-Slider
    companion object {

        private var mPager: ViewPager? = null
        private var currentPage = 0
        private var NUM_PAGES = 0
    }

    // logs that the persons uses the app today
    inner class AsyncToday : AsyncTask<String, String, String>() {
        // stub function
        override fun onPreExecute() {
            super.onPreExecute()
        }

        // main function
        override fun doInBackground(vararg p0: String?): String {
            var Result: String = ""
            try {
                updateIsOnlineToday(sp?.getInt("id",0)!!)
            } catch (e: Exception) {
                println(e)
            }
            return Result
        }

        // what to do after response arrives
        override fun onPostExecute(result: String?) {
        }
    }
    inner class AsyncIsOnlineToday : AsyncTask<String, String, String>() {

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
                AsyncToday().execute()
            }else{
                Toast.makeText(this@MainActivity,"Keine Internetverbindung möglich", Toast.LENGTH_LONG)
            }
        }
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
                schuelerListe = getAllSchueler(this@MainActivity)
            } catch (e: Exception) {
                println(e)
            }
            return Result
        }

        // what to do after response arrives
        override fun onPostExecute(result: String?) {
            //Starting the service to download the photos
            if (ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this@MainActivity,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.INTERNET),
                    SERVICE_PERMISSION_REQUEST)
            }else{
                Log.d("Permission","Already Granted")
                val intent = Intent(this@MainActivity, PhotoDownloadService::class.java)
                val idArray : ArrayList<Int> = ArrayList()
                val updateIdArray : ArrayList<Int> = ArrayList()
                val updateDateArray : ArrayList<String> = ArrayList()
                var i = 0
                for(schueler in schuelerListe!!){
                    idArray?.add(i,schueler.id)
                    i++
                }
                i = 0
                for(item in photoUpdateListe!!){
                    updateIdArray?.add(i, item.photo_id)
                    updateDateArray?.add(i, item.last_update)
                    i++
                }
                intent.putExtra("schuelerList", idArray)
                intent.putExtra("updateIdList", updateIdArray)
                intent.putExtra("updateDateList", updateDateArray)
//                this@MainActivity.startService(intent)
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
                Toast.makeText(this@MainActivity,"Keine Internetverbindung möglich", Toast.LENGTH_LONG)
            }
        }
    }

    inner class AsyncGetPhotoUpdate : AsyncTask<String, String, String>() {
        // stub function
        override fun onPreExecute() {
            super.onPreExecute()
        }

        // main function
        override fun doInBackground(vararg p0: String?): String {
            var Result: String = ""
            try {
                photoUpdateListe = getPhotoUpdate(this@MainActivity)
            } catch (e: Exception) {
                println(e)
            }
            return Result
        }

        // what to do after response arrives
        override fun onPostExecute(result: String?) {
            //Starting the service to download the photos
//            val intent = Intent(this@MainActivity, PhotoDownloadService::class.java)
//            val idArray : ArrayList<Int> = ArrayList()
//            var i = 0
//            for(schueler in schuelerListe!!){
//                idArray?.add(i,schueler.id)
//                i++
//            }
//            intent.putExtra("schuelerList", idArray)
//            this@MainActivity.startService(intent)

        }
    }
    inner class AsyncIsOnlineGetPhotoUpdate : AsyncTask<String, String, String>() {

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
                AsyncGetPhotoUpdate().execute()
            }else{
                Toast.makeText(this@MainActivity,"Keine Internetverbindung möglich", Toast.LENGTH_LONG)
            }
        }
    }

    inner class AsyncAddTempProfile : AsyncTask<String, String, String>() {

        var newId: Int ? = null
        // stub function
        override fun onPreExecute() {
            super.onPreExecute()
        }

        // main function
        override fun doInBackground(vararg p0: String?): String {

            var Result: String = ""
            try {
                newId = addTempProfile(this@MainActivity)
                Log.d("AddTemp", "newId: " + newId.toString())
            } catch (e: Exception) {
                println(e)
            }
            return Result
        }

        // what to do after response arrives
        override fun onPostExecute(result: String?) {
            sp?.edit()?.putInt("id", newId!!)?.apply()
            sp?.edit()?.putInt("temp", 1)?.apply()
        }
    }
    inner class AsyncIsOnlineAddTempProfile : AsyncTask<String, String, String>() {

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
                AsyncAddTempProfile().execute()
            }else{
                Toast.makeText(this@MainActivity,"Keine Internetverbindung möglich", Toast.LENGTH_LONG)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("Permission","Granted")
                    ProfilPressed(this, sp!!.getInt("auth", 0))
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("Permission","Denied")
                }
                return
            }
            SERVICE_PERMISSION_REQUEST -> {
                val intent = Intent(this@MainActivity, PhotoDownloadService::class.java)
                val idArray : ArrayList<Int> = ArrayList()
                val updateIdArray : ArrayList<Int> = ArrayList()
                val updateDateArray : ArrayList<String> = ArrayList()
                var i = 0
                for(schueler in schuelerListe!!){
                    idArray?.add(i,schueler.id)
                    i++
                }
                i = 0
                for(item in photoUpdateListe!!){
                    updateIdArray?.add(i, item.photo_id)
                    updateDateArray?.add(i, item.last_update)
                    i++
                }
                intent.putExtra("schuelerList", idArray)
                intent.putExtra("updateIdList", updateIdArray)
                intent.putExtra("updateDateList", updateDateArray)
//                this@MainActivity.startService(intent)
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

}
