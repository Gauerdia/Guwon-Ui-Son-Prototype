package szymendera_development.guwon_app

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.ipaulpro.afilechooser.utils.FileUtils
import kotlinx.android.synthetic.main.activity_schueler_profil.*
import kotlinx.android.synthetic.main.app_bar_schueler_profil.*
import kotlinx.android.synthetic.main.content_schueler_profil_upload.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import szymendera_development.guwon_app.Remote.RetrofitClient
import szymendera_development.guwon_app.Remote.UploadAPI
import szymendera_development.guwon_app.Utils.*

class SchuelerProfilUploadActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    ProgressRequestBody.UploadCallbacks{

    override fun onProgressUpdate(percentage: Int){
//        dialog.progress = percentage
    }

    val BASE_URL = "https://marc-szymendera.de"

    var sp: SharedPreferences? = null
    var intentId : Int? = null

    val apiUpload: UploadAPI
        get() = RetrofitClient.getClient(BASE_URL).create(UploadAPI::class.java)

    private val PERMISSION_REQUEST: Int = 1000
    private val PICK_IMAGE_REQUEST: Int = 1001


    var selectedFileUri: Uri? = null
    lateinit var dialog: ProgressDialog

    lateinit var mService: UploadAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schueler_profil_upload)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST)

        intentId = intent.getIntExtra("id", 0)

        mService = apiUpload

        sp = getSharedPreferences("com.szymendera_development.guwon_app", MODE_PRIVATE)

        iv_upload!!.setOnClickListener{chooseImage()}
        btn_upload!!.setOnClickListener{uploadFile()}

        // Making the Logo in the NavBar-Header clickable
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val headerview = navigationView.getHeaderView(0)
        val navheaderlogo = headerview.findViewById<ImageView>(R.id.navHeaderLogo)
        navheaderlogo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(this@SchuelerProfilUploadActivity, MainActivity::class.java)
                this@SchuelerProfilUploadActivity.startActivity(intent)
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
                ProfilPressed(this, sp!!.getInt("auth", 0))
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

    fun uploadFile() {
        if(selectedFileUri != null){
            val file = FileUtils.getFile(this, selectedFileUri)
            val requestFile = ProgressRequestBody(file, this)
            val body = MultipartBody.Part.createFormData("uploaded_file", "profilphoto" + intentId + ".jpg", requestFile)
            Thread(Runnable{
                mService.uploadFile(body)
                    .enqueue(object: retrofit2.Callback<String>{
                        override fun onFailure(call: Call<String>?, t: Throwable?){
                            Toast.makeText(this@SchuelerProfilUploadActivity,t!!.message, Toast.LENGTH_SHORT).show()
                        }
                        override fun onResponse(call: Call<String>?, response: Response<String>?){
                            Toast.makeText(this@SchuelerProfilUploadActivity,"Uploading successful", Toast.LENGTH_SHORT).show()
                            updateImageVersion(intentId!!)
                        }
                    })
            }).start()
            if(sp?.getString(intentId.toString(),"error") == "error"){
                updatePhotoDate(intentId!!,"ADD")
                Log.d("SchuelerProfilUpload","ADD Photodate")
            }else{
                updatePhotoDate(intentId!!,"UPDATE")
                Log.d("SchuelerProfilUpload", "Update Photodate")
            }
        }else{
            Toast.makeText(this, "please choose File", Toast.LENGTH_SHORT).show()
        }
    }

    fun chooseImage(){
        val getContentIntent = FileUtils.createGetContentIntent()
        val intent = Intent.createChooser(getContentIntent, "Select a file")
        startActivityForResult(intent,PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PICK_IMAGE_REQUEST){
                if (data != null){
                    selectedFileUri = data.data
                    if( selectedFileUri != null && !selectedFileUri!!.path.isEmpty()){
                        iv_upload!!.setImageURI(selectedFileUri)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_REQUEST -> {
                if(grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Granted", Toast.LENGTH_SHORT)
                else
                    Toast.makeText(this, "Not Granted", Toast.LENGTH_SHORT)

            }
        }
    }

}



