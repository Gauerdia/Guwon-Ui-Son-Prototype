package szymendera_development.guwon_app.Remote

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.Call

interface UploadAPI {
    @Multipart
    @POST("/Guwon/uploadtest2.php")
    fun uploadFile(@Part file: MultipartBody.Part): Call<String>
}