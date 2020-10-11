package szymendera_development.guwon_app.Utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.*
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.content_suche_schueler.*
import szymendera_development.guwon_app.R
import java.lang.StringBuilder


// The Dialog to show the value/texts of the different disciplines of a particular Schueler
fun showDisciplineDialogRead(context: Context, message: String, discipline: String, vorname: String, nachname: String){
    val dialog = Dialog(context,R.style.SecondDialogTheme)
    dialog.setContentView(R.layout.dialog_show_comments)
    dialog.setTitle(discipline + " " + vorname + " " + nachname)
    val diaTextTv = dialog.findViewById<TextView>(R.id.dialogTextView)
    diaTextTv.text = message
    val diaCancBtn: Button = dialog.findViewById(R.id.dialogCancelBtn)
    //cancel button click of custom layout
    diaCancBtn.setOnClickListener {
        //dismiss dialog
        dialog.dismiss()
    }
    dialog.show()
}
// The Dialog to edit the value/texts of the different disciplines of a particular Schueler
fun showDisciplineDialogEdit(context: Context, discipline: String, vorname: String,
                             nachname: String, id: Int, author: String, handler: Handler, run: Runnable){
    val dialog = Dialog(context,R.style.SecondDialogTheme)
    dialog.setContentView(R.layout.dialog_edit_comments)
    dialog.setTitle(discipline + " " + vorname + " " + nachname)
    val diaEditEt = dialog.findViewById<EditText>(R.id.dialogEditText)
    val diaSendBtn: Button = dialog.findViewById(R.id.dialogSendBtn)
    val diaCancBtn: Button = dialog.findViewById(R.id.dialogCancelBtn)
    diaSendBtn.setOnClickListener {
        //dismiss dialog
        dialog.dismiss()
        //get text from EditTexts of custom layout
        val value = author +  ": " + diaEditEt.text.toString()
        updateDiscipline(value, discipline, id, context, handler, run)
        println("value,discipline,id,context in dialog: " + value + " " + discipline + " " + id + " " + context)
    }
    //cancel button click of custom layout
    diaCancBtn.setOnClickListener {
        //dismiss dialog
        dialog.dismiss()
    }
    dialog.show()
}
// The Dialog to show the value/texts of the Pruefungseignung of a particular Schueler
fun showPruefungsEignungEditDialog(context: Context, vorname: String, nachname: String, id: Int, handler: Handler, run: Runnable){
    val dialog = Dialog(context,R.style.SecondDialogTheme)
    dialog.setContentView(R.layout.dialog_edit_pruefungseignung)
    dialog.setTitle("Prüfungseignung:" + " " + vorname + " " + nachname)
    val diaBereitBtn: Button = dialog.findViewById<Button>(R.id.dialogBereitBtn)
    val diaEventuellBtn: Button = dialog.findViewById<Button>(R.id.dialogEventuellBtn)
    val diaCancBtn: Button = dialog.findViewById<Button>(R.id.dialogCancelBtn)
    diaBereitBtn.setOnClickListener {
        //dismiss dialog
        dialog.dismiss()
        //get text from EditTexts of custom layout
        val value = vorname + ": Bereit"
        updateDiscipline(value, "Pruefungs_Bereit", id, context, handler, run)
    }
    diaEventuellBtn.setOnClickListener {
        //dismiss dialog
        dialog.dismiss()
        //get text from EditTexts of custom layout
        val value = vorname + ": Eventuell"
        updateDiscipline(value, "Pruefungs_Bereit", id, context, handler, run)
    }
    //cancel button click of custom layout
    diaCancBtn.setOnClickListener {
        //dismiss dialog
        dialog.dismiss()
    }
    dialog.show()
}
// Invoked when someone successfully achieved an exam and gets promoted
fun askRangRiseDialog(context: Context, id: Int, name: String, handler: Handler, run: Runnable){
    val dialog = Dialog(context,R.style.SecondDialogTheme)
    dialog.setContentView(R.layout.dialog_rank_rise)
    val diaTextView = dialog.findViewById<TextView>(R.id.rank_rise_text_view)
    val diaJaBtn: Button = dialog.findViewById<Button>(R.id.rank_rise_button_ja)
    val diaNeinBtn: Button = dialog.findViewById<Button>(R.id.rank_rise_button_nein)
    diaTextView.text = "Sind Sie sicher, dass dieser Schüler/diese Schülerin einen Rang aufsteigen soll?"
    diaJaBtn.setOnClickListener {
        //dismiss dialog
        dialog.dismiss()
        updateDiscipline(name, "Rang", id, context, handler, run)

    }
    //cancel button click of custom layout
    diaNeinBtn.setOnClickListener {
        //dismiss dialog
        dialog.dismiss()
    }
    dialog.show()
}
// Invoked when a Schueler shall be added to the database
fun addSchuelerDialog(context: Context){
    val dialog = Dialog(context, R.style.SecondDialogTheme)
    dialog.setContentView(R.layout.dialog_add_schueler)
    dialog.setTitle("Schüler Hinzufügen")
    val diaEtvorname = dialog.findViewById<EditText>(R.id.add_schueler_et_vorname)
    val diaEtnachname = dialog.findViewById<EditText>(R.id.add_schueler_et_nachname)
    val diaSendBtn = dialog.findViewById<Button>(R.id.dialogSendBtn)
    val diaCancBtn = dialog.findViewById<Button>(R.id.dialogCancelBtn)
    val schuelerCheckBox = dialog.findViewById<CheckBox>(R.id.add_schueler_cb_schueler)
    val schuelerCheckBoxTaek = dialog.findViewById<CheckBox>(R.id.add_schueler_cb_taek)
    val schuelerCheckBoxKick = dialog.findViewById<CheckBox>(R.id.add_schueler_cb_kick)
    val schuelerCheckBoxYoga = dialog.findViewById<CheckBox>(R.id.add_schueler_cb_yoga)
    val schuelerCheckBoxPilates = dialog.findViewById<CheckBox>(R.id.add_schueler_cb_pilates)
    val diaRangSpinner  = dialog.findViewById<Spinner>(R.id.add_schueler_spinner_rang)
    val SportartItems = arrayOf("Taekwon-Do", "Kickboxen", "Pilates", "Yoga")
    val RangItems = arrayOf("10. Kup", "9. Kup", "8. Kup", "7. Kup",
        "6. Kup", "5. Kup", "4. Kup", "3. Kup", "2. Kup", "1. Kup",
        "1.Dan", "2.Dan", "3.Dan","4.Dan", "5.Dan", "6.Dan","7.Dan", "8.Dan", "9.Dan")
    val Sportartadapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, SportartItems)
    val Rangadapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, RangItems)
    diaRangSpinner.setAdapter(Rangadapter)
    diaSendBtn.setOnClickListener {
        dialog.dismiss()
        var tempString = "0"
        when(diaRangSpinner.selectedItem.toString()){
            "10. Kup"->{tempString = "0"} "9. Kup"->{tempString = "1"} "8. Kup"->{tempString = "2"} "7. Kup"->{tempString = "3"}
            "6. Kup"->{tempString = "4"} "5. Kup"->{tempString = "5"} "4. Kup"->{tempString = "6"} "3. Kup"->{tempString = "7"}
        "2. Kup"->{tempString = "8"} "1. Kup"->{tempString = "9"}
            "1.Dan"->{tempString = "10"} "2.Dan"->{tempString = "11"} "3.Dan","4.Dan"->{tempString = "12"} "4.Dan"->{tempString = "13"}
        "5.Dan"->{tempString = "14"}"6.Dan"->{tempString = "15"} "7.Dan"->{tempString = "16"} "8.Dan"->{tempString = "17"} "9.Dan"->{tempString = "18"}
        }
        addSchueler(
            context,
            diaEtvorname.text.toString(),
            diaEtnachname.text.toString(),
            tempString,
            schuelerCheckBox.isChecked,
            schuelerCheckBoxTaek.isChecked,
            schuelerCheckBoxKick.isChecked,
            schuelerCheckBoxYoga.isChecked,
            schuelerCheckBoxPilates.isChecked
        )

    }
    //cancel button click of custom layout
    diaCancBtn.setOnClickListener {
        //dismiss dialog
        dialog.dismiss()
    }
    dialog.show()
}
// Invoked when an new question shall be added to the database
fun addQuizDialog(context: Context){
    val dialog = Dialog(context, R.style.SecondDialogTheme)
    dialog.setContentView(R.layout.dialog_add_quiz)
    dialog.setTitle("Quizfrage Hinzufügen")
    var RichtigeAntwort : String = "z"
    val diaFrage = dialog.findViewById<EditText>(R.id.add_quiz_et_frage)
    val diaAntwortA = dialog.findViewById<EditText>(R.id.add_quiz_et_antwort_a)
    val diaAntwortB = dialog.findViewById<EditText>(R.id.add_quiz_et_antwort_b)
    val diaAntwortC = dialog.findViewById<EditText>(R.id.add_quiz_et_antwort_c)
    val diaSendBtn = dialog.findViewById<Button>(R.id.dialogSendBtn)
    val diaCancBtn = dialog.findViewById<Button>(R.id.dialogCancelBtn)
    val AntwortARadioButton = dialog.findViewById<RadioButton>(R.id.add_quiz_rb_richtig_a)
    val AntwortBRadioButton = dialog.findViewById<RadioButton>(R.id.add_quiz_rb_richtig_b)
    val AntwortCRadioButton = dialog.findViewById<RadioButton>(R.id.add_quiz_rb_richtig_c)
    val diaRangSpinner  = dialog.findViewById<Spinner>(R.id.add_quiz_spinner_rang)
    val RangItems = arrayOf("10. Kup", "9. Kup", "8. Kup", "7. Kup",
        "6. Kup", "5. Kup", "4. Kup", "3. Kup", "2. Kup", "1. Kup",
        "1.Dan", "2.Dan", "3.Dan","4.Dan", "5.Dan", "6.Dan","7.Dan", "8.Dan", "9.Dan")
    AntwortARadioButton.setOnClickListener{
        RichtigeAntwort = "a"
    }
    AntwortBRadioButton.setOnClickListener{
        RichtigeAntwort = "b"
    }
    AntwortCRadioButton.setOnClickListener{
        RichtigeAntwort = "c"
    }
    val Rangadapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, RangItems)
    diaRangSpinner.setAdapter(Rangadapter)
    diaSendBtn.setOnClickListener {
        dialog.dismiss()
        var tempString = "0"
        when(diaRangSpinner.selectedItem.toString()){
            "10. Kup"->{tempString = "0"} "9. Kup"->{tempString = "1"} "8. Kup"->{tempString = "2"} "7. Kup"->{tempString = "3"}
            "6. Kup"->{tempString = "4"} "5. Kup"->{tempString = "5"} "4. Kup"->{tempString = "6"} "3. Kup"->{tempString = "7"}
            "2. Kup"->{tempString = "8"} "1. Kup"->{tempString = "9"}
            "1.Dan"->{tempString = "10"} "2.Dan"->{tempString = "11"} "3.Dan","4.Dan"->{tempString = "12"} "4.Dan"->{tempString = "13"}
            "5.Dan"->{tempString = "14"}"6.Dan"->{tempString = "15"} "7.Dan"->{tempString = "16"} "8.Dan"->{tempString = "17"} "9.Dan"->{tempString = "18"}
        }
        addQuizFrage(
            context,
            diaFrage.text.toString(),
            diaAntwortA.text.toString(),
            diaAntwortB.text.toString(),
            diaAntwortC.text.toString(),
            RichtigeAntwort,
            tempString
        )

    }
    //cancel button click of custom layout
    diaCancBtn.setOnClickListener {
        //dismiss dialog
        dialog.dismiss()
    }
    dialog.show()
}

