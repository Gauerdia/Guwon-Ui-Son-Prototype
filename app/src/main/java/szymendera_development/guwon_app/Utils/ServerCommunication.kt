package szymendera_development.guwon_app.Utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import okhttp3.RequestBody
import okhttp3.OkHttpClient
import java.io.*
import java.util.*
import kotlin.concurrent.timerTask
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import com.google.gson.Gson
import szymendera_development.guwon_app.*
import java.net.*
import java.text.SimpleDateFormat


val AnwesenheitStundePunkte = 10







/*        ****      ADD     ****         */

// Adds a new question to the pool of Qu/**/estions
fun addQuizFrage(
    context: Context,
    Frage: String,
    AntwortA: String,
    AntwortB: String,
    AntwortC: String,
    RichtigeAntwort: String,
    Rang: String
) {
    val url = "https://marc-szymendera.de/Guwon/addQuizFrage.php"
    val params = JSONObject()
    try {
        params.put("Frage", Frage)
        params.put("Antwort_A", AntwortA)
        params.put("Antwort_B", AntwortB)
        params.put("Antwort_C", AntwortC)
        params.put("Richtige_Antwort", RichtigeAntwort)
        params.put("Mindest_Rang", Rang)


    } catch (e: JSONException) {
        Log.d("Post_JSONException: ", e.toString())
    }

    val MyRequestQueue = Volley.newRequestQueue(context)
    val jsObjRequest = JsonObjectRequest(
        com.android.volley.Request.Method.POST,
        url,
        params,
        com.android.volley.Response.Listener { response -> Log.d("Post_Response(Basic): ", response.toString()) },
        com.android.volley.Response.ErrorListener { response ->
            Log.d("Post_Response(Error): ", response.toString())
            try {
                val responseBody = String(response.networkResponse.data, Charsets.UTF_8)
                val jsonObject = JSONObject(responseBody)
                Log.d("Post_Respnse(Err,scd): ", response.toString())
            } catch (e: JSONException) {
                Log.d("Post_JSONException): ", e.toString())
            } catch (error: UnsupportedEncodingException) {
                Log.d("Post_EncdingException: ", error.toString())
            }
        })
    MyRequestQueue.add(jsObjRequest)
}

// Add Anwesenheit of another day than today
fun addAnwesenheitNachtraeglich(
    context: Context,
    id: Int,
    date: String,
    User: String,
    vorname: String,
    nachname: String,
    description: String
) {
    val url = "https://marc-szymendera.de/Guwon/addAnwesenheitNachtraeglich.php"
    val params = JSONObject()
    try {
        params.put("id", id)
        params.put("date", date)
        params.put("user", User)
        params.put("vorname", vorname)
        params.put("nachname", nachname)
        params.put("desc", description)

    } catch (e: JSONException) {
        Log.d("Post_JSONException: ", e.toString())
    }

    val MyRequestQueue = Volley.newRequestQueue(context)
    val jsObjRequest = JsonObjectRequest(
        com.android.volley.Request.Method.POST,
        url,
        params,
        com.android.volley.Response.Listener { response -> Log.d("Post_Response(Basic): ", response.toString()) },
        com.android.volley.Response.ErrorListener { response ->
            Log.d("Post_Response(Error): ", response.toString())
            try {
                val responseBody = String(response.networkResponse.data, Charsets.UTF_8)
                val jsonObject = JSONObject(responseBody)
                Log.d("Post_Respnse(Err,scd): ", response.toString())
            } catch (e: JSONException) {
                Log.d("Post_JSONException): ", e.toString())
            } catch (error: UnsupportedEncodingException) {
                Log.d("Post_EncdingException: ", error.toString())
            }
        })
    MyRequestQueue.add(jsObjRequest)
}


// Adds a new Schueler to the database
fun addSchueler(
    context: Context,
    vorname: String,
    nachname: String,
    Rang: String,
    Schueler: Boolean,
    DoesTaek: Boolean,
    DoesKick: Boolean,
    DoesYoga: Boolean,
    DoesPilates: Boolean
) {
    val url = "https://marc-szymendera.de/Guwon/addSchueler.php"
    val params = JSONObject()
    var tempArray =
        listOf("fe03dfwl3", "swsq2ddwd", "llz23ßex", "pklx23ds", "bgoje03", "sxahl213", "erutoi4", "mnvcnp2")
    val random = tempArray[Random().nextInt((tempArray.size) - 0) + 0]
    try {
        params.put("vorname", vorname)
        params.put("nachname", nachname)
        params.put("Gibon", "")
        params.put("Teul", "")
        params.put("Hanbon", "")
        params.put("Gyeokpa", "")
        params.put("Daeryeon", "")
        params.put("Chayu", "")
        params.put("Hosinsul", "")
        params.put("Pruefungs_Bereit", "")
        params.put("Rang", Rang)
        params.put("Anwesenheit", "")
        params.put("Anwesenheit_num", 0)
        params.put("Schueler", Schueler)
        params.put("TaekwonDo", DoesTaek)
        params.put("Kickboxen", DoesKick)
        params.put("Yoga", DoesYoga)
        params.put("Pilates", DoesPilates)
        params.put("password", random)
        params.put("Last_Quiz", "24-02-2018")

    } catch (e: JSONException) {
        Log.d("Post_JSONException: ", e.toString())
    }

    val MyRequestQueue = Volley.newRequestQueue(context)
    val jsObjRequest = JsonObjectRequest(
        com.android.volley.Request.Method.POST,
        url,
        params,
        com.android.volley.Response.Listener { response -> Log.d("Post_Response(Basic): ", response.toString()) },
        com.android.volley.Response.ErrorListener { response ->
            Log.d("Post_Response(Error): ", response.toString())
            try {
                val responseBody = String(response.networkResponse.data, Charsets.UTF_8)
                val jsonObject = JSONObject(responseBody)
                Log.d("Post_Respnse(Err,scd): ", response.toString())
            } catch (e: JSONException) {
                Log.d("Post_JSONException): ", e.toString())
            } catch (error: UnsupportedEncodingException) {
                Log.d("Post_EncdingException: ", error.toString())
            }
        })
    MyRequestQueue.add(jsObjRequest)
}

