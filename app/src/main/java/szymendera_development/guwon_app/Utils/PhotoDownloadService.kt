package szymendera_development.guwon_app.Utils

import android.app.DownloadManager
import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timerTask


class PhotoDownloadService : IntentService("PhotoDownloadService") {

    var schuelerIDListe: ArrayList<Int> = ArrayList()
    var DownloadNecessaryArray: ArrayList<Int> = ArrayList()

    var photoIdUpdateListe: ArrayList<Int> = ArrayList()
    var photoDateUpdateListe: ArrayList<String> = ArrayList()

    var UpdateNecessary = false

    val timer = Timer()
    var iterator = 0

    override fun onHandleIntent(intent: Intent?) {
        schuelerIDListe = intent?.getIntegerArrayListExtra("schuelerList")!!
        for(item in schuelerIDListe){
            DownloadNecessaryArray.add(item)
        }

        photoIdUpdateListe = intent?.getIntegerArrayListExtra("updateIdList")!!
        photoDateUpdateListe = intent?.getStringArrayListExtra("updateDateList")!!


        var sp: SharedPreferences = getSharedPreferences("com.szymendera_development.guwon_app", MODE_PRIVATE)
        var sp_editor: SharedPreferences.Editor = sp.edit()

        for (schuelerID in schuelerIDListe!!) {

            // Checking, if a photo has been updated
            Log.d("Downloadservice","SchuelerID: " + schuelerID)
            try {
                val sdf = SimpleDateFormat("dd-MM-yyyy")
                var localDate: Date? = null
                var j = 0
                for (item in photoIdUpdateListe) {
                    if (item == schuelerID) {
                        localDate = sdf.parse(photoDateUpdateListe.get(j))
                    }
                    j++
                }
                val serverDate = sdf.parse(sp.getString(schuelerID.toString(), "default"))
                if (serverDate.after(localDate)) {
                    UpdateNecessary = true
                }
            }catch(e:java.lang.Exception){
                Log.d("DownloadService", "Error in Date-Comparison")
            }

                var ProfilPhotoId = "profilphoto" + schuelerID.toString()

                try {
                    Log.d("DownloadService", "Check, if File exists")
                    // Getting the path where we want it to be in the end
                    var file = File(filesDir.toString() + File.separator + ProfilPhotoId + ".jpg")

//                val sdf = SimpleDateFormat("dd-MM-yyyy")
//                val strDate = sdf.parse()
//                if (Date().after(strDate)) {

                    if (!file.exists() || UpdateNecessary == true) {
                        DownloadNecessaryArray.set(iterator, 1)
                        Log.d("DownloadService", "File doesn't exist")
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
                                    File.separator + ".guwon_app" + File.separator, ProfilPhotoId + "." + "jpg"
                        )


                        /* allow the MediaScanner to scan the downloaded file */
                        request?.allowScanningByMediaScanner()
                        val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                        /* this is our unique download id */
                        val DL_ID = dm.enqueue(request)
                        val sdf = SimpleDateFormat("dd-MM-yyyy")
                        val c = Calendar.getInstance()
                        val resultdate = Date(c.timeInMillis)
                        var dateInString = sdf.format(resultdate)
                        sp_editor.putString(schuelerID.toString(), dateInString.toString())?.apply()
                    } else {
                        Log.d("Profil", "File exists, no download necessary")
                        DownloadNecessaryArray.set(iterator, 0)
                    }
                } catch (e: Exception) {
                    Log.d("DownloadService", e.toString())
                }
                iterator++
            UpdateNecessary = false
            updatePhotoDate(schuelerID,"UPDATE")
            timer.schedule(timerTask {}, 30)
            }
            iterator = 0
            timer.schedule(timerTask {}, 300)
            copyAll()
        }

        fun copyAll() {
            for (schuelerID in schuelerIDListe!!) {

                var ProfilPhotoId = "profilphoto" + schuelerID.toString()

                Log.d("DownloadService","ProfilPhotoId: " + ProfilPhotoId)
                if (DownloadNecessaryArray.get(iterator) == 1) {
                    var tempsrc = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +
                                File.separator + ".guwon_app" + File.separator + ProfilPhotoId + ".jpg"
                    )

                    var temptarget = File(filesDir.toString() + File.separator + ProfilPhotoId + ".jpg")
                    copyFile(tempsrc, temptarget)
                } else {

                }
                iterator++
            }
            stopSelf()
        }

        // Copy the file from the downloads directory to the app-only directory
        @Throws(IOException::class)
        fun copyFile(src: File, dst: File) {
            try {
                Log.d("DownloadService", "Copying")
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
            } catch (e: java.lang.Exception) {
                Log.d("DownloadService", "Copy-Error:" + e.toString())
            }
        }
    }