fun showAddEventDialog(activity: Activity){
    val dialog = Dialog(activity, R.style.SecondDialogTheme)
    dialog.setContentView(R.layout.dialog_add_event)
    dialog.setTitle("Event hinzufügen")
    val diaSendBtn = dialog.findViewById<Button>(R.id.dialogSendBtn)
    val diaCancBtn = dialog.findViewById<Button>(R.id.dialogCancelBtn)

    val diaAnlass = dialog.findViewById<EditText>(R.id.add_event_anlass)
    val diaOrt = dialog.findViewById<EditText>(R.id.add_event_ort)

    val diaTagSpinner  = dialog.findViewById<Spinner>(R.id.add_event_tag)
    val diaMonatSpinner  = dialog.findViewById<Spinner>(R.id.add_event_monat)
    val diaJahrSpinner  = dialog.findViewById<Spinner>(R.id.add_event_jahr)
    val diaStundeSpinner  = dialog.findViewById<Spinner>(R.id.add_event_stunde)
    val diaMinutenSpinner  = dialog.findViewById<Spinner>(R.id.add_event_minuten)

    val TagItems = arrayOf("01", "02", "03", "04","05", "06", "07", "08","09","10",
        "11", "12", "13", "14","15", "16", "17", "18","19","20",
        "21", "22", "23", "24","25", "26", "27", "28","29","30","31")
    val MonatItems = arrayOf("Januar", "Februar", "März", "April","Mai","Juni","Juli","August",
        "September", "Oktober","November","Dezember")
    val JahrItems = arrayOf("2019", "2020", "2021")
    val StundenItems = arrayOf("00", "01", "02", "03","04","05","06","07","08","09","10",
        "11", "12", "13", "14","15","16","17","18","19","20","21",
        "22","23")
    val MinutenItems = arrayOf("00", "15", "30", "45")

    val Tagadapter = ArrayAdapter(activity.applicationContext, android.R.layout.simple_spinner_dropdown_item, TagItems)
    val Monatadapter = ArrayAdapter(activity.applicationContext, android.R.layout.simple_spinner_dropdown_item, MonatItems)
    val Jahradapter = ArrayAdapter(activity.applicationContext, android.R.layout.simple_spinner_dropdown_item, JahrItems)
    val Stundenadapter = ArrayAdapter(activity.applicationContext, android.R.layout.simple_spinner_dropdown_item, StundenItems)
    val Minutenadapter = ArrayAdapter(activity.applicationContext, android.R.layout.simple_spinner_dropdown_item, MinutenItems)

    diaTagSpinner.setAdapter(Tagadapter)
    diaMonatSpinner.setAdapter(Monatadapter)
    diaJahrSpinner.setAdapter(Jahradapter)
    diaStundeSpinner.setAdapter(Stundenadapter)
    diaMinutenSpinner.setAdapter(Minutenadapter)

    diaSendBtn.setOnClickListener {
        // Saving the chosen month as a digit to ease the handling.
        var MonatAsDigit: String? = null
        when(diaMonatSpinner.selectedItem.toString()){
            "Januar"->{MonatAsDigit = "01"}"Februar"->{MonatAsDigit = "02"}"März"->{MonatAsDigit = "03"}"April"->{MonatAsDigit = "04"}
            "Mai"->{MonatAsDigit = "05"}"Juni"->{MonatAsDigit = "06"}"Juli"->{MonatAsDigit = "07"}"August"->{MonatAsDigit = "08"}
            "September"->{MonatAsDigit = "09"}"Oktober"->{MonatAsDigit= "10"}"November"->{MonatAsDigit = "11"}"Dezember"->{MonatAsDigit = "12"}
        }
        showAddEventTeilnehmerDialog(activity,
            diaTagSpinner.selectedItem.toString(),
            MonatAsDigit.toString(),
            diaJahrSpinner.selectedItem.toString(),
            diaStundeSpinner.selectedItem.toString(),
            diaMinutenSpinner.selectedItem.toString(),
            diaAnlass.text.toString(),
            diaOrt.text.toString())
        dialog.dismiss()
    }
    //cancel button click of custom layout
    diaCancBtn.setOnClickListener {
        //dismiss dialog
        dialog.dismiss()
    }
    dialog.show()
}