// Is called when the user finished marking the Schuelers who've come and registers
// them in the database.
fun enterAnwesenheit(username: String, stunden: String, punkteWertigkeit: Int,context: Context) {
    val url = "https://marc-szymendera.de/Guwon/enterAnwesenheit.php"
    val params = JSONObject()
    var gesamt_punkte = (stunden.toFloat() * punkteWertigkeit)
    try {
        params.put("username", username)
        params.put("stunden", stunden)
        params.put("punkte", gesamt_punkte)
    } catch (e: JSONException) {
        Log.d("enterAnwesenheit", e.toString())
    }
    val MyRequestQueue = Volley.newRequestQueue(context)
    val jsObjRequest = JsonObjectRequest(
        com.android.volley.Request.Method.PUT,
        url,
        params,
        com.android.volley.Response.Listener { response ->
            Log.d("enterAnwesenheit", "Response: "+response.toString())
        },
        com.android.volley.Response.ErrorListener { response ->
            Log.d("enterAnwesenheit", "ErrorResponse: "+response.toString())
            try {
                val responseBody = String(response.networkResponse.data, Charsets.UTF_8)
                val jsonObject = JSONObject(responseBody)
                Log.d("enterAnwesenheit", "JSONResponse: "+response.toString())
            } catch (e: JSONException) {
                Log.d("enterAnwesenheit", "JSONException: "+e.toString())
            } catch (error: UnsupportedEncodingException) {
                Log.d("enterAnwesenheit", "UnsupportedEncodingException: "+error.toString())
            }
        })
    MyRequestQueue.add(jsObjRequest)
    val RefreshIntent = Intent(context, ProfilActivity::class.java)
    context.startActivity(RefreshIntent)
}

fun addEventTeilnahme(activity: Activity, tag:String, monat:String, jahr:String,
                      stunde:String,minute: String,anlass: String,ort: String){
    val url = "https://marc-szymendera.de/Guwon/postEventTeilnahme.php"
    val params = JSONObject()
    try {
        params.put("tag", tag)
        params.put("monat", monat)
        params.put("jahr", jahr)
        params.put("stunde", stunde)
        params.put("minuten", minute)
        params.put("anlass", anlass)
        params.put("ort", ort)
    } catch (e: JSONException) {
        Log.d("enterAnwesenheit", e.toString())
    }
    val MyRequestQueue = Volley.newRequestQueue(activity)
    val jsObjRequest = JsonObjectRequest(
        com.android.volley.Request.Method.PUT,
        url,
        params,
        com.android.volley.Response.Listener { response ->
            Log.d("enterAnwesenheit", "Response: "+response.toString())
        },
        com.android.volley.Response.ErrorListener { response ->
            Log.d("enterAnwesenheit", "ErrorResponse: "+response.toString())
            try {
                val responseBody = String(response.networkResponse.data, Charsets.UTF_8)
                val jsonObject = JSONObject(responseBody)
                Log.d("enterAnwesenheit", "JSONResponse: "+response.toString())
            } catch (e: JSONException) {
                Log.d("enterAnwesenheit", "JSONException: "+e.toString())
            } catch (error: UnsupportedEncodingException) {
                Log.d("enterAnwesenheit", "UnsupportedEncodingException: "+error.toString())
            }
        })
    MyRequestQueue.add(jsObjRequest)
}

fun resetEventTeilnahme(activity: Activity){
    val url = "https://marc-szymendera.de/Guwon/resetEventTeilnahme.php"
    val params = JSONObject()
    try {
    } catch (e: JSONException) {
        Log.d("enterAnwesenheit", e.toString())
    }
    val MyRequestQueue = Volley.newRequestQueue(activity)
    val jsObjRequest = JsonObjectRequest(
        com.android.volley.Request.Method.PUT,
        url,
        params,
        com.android.volley.Response.Listener { response ->
            Log.d("enterAnwesenheit", "Response: "+response.toString())
        },
        com.android.volley.Response.ErrorListener { response ->
            Log.d("enterAnwesenheit", "ErrorResponse: "+response.toString())
            try {
                val responseBody = String(response.networkResponse.data, Charsets.UTF_8)
                val jsonObject = JSONObject(responseBody)
                Log.d("enterAnwesenheit", "JSONResponse: "+response.toString())
            } catch (e: JSONException) {
                Log.d("enterAnwesenheit", "JSONException: "+e.toString())
            } catch (error: UnsupportedEncodingException) {
                Log.d("enterAnwesenheit", "UnsupportedEncodingException: "+error.toString())
            }
        })
    MyRequestQueue.add(jsObjRequest)
}

