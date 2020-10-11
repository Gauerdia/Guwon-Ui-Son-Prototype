package szymendera_development.guwon_app

import android.app.DownloadManager
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_profil.*
import kotlinx.android.synthetic.main.app_bar_profil.*
import java.text.SimpleDateFormat
import java.util.*
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import szymendera_development.guwon_app.Fragments.ProfilLowerFragment
import szymendera_development.guwon_app.Fragments.ProfilUpperFragment
import szymendera_development.guwon_app.Utils.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class ProfilActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener ,
ProfilLowerFragment.OnFragmentInteractionListener, ProfilUpperFragment.OnFragmentInteractionListener{

    var sp: SharedPreferences? = null
    private var tv_vorname: TextView? = null
    private var tv_nachname: TextView? = null
    private var tv_rang: TextView? = null
    private var tv_sportart: TextView? = null
    var authorized = 0
    var iv_photo: ImageView? = null
    var ProfilPhotoId: String? = null

    private val handler = Handler()
    var handlerTimeout = 0

    private val PERMISSION_REQUEST: Int = 1000

    var schuelerListe: List<Schueler>? = null
    var schuelerCompetitionListe : List<SchuelerCompetition>? = null

    var fragUpper = ProfilUpperFragment()
    var fragLower = ProfilLowerFragment()

    var particularSchueler = SchuelerComplete(
        0, "vorname", "nachname", "", "", "", "", "", "",
        "", "", 0, "", 0, 0, 0, 0,
        0, 0, 0, "01-01-2018", 0, 0,
        0, 0, 0,0)

    var view1: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        setSupportActionBar(toolbar)

        sp = getSharedPreferences("com.szymendera_development.guwon_app",MODE_PRIVATE)
        authorized = intent.getIntExtra("authorized", 0)


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        fragLower = ProfilLowerFragment()
        fragUpper = ProfilUpperFragment()

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.profil_lower_fragment, fragLower)
        ft.replace(R.id.profil_upper_fragment, fragUpper)
        ft.commit()

        tv_vorname = findViewById(R.id.profil_frag_vorname)
        tv_nachname = findViewById(R.id.profil_frag_nachname)
        tv_rang = findViewById(R.id.profil_frag_rang)
        tv_sportart = findViewById(R.id.profil_frag_sportart)
        iv_photo = findViewById(R.id.profil_frag_photo)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this,
                            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            PERMISSION_REQUEST)
        }



        nav_view.setNavigationItemSelectedListener(this)

        ProfilPhotoId = "profilphoto" + sp?.getInt("id",0).toString()

        // Making the Logo in the NavBar-Header clickable
        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        val headerview = navigationView.getHeaderView(0)
        val navheaderlogo = headerview.findViewById<ImageView>(R.id.navHeaderLogo)
        navheaderlogo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(this@ProfilActivity, MainActivity::class.java)
                this@ProfilActivity.startActivity(intent)
            }
        })
        AsyncIsOnlineGetProfil().execute()