fun showAddEventTeilnehmerDialog(activity: Activity, tag: String,monat:String,jahr:String,
                                 stunde:String,minuten:String,anlass:String,ort:String){
    val dialog = Dialog(activity, R.style.SecondDialogTheme)
    dialog.setContentView(R.layout.dialog_add_event_teilnehmer)
    dialog.setTitle("Teilnehmer hinzufügen")
    val diaSendBtn = dialog.findViewById<Button>(R.id.dialogSendBtn)
    val diaCancBtn = dialog.findViewById<Button>(R.id.dialogCancelBtn)
    val recyclerview = dialog.findViewById<RecyclerView>(R.id.recyclerView_event_teilnehmer)
    recyclerview.layoutManager = LinearLayoutManager(activity.applicationContext)
    AsyncIsOnlineGetSchuelerDialog(activity,recyclerview).execute()

    diaSendBtn.setOnClickListener {
        addEventTeilnahme(activity,tag,monat,jahr,stunde,minuten,anlass,ort)
        dialog.dismiss()
    }
    //cancel button click of custom layout
    diaCancBtn.setOnClickListener {
        //dismiss dialog
        resetEventTeilnahme(activity)
        dialog.dismiss()
    }
    dialog.show()

}
// Invoked when nachträgliche anwesenheit is to be entered
fun NachträglichAnwesendDialog(context: Context, id: Int, vorname: String, nachname: String){
    val dialog = Dialog(context, R.style.SecondDialogTheme)
    dialog.setContentView(R.layout.dialog_anwesenheit_nachtraeglich)
    dialog.setTitle("Nachträgliche Anwesenheit")
    val diaTagSpinner  = dialog.findViewById<Spinner>(R.id.anwesenheit_nachtraeglich_tag)
    val TagItems = arrayOf("01","02","03","04","05","06","07","08","09","10","11","12","13","14","15",
        "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31")
    val diaMonatSpinner  = dialog.findViewById<Spinner>(R.id.anwesenheit_nachtraeglich_monat)
    val MonatItems = arrayOf("01","02","03","04","05","06","07","08","09","10","11","12")
    val diaJahrSpinner  = dialog.findViewById<Spinner>(R.id.anwesenheit_nachtraeglich_jahr)
    val JahrItems = arrayOf("2019", "2020","2021")
    val diaStundenSpinner  = dialog.findViewById<Spinner>(R.id.anwesenheit_nachtraeglich_stunden)
    val StundenItems = arrayOf("1", "2","3")
    val diaSendBtn = dialog.findViewById<Button>(R.id.dialogSendBtn)
    val diaCancBtn = dialog.findViewById<Button>(R.id.dialogCancelBtn)
    val Tagadapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, TagItems)
    val Monatadapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, MonatItems)
    val Jahradapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, JahrItems)
    val Stundenadapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, StundenItems)
    diaTagSpinner.setAdapter(Tagadapter)
    diaMonatSpinner.setAdapter(Monatadapter)
    diaJahrSpinner.setAdapter(Jahradapter)
    diaStundenSpinner.setAdapter(Stundenadapter)


    diaSendBtn.setOnClickListener {
        dialog.dismiss()
        var tempString = StringBuilder()

        tempString.append(diaTagSpinner.selectedItem.toString())
        tempString.append("-")
        tempString.append(diaMonatSpinner.selectedItem.toString())
        tempString.append("-")
        tempString.append(diaJahrSpinner.selectedItem.toString())
        addAnwesenheitNachtraeglich(
            context,
            id,
            tempString.toString(),
            "tobias",
            vorname,
            nachname,
            diaStundenSpinner.selectedItem.toString()
        )
    }
    //cancel button click of custom layout
    diaCancBtn.setOnClickListener {
        //dismiss dialog
        dialog.dismiss()
    }
    dialog.show()

}
// Invokes the Kontakt-Formular
fun showKontaktDialog(context: Context,id: Int){

    val dialog = Dialog(context, R.style.SecondDialogTheme)
    dialog.setContentView(R.layout.dialog_kontakt)
    dialog.setTitle("Nachricht Schreiben")
    val diaSendBtn = dialog.findViewById<Button>(R.id.dialogSendBtn)
    val diaCancBtn = dialog.findViewById<Button>(R.id.dialogCancelBtn)
    val diaVorname = dialog.findViewById<EditText>(R.id.add_kontakt_vorname)
    val diaNachname = dialog.findViewById<EditText>(R.id.add_kontakt_nachname)
    val diaBetreff = dialog.findViewById<EditText>(R.id.add_kontakt_betreff)
    val diaNachricht = dialog.findViewById<EditText>(R.id.add_kontakt_text)
    diaSendBtn.setOnClickListener {
        AsyncIsOnlineAddNachricht(
            context,
            diaVorname.text.toString(),
            diaNachname.text.toString(),
            diaBetreff.text.toString(),
            diaNachricht.text.toString(),
            id
        ).execute()
        dialog.dismiss()
    }
    //cancel button click of custom layout
    diaCancBtn.setOnClickListener {
        //dismiss dialog
        dialog.dismiss()
    }
    dialog.show()
}