fun addNachricht(context: Context, vorname_sender: String, nachname_sender: String,
                 betreff: String, nachricht: String, id_sender:Int, id_empfaenger: Int) {
    val url = "https://marc-szymendera.de/Guwon/postNachricht.php"

    val sdf = SimpleDateFormat("dd-MM-yyyy")
    val c = Calendar.getInstance()
    val resultdate = Date(c.timeInMillis)
    var dateInString = sdf.format(resultdate)

    val params = JSONObject()
    try {
        params.put("vorname_sender", vorname_sender)
        params.put("nachname_sender", nachname_sender)
        params.put("betreff", betreff)
        params.put("nachricht", nachricht)
        params.put("datum",dateInString)
        params.put("id_sender", id_sender)
        params.put("id_empfaenger",id_empfaenger)
    } catch (e: JSONException) {
        Log.d("Post_JSONException: ", e.toString())
    }

    val MyRequestQueue = Volley.newRequestQueue(context)
    val jsObjRequest = JsonObjectRequest(
        com.android.volley.Request.Method.POST,
        url,
        params,
        com.android.volley.Response.Listener { response -> Log.d("Post_Response(Basic): ", response.toString()) },
        com.android.volley.Response.ErrorListener { response ->
            Log.d("Post_Response(Error): ", response.toString())
            try {
                val responseBody = String(response.networkResponse.data, Charsets.UTF_8)
                val jsonObject = JSONObject(responseBody)
                Log.d("Post_Respnse(Err,scd): ", response.toString())
            } catch (e: JSONException) {
                Log.d("Post_JSONException): ", e.toString())
            } catch (error: UnsupportedEncodingException) {
                Log.d("Post_EncdingException: ", error.toString())
            }
        })
    MyRequestQueue.add(jsObjRequest)
}

fun addTermin(context: Context,tag:String, monat:String, jahr:String, stunde:String, minute:String,anlass:String,
              ort:String) {
    val url = "https://marc-szymendera.de/Guwon/postTermin.php"
    val params = JSONObject()
    try {
        params.put("tag", tag)
        params.put("monat", monat)
        params.put("jahr", jahr)
        params.put("stunde", stunde)
        params.put("minute", minute)
        params.put("anlass", anlass)
        params.put("ort", ort)
    } catch (e: JSONException) {
        Log.d("Post_JSONException: ", e.toString())
    }

    val MyRequestQueue = Volley.newRequestQueue(context)
    val jsObjRequest = JsonObjectRequest(
        com.android.volley.Request.Method.POST,
        url,
        params,
        com.android.volley.Response.Listener { response -> Log.d("Post_Response(Basic): ", response.toString()) },
        com.android.volley.Response.ErrorListener { response ->
            Log.d("Post_Response(Error): ", response.toString())
            try {
                val responseBody = String(response.networkResponse.data, Charsets.UTF_8)
                val jsonObject = JSONObject(responseBody)
                Log.d("Post_Respnse(Err,scd): ", response.toString())
            } catch (e: JSONException) {
                Log.d("Post_JSONException): ", e.toString())
            } catch (error: UnsupportedEncodingException) {
                Log.d("Post_EncdingException: ", error.toString())
            }
        })
    MyRequestQueue.add(jsObjRequest)
}

fun addTempProfile(activity: Activity): Int{
    Log.d("Fetching JSON","AddTempProfile")
    var waitingvar = 0
    val timer = Timer()
    var ReturnedId:Int? = null


    val url = "https://marc-szymendera.de/Guwon/addTempProfile.php"
    val jsonObject = JSONObject()
    try {
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    val JSON = MediaType.parse("application/json; charset=utf-8")
    val Jsonbody = RequestBody.create(JSON, jsonObject.toString())

    val request = Request.Builder().url(url).post(Jsonbody).build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            Log.d("addTempProfile",body)

            val gson = GsonBuilder().create()

            val GsonId = gson.fromJson(body, IdClass::class.java)
            activity.runOnUiThread {
                ReturnedId = GsonId.id

                waitingvar = 1
            }

        }

        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request")
        }
    })
    while (waitingvar != 1) {
        timer.schedule(timerTask {}, 30)
    }
    return ReturnedId!!
}

/*       *****       Update      ****        */

// Updates Disciplines depending on input-arguments
fun updateDiscipline(value: String, discipline: String, id: Int, context: Context, handler: Handler, run: Runnable) {
    val url = "https://marc-szymendera.de/Guwon/updateSchueler.php"
    val params = JSONObject()
    try {
        params.put("value", value)
        params.put("column", discipline)
        params.put("id", id)
    } catch (e: JSONException) {
        Log.d("EditDisc, catch", e.toString())
    }
    val MyRequestQueue = Volley.newRequestQueue(context)
    val jsObjRequest = JsonObjectRequest(
        com.android.volley.Request.Method.PUT,
        url,
        params,
        com.android.volley.Response.Listener { response ->
            Log.d("Post_Response(Basic): ", response.toString())
            handler.post(run)
        },
        com.android.volley.Response.ErrorListener { response ->
            Log.d("Post_Response(Error): ", response.toString())
            try {
                val responseBody = String(response.networkResponse.data, Charsets.UTF_8)
                val jsonObject = JSONObject(responseBody)
                Log.d("EditDisc, catch2", response.toString())
            } catch (e: JSONException) {
                Log.d("EditDisc, catch3", e.toString())
            } catch (error: UnsupportedEncodingException) {
                Log.d("EditDisc, catch4", error.toString())
            }
        })
    MyRequestQueue.add(jsObjRequest)
}

