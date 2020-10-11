package szymendera_development.guwon_app

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_upload_file.*
import kotlinx.android.synthetic.main.app_bar_upload_file.*
import android.app.ProgressDialog
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import android.util.Log
import szymendera_development.guwon_app.Utils.FilePath
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class UploadFileActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val PICK_FILE_REQUEST = 1
    private var selectedFilePath: String? = null
    private val SERVER_URL = "https://marc-szymendera.de/Guwon/uploadFile.php"
    var ivAttachment: ImageView? = null
    var bUpload: Button? = null
    var tvFileName: TextView? = null
    var dialog: ProgressDialog? = null
    var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_file)
        setSupportActionBar(toolbar)
        ivAttachment = findViewById(R.id.ivAttachment)
        bUpload = findViewById(R.id.b_upload)
        tvFileName = findViewById(R.id.tv_file_name)
        val intent = intent
        id = intent.getIntExtra("id",0)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    @Override
    fun onClick(v: View) {
        if (v == ivAttachment) {
            //on attachment icon click
            showFileChooser()
        }
        if (v == bUpload) {
            //on upload button Click
            if (selectedFilePath != null) {
                dialog = ProgressDialog.show(this, "", "Uploading File...", true)
                Thread (object: Runnable {
                    @Override
                    override fun  run() {
                        //creating new thread to handle Http Operations
                        uploadFile(selectedFilePath!!)
                    }
                }).start()
            } else {
                Toast.makeText(this, "Please choose a File First", Toast.LENGTH_SHORT).show()
                Log.i("Upload Error", "Error 2")
            }
        }
    }

    private fun showFileChooser() {
        val intent = Intent()
        //sets the select file to all types of files
        intent.type = "*/*"
        //allows to select data and return it
        intent.action = Intent.ACTION_GET_CONTENT
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int,resultCode: Int,data : Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PICK_FILE_REQUEST){
                if(data == null){
                    return
                }

                var selectedFileUri = data.getData()
                Log.d("Test", selectedFileUri.toString())
                selectedFilePath = FilePath.getPath(this,selectedFileUri)
                Log.i("File Path","Selected File Path:" + selectedFilePath)

                if(selectedFilePath != null && !selectedFilePath.equals("")){
                    tvFileName?.setText(selectedFilePath)
                }else{
                    Toast.makeText(this,"Cannot upload file to server",Toast.LENGTH_SHORT).show()
                    Log.i("Upload Error", "Error 1")
                }
            }
        }
    }

    //android upload file to server
    fun uploadFile(fselectedFilePath: String): Int{

        var serverResponseCode = 0

        var connection: HttpURLConnection
        var dataOutputStream: DataOutputStream
        var lineEnd = "\r\n"
        var twoHyphens = "--"
        var boundary = "*****"


        var bytesRead : Int
        var bytesAvailable : Int
        var bufferSize : Int
        var buffer : Array<Byte>
        var maxBufferSize = 1 * 1024 * 1024
        var selectedFile = File(selectedFilePath)


        var parts : List<String> = selectedFilePath!!.split("/")
        var fileName = parts[parts.size-1]

        // TODO:
        // ALGO, der Leerzeichen löscht

//        var fileName = "photo_" + id + ".jpg"
        if (!selectedFile.isFile()){
            dialog?.dismiss()

            this.runOnUiThread{
                object: Runnable {
                    override fun run() {
                        tvFileName?.setText("Source File Doesn't Exist: " + selectedFilePath)
                        Log.i("Upload Error", "Error 3")
                    }
                }
            }
            return 0
        }else{
            try{
                var fileInputStream: FileInputStream = FileInputStream(selectedFile)
                var url: URL = URL(SERVER_URL)
                connection =  url.openConnection() as HttpURLConnection
                connection.setDoInput(true)//Allow Inputs
                connection.setDoOutput(true)//Allow Outputs
                connection.setUseCaches(false)//Don't use a cached Copy
                connection.setRequestMethod("POST")
                connection.setRequestProperty("Connection", "Keep-Alive")
                connection.setRequestProperty("ENCTYPE", "multipart/form-data")
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary)
                connection.setRequestProperty("uploaded_file",selectedFilePath)

                //creating new dataoutputstream
                dataOutputStream = DataOutputStream(connection.getOutputStream())

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd)
                var alteredName = "photo_" + id
                Log.i("alteredName", alteredName)
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd)
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"alteredName\";filename=\""
//                        + selectedFilePath + "\"" + lineEnd)

                dataOutputStream.writeBytes(lineEnd)

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available()
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize)
                //setting the buffer as byte array of size of bufferSize
                var buffer = ByteArray(bufferSize)

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize)

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize)
                    bytesAvailable = fileInputStream.available()
                    bufferSize = Math.min(bytesAvailable,maxBufferSize)
                    bytesRead = fileInputStream.read(buffer,0,bufferSize)
                }

                dataOutputStream.writeBytes(lineEnd)
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)

                serverResponseCode = connection.getResponseCode()
                var serverResponseMessage = connection.getResponseMessage()

                Log.i("Server Response", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode)

                //response code of 200 indicates the server status OK
                if(serverResponseCode == 200){
                    runOnUiThread(object: Runnable {
                        override fun run() {
                            tvFileName?.setText("File Upload completed." + fileName)
                        }
                    })
                }

                //closing the input and output streams
                fileInputStream.close()
                dataOutputStream.flush()
                dataOutputStream.close()



            } catch (e : FileNotFoundException) {
                e.printStackTrace()
//                runOnUiThread(object: Runnable {
//                    override fun run() {
//                        Toast.makeText(this,"File Not Found",Toast.LENGTH_SHORT).show()
//                    }
//                }
                Toast.makeText(this,"File Not Found",Toast.LENGTH_SHORT).show()
                Log.i("Upload Error", "Error 4")
            } catch (e : MalformedURLException) {
                e.printStackTrace()
                Toast.makeText(this, "URL error!", Toast.LENGTH_SHORT).show()
                Log.i("Upload Error", "Error 5")
            } catch (e : IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show()
                Log.i("Upload Error", "Error 6")
            }
            dialog?.dismiss()
            return serverResponseCode
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
        menuInflater.inflate(R.menu.upload_file, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.profil -> {
                // Handle the camera action
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