fun showUpdateBannerTextDialog(context: Context, target: String){
    when(target){
        "BANNER" -> {
            val dialog = Dialog(context, R.style.SecondDialogTheme)
            dialog.setContentView(R.layout.dialog_edit_banner)
            dialog.setTitle("Bannertext ändern")
            val diaContent = dialog.findViewById<EditText>(R.id.dialogEditText_banner)
            val diaSendBtn = dialog.findViewById<Button>(R.id.dialogSendBtn)
            val diaCancBtn = dialog.findViewById<Button>(R.id.dialogCancelBtn)
            val diaUpdateBtn = dialog.findViewById<Button>(R.id.dialogUpdateBannerBtn)
            diaSendBtn.setOnClickListener {
                dialog.dismiss()
                updateAktuelles(diaContent.text.toString(), context, 0, "")
            }
            //cancel button click of custom layout
            diaCancBtn.setOnClickListener {
                //dismiss dialog
                dialog.dismiss()
            }
            diaUpdateBtn.setOnClickListener {
                UploadBannerPressed(context, 0)
                dialog.dismiss()
            }
            dialog.show()
        }
        "SUB-BANNER" -> {
            val dialog = Dialog(context, R.style.SecondDialogTheme)
            dialog.setContentView(R.layout.dialog_edit_sub_banner)
            dialog.setTitle("Bannertext ändern")
            val diaSpinner  = dialog.findViewById<Spinner>(R.id.dialogSpinner)
            val SpinnerItems = arrayOf("oberes Sub-Banner", "unteres Sub-Banner")
            val Spinneradapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, SpinnerItems)
            diaSpinner.setAdapter(Spinneradapter)
            val diaContent = dialog.findViewById<EditText>(R.id.dialogEditText_banner)
            val diaExcerpt = dialog.findViewById<EditText>(R.id.dialogEditText_banner_caption)
            val diaSendBtn = dialog.findViewById<Button>(R.id.dialogSendBtn)
            val diaCancBtn = dialog.findViewById<Button>(R.id.dialogCancelBtn)
            val diaUpdateBtn = dialog.findViewById<Button>(R.id.dialogUpdateBannerBtn)
            diaSendBtn.setOnClickListener {
                var BannerId = 1
                if(diaSpinner.selectedItem.toString() == "oberes Sub-Banner"){
                }else{
                    BannerId = 2
                }
                dialog.dismiss()
                updateAktuelles(
                    diaContent.text.toString(),
                    context,
                    BannerId,
                    diaExcerpt.text.toString()
                )
            }
            //cancel button click of custom layout
            diaCancBtn.setOnClickListener {
                //dismiss dialog
                dialog.dismiss()
            }
            diaUpdateBtn.setOnClickListener {
                var BannerId = 1
                if(diaSpinner.selectedItem.toString() == "oberes Sub-Banner"){
                }else{
                    BannerId = 2
                }
                UploadBannerPressed(context, BannerId)
                dialog.dismiss()
            }
            dialog.show()

        }
    }
}