fun updateDiscipline(value: String, discipline: String, id: Int, context: Context) {
    val url = "https://marc-szymendera.de/Guwon/updateSchueler.php"
    val params = JSONObject()
    try {
        params.put("value", value)
        params.put("column", discipline)
        params.put("id", id)
    } catch (e: JSONException) {
        Log.d("EditDisc, catch", e.toString())
    }
    val MyRequestQueue = Volley.newRequestQueue(context)
    val jsObjRequest = JsonObjectRequest(
        com.android.volley.Request.Method.PUT,
        url,
        params,
        com.android.volley.Response.Listener { response ->
            Log.d("Post_Response(Basic): ", response.toString())
        },
        com.android.volley.Response.ErrorListener { response ->
            Log.d("Post_Response(Error): ", response.toString())
            try {
                val responseBody = String(response.networkResponse.data, Charsets.UTF_8)
                val jsonObject = JSONObject(responseBody)
                Log.d("EditDisc, catch2", response.toString())
            } catch (e: JSONException) {
                Log.d("EditDisc, catch3", e.toString())
            } catch (error: UnsupportedEncodingException) {
                Log.d("EditDisc, catch4", error.toString())
            }
        })
    MyRequestQueue.add(jsObjRequest)
}

fun updateAktuelles(value: String, context: Context, target: Int, excerpt: String){
    val url = "https://marc-szymendera.de/Guwon/updateAktuelles.php"
    val params = JSONObject()
    try {
        params.put("value", value)
        params.put("target",target)
        params.put("excerpt", excerpt)
    } catch (e: JSONException) {
        Log.d("EditDisc, catch", e.toString())
    }
    val MyRequestQueue = Volley.newRequestQueue(context)
    val jsObjRequest = JsonObjectRequest(
        com.android.volley.Request.Method.PUT,
        url,
        params,
        com.android.volley.Response.Listener { response ->
            Log.d("Post_Response(Basic): ", response.toString())
        },
        com.android.volley.Response.ErrorListener { response ->
            Log.d("Post_Response(Error): ", response.toString())
            try {
                val responseBody = String(response.networkResponse.data, Charsets.UTF_8)
                val jsonObject = JSONObject(responseBody)
                Log.d("EditDisc, catch2", response.toString())
            } catch (e: JSONException) {
                Log.d("EditDisc, catch3", e.toString())
            } catch (error: UnsupportedEncodingException) {
                Log.d("EditDisc, catch4", error.toString())
            }
        })
    MyRequestQueue.add(jsObjRequest)
}

fun updateLastQuiz(id: Int, date: String,punkte: Int, context: Context){
    val url = "https://marc-szymendera.de/Guwon/updateLastQuiz.php"
    val params = JSONObject()
    try {
        params.put("date", date)
        params.put("id", id)
        params.put("punkte",punkte)
    } catch (e: JSONException) {
        Log.d("EditDisc, catch", e.toString())
    }
    val MyRequestQueue = Volley.newRequestQueue(context)
    val jsObjRequest = JsonObjectRequest(
        com.android.volley.Request.Method.PUT,
        url,
        params,
        com.android.volley.Response.Listener { response ->
            Log.d("Post_Response(Basic): ", response.toString())
        },
        com.android.volley.Response.ErrorListener { response ->
            Log.d("Post_Response(Error): ", response.toString())
            try {
                val responseBody = String(response.networkResponse.data, Charsets.UTF_8)
                val jsonObject = JSONObject(responseBody)
                Log.d("EditDisc, catch2", response.toString())
            } catch (e: JSONException) {
                Log.d("EditDisc, catch3", e.toString())
            } catch (error: UnsupportedEncodingException) {
                Log.d("EditDisc, catch4", error.toString())
            }
        })
    MyRequestQueue.add(jsObjRequest)
}

fun updatePhotoDate(id: Int,command:String){

    val url = "https://marc-szymendera.de/Guwon/updatePhotoDate.php"
    val jsonObject = JSONObject()
    try {
        jsonObject.put("id", id)
        jsonObject.put("command", command)
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    val JSON = MediaType.parse("application/json; charset=utf-8")
    val Jsonbody = RequestBody.create(JSON, jsonObject.toString())

    val request = Request.Builder().url(url).post(Jsonbody).build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            println(body)

            val gson = GsonBuilder().create()

        }
        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request")
        }
    })
}

fun updateBannerDate(filename: String){

    val url = "https://marc-szymendera.de/Guwon/updateBannerDate.php"
    val jsonObject = JSONObject()
    try {
        jsonObject.put("filename", filename)
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    val JSON = MediaType.parse("application/json; charset=utf-8")
    val Jsonbody = RequestBody.create(JSON, jsonObject.toString())

    val request = Request.Builder().url(url).post(Jsonbody).build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            println(body)

            val gson = GsonBuilder().create()

        }
        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request")
        }
    })
}

