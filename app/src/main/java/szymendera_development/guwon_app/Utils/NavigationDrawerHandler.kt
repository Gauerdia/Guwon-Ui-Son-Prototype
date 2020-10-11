package szymendera_development.guwon_app.Utils

import android.content.Context
import android.content.Intent
import szymendera_development.guwon_app.*

/**
 * Created by szymendera on 05.12.2018.
 */
fun AktuellesPressed(context: Context){
    val intent = Intent(context, AktuellesActivity::class.java)
    context.startActivity(intent)
}
fun TeamPressed(context: Context){
    val intent = Intent(context, TeamActivity::class.java)
    context.startActivity(intent)
}
fun TerminePressed(context: Context){
    val intent = Intent(context, TermineActivity::class.java)
    context.startActivity(intent)
}
fun KursePressed(context: Context){
    val intent = Intent(context, KurseActivity::class.java)
    context.startActivity(intent)
}
fun TaekwondoPressed(context: Context){
    val intent = Intent(context, TaekwondoActivity::class.java)
    context.startActivity(intent)
}
fun KickboxenPressed(context: Context){
    val intent = Intent(context, KickboxenActivity::class.java)
    context.startActivity(intent)
}
fun PilatisPressed(context: Context){
    val intent = Intent(context, PilatisActivity::class.java)
    context.startActivity(intent)
}
fun YogaPressed(context: Context){
    val intent = Intent(context, YogaActivity::class.java)
    context.startActivity(intent)
}
fun NordicWalkingPressed(context: Context){
    val intent = Intent(context, NordicWalkingActivity::class.java)
    context.startActivity(intent)
}
fun ProfilPressed(context: Context, authorized:Int){
    val intent = Intent(context, ProfilActivity::class.java)
    intent.putExtra("authorized", authorized)
    context.startActivity(intent)
}
fun SearchPressed(context: Context){
    val intent = Intent(context, SucheSchuelerActivity::class.java)
    context.startActivity(intent)
}
fun SchuelerPressed(context: Context){
    val intent = Intent(context, SchuelerProfilActivity::class.java)
    context.startActivity(intent)
}
fun ProfilAnwesenheitPressed(context: Context){
    val intent = Intent(context, ProfilAnwesenheitActivity::class.java)
    context.startActivity(intent)
}
fun ProfilKalenderPressed(context: Context){
    val intent = Intent(context, ProfilKalenderActivity::class.java)
    context.startActivity(intent)
}
fun QuizPressed(context: Context){
    val intent = Intent(context, QuizActivity::class.java)
    context.startActivity(intent)
}
fun RanglistePressed(context: Context){
    val intent = Intent(context, RanglisteActivity::class.java)
    context.startActivity(intent)
}
fun KontaktPressed(context: Context,id:Int){
    showKontaktDialog(context,id)
}
fun EventsPressed(context: Context){
    val intent = Intent(context, EventsActivity::class.java)
    context.startActivity(intent)
}
fun UploadTestPressed(context: Context,id:Int){
    val intent = Intent(context, SchuelerProfilUploadActivity::class.java)
    intent.putExtra("id", id)
    context.startActivity(intent)
}
fun UploadBannerPressed(context: Context,id:Int){
    val intent = Intent(context, BannerUploadActivity::class.java)
    intent.putExtra("id", id)
    context.startActivity(intent)
}