package szymendera_development.guwon_app.Utils

class SchuelerFeed(val SchuelerListe: List<Schueler>)

class Schueler(val id: Int, val vorname: String,val nachname: String,
               val TaekwonDo: Int, val Kickboxen: Int, val Yoga: Int, val Pilates: Int,
               val Event_Teilnahme_Temp:Int)
class SchuelerComplete(val id:Int, val vorname: String, val nachname: String,
                       val Gibon: String, val Teul:String, val Hanbon: String,
                       val Gyeokpa:String, val Daeryeon: String, val Chayu:String,
                       val Hosinsul: String, val Pruefungs_Bereit: String, val Rang: Int,
                       val Anwesenheit: String, val Anwesenheit_num: Int, IsTemp: Int,
                       val TaekwonDo:Int,val Kickboxen: Int,val Yoga: Int,val Pilates: Int,
                       val Anwesenheit_temp: Int, val last_quiz: String, val authorized: Int,
                       val Anwesenheit_Score:Int, val Overall_Score:Int, val Quiz_Score:Int,
                       val Event_Score:Int, val image_version:Int)

class SchuelerTemp(val id: Int, val vorname: String, val nachname: String, val Anwesenheit_temp: Int,
                   val TaekwonDo: Int, val Kickboxen: Int, val Yoga: Int, val Pilates: Int, val username: String)

class CustomDate(val id: Int, val vorname: String, val nachname: String, val description:String, val date: String)

class Quiz(val id: Int, val Frage: String, val Antwort_A: String, val Antwort_B: String, val Antwort_C:String,
           val Richtige_Antwort: String)

class TestDate(val id: Int, val date: String)

class SchuelerCompetition(val id:Int, val vorname: String, val nachname: String, val Overall_Score: Int)

class ValidationResponse(val success: Int, val id: Int, val authorized: Int)

class Aktuelles(val id: Int, val content: String, val name: String, val excerpt: String)

class Termine(val id:Int, val tag:String, val monat:String, val jahr:String, val uhrzeit:String, val anlass:String, val ort:String)

class Events(val id:Int,val turnier:Int,val lehrgang:Int,val pruefung: Int, val teilnehmer_id:Int, val anlass:String,
             val ort:String, val uhrzeit:String, val tag:String, val monat:String, val jahr:String)

class PhotoUpdate(val id:Int, val photo_id:Int, val last_update:String)

class BannerUpdate(val id:Int, val name:String, val last_update: String, val version:Int)

class Kommunikation(val id:Int, val vorname_sender:String, val vorname_empfaenger:String, val nachname_empfaenger:String,
                    val nachname_sender:String, val betreff:String, val nachricht:String, val gelesen:Int, val datum:String,
                    val id_sender:Int, val id_empfaenger:Int, val beantwortet_von: String)
class IdClass(val id:Int)