//        AsyncGetPhoto("profilphoto").execute()
        AsyncIsOnlineGetPhoto().execute()
        AsyncIsOnlineGetRangliste().execute()

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
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("Permission","Denied")
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onFragmentInteraction(uri: Uri) {
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
            menuInflater.inflate(R.menu.profil_menu_auth, menu)
        }else{
            menuInflater.inflate(R.menu.profil_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.schueler_suchen -> {
                SearchPressed(this)
//                test()
                return true
            }
            R.id.anwesenheit -> {
                ProfilAnwesenheitPressed(this)
                return true
            }
            R.id.events -> {
                EventsPressed(this)
                return true
            }
            R.id.item_add_event -> {
                showAddEventDialog(this@ProfilActivity)
                return true
            }
            R.id.kalender -> {
                ProfilKalenderPressed(this)
                return true
            }
            R.id.schuelerHinzufuegen ->{
                addSchuelerDialog(this)
                return true
            }
            R.id.item_quiz -> {
                // Getting the current date and the date the quiz has been
                // finished the last time, to check if it's allowed to do the quiz.
                try {
                    val sdf = SimpleDateFormat("dd-MM-yyyy")
                    val strDate = sdf.parse(particularSchueler.last_quiz)
                    if (Date().after(strDate)) {
                        QuizPressed(this)
                    }
                    else{
                        toast("Das Quiz ist einmal pro Woche durchführbar! Bitte habe Geduld.")
                    }
                }catch(e:java.lang.Exception){
                    Log.d("error", e.toString())
                }
                return true
            }
            R.id.item_quiz_hinzufuegen -> {
                addQuizDialog(this)
                return true
            }
            R.id.item_rangliste -> {
                RanglistePressed(this)
                return true
            }
            R.id.item_log_out ->{
                sp?.edit()?.putBoolean("loggedIn", false)?.apply()
                sp?.edit()?.putInt("id", 0)?.apply()
                sp?.edit()?.putString("vorname","")?.apply()
                sp?.edit()?.putInt("auth", 0)?.apply()
                    val intent = Intent(this@ProfilActivity, MainActivity::class.java)
                    this@ProfilActivity.startActivity(intent)
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
//            R.id.imageView -> {
//                AktuellesPressed(this)
//            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    // Checks, if an internet connection is availabe and starts GetProfil afterwards
    inner class AsyncIsOnlineGetProfil : AsyncTask<String, String, String>() {

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
                AsyncGetProfil(sp!!.getInt("id",0)).execute()
            }else{
                Toast.makeText(this@ProfilActivity,"Keine Internetverbindung möglich", Toast.LENGTH_LONG)
            }
        }
    }

    // Gets the information from the server to display the profile
    inner class AsyncGetProfil(id: Int) : AsyncTask<String, String, String>() {
        val id_inner = id
        // stub function
        override fun onPreExecute() { super.onPreExecute() }
        // main function
        override fun doInBackground(vararg p0: String?): String {
            var Result: String = ""
            try {
                particularSchueler =
                    getParticularSchueler(id_inner, this@ProfilActivity)
            } catch (e: Exception) {
                println(e)
            }
            return Result
        }
        // what to do after response arrives
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            fragUpper.setVorname(particularSchueler.vorname)
            fragUpper.setNachname(particularSchueler.nachname)
            var sportart_value = fragUpper.getSportart()
            var sportart_value2 = ""
            var SportartenCounter = 0
            if(particularSchueler.TaekwonDo == 1){
                    sportart_value = "Taekwon-Do"
                SportartenCounter++
            }
            if(particularSchueler.Kickboxen == 1){
                if(SportartenCounter == 0){
                    sportart_value = "Kickboxen"
                }else{
                    sportart_value += ",Kickboxen"
                }
                SportartenCounter++

            }
            if(particularSchueler.Yoga == 1){
                if(SportartenCounter == 0){
                    sportart_value = "Yoga"
                }else{
                    sportart_value += ",Yoga"
                }
                SportartenCounter++
            }
            if(particularSchueler.Pilates == 1){
                if(SportartenCounter == 0){
                    sportart_value = "Pilates"
                }else{
                    sportart_value += ",Pilates"
                }
            }
            fragUpper.setSportart(sportart_value)
            when(particularSchueler.Rang){
                0 -> {fragUpper.setRang("10.Kup")}
                1 -> {fragUpper.setRang("9.Kup")}
                2 -> {fragUpper.setRang("8.Kup")}
                3 -> {fragUpper.setRang("7.Kup") }
                4 -> {fragUpper.setRang("6.Kup") }
                5 -> {fragUpper.setRang("5.Kup")}
                6 -> {fragUpper.setRang("4.Kup")}
                7 -> {fragUpper.setRang("3.Kup")}
                8 -> {fragUpper.setRang("2.Kup")}
                9 -> {fragUpper.setRang("1.Kup")}
                10 -> {fragUpper.setRang("1.Dan")}
                11 -> {fragUpper.setRang("2.Dan")}
                12 -> {fragUpper.setRang("3.Dan")}
                13 -> {fragUpper.setRang("4.Dan") }
                14 -> {fragUpper.setRang("5.Dan")}
                15 -> {fragUpper.setRang("6.Dan")}
                16 -> {fragUpper.setRang("7.Dan")}
                17 -> {fragUpper.setRang("8.Dan")}
                18 -> {fragUpper.setRang("9.Dan")}
                else -> {
                    tv_rang?.text = "Out of Range"
                }
            }
        }
    }

    // Checks, if an internet connection is availabe and starts GetPhoto afterwards
    inner class AsyncIsOnlineGetPhoto : AsyncTask<String, String, String>() {

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
                AsyncGetPhoto().execute()
            }else{
                Toast.makeText(this@ProfilActivity,"Keine Internetverbindung möglich", Toast.LENGTH_LONG)
            }
        }
    }

    // Gets and Sets the desired Photo from the server/ the app-directory
    inner class AsyncGetPhoto : AsyncTask<String, String, String>() {

        var DownloadNecessary: Boolean = false

        // stub function
        override fun onPreExecute() { super.onPreExecute() }
        // main function
        override fun doInBackground(vararg p0: String?): String {
            var Result: String = ""
            try {
//                var file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +
//                        File.separator + ".guwon_app", ProfilPhotoId + "." + "jpg")
                var file = File(filesDir.toString() + File.separator + ProfilPhotoId + ".jpg")
                Log.d("Profil","Check, if File exists")
                if(!file.exists()){
                    DownloadNecessary = true
                    Log.d("Profil","File doesn't exist")
                    var request: DownloadManager.Request? = null
                    var temp = "https://marc-szymendera.de/Guwon/Images/" + ProfilPhotoId + ".jpg"
                    val url = temp.replace("\\s+".toRegex(), "")
                    try {
                        request = DownloadManager.Request(Uri.parse(url))
                    } catch (e: IllegalArgumentException) {
                        Log.d("DownloadManager", e.toString())
                    }

                    /* allow mobile and WiFi downloads */
                    request?.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                    request?.setTitle(ProfilPhotoId + ".jpg")
                    request?.setDescription("Downloading file")

                    /* we let the user see the download in a notification */
                    request?.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)

                    /* set the destination path for this download */
                    request?.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS +
                            File.separator + ".guwon_app", ProfilPhotoId + "." + "jpg")


                    /* allow the MediaScanner to scan the downloaded file */
                    request?.allowScanningByMediaScanner()
                    val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    /* this is our unique download id */
                    val DL_ID = dm.enqueue(request)
                }else{
                    Log.d("Profil","File exists, no download necessary")
                }
            } catch (e: Exception) {
                Log.d("Downloadmanager",e.toString())
            }
            return Result
        }
        // what to do after response arrives
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if(DownloadNecessary){
                handler.post(getPhotoRunnable)
            }else{
                fragUpper.setImageView(ProfilPhotoId!!)
            }
        }
    }

    // Checks for the wanted photo until it is ready, sets it to the desired place and terminates.
    val getPhotoRunnable = object : Runnable {
        override fun run() {
            var file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +
                    File.separator + ".guwon_app", ProfilPhotoId + "." + "jpg")
            Log.d("Profil", "file:" + file.toString())
            if(file.exists()){
                var tempsrc = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +
                        File.separator + ".guwon_app" + File.separator + ProfilPhotoId + "." + "jpg")

                var temptarget = File(filesDir.toString() + File.separator + ProfilPhotoId + ".jpg")


                AsyncCopy(tempsrc,temptarget).execute()
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

    // Copy the file from the downloads directory to the app-only directory
//    @Throws(IOException::class)
//    fun copyFile(src: File, dst: File) {
//        val inChannel = FileInputStream(src).getChannel()
//        val outChannel = FileOutputStream(dst).getChannel()
//        try {
//            inChannel!!.transferTo(0, inChannel!!.size(), outChannel)
//        } finally {
//            if (inChannel != null)
//                inChannel!!.close()
//            if (outChannel != null)
//                outChannel!!.close()
//        }
//        val file = src
//        val deleted = file.delete()
//    }

    // Gets Rangliste from Server to display the scores
    inner class AsyncGetRangliste : AsyncTask<String, String, String>() {
        // stub function
        override fun onPreExecute() {
            super.onPreExecute()
        }

        // main function
        override fun doInBackground(vararg p0: String?): String {
            var Result: String = ""
            try {
                schuelerCompetitionListe =
                    getSchuelerCompetition(this@ProfilActivity)
            } catch (e: Exception) {
                println(e)
            }
            return Result
        }

        // what to do after response arrives
        override fun onPostExecute(result: String?) {
            var i = 1
            for(item in schuelerCompetitionListe!!){
                if(item.vorname == particularSchueler.vorname){
                    fragLower.setRanglistenPlatz(i)
                }
                i++
            }
            fragLower.setGesamtPunkte(particularSchueler.Overall_Score)
            fragLower.setTrainingsPunkte(particularSchueler.Anwesenheit_Score)
            fragLower.setQuizPunkte(particularSchueler.Quiz_Score)
            fragLower.setEventPunkte(particularSchueler.Event_Score)
        }
    }

    // Checks, if an internet connection is availabe and starts GetRangliste afterwards
    inner class AsyncIsOnlineGetRangliste : AsyncTask<String, String, String>() {

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
                AsyncGetRangliste().execute()
            }else{
                toast("Keine Internetverbindung möglich")
            }
        }
    }

    inner class AsyncCopy(src:File, dst:File) : AsyncTask<String, String, String>() {

        val src = src
        val dst = dst
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
            fragUpper.setImageView(ProfilPhotoId!!)
            super.onPostExecute(result)
        }
    }

}