fun updateImageVersion(id: Int){

    val url = "https://marc-szymendera.de/Guwon/updateImageVersion.php"
    val jsonObject = JSONObject()
    try {
        jsonObject.put("id", id)
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    val JSON = MediaType.parse("application/json; charset=utf-8")
    val Jsonbody = RequestBody.create(JSON, jsonObject.toString())

    val request = Request.Builder().url(url).post(Jsonbody).build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            println(body)

            val gson = GsonBuilder().create()

        }
        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request")
        }
    })

}

fun updateGelesen(id:Int){

    val url = "https://marc-szymendera.de/Guwon/updateGelesen.php"
    val jsonObject = JSONObject()
    try {
        jsonObject.put("id", id)
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    val JSON = MediaType.parse("application/json; charset=utf-8")
    val Jsonbody = RequestBody.create(JSON, jsonObject.toString())

    val request = Request.Builder().url(url).post(Jsonbody).build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            println(body)

            val gson = GsonBuilder().create()

        }
        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request")
        }
    })
    Log.d("Test","id:" + id)
}

fun updateBeantwortet(id: Int,vorname: String){

    val url = "https://marc-szymendera.de/Guwon/updateBeantwortet.php"
    val jsonObject = JSONObject()
    try {
        jsonObject.put("id", id)
        jsonObject.put("vorname", vorname)
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    val JSON = MediaType.parse("application/json; charset=utf-8")
    val Jsonbody = RequestBody.create(JSON, jsonObject.toString())

    val request = Request.Builder().url(url).post(Jsonbody).build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            println(body)

            val gson = GsonBuilder().create()

        }
        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request")
        }
    })

}

fun updateIsOnlineToday(id:Int){
    val url = "https://marc-szymendera.de/Guwon/updateIsOnlineToday.php"
    val jsonObject = JSONObject()
    try {
        jsonObject.put("id", id)
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    val JSON = MediaType.parse("application/json; charset=utf-8")
    val Jsonbody = RequestBody.create(JSON, jsonObject.toString())

    val request = Request.Builder().url(url).post(Jsonbody).build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            println(body)

            val gson = GsonBuilder().create()

        }
        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request")
        }
    })
}

fun validateLogIn(activity: Activity, name: String, password:String) : ValidationResponse {
    Log.d("Fetching JSON","Validate Log In")
    var waitingvar = 0
    val timer = Timer()

    var validationResponse = ValidationResponse(0, 0, 0)

    val url = "https://marc-szymendera.de/Guwon/validateLogIn.php"
    val jsonObject = JSONObject()
    try {
        jsonObject.put("name", name)
        jsonObject.put("password", password)
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    val JSON = MediaType.parse("application/json; charset=utf-8")
    val Jsonbody = RequestBody.create(JSON, jsonObject.toString())

    val request = Request.Builder().url(url).post(Jsonbody).build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            println(body)

            val gson = GsonBuilder().create()

            val permits: ValidationResponse = gson.fromJson(body, ValidationResponse::class.java)
            activity.runOnUiThread {
                validationResponse = permits
                waitingvar = 1
            }

        }

        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request")
        }
    })
    while (waitingvar != 1) {
        timer.schedule(timerTask {}, 30)
    }
    return validationResponse
}

/*           *****   GET   ****           */

// Returns all Schueler with the Information of their score
fun getSchuelerCompetition(
    activity: Activity
): List<SchuelerCompetition> {
    Log.d("Fetching JSON","GetSchuelerCompetition")

    var waitingvar = 0
    val timer = Timer()
    var SchuelerCompetitionReturn: List<SchuelerCompetition>? = null

    val url = "http://marc-szymendera.de/Guwon/getAllSchuelerCompetition.php"
    val request = Request.Builder().url(url).build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            println(body)
            Log.d("body", body)
            val gson = GsonBuilder().create()

            val permits: List<SchuelerCompetition> =
                gson.fromJson(body, Array<SchuelerCompetition>::class.java).toList()
            val mutableSchueler = mutableListOf<SchuelerCompetition>()
            for (permit in permits) {
                mutableSchueler.add(permit)
            }
            QuickSort(mutableSchueler, 0, mutableSchueler.size - 1)


            activity.runOnUiThread {
                // filling dynamic view with the items we receive from the server and equiping them
                // with a click-listener
//                recyclerView_main.adapter = GsonAdapterCompetition(
//                    mutableSchueler,
//                    { partItem: SchuelerCompetition -> partItemClicked(partItem, activity.applicationContext) })
                SchuelerCompetitionReturn = mutableSchueler
                waitingvar = 1
            }
        }

        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request")
        }
    })
    while (waitingvar != 1) {
        timer.schedule(timerTask {}, 30)
    }
    return SchuelerCompetitionReturn!!
}

// Getting Json of all Schueler
fun getAllSchuelerAnwesenheit(activity: Activity, username: String): List<SchuelerTemp> {
    Log.d("Fetching JSON","GetAllSchuelerAnwesenheit")

    var schuelerListe: List<SchuelerTemp>? = null
    var waitingvar = 0
    val timer = Timer()

    val url = "http://marc-szymendera.de/Guwon/getAllSchuelerAnwesenheit.php"
    val jsonObject = JSONObject()
    try {
        jsonObject.put("username", username)
    } catch (e: JSONException) {
        e.printStackTrace()
    }


    val JSON = MediaType.parse("application/json; charset=utf-8")
    val Jsonbody = RequestBody.create(JSON, jsonObject.toString())

    val request = Request.Builder().url(url).post(Jsonbody).build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            Log.d("GetAllSchuelerAnw", body)

            val gson = GsonBuilder().create()

            val schuelerListe_temp: List<SchuelerTemp> = gson.fromJson(body, Array<SchuelerTemp>::class.java).toList()
            activity.runOnUiThread {
                // filling dynamic view with the items we receive from the server and equiping them
                // with a click-listener
                schuelerListe = schuelerListe_temp
                waitingvar = 1
            }
        }

        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request")
        }
    })
    while (waitingvar != 1) {
        timer.schedule(timerTask {}, 30)
    }
    return schuelerListe!!
}

