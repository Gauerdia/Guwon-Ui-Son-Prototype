package szymendera_development.guwon_app

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_aktuelles.*
import kotlinx.android.synthetic.main.app_bar_aktuelles.*
import szymendera_development.guwon_app.Fragments.AktuellesLowerDetailFragment
import szymendera_development.guwon_app.Fragments.AktuellesLowerDetailSubBannerFragment
import szymendera_development.guwon_app.Fragments.AktuellesLowerFragment
import szymendera_development.guwon_app.Fragments.AktuellesUpperFragment
import szymendera_development.guwon_app.Utils.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timerTask

class AktuellesActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    AktuellesLowerFragment.OnFragmentInteractionListener, AktuellesUpperFragment.OnFragmentInteractionListener,
    AktuellesUpperFragment.FragmentChangeListener, AktuellesLowerDetailFragment.OnFragmentInteractionListener,
    AktuellesLowerFragment.FragmentChangeListener, AktuellesLowerDetailSubBannerFragment.OnFragmentInteractionListener,
    AktuellesLowerDetailSubBannerFragment.FragmentChangeListener {

    var sp: SharedPreferences? = null

    var fragUpper = AktuellesUpperFragment()
    var fragLower = AktuellesLowerFragment()
    val ft = supportFragmentManager

    var TV_SubBanner1: String? = null
    var TV_SubBanner2: String? = null

    private val handler = Handler()
    var handlerTimeout = 0
    var timer = Timer()

    var PERMISSION_REQUEST = 1000

    var AktuellesListe: List<Aktuelles>? = null
    var BannerUpdateListe: List<BannerUpdate>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aktuelles)

        sp = getSharedPreferences("com.szymendera_development.guwon_app", MODE_PRIVATE)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.aktuelles_lower_fragment, fragLower)
        ft.replace(R.id.aktuelles_upper_fragment, fragUpper)
        ft.commit()

        nav_view.setNavigationItemSelectedListener(this)

        AsyncIsOnlineGetBannerUpdate().execute()

        // Making the Logo in the NavBar-Header clickable
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val headerview = navigationView.getHeaderView(0)
        val navheaderlogo = headerview.findViewById<ImageView>(R.id.navHeaderLogo)
        navheaderlogo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(this@AktuellesActivity, MainActivity::class.java)
                this@AktuellesActivity.startActivity(intent)
            }
        })
}




    // Back-Button
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // Menu right upper corner
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (sp?.getInt("auth", 0) == 1) {
            menuInflater.inflate(R.menu.aktuelles_auth_menu, menu)
        } else {
            menuInflater.inflate(R.menu.template, menu)
        }
        return true
    }

    // Handler for Menu right upper corner
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.profil -> {
                checkIfLoggedIn(this, sp?.getBoolean("loggedIn", false)!!, sp?.getInt("auth", 0)!!, PERMISSION_REQUEST)
                return true
            }
            R.id.banner_aendern -> {
                showUpdateBannerTextDialog(this, "BANNER")
                return true
            }
            R.id.sub_banner_aendern -> {
                showUpdateBannerTextDialog(this, "SUB-BANNER")
                return true
            }
            R.id.banner_masse -> {
                showMasseDialog(this@AktuellesActivity)
                return true}
            R.id.clean_up -> {
                clean_up()
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

    // Handler for NavDrawer
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

    override fun onFragmentInteraction(uri: Uri) {
    }

    override fun replaceFragment(fragment: Fragment, id: Int) {
        var temp_content_banner: String? = null
        var temp_excerpt_banner: String? = null
        var temp_content_subbanner1: String? = null
        var temp_excerpt_subbanner1: String? = null
        var temp_content_subbanner2: String? = null
        var temp_excerpt_subbanner2: String? = null
        for (item in AktuellesListe!!) {
            if (item.name == "Banner") {
                temp_content_banner = item.content
                temp_excerpt_banner = item.excerpt
            } else if (item.name == "SubBanner1") {
                temp_content_subbanner1 = item.content
                temp_excerpt_subbanner1 = item.excerpt
            } else if (item.name == "SubBanner2") {
                temp_content_subbanner2 = item.content
                temp_excerpt_subbanner2 = item.excerpt
            }
        }

        val arguments = Bundle()
        arguments.putString("content_banner", temp_content_banner)
        arguments.putString("excerpt_banner", temp_excerpt_banner)
        arguments.putString("content_subbanner1", temp_content_subbanner1)
        arguments.putString("excerpt_subbanner1", temp_excerpt_subbanner1)
        arguments.putString("content_subbanner2", temp_content_subbanner2)
        arguments.putString("excerpt_subbanner2", temp_excerpt_subbanner2)
        arguments.putInt("id", id)
        fragment.setArguments(arguments)
        ft.beginTransaction()
            .replace(R.id.aktuelles_lower_fragment, fragment, "yes")
            .commit()


    }

    // Gets all text-data which is displayed in Aktuelles from the server
    inner class AsyncGetAktuelles : AsyncTask<String, String, String>() {
        // stub function
        override fun onPreExecute() {
            super.onPreExecute()
        }

        // main function
        override fun doInBackground(vararg p0: String?): String {
            var Result: String = ""
            try {
                AktuellesListe = getAktuelles(this@AktuellesActivity)
            } catch (e: Exception) {
                println(e)
            }
            return Result
        }

        // what to do after response arrives
        override fun onPostExecute(result: String?) {
            var value1: String? = null
            var value2: String? = null
            for (aktuelles in AktuellesListe!!) {
                if (aktuelles.name == "SubBanner1") {
                    value1 = aktuelles.excerpt
                } else if (aktuelles.name == "SubBanner2") {
                    value2 = aktuelles.excerpt
                } else {

                }
            }
            fragLower.setBannerText1(value1!!)
            fragLower.setBannerText2(value2!!)
            AsyncIsOnlineGetBanner().execute()
        }
    }
    inner class AsyncIsOnlineGetAktuelles : AsyncTask<String, String, String>() {

        var success: Boolean? = null

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
            if (success!!) {
                AsyncGetAktuelles().execute()
            } else {
                Toast.makeText(this@AktuellesActivity, "Keine Internetverbindung möglich", Toast.LENGTH_LONG)
            }
        }
    }

    // Gets an array with the last update dates and the version number of the
    // different bannersfrom the server
    inner class AsyncGetBannerUpdate : AsyncTask<String, String, String>() {
        // stub function
        override fun onPreExecute() {
            super.onPreExecute()
        }

        // main function
        override fun doInBackground(vararg p0: String?): String {
            var Result: String = ""
            try {
                BannerUpdateListe = getBannerUpdate(this@AktuellesActivity)
            } catch (e: Exception) {
                println(e)
            }
            return Result
        }

        // what to do after response arrives
        override fun onPostExecute(result: String?) {
            AsyncIsOnlineGetAktuelles().execute()
        }
    }
    inner class AsyncIsOnlineGetBannerUpdate : AsyncTask<String, String, String>() {

        var success: Boolean? = null

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
            if (success!!) {
                AsyncGetBannerUpdate().execute()
            } else {
                Toast.makeText(this@AktuellesActivity, "Keine Internetverbindung möglich", Toast.LENGTH_LONG)
            }
        }
    }

    // Checks, if an internet connection is available and starts GetPhoto afterwards
    inner class AsyncIsOnlineGetBanner : AsyncTask<String, String, String>() {

        var success: Boolean? = null

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
            if (success!!) {
                AsyncGetBanner().execute()
            } else {
                Toast.makeText(this@AktuellesActivity, "Keine Internetverbindung möglich", Toast.LENGTH_LONG)
            }
        }
    }

    // Gets and Sets the desired Photo from the server/ the app-directory
    inner class AsyncGetBanner : AsyncTask<String, String, String>() {

        var DownloadNecessary = intArrayOf(0,0,0)

        // stub function
        override fun onPreExecute() {
            super.onPreExecute()
        }

        // main function
        override fun doInBackground(vararg p0: String?): String {

            var Result: String = ""

            // The date-format we use throughout the whole app
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            // An Editor to store the last banner-update-date
            var sp_editor: SharedPreferences.Editor = sp!!.edit()
            // x iterates through BannerArray. We don't use a for-loop because
            // we need the index
            var x = 0
            var BannerArray = arrayOf("banner","subbanner1","subbanner2")
            // A Boolean which is set, when the local last-change-date and the
            // server last-change-date differ
            var IsAfter = false

            try {
                while (x<3) {
                    // Checking, if File exists already
                    var file = File(filesDir.toString() + File.separator + BannerArray.get(x) + ".jpg")
                    Log.d("Profil", "Check, if File exists")
                    var serverversion: Int? = null

                    // Checking, if the banner has been updated
                    try {
                        // finding the index of the temporary banner
                        var tempID = 0
                        var y = 0
                        for(banner in BannerUpdateListe!!){
                            if(BannerArray.get(x) == banner.name){
                               tempID = y
                                break
                            }
                            y++
                        }
                        // Getting the date when we updated the banner for the last time
//                        var localdate = sdf.parse(sp?.getString(BannerArray.get(x), "01-01-2018"))
                        var localversion = sp?.getInt(BannerArray.get(x), 0)
                        // Getting the date when the server updated the banner for the last time
//                        var serverdate = sdf.parse(BannerUpdateListe?.get(tempID)?.last_update)
                        serverversion = BannerUpdateListe?.get(tempID)?.version
                        Log.d("Aktuelles", BannerArray.get(x) + " local: " + localversion + "server: " + serverversion)
                        // Putting the result in a boolean
                        IsAfter =  (serverversion!! > localversion!!)
                    }catch (e:java.lang.Exception){
                        Log.d("Aktuelles","Data-Comparison-Error: " + e.toString())
                    }

                    if (!file.exists() || IsAfter) {
                        // We set an Array-Element to true, because we want to start a handler
                        // if a download is necessary.
                        DownloadNecessary.set(x, 1)
                        Log.d("Aktuelles", "File doesn't exist")
                        // Starting a request to fetch the banner from the server
                        var request: DownloadManager.Request? = null
                        var temp = "https://marc-szymendera.de/Guwon/Images/" + BannerArray.get(x) + ".jpg"
                        val url = temp.replace("\\s+".toRegex(), "")
                        try {
                            request = DownloadManager.Request(Uri.parse(url))
                        } catch (e: IllegalArgumentException) {
                            Log.d("DownloadManager", e.toString())
                        }

                        /* allow mobile and WiFi downloads */
                        request?.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                        request?.setTitle(BannerArray.get(x) + ".jpg")
                        request?.setDescription("Downloading file")

                        /* we let the user see the download in a notification */
//                        request?.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)

                        /* set the destination path for this download */
                        request?.setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS +
                                    File.separator + ".guwon_app", BannerArray.get(x) + ".jpg"
                        )


                        /* allow the MediaScanner to scan the downloaded file */
                        request?.allowScanningByMediaScanner()
                        val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                        /* this is our unique download id */
                        val DL_ID = dm.enqueue(request)
                    } else {
                        Log.d("Profil", "File exists, no download necessary")
                    }
                    // Getting today's date to save it in the local last-change-array
                    val c = Calendar.getInstance()
                    val resultdate = Date(c.timeInMillis)
                    var dateInString = sdf.format(resultdate)
                    // Saving today's date into the shared pref
//                    sp_editor.putString(BannerArray.get(x),dateInString).commit()
                    sp_editor.putInt(BannerArray.get(x),serverversion!!).commit()
                    x++
                    IsAfter = false
                }
            } catch (e: Exception) {
                Log.d("Downloadmanager", e.toString())
            }
            return Result
        }

        // what to do after response arrives
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var x = 0
            while(x<3){
                if(x == 0 && DownloadNecessary.get(x) == 1){
                    handler.post(getBannerRunnable)
                }else if(x ==  0 && DownloadNecessary.get(x) == 0){
                    fragUpper.setBanner("banner")
                }
                if(x == 1 && DownloadNecessary.get(x) == 1){
                    handler.post(getSubBanner1Runnable)
                }else if(x ==  1 && DownloadNecessary.get(x) == 0){
                    fragLower.setSubBanner1()
                }
                if(x == 2 && DownloadNecessary.get(x) == 1){
                    handler.post(getSubBanner2Runnable)
                }else if(x ==  2 && DownloadNecessary.get(x) == 0){
                    fragLower.setSubBanner2()
                }
                x++
            }
        }
    }

    // Checks for the wanted photo until it is ready, sets it to the desired place and terminates.
    val getBannerRunnable = object : Runnable {
        override fun run() {
            var file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +
                        File.separator + ".guwon_app", "banner" + ".jpg")
            Log.d("Aktuelles", "file:" + file.toString())
            if(file.exists()){
                var tempsrc = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +
                        File.separator + ".guwon_app" + File.separator + "banner" + ".jpg")

                var temptarget = File(filesDir.toString() + File.separator + "banner" + ".jpg")

                AsyncCopy(tempsrc,temptarget,"banner").execute()


                Log.d("Profil", "file exists in handler")
                handler.removeCallbacks(this)
            }else{
                if(handlerTimeout < 10) {
                    Log.d("Handler", "file doesnt exist in handler")
                    handler.postDelayed(this, 1000)
                    handlerTimeout++
                }else{
                    Log.d("Handler", "terminates with code 404")
                    handler.removeCallbacks(this)
                }
            }
        }
    }

    val getSubBanner1Runnable = object : Runnable {
        override fun run() {
            var file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +
                        File.separator + ".guwon_app", "subbanner1" + ".jpg")
            Log.d("Aktuelles", "file:" + file.toString())
            if(file.exists()){
                var tempsrc = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +
                        File.separator + ".guwon_app" + File.separator + "subbanner1" + ".jpg")

                var temptarget = File(filesDir.toString() + File.separator + "subbanner1" + ".jpg")
//                copyFile(tempsrc, temptarget)
                AsyncCopy(tempsrc,temptarget,"subbanner1").execute()
                Log.d("Profil", "file exists in handler")

                handler.removeCallbacks(this)
            }else{
                if(handlerTimeout < 10) {
                    Log.d("Handler", "file doesnt exist in handler")
                    handler.postDelayed(this, 1000)
                    handlerTimeout++
                }else{
                    Log.d("Handler", "terminates with code 404")
                    handler.removeCallbacks(this)
                }
            }
        }
    }

    val getSubBanner2Runnable = object : Runnable {
        override fun run() {
            var file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +
                        File.separator + ".guwon_app", "subbanner2" + ".jpg")
            Log.d("Aktuelles", "file:" + file.toString())
            if(file.exists()){
                var tempsrc = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +
                        File.separator + ".guwon_app" + File.separator + "subbanner2" + ".jpg")

                var temptarget = File(filesDir.toString() + File.separator + "subbanner2" + ".jpg")
//                copyFile(tempsrc, temptarget)
//                timer.schedule(timerTask{},100)
                Log.d("Profil", "file exists in handler")
                AsyncCopy(tempsrc,temptarget,"subbanner2").execute()
                handler.removeCallbacks(this)
            }else{
                if(handlerTimeout < 10) {
                    Log.d("Handler", "file doesnt exist in handler")
                    handler.postDelayed(this, 1000)
                    handlerTimeout++
                }else{
                    Log.d("Handler", "terminates with code 404")
                    handler.removeCallbacks(this)
                }
            }
        }
    }

    // Copy the file from the downloads directory to the app-only directory
    @Throws(IOException::class)
    fun copyFile(src: File, dst: File) {
        val inChannel = FileInputStream(src).getChannel()
        val outChannel = FileOutputStream(dst).getChannel()
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel)
        } finally {
            if (inChannel != null)
                inChannel.close()
            if (outChannel != null)
                outChannel.close()
        }
        val file = src
        val deleted = file.delete()
    }

    inner class AsyncCopy(src:File, dst:File, bannername:String) : AsyncTask<String, String, String>() {

        val src = src
        val dst = dst
        val bannername = bannername
        // stub function
        override fun onPreExecute() {
            super.onPreExecute()
        }

        // main function
        override fun doInBackground(vararg p0: String?): String {
            var Result: String = ""
            try {
                val inChannel = FileInputStream(src).getChannel()
                val outChannel = FileOutputStream(dst).getChannel()
                try {
                    inChannel!!.transferTo(0, inChannel!!.size(), outChannel)
                } finally {
                    if (inChannel != null)
                        inChannel!!.close()
                    if (outChannel != null)
                        outChannel!!.close()
                }
                val file = src
                val deleted = file.delete()
            } catch (e: Exception) {
                println(e)
            }
            return Result
        }

        // what to do after response arrives
        override fun onPostExecute(result: String?) {
            if(bannername == "banner"){
                fragUpper.setBanner("banner")
            }else if(bannername == "subbanner1"){
                fragLower.setSubBanner1()
            }else if(bannername == "subbanner2"){
                fragLower.setSubBanner2()
            }
            super.onPostExecute(result)
        }
    }

    fun clean_up(){
        var file1 = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +
                    File.separator + ".guwon_app", "banner" + ".jpg")
        var file2 =File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
            , "banner" + ".jpg")
        var file1_clean = false
        var file2_clean = false
        while(file1_clean == false){
            if (file1.exists()){
                val deleted = file1.delete()
            }else{
                file1_clean = true
            }
        }
        while(file2_clean == false){
            if (file2.exists()){
                val deleted = file2.delete()
            }else{
                file2_clean = true
            }
        }
    }

}