fun showAddTerminDialog(context: Context){
    val dialog = Dialog(context, R.style.SecondDialogTheme)
    dialog.setContentView(R.layout.dialog_add_termin)
    dialog.setTitle("Termin hinzufügen")
    val diaSendBtn = dialog.findViewById<Button>(R.id.dialogSendBtn)
    val diaCancBtn = dialog.findViewById<Button>(R.id.dialogCancelBtn)

    val diaAnlass = dialog.findViewById<EditText>(R.id.add_termin_anlass)
    val diaOrt = dialog.findViewById<EditText>(R.id.add_termin_ort)

    val diaTagSpinner  = dialog.findViewById<Spinner>(R.id.add_termin_tag)
    val diaMonatSpinner  = dialog.findViewById<Spinner>(R.id.add_termin_monat)
    val diaJahrSpinner  = dialog.findViewById<Spinner>(R.id.add_termin_jahr)
    val diaStundeSpinner  = dialog.findViewById<Spinner>(R.id.add_termin_stunde)
    val diaMinutenSpinner  = dialog.findViewById<Spinner>(R.id.add_termin_minuten)

    val TagItems = arrayOf("01", "02", "03", "04","05", "06", "07", "08","09","10",
                           "11", "12", "13", "14","15", "16", "17", "18","19","20",
                           "21", "22", "23", "24","25", "26", "27", "28","29","30","31")
    val MonatItems = arrayOf("Januar", "Februar", "März", "April","Mai","Juni","Juli","August",
                             "September", "Oktober","November","Dezember")
    val JahrItems = arrayOf("2019", "2020", "2021")
    val StundenItems = arrayOf("00", "01", "02", "03","04","05","06","07","08","09","10",
                              "11", "12", "13", "14","15","16","17","18","19","20","21",
                              "22","23")
    val MinutenItems = arrayOf("00", "15", "30", "45")

    val Tagadapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, TagItems)
    val Monatadapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, MonatItems)
    val Jahradapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, JahrItems)
    val Stundenadapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, StundenItems)
    val Minutenadapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, MinutenItems)

    diaTagSpinner.setAdapter(Tagadapter)
    diaMonatSpinner.setAdapter(Monatadapter)
    diaJahrSpinner.setAdapter(Jahradapter)
    diaStundeSpinner.setAdapter(Stundenadapter)
    diaMinutenSpinner.setAdapter(Minutenadapter)

    diaSendBtn.setOnClickListener {
        var MonatAsDigit: String? = null
        when(diaMonatSpinner.selectedItem.toString()){
            "Januar"->{MonatAsDigit = "01"}"Februar"->{MonatAsDigit = "02"}"März"->{MonatAsDigit = "03"}"April"->{MonatAsDigit = "04"}
            "Mai"->{MonatAsDigit = "05"}"Juni"->{MonatAsDigit = "06"}"Juli"->{MonatAsDigit = "07"}"August"->{MonatAsDigit = "08"}
            "September"->{MonatAsDigit = "09"}"Oktober"->{MonatAsDigit= "10"}"November"->{MonatAsDigit = "11"}"Dezember"->{MonatAsDigit = "12"}
        }
        addTermin(
            context,
            diaTagSpinner.selectedItem.toString(),
            MonatAsDigit.toString(),
            diaJahrSpinner.selectedItem.toString(),
            diaStundeSpinner.selectedItem.toString(),
            diaMinutenSpinner.selectedItem.toString(),
            diaAnlass.text.toString(),
            diaOrt.text.toString()
        )
        dialog.dismiss()
    }
    //cancel button click of custom layout
    diaCancBtn.setOnClickListener {
        //dismiss dialog
        dialog.dismiss()
    }
    dialog.show()
}