// Getting Json of all Schueler
fun getAllSchueler(activity: Activity) : List<Schueler> {
    Log.d("Fetching JSON","GetAllSchueler")

    var schuelerListe: List<Schueler>? = null
    var waitingvar = 0
    val timer = Timer()

    val url = "http://marc-szymendera.de/Guwon/getAllSchueler.php"
    val request = Request.Builder().url(url).build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            println(body)

            val gson = GsonBuilder().create()


            var schuelerListe_temp = gson.fromJson(body, Array<Schueler>::class.java).toList()

            activity.runOnUiThread {
                schuelerListe = schuelerListe_temp
                waitingvar = 1
            }
        }

        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request")
        }
    })
    while (waitingvar != 1) {
        timer.schedule(timerTask {}, 30)
    }
    return schuelerListe!!
}

// Reponses a particular Schueler depending on the input arguments
fun getParticularSchueler(id: Int, activity: Activity): SchuelerComplete {
    Log.d("Fetching JSON","GetParticularSchueler")
    var waitingvar = 0
    val timer = Timer()

    var particularSchueler = SchuelerComplete(
        0, "vorname", "nachname", "", "", "", "", "", ""
        , "", "", 0, "", 0, 0, 0, 0, 0, 0, 0
        , "01-01-2018", 0, 0, 0, 0, 0,0
    )

    val url = "https://marc-szymendera.de/Guwon/getParticularSchueler.php"
    val jsonObject = JSONObject()
    try {
        jsonObject.put("id", id)
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    val JSON = MediaType.parse("application/json; charset=utf-8")
    val Jsonbody = RequestBody.create(JSON, jsonObject.toString())

    val request = Request.Builder().url(url).post(Jsonbody).build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            println(body)

            val gson = GsonBuilder().create()

            val permits: SchuelerComplete = gson.fromJson(body, SchuelerComplete::class.java)
            activity.runOnUiThread {
                particularSchueler = permits
                waitingvar = 1
            }

        }

        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request")
        }
    })
    while (waitingvar != 1) {
        timer.schedule(timerTask {}, 30)
    }
    return particularSchueler
}

// Returns a List of all Quiz-Questions which are available
fun getQuiz(activity: Activity): List<Quiz> {
    Log.d("Fetching JSON","GetQuiz")
    var waitingvar = 0
    val timer = Timer()

    var QuizReturn: List<Quiz>? = null

    val url = "https://marc-szymendera.de/Guwon/getQuiz.php"
    val jsonObject = JSONObject()
    try {
        jsonObject.put("Mindest_Rang", 1)
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    val JSON = MediaType.parse("application/json; charset=utf-8")
    val Jsonbody = RequestBody.create(JSON, jsonObject.toString())

    val request = Request.Builder().url(url).post(Jsonbody).build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            println(body)

            val gson = GsonBuilder().create()

            val permits: List<Quiz> = gson.fromJson(body, Array<Quiz>::class.java).toList()
            activity.runOnUiThread {
                QuizReturn = permits
                waitingvar = 1
            }

        }

        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request")
        }
    })
    while (waitingvar != 1) {
        timer.schedule(timerTask {}, 30)
    }
    return QuizReturn!!
}

fun getAktuelles(activity: Activity): List<Aktuelles>{
    Log.d("Fetch JSON","getAktuelles")
    var waitingvar = 0
    val timer = Timer()

    var AktuellesReturn: List<Aktuelles>? = null

    val url = "https://marc-szymendera.de/Guwon/getAktuelles.php"
    val jsonObject = JSONObject()
    try {
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    val JSON = MediaType.parse("application/json; charset=utf-8")
    val Jsonbody = RequestBody.create(JSON, jsonObject.toString())

    val request = Request.Builder().url(url).post(Jsonbody).build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            println(body)

            val gson = GsonBuilder().create()

            val permits: List<Aktuelles> = gson.fromJson(body, Array<Aktuelles>::class.java).toList()
            activity.runOnUiThread {
                AktuellesReturn = permits
                waitingvar = 1
            }

        }

        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request")
        }
    })
    while (waitingvar != 1) {
        timer.schedule(timerTask {}, 30)
    }
    return AktuellesReturn!!
}

fun getDate(activity: Activity, id: Int): MutableList<CustomDate> {
    Log.d("Fetching JSON", "getDate")
    var waitingvar = 0
    val timer = Timer()

    var CustomDateReturn: MutableList<CustomDate>? = null

    val url = "https://marc-szymendera.de/Guwon/getDate.php"
    val jsonObject = JSONObject()
    try {
        jsonObject.put("id", id)
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    val JSON = MediaType.parse("application/json; charset=utf-8")
    val Jsonbody = RequestBody.create(JSON, jsonObject.toString())

    val request = Request.Builder().url(url).post(Jsonbody).build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            println(body)

            val gson = GsonBuilder().create()

            val permits: List<CustomDate> = gson.fromJson(body, Array<CustomDate>::class.java).toList()
            activity.runOnUiThread {
                CustomDateReturn = permits.toMutableList()
                waitingvar = 1
            }

        }

        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request")
        }
    })
    while (waitingvar != 1) {
        timer.schedule(timerTask {}, 30)
    }
    return CustomDateReturn!!
}

