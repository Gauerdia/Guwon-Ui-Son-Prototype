package szymendera_development.guwon_app

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Handler
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
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_schueler_profil.*
import kotlinx.android.synthetic.main.app_bar_schueler_profil.*
import szymendera_development.guwon_app.Fragments.SchuelerProfilLowerFragment
import szymendera_development.guwon_app.Fragments.SchuelerProfilUpperFragment
import szymendera_development.guwon_app.Remote.RetrofitClient
import szymendera_development.guwon_app.Remote.UploadAPI
import szymendera_development.guwon_app.Utils.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset
import java.util.*

class SchuelerProfilActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener,
    SchuelerProfilLowerFragment.OnFragmentInteractionListener, SchuelerProfilUpperFragment.OnFragmentInteractionListener{

    private var tv_vorname: TextView? = null
    private var tv_nachname: TextView? = null
    private var tv_rang: TextView? = null
    private var tv_sportart: TextView? = null
    var schuelerID = 0

    var timer: Timer? = null
    val handler = Handler()
    var handlerTimeout = 0

    var ProfilPhotoId: String? = null

    var fragUpper = SchuelerProfilUpperFragment()
    var fragLower = SchuelerProfilLowerFragment()

    // values for the upload-function
    private val PERMISSION_REQUEST: Int = 1000
    private val PICK_IMAGE_REQUEST: Int = 1001
    val BASE_URL = "https://marc-szymendera.de"
    val apiUpload: UploadAPI
        get() = RetrofitClient.getClient(BASE_URL).create(UploadAPI::class.java)
    lateinit var mService: UploadAPI
    var selectedFileUri: Uri? = null

    var sp: SharedPreferences? = null

    var particularSchueler = SchuelerComplete(
        0, "vorname", "nachname", "", "", "", "", "", "",
        "", "", 0, "", 0, 0, 0, 0,
        0, 0, 0, "01-01-2018", 0, 0, 0, 0,
        0,0
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schueler_profil)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        sp = getSharedPreferences("com.szymendera_development.guwon_app", MODE_PRIVATE)

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.schueler_profil_lower_fragment, fragLower)
        ft.replace(R.id.schueler_profil_upper_fragment, fragUpper)
        ft.commit()

        tv_vorname = findViewById(R.id.schueler_profil_frag_vorname)
        tv_nachname = findViewById(R.id.schueler_profil_frag_nachname)
        tv_rang = findViewById(R.id.schueler_profil_frag_rang)
        tv_sportart = findViewById(R.id.schueler_profil_frag_sportart)

        nav_view.setNavigationItemSelectedListener(this)

        var SucheIntent = getIntent()

        // Receiving who we are dealing with.
        schuelerID = SucheIntent.getIntExtra("id", 0)

        // Receiving the information about the Schueler and storing them in
        // a locally accessible variable called particularSchueler.
        // The function will also start to search for the photo of the schueler.
        AsyncGetParticularSchueler(schuelerID).execute()

        // Making the Logo in the NavBar-Header clickable //
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val headerview = navigationView.getHeaderView(0)
        val navheaderlogo = headerview.findViewById<ImageView>(R.id.navHeaderLogo)
        navheaderlogo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(this@SchuelerProfilActivity, MainActivity::class.java)
                this@SchuelerProfilActivity.startActivity(intent)
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
        menuInflater.inflate(R.menu.schueler_profil, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.item_profil -> {
                ProfilPressed(this, sp?.getInt("auth", 0)!!)
                return true
            }
            R.id.item_rank_erhoehen -> {
                RangClicked()
                return true
            }
            R.id.item_anwesenheit_nachtraeglich -> {
                AnwesenheitNachträglichClicked()
                return true
            }
            R.id.item_bild_hochladen -> {
//                UploadFilePressed()
                UploadTestPressed(this, particularSchueler.id)
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

    fun GibonReadClicked(view: View) {
        showDisciplineDialogRead(
            this,
            particularSchueler.Gibon,
            "Gibon",
            particularSchueler.vorname,
            particularSchueler.nachname
        )
    }

    fun TeulReadClicked(view: View) {
        showDisciplineDialogRead(
            this,
            particularSchueler.Teul,
            "Teul",
            particularSchueler.vorname,
            particularSchueler.nachname
        )
    }

    fun HanbonReadClicked(view: View) {
        showDisciplineDialogRead(
            this,
            particularSchueler.Hanbon,
            "Hanbon",
            particularSchueler.vorname,
            particularSchueler.nachname
        )
    }

    fun GyeokpaReadClicked(view: View) {
        showDisciplineDialogRead(
            this,
            particularSchueler.Gyeokpa,
            "Gyeokpa",
            particularSchueler.vorname,
            particularSchueler.nachname
        )
    }

    fun DaeryeonReadClicked(view: View) {
        showDisciplineDialogRead(
            this,
            particularSchueler.Daeryeon,
            "Daeryeon",
            particularSchueler.vorname,
            particularSchueler.nachname
        )
    }

    fun ChayuReadClicked(view: View) {
        showDisciplineDialogRead(
            this,
            particularSchueler.Chayu,
            "Chayu",
            particularSchueler.vorname,
            particularSchueler.nachname
        )
    }

    fun HosinsulReadClicked(view: View) {
        showDisciplineDialogRead(
            this,
            particularSchueler.Hosinsul,
            "Hosinsul",
            particularSchueler.vorname,
            particularSchueler.nachname
        )
    }

    fun GibonEditClicked(view: View) {
        println("EditClicked. Id: " + particularSchueler.id)
        showDisciplineDialogEdit(
            this,
            "Gibon",
            particularSchueler.vorname,
            particularSchueler.nachname,
            particularSchueler.id,
            sp!!.getString("vorname", "jemand"),
            handler,
            updateRangRunnable
        )
    }

    fun TeulEditClicked(view: View) {
        showDisciplineDialogEdit(
            this,
            "Teul",
            particularSchueler.vorname,
            particularSchueler.nachname,
            particularSchueler.id,
            sp!!.getString("vorname", "jemand"),
            handler,
            updateRangRunnable
        )
    }

    fun HanbonEditClicked(view: View) {
        showDisciplineDialogEdit(
            this,
            "Hanbon",
            particularSchueler.vorname,
            particularSchueler.nachname,
            particularSchueler.id,
            sp!!.getString("vorname", "jemand"),
            handler,
            updateRangRunnable
        )
    }

    fun GyeokpaEditClicked(view: View) {
        showDisciplineDialogEdit(
            this,
            "Gyeokpa",
            particularSchueler.vorname,
            particularSchueler.nachname,
            particularSchueler.id,
            sp!!.getString("vorname", "jemand"),
            handler,
            updateRangRunnable
        )
    }

    fun DaeryeonEditClicked(view: View) {
        showDisciplineDialogEdit(
            this,
            "Daeryeon",
            particularSchueler.vorname,
            particularSchueler.nachname,
            particularSchueler.id,
            sp!!.getString("vorname", "jemand"),
            handler,
            updateRangRunnable
        )
    }

    fun ChayuEditClicked(view: View) {
        showDisciplineDialogEdit(
            this,
            "Chayu",
            particularSchueler.vorname,
            particularSchueler.nachname,
            particularSchueler.id,
            sp!!.getString("vorname", "jemand"),
            handler,
            updateRangRunnable
        )
    }

    fun HosinsulEditClicked(view: View) {
        showDisciplineDialogEdit(
            this,
            "Hosinsul",
            particularSchueler.vorname,
            particularSchueler.nachname,
            particularSchueler.id,
            sp!!.getString("vorname", "jemand"),
            handler,
            updateRangRunnable
        )
    }

    fun PruefungsEignungReadClicked(view: View) {
        showDisciplineDialogRead(
            this,
            particularSchueler.Pruefungs_Bereit,
            "Pruefungs_Bereit",
            particularSchueler.vorname,
            particularSchueler.nachname
        )
    }

    fun PruefungsEignungEditClicked(view: View) {
        showPruefungsEignungEditDialog(
            this, "tobias",
            particularSchueler.nachname, particularSchueler.id,
            handler,
            updateRangRunnable
        )
    }

    fun RangClicked() {
        if(particularSchueler.Rang < 19) {
            askRangRiseDialog(
                this,
                particularSchueler.id,
                sp!!.getString("vorname", "jemand"),
                handler,
                updateRangRunnable
            )
        }
        else{
            Toast.makeText(this,"maximaler Rang wurde bereits erreicht.", Toast.LENGTH_SHORT)
        }
    }

    fun AnwesenheitNachträglichClicked() {
        NachträglichAnwesendDialog(
            this,
            particularSchueler.id,
            particularSchueler.vorname,
            particularSchueler.nachname
        )
    }

    inner class AsyncGetParticularSchueler(id: Int) : AsyncTask<String, String, String>() {
        val id_inner = id
        // stub function
        override fun onPreExecute() {
            super.onPreExecute()
        }

        // main function
        override fun doInBackground(vararg p0: String?): String {
            var Result: String = ""
            try {
                // Get all information about him/her
                particularSchueler =
                    getParticularSchueler(id_inner, this@SchuelerProfilActivity)
            } catch (e: Exception) {
                println(e)
            }
            return Result
        }

        // what to do after response arrives
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            // Setting all the visible parameters concerning the person
            fragUpper.setVorname(particularSchueler.vorname)
            fragUpper.setNachname(particularSchueler.nachname)
            var sportart_value = ""
            var SportartenCounter = 0
            if (particularSchueler.TaekwonDo == 1) {
                sportart_value = "Taekwon-Do"
                SportartenCounter++
            }
            if (particularSchueler.Kickboxen == 1) {
                if (SportartenCounter == 0) {
                    sportart_value = "Kickboxen"
                } else {
                    sportart_value += ",Kickboxen"
                }
                SportartenCounter++
            }
            if (particularSchueler.Yoga == 1) {
                if (SportartenCounter == 0) {
                    sportart_value = "Yoga"
                } else {
                    sportart_value += ", Yoga"
                }
                SportartenCounter++
            }
            if (particularSchueler.Pilates == 1) {
                if (SportartenCounter == 0) {
                    sportart_value = "Pilates"
                } else {
                    sportart_value += ", Pilates"
                }
            }
            fragUpper.setSportart(sportart_value)
            when (particularSchueler.Rang) {
                0 -> {
                    fragUpper.setRang("10.Kup")
                }
                1 -> {
                    fragUpper.setRang("9.Kup")
                }
                2 -> {
                    fragUpper.setRang("8.Kup")
                }
                3 -> {
                    fragUpper.setRang("7.Kup")
                }
                4 -> {
                    fragUpper.setRang("6.Kup")
                }
                5 -> {
                    fragUpper.setRang("5.Kup")
                }
                6 -> {
                    fragUpper.setRang("4.Kup")
                }
                7 -> {
                    fragUpper.setRang("3.Kup")
                }
                8 -> {
                    fragUpper.setRang("2.Kup")
                }
                9 -> {
                    fragUpper.setRang("1.Kup")
                }
                10 -> {
                    fragUpper.setRang("1.Dan")
                }
                11 -> {
                    fragUpper.setRang("2.Dan")
                }
                12 -> {
                    fragUpper.setRang("3.Dan")
                }
                13 -> {
                    fragUpper.setRang("4.Dan")
                }
                14 -> {
                    fragUpper.setRang("5.Dan")
                }
                15 -> {
                    fragUpper.setRang("6.Dan")
                }
                16 -> {
                    fragUpper.setRang("7.Dan")
                }
                17 -> {
                    fragUpper.setRang("8.Dan")
                }
                18 -> {
                    fragUpper.setRang("9.Dan")
                }
                else -> {
                    tv_rang?.text = "Out of Range"
                }
            }
            ProfilPhotoId = "profilphoto" + particularSchueler.id
            // Checking for a photo
            AsyncGetPhoto().execute()
//            handler.removeCallbacks(updateRangRunnable)
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
            super.onPostExecute(result)
        }
    }

    inner class AsyncGetPhoto : AsyncTask<String, String, String>() {

        var DownloadNecessary: Boolean = false

        // stub function
        override fun onPreExecute() { super.onPreExecute() }

        // main function
        override fun doInBackground(vararg p0: String?): String {
            var Result: String = ""
            var sp_editor: SharedPreferences.Editor = sp!!.edit()
            var sp_id = "image_version_" + particularSchueler.id
            var localversion = sp?.getInt(sp_id, 0)
            var serverversion = particularSchueler.image_version

            try {
                Log.d("SchuelerProfil","Check, if File exists")
                var file = File(filesDir.toString() + File.separator + ProfilPhotoId + ".jpg")


                var IsAfter =  (serverversion > localversion!!)

                if(!file.exists() || IsAfter) {
                    Log.d("SchuelerProfil","File does not exist/is outdated, download necessary")
                    DownloadNecessary = true
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
//                    request?.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)

                    /* set the destination path for this download */
                    request?.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS +
                                File.separator + ".guwon_app", ProfilPhotoId + ".jpg"
                    )
                    /* allow the MediaScanner to scan the downloaded file */
                    request?.allowScanningByMediaScanner()
                    val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    /* this is our unique download id */
                    val DL_ID = dm.enqueue(request)
                }
                else {
                    Log.d("SchuelerProfil","File exists, no download necessary")
                }
                sp_editor.putInt(sp_id,serverversion!!).commit()
            } catch (e: Exception) {
                Log.d("Downloadmanager",e.toString())
            }
            return Result
        }
        // what to do after response arrives
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if(DownloadNecessary){
                // starting a handler which will wait until our photo
                // has been successfully downloaded. He will then
                // put it into its place and terminate.
                handler.post(getPhotoRunnable)
            }else{
                // photo already on the phone? then we can instantly
                // put it into its place.
                fragUpper.updatePhoto(ProfilPhotoId!!)
            }
        }
    }

    val getPhotoRunnable = object : Runnable {
        override fun run() {
            var file = File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).toString() +
                    File.separator + ".guwon_app", ProfilPhotoId + "." + "jpg")
            Log.d("SchuelerProfil", "file:" + file.toString())
            if(file.exists()){
                var tempsrc = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + File.separator + ".guwon_app" + File.separator + ProfilPhotoId + "." + "jpg")

                var temptarget = File(filesDir.toString() + File.separator + ProfilPhotoId + ".jpg")
                AsyncCopy(tempsrc,temptarget).execute()
                Log.d("SchuelerProfil", "file exists in handler")
                fragUpper.updatePhoto(ProfilPhotoId!!)
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

    val updateRangRunnable = object : Runnable{
        override fun run() {
            Log.d("UpdateRangRun", "active")
            handler.postDelayed(this, 2000)
            AsyncGetParticularSchueler(schuelerID).execute()
        }
    }

    @Throws(IOException::class)
    fun copyFile(src: File, dst: File) {
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
    }

    override fun onFragmentInteraction(uri: Uri) {

    }
}