// Extension function to show toast message
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun checkIfLoggedIn(activity: Activity, loggedIn: Boolean,authorization:Int, PERMISSION_REQUEST:Int){
    if (!loggedIn) {
        Log.d("ItemClick:", "logged == false")
        showLogInDialog(activity)
    } else {
        Log.d("ItemClick:", "else")
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST)
        }else{
            Log.d("Permission","Already Granted")
            ProfilPressed(activity, authorization)
        }
    }
}

// The Dialog to log someone in
fun showLogInDialog(activity: Activity) {

        val dialog = Dialog(activity, R.style.SecondDialogTheme)
        dialog.setContentView(R.layout.dailog_signin)
        dialog.setTitle("Login")

        val diaNameEt = dialog.findViewById<EditText>(R.id.dialogNameEt)
        val diaPassEt = dialog.findViewById<EditText>(R.id.dialogPasswEt)
        val diaLogBtn: Button = dialog.findViewById(R.id.dialogLoginBtn)
        val diaCancBtn: Button = dialog.findViewById(R.id.dialogCancelBtn)

        //login button click of custom layout
        diaLogBtn.setOnClickListener {
            //dismiss dialog
            dialog.dismiss()
            //get text from EditTexts of custom layout
            val name = diaNameEt.text.toString()
            val password = diaPassEt.text.toString()
            try {
                AsyncValidateLogIn(activity,name, password).execute()
            } catch(e: Exception){
                println(e)
            }
        }
        //cancel button click of custom layout
        diaCancBtn.setOnClickListener {
            //dismiss dialog
            dialog.dismiss()
        }
        dialog.show()
}
// a static dialog for the sizes of the banners
fun showMasseDialog(activity: Activity){
    val dialog = Dialog(activity, R.style.SecondDialogTheme)
    dialog.setContentView(R.layout.dialog_banner_masse)
    val diaCancBtn: Button = dialog.findViewById(R.id.dialogCancelBtn)
    //cancel button click of custom layout
    diaCancBtn.setOnClickListener {
        //dismiss dialog
        dialog.dismiss()
    }
    dialog.show()
}

fun showKommDialog(activity: Activity,vorname: String,nachname: String,id_nutzer:Int, auth:Int){
    val dialog = Dialog(activity, R.style.SecondDialogTheme)
    dialog.setContentView(R.layout.dialog_messages)
    dialog.setTitle("Nachrichten")
    val diaCancBtn = dialog.findViewById<Button>(R.id.dialogCancelBtn)
    val recyclerview = dialog.findViewById<RecyclerView>(R.id.recyclerView_messages)
    recyclerview.layoutManager = LinearLayoutManager(activity.applicationContext)
    AsyncIsOnlineGetKomm(activity,recyclerview,vorname,id_nutzer,auth).execute()

    //cancel button click of custom layout
    diaCancBtn.setOnClickListener {
        //dismiss dialog
        dialog.dismiss()
    }
    dialog.show()
}

fun showNachrichtDetailDialog(activity: Activity,item: Kommunikation,vorname: String,id_nutzer: Int,auth: Int){
    val dialog = Dialog(activity, R.style.SecondDialogTheme)
    if(auth == 1){
        dialog.setContentView(R.layout.dialog_messages_detail_auth)
        val tv_nachname = dialog.findViewById<TextView>(R.id.tv_messages_detail_nachname_dyn)
        val tv_beantwortet_von = dialog.findViewById<TextView>(R.id.tv_messages_detail_beantwortet_von_dyn)
        tv_nachname.text = item.nachname_sender
        tv_beantwortet_von.text = item.beantwortet_von
    }else{
        dialog.setContentView(R.layout.dialog_messages_detail)
    }
    val diaCancBtn = dialog.findViewById<Button>(R.id.dialogCancelBtn)
    val diaSendBtn = dialog.findViewById<Button>(R.id.dialogSendBtn)
    val tv_betreff = dialog.findViewById<TextView>(R.id.tv_messages_detail_betreff)
    val tv_vorname = dialog.findViewById<TextView>(R.id.tv_messages_detail_vorname_dyn)
    val tv_nachricht = dialog.findViewById<TextView>(R.id.tv_messages_detail_nachricht_dyn)


    tv_betreff.text = item.betreff
    tv_vorname.text = item.vorname_sender
    tv_nachricht.text = item.nachricht


    updateGelesen(item.id)

    //cancel button click of custom layout
    diaCancBtn.setOnClickListener {
        //dismiss dialog
        dialog.dismiss()
    }
    diaSendBtn.setOnClickListener {
        showNachrichtAntwortDialog(activity,item,vorname,id_nutzer)
    }
    dialog.show()
}

fun showNachrichtAntwortDialog(activity: Activity,item: Kommunikation,vorname: String, id_nutzer: Int){
    val dialog = Dialog(activity, R.style.SecondDialogTheme)
    dialog.setContentView(R.layout.dialog_messages_antwort)
    val diaCancBtn = dialog.findViewById<Button>(R.id.dialogCancelBtn)
    val diaSendBtn = dialog.findViewById<Button>(R.id.dialogSendBtn)
    val tv_betreff = dialog.findViewById<TextView>(R.id.tv_messages_antwort_betreff)
    val et_nachricht = dialog.findViewById<EditText>(R.id.et_messages_antwort)

    tv_betreff.text = item.betreff

    //cancel button click of custom layout
    diaCancBtn.setOnClickListener {
        //dismiss dialog
        dialog.dismiss()
    }
    diaSendBtn.setOnClickListener {
        updateBeantwortet(item.id, vorname)
        Log.d("ShowDialog", item.betreff.toString() + " " + item.id_sender.toString() + " " + item.id_empfaenger.toString())
        addNachricht(activity,vorname,"",item.betreff,et_nachricht.text.toString(),id_nutzer,item.id_sender)
        dialog.dismiss()
    }
    dialog.show()
}

class AsyncGetKomm(activity: Activity,recyclerView: RecyclerView,vorname: String,id_nutzer: Int, auth:Int) : AsyncTask<String, String, String>() {

    var KommListe: List<Kommunikation>? = null
    var activity = activity
    var recyclerview = recyclerView
    var vorname = vorname
    var id_nutzer = id_nutzer
    var auth = auth

    // stub function
    override fun onPreExecute() {
        super.onPreExecute()
    }

    // main function
    override fun doInBackground(vararg p0: String?): String {
        var Result: String = ""
        try {
            KommListe = getKommunikation(activity)
        } catch (e: Exception) {
            println(e)
        }
        return Result
    }

    // what to do after response arrives
    override fun onPostExecute(result: String?) {
        var TempListe = mutableListOf<Kommunikation>()
        if(auth == 1){
            for(item in KommListe!!){
                if(item.id_empfaenger == 0 || item.id_empfaenger ==id_nutzer){
                    TempListe.add(item)
                }
            }
        }else{
            for(item in KommListe!!){
                if(item.id_empfaenger ==id_nutzer){
                    TempListe.add(item)
                }
            }
        }

        recyclerview.adapter = GsonAdapterKommDialog(
            TempListe!!,
            { partItem: Kommunikation ->MessageClicked(activity,partItem,vorname,id_nutzer,auth) })
    }
}

class AsyncIsOnlineGetKomm(activity: Activity,recyclerview: RecyclerView,vorname: String,id_nutzer: Int,auth: Int) : AsyncTask<String, String, String>() {

    var success : Boolean? = null
    var activity = activity
    var recyclerView = recyclerview
    var vorname = vorname
    var id_nutzer = id_nutzer
    var auth = auth

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
            AsyncGetKomm(activity,recyclerView,vorname,id_nutzer,auth).execute()
        }else{
            Toast.makeText(activity,"Keine Internetverbindung möglich", Toast.LENGTH_LONG)
        }
    }
}

class AsyncValidateLogIn(activity: Activity,iname: String, ipassword: String) : AsyncTask<String, String, String>() {

    var name = iname
    var password = ipassword
    var success = 0
    var id = 0
    var activity = activity
    var validationResponse = ValidationResponse(0, 0, 0)
    var sp: SharedPreferences = activity.getSharedPreferences("com.szymendera_development.guwon_app",
        AppCompatActivity.MODE_PRIVATE)

    override fun onPreExecute() {
        super.onPreExecute()
    }
    override fun doInBackground(vararg p0: String?): String {
        var Result: String = ""
        try {
            // Filling a local Version of the wanted Array with the Information
            // from the server
            validationResponse =
                validateLogIn(activity, name, password)
        } catch (e: Exception) {
            println(e)
        }
        return Result
    }

    // what to do after response arrives
    override fun onPostExecute(result: String?) {
        //set the input text in TextView
        if (validationResponse.success == 1) {
            Log.d("success","1")
            sp?.edit()?.putBoolean("loggedIn", true)?.apply()
            if(sp?.getInt("temp", 0) == 1){
                deleteSchuelerById(activity,sp?.getInt("id", 0))
                sp?.edit().putInt("temp",0)?.apply()
            }
            sp?.edit()?.putInt("id", validationResponse.id)?.apply()
            sp?.edit()?.putString("vorname",name)?.apply()
            sp?.edit()?.putInt("auth", validationResponse.authorized)?.apply()
            activity.toast("Du hast dich erfolgreich eingeloggt!")
        } else {
            Log.d("success","0")
            activity.toast("Falscher Name || Falsches Passwort.")
        }
    }
}

class AsyncIsOnlineAddNachricht(i_context:Context,i_vorname: String, i_nachname: String,
                                i_betreff:String, i_nachricht:String, id: Int)
    : AsyncTask<String, String, String>() {

    var context = i_context
    var vorname = i_vorname
    var nachname = i_nachname
    var betreff = i_betreff
    var nachricht = i_nachricht
    var id = id

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
            AsyncAddNachricht(context, vorname, nachname, betreff, nachricht,id).execute()
        }else{
            Toast.makeText(context,"Keine Internetverbindung möglich", Toast.LENGTH_LONG)
        }
    }
}

class AsyncAddNachricht(i_context: Context, i_vorname: String,i_nachname: String,
                        i_betreff:String,i_nachricht:String,id:Int)
    : AsyncTask<String, String, String>() {

    var context = i_context
    var vorname = i_vorname
    var nachname = i_nachname
    var betreff = i_betreff
    var nachricht = i_nachricht
    var id = id

    var success : Boolean? = null

    // stub function
    override fun onPreExecute() {
        super.onPreExecute()
    }

    // main function
    override fun doInBackground(vararg p0: String?): String {
        var Result: String = ""
        try {
            addNachricht(context, vorname, nachname, betreff, nachricht,id,0)
        } catch (e: Exception) {
            println(e)
        }
        return Result
    }

    // what to do after response arrives
    override fun onPostExecute(result: String?) {
    }
}

class AsyncGetSchuelerDialog(activity: Activity,recyclerView: RecyclerView) : AsyncTask<String, String, String>() {

    var schuelerListe: List<Schueler>? = null
    var activity = activity
    var recyclerview = recyclerView
    // stub function
    override fun onPreExecute() {
        super.onPreExecute()
    }

    // main function
    override fun doInBackground(vararg p0: String?): String {
        var Result: String = ""
        try {
            schuelerListe = getAllSchueler(activity)
        } catch (e: Exception) {
            println(e)
        }
        return Result
    }

    // what to do after response arrives
    override fun onPostExecute(result: String?) {
        recyclerview.adapter = GsonAdapterDialog(
            schuelerListe!!,
            { partItem: Schueler -> EventTeilnahmeClicked(partItem) })
    }
}

class AsyncIsOnlineGetSchuelerDialog(activity: Activity,recyclerView: RecyclerView) : AsyncTask<String, String, String>() {

    var success : Boolean? = null
    var recyclerview = recyclerView
    var activity = activity

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
            AsyncGetSchuelerDialog(activity,recyclerview).execute()
        }else{
            Toast.makeText(activity,"Keine Internetverbindung möglich", Toast.LENGTH_LONG)
        }
    }
}