fun getTermine(activity: Activity): List<Termine>{
    Log.d("Fetch JSON","getTermine")
    var waitingvar = 0
    val timer = Timer()

    var TermineReturn: List<Termine>? = null

    val url = "https://marc-szymendera.de/Guwon/getTermine.php"
    val jsonObject = JSONObject()
    try {
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    val JSON = MediaType.parse("application/json; charset=utf-8")
    val Jsonbody = RequestBody.create(JSON, jsonObject.toString())

    val request = Request.Builder().url(url).post(Jsonbody).build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            println(body)

            val gson = GsonBuilder().create()

            val tempList: List<Termine> = gson.fromJson(body, Array<Termine>::class.java).toList()
            activity.runOnUiThread {
                TermineReturn = tempList
                waitingvar = 1
            }

        }

        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request")
        }
    })
    while (waitingvar != 1) {
        timer.schedule(timerTask {}, 30)
    }
    return TermineReturn!!

}

fun getEvents(activity: Activity,id:Int): List<Events>{
    Log.d("Fetch JSON","getEvents")
    var waitingvar = 0
    val timer = Timer()

    var EventsReturn: List<Events>? = null

    val url = "https://marc-szymendera.de/Guwon/getEvents.php"
    val jsonObject = JSONObject()
    try {
        jsonObject.put("id", id)
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    val JSON = MediaType.parse("application/json; charset=utf-8")
    val Jsonbody = RequestBody.create(JSON, jsonObject.toString())

    val request = Request.Builder().url(url).post(Jsonbody).build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            println(body)

            val gson = GsonBuilder().create()

            val tempList: List<Events> = gson.fromJson(body, Array<Events>::class.java).toList()
            activity.runOnUiThread {
                EventsReturn = tempList
                waitingvar = 1
            }

        }

        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request")
        }
    })
    while (waitingvar != 1) {
        timer.schedule(timerTask {}, 30)
    }
    return EventsReturn!!

}

fun getKommunikation(activity: Activity): List<Kommunikation>{
    Log.d("Fetch JSON","getKommunikation")
    var waitingvar = 0
    val timer = Timer()

    var KommReturnTemp = mutableListOf<Kommunikation>()
    var KommReturn : List<Kommunikation>? = null

    val url = "https://marc-szymendera.de/Guwon/getKommunikation.php"
    val jsonObject = JSONObject()
    try {
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    val JSON = MediaType.parse("application/json; charset=utf-8")
    val Jsonbody = RequestBody.create(JSON, jsonObject.toString())

    val request = Request.Builder().url(url).post(Jsonbody).build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            println(body)

            val gson = GsonBuilder().create()

            val tempList: List<Kommunikation> = gson.fromJson(body, Array<Kommunikation>::class.java).toList()
            for(item in tempList.asReversed()){
                KommReturnTemp.add(item)
            }
            activity.runOnUiThread {
                KommReturn = KommReturnTemp
                waitingvar = 1
            }

        }

        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request")
        }
    })
    while (waitingvar != 1) {
        timer.schedule(timerTask {}, 30)
    }
    return KommReturn!!

}

// Getting Json of the update-dates of the photos
fun getPhotoUpdate(activity: Activity) : List<PhotoUpdate> {

    Log.d("Fetching JSON", "getPhotoUpdate")

    var photoUpdateListe: List<PhotoUpdate>? = null
    var waitingvar = 0
    val timer = Timer()

    val url = "http://marc-szymendera.de/Guwon/getPhotoUpdate.php"
    val request = Request.Builder().url(url).build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            println(body)

            val gson = GsonBuilder().create()


            var photoUpdateListe_temp = gson.fromJson(body, Array<PhotoUpdate>::class.java).toList()

            activity.runOnUiThread {
                photoUpdateListe = photoUpdateListe_temp
                waitingvar = 1
            }
        }

        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request")
        }
    })
    while (waitingvar != 1) {
        timer.schedule(timerTask {}, 30)
    }
    return photoUpdateListe!!
}

fun getBannerUpdate(activity: Activity) : List<BannerUpdate> {

    Log.d("Fetching JSON", "getBannerUpdate")

    var bannerUpdateListe: List<BannerUpdate>? = null
    var waitingvar = 0
    val timer = Timer()

    val url = "http://marc-szymendera.de/Guwon/getBannerUpdate.php"
    val request = Request.Builder().url(url).build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            println(body)

            val gson = GsonBuilder().create()


            var bannerUpdateListe_temp = gson.fromJson(body, Array<BannerUpdate>::class.java).toList()

            activity.runOnUiThread {
                bannerUpdateListe = bannerUpdateListe_temp
                waitingvar = 1
            }
        }

        override fun onFailure(call: Call?, e: IOException?) {
            println("Failed to execute request")
        }
    })
    while (waitingvar != 1) {
        timer.schedule(timerTask {}, 30)
    }
    return bannerUpdateListe!!
}

/*   *****  MISCELLANEOUS    *******  */

fun deleteTermin(context: Context,id:Int){
    val url = "https://marc-szymendera.de/Guwon/deleteTermin.php"
    val params = JSONObject()
    try {
        params.put("id", id)
    } catch (e: JSONException) {
        Log.d("Post_JSONException: ", e.toString())
    }

    val MyRequestQueue = Volley.newRequestQueue(context)
    val jsObjRequest = JsonObjectRequest(
        com.android.volley.Request.Method.POST,
        url,
        params,
        com.android.volley.Response.Listener { response -> Log.d("Post_Response(Basic): ", response.toString()) },
        com.android.volley.Response.ErrorListener { response ->
            Log.d("Post_Response(Error): ", response.toString())
            try {
                val responseBody = String(response.networkResponse.data, Charsets.UTF_8)
                val jsonObject = JSONObject(responseBody)
                Log.d("Post_Respnse(Err,scd): ", response.toString())
            } catch (e: JSONException) {
                Log.d("Post_JSONException): ", e.toString())
            } catch (error: UnsupportedEncodingException) {
                Log.d("Post_EncdingException: ", error.toString())
            }
        })
    MyRequestQueue.add(jsObjRequest)
}

fun deleteSchuelerById(context: Context,id:Int){
    val url = "https://marc-szymendera.de/Guwon/deleteSchuelerById.php"
    val params = JSONObject()
    try {
        params.put("id", id)
    } catch (e: JSONException) {
        Log.d("Post_JSONException: ", e.toString())
    }

    val MyRequestQueue = Volley.newRequestQueue(context)
    val jsObjRequest = JsonObjectRequest(
        com.android.volley.Request.Method.POST,
        url,
        params,
        com.android.volley.Response.Listener { response -> Log.d("Post_Response(Basic): ", response.toString()) },
        com.android.volley.Response.ErrorListener { response ->
            Log.d("Post_Response(Error): ", response.toString())
            try {
                val responseBody = String(response.networkResponse.data, Charsets.UTF_8)
                val jsonObject = JSONObject(responseBody)
                Log.d("Post_Respnse(Err,scd): ", response.toString())
            } catch (e: JSONException) {
                Log.d("Post_JSONException): ", e.toString())
            } catch (error: UnsupportedEncodingException) {
                Log.d("Post_EncdingException: ", error.toString())
            }
        })
    MyRequestQueue.add(jsObjRequest)
}

class GetImages constructor(
//    private val requestUrl: String,
    private val requestUrl: String = "https://marc-szymendera.de/Guwon/returnImage.php",
    private val view: ImageView,
    private val imagename_: String
) : AsyncTask<Any, Any, Any>() {
    private var bitmap: Bitmap? = null
    private val fos: FileOutputStream? = null


    override fun doInBackground(vararg objects: Any): Any? {
        Log.d("GetImages", "launched")
        try {
            val url = URL(requestUrl)
            val conn = url.openConnection()
            bitmap = BitmapFactory.decodeStream(conn.getInputStream())
        } catch (ex: Exception) {
            Log.d("GetImages", ex.toString())
        }

        return null
    }

    override fun onPostExecute(o: Any) {
        if (!ImageStorage.checkifImageExists(imagename_)) {
            view.setImageBitmap(bitmap)
            ImageStorage.saveToSdCard(bitmap, imagename_)
        }
    }
}

fun isOnline(): Boolean{
    try {
        val timeoutMs = 1500
        val sock = Socket()
        val sockaddr = InetSocketAddress("8.8.8.8", 53)

        sock.connect(sockaddr, timeoutMs)
        sock.close()

        return true
    } catch (e: IOException) {
        return false
    }

}

// Sorts a List by the value of their score
fun QuickSort(mutableSchueler: MutableList<SchuelerCompetition>, links: Int, rechts: Int) {
    if (links < rechts) {
        var pivot = mutableSchueler[rechts].Overall_Score // +1?
        var i = links
        var j = rechts - 1

        while (i <= j) {
            if (mutableSchueler[i].Overall_Score < pivot) {
                var temp: SchuelerCompetition = mutableSchueler[i]
                mutableSchueler[i] = mutableSchueler[j]
                mutableSchueler[j] = temp
                j--
            } else {
                i++
            }
        }

        var temp = mutableSchueler[i]
        mutableSchueler[i] = mutableSchueler[rechts]
        mutableSchueler[rechts] = temp

        QuickSort(mutableSchueler, links, i - 1)
        QuickSort(mutableSchueler, i + 1, rechts)
    }
}

// Changes Intent when List-Item is clicked in SchuelerSuche
fun partItemClicked(partItem: Schueler, context: Context) {
    val RefreshIntent = Intent(context, SchuelerProfilActivity::class.java)
    RefreshIntent.putExtra("id", partItem.id)
    context.startActivity(RefreshIntent)
}

fun AnwesenheitClicked(partItem: SchuelerTemp) {
}

fun EventTeilnahmeClicked(partItem: Schueler) {
}

fun MessageClicked(activity: Activity,partItem: Kommunikation,vorname: String,id_nutzer:Int,auth:Int){

    showNachrichtDetailDialog(activity,partItem,vorname,id_nutzer,auth)
}
// Triggered, when one of the elements inside the recyclerView is clicked
fun partItemClicked(partItem: SchuelerCompetition, context: Context) {}

