package szymendera_development.guwon_app.Utils

import android.app.Activity
import android.graphics.Color
import android.provider.CalendarContract
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.competition_row.view.*
import kotlinx.android.synthetic.main.kommunikation_row.view.*
import kotlinx.android.synthetic.main.schueler_anwesenheit_row.view.*
import kotlinx.android.synthetic.main.schueler_row.view.*
import szymendera_development.guwon_app.*


class GsonAdapter(val schueler: List<Schueler>, val clickListener: (Schueler) -> Unit) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // numberOfItems
    override fun getItemCount(): Int {
        return schueler.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        // how do we even create a view
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.schueler_row, parent, false)
        return PartViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PartViewHolder).bind(schueler[position], clickListener)
    }

    class PartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(part: Schueler, clickListener: (Schueler) -> Unit) {
            itemView.textView_vorname_dynamic?.text = part.vorname
            itemView.textView_nachname_dynamic?.text = part.nachname
            itemView.setOnClickListener {clickListener(part) }
        }
    }

}

class GsonAdapterMutable(val schueler: MutableList<Schueler>, val clickListener: (Schueler) -> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // numberOfItems
    override fun getItemCount(): Int {
        return schueler.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        // how do we even create a view
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.schueler_row, parent, false)
        return PartViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val value = schueler.get(position)
//        holder?.view?.textView_vorname_dynamic?.text = value.vorname
//        holder?.view?.textView_nachname_dynamic?.text = value.nachname
        (holder as PartViewHolder).bind(schueler[position], clickListener)
    }

    class PartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(part: Schueler, clickListener: (Schueler) -> Unit) {
            itemView.textView_vorname_dynamic?.text = part.vorname
            itemView.textView_nachname_dynamic?.text = part.nachname
            itemView.setOnClickListener { clickListener(part) }
        }
    }

}

class GsonAdapterAnwesenheit(val schueler: List<SchuelerTemp>,
                             val clickListener: (SchuelerTemp) -> Unit, activity: Activity, i_name:String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var name = i_name

    // numberOfItems
    override fun getItemCount(): Int {
        return schueler.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        // how do we even create a view
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.schueler_anwesenheit_row, parent, false)
        return PartViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PartViewHolder).bind(schueler[position], clickListener)
    }

    class PartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(part: SchuelerTemp, clickListener: (SchuelerTemp) -> Unit) {
            itemView.tv_anwesenheit_vorname_dynamic?.text = part.vorname
            itemView.tv_anwesenheit_nachname_dynamic?.text = part.nachname
            if(part.Anwesenheit_temp == 0){
//                itemView.tv_anwesenheit_anwesenheit?.text = "Anwesend?"
                itemView.iv_anwesenheit.setImageResource(R.drawable.cross)
                itemView.iv_anwesenheit.setTag(R.drawable.cross, true)
                itemView.iv_anwesenheit.setTag(R.drawable.check, false)
            }else{
//                itemView.tv_anwesenheit_anwesenheit?.text = "Anwesend!"
                itemView.iv_anwesenheit.setImageResource(R.drawable.check)
                itemView.iv_anwesenheit.setTag(R.drawable.check, true)
                itemView.iv_anwesenheit.setTag(R.drawable.cross, false)
            }
            itemView.setOnClickListener {
                clickListener(part)
                if(itemView.iv_anwesenheit.getTag(R.drawable.cross) == true){
                    itemView.iv_anwesenheit.setImageResource(R.drawable.check)
                    itemView.iv_anwesenheit.setTag(R.drawable.cross, false)
                    itemView.iv_anwesenheit.setTag(R.drawable.check, true)
                   updateDiscipline(
                        part.username,
                        "Anwesenheit_Temp",
                        part.id,
                        itemView.context
                    )
                }
                else if(itemView.iv_anwesenheit.getTag(R.drawable.check) == true){
                    itemView.iv_anwesenheit.setImageResource(R.drawable.cross)
                    itemView.iv_anwesenheit.setTag(R.drawable.cross, true)
                    itemView.iv_anwesenheit.setTag(R.drawable.check, false)
                    updateDiscipline(
                        part.username,
                        "Anwesenheit_Temp",
                        part.id,
                        itemView.context
                    )
                }
            }
        }
    }

}
// Used when we want to make Items clickable in the Competition-Activity
class GsonAdapterCompetition(val schueler: List<SchuelerCompetition>, val clickListener: (SchuelerCompetition) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var i = 1
    // numberOfItems
    override fun getItemCount(): Int {
        return schueler.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        // how do we even create a view
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.competition_row, parent, false)
        return PartViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PartViewHolder).bind(schueler[position], clickListener,position)
    }

    class PartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(part: SchuelerCompetition, clickListener: (SchuelerCompetition) -> Unit, position:Int) {
            itemView.competition_tv_id_dynamic?.text = (position+1).toString() + "."
                itemView.competition_tv_vorname_dynamic?.text = part.vorname
            itemView.competition_tv_nachname_dynamic?.text = part.nachname
            itemView.competition_tv_overall_score_dynamic?.text = part.Overall_Score.toString()
            itemView.setOnClickListener {clickListener(part) }
        }
    }

}
// Used when we want to make Items clickable, which we sorted for the Competition-Activity
//class GsonAdapterMutableCompetition(val schueler: MutableList<SchuelerCompetition>, val clickListener: (SchuelerCompetition) -> Unit)
//    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//    // numberOfItems
//    override fun getItemCount(): Int {
//        return schueler.count()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
//        // how do we even create a view
//        val layoutInflater = LayoutInflater.from(parent?.context)
//        val cellForRow = layoutInflater.inflate(R.layout.competition_row, parent, false)
//        return PartViewHolder(cellForRow)
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
////        val value = schueler.get(position)
////        holder?.view?.textView_vorname_dynamic?.text = value.vorname
////        holder?.view?.textView_nachname_dynamic?.text = value.nachname
//        (holder as PartViewHolder).bind(schueler[position], clickListener)
//    }
//
//    class PartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(part: SchuelerCompetition, clickListener: (SchuelerCompetition) -> Unit) {
//            itemView.competition_tv_vorname_dynamic?.text = part.vorname
//            itemView.competition_tv_nachname_dynamic?.text = part.nachname
//            itemView.competition_tv_overall_score_dynamic?.text = part.Overall_Score.toString()
//            itemView.setOnClickListener { clickListener(part) }
//        }
//    }
//
//}

class GsonAdapterDialog(val schueler: List<Schueler>, val clickListener: (Schueler) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // numberOfItems
    override fun getItemCount(): Int {
        return schueler.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        // how do we even create a view
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.schueler_anwesenheit_row, parent, false)
        return PartViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PartViewHolder).bind(schueler[position], clickListener)
    }

    class PartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(part: Schueler, clickListener: (Schueler) -> Unit) {
            itemView.tv_anwesenheit_vorname_dynamic?.text = part.vorname
            itemView.tv_anwesenheit_nachname_dynamic?.text = part.nachname
            if(part.Event_Teilnahme_Temp == 0){
                itemView.iv_anwesenheit.setImageResource(R.drawable.cross)
                itemView.iv_anwesenheit.setTag(R.drawable.cross, true)
                itemView.iv_anwesenheit.setTag(R.drawable.check, false)
            }else{

                itemView.iv_anwesenheit.setImageResource(R.drawable.check)
                itemView.iv_anwesenheit.setTag(R.drawable.check, true)
                itemView.iv_anwesenheit.setTag(R.drawable.cross, false)
            }
            itemView.setOnClickListener {
                clickListener(part)
                if(itemView.iv_anwesenheit.getTag(R.drawable.cross) == true){
                    itemView.iv_anwesenheit.setImageResource(R.drawable.check)
                    itemView.iv_anwesenheit.setTag(R.drawable.cross, false)
                    itemView.iv_anwesenheit.setTag(R.drawable.check, true)
                    updateDiscipline(
                        "",
                        "Event_Teilnahme_Temp",
                        part.id,
                        itemView.context
                    )
                }
                else if(itemView.iv_anwesenheit.getTag(R.drawable.check) == true){
                    itemView.iv_anwesenheit.setImageResource(R.drawable.cross)
                    itemView.iv_anwesenheit.setTag(R.drawable.cross, true)
                    itemView.iv_anwesenheit.setTag(R.drawable.check, false)
                    updateDiscipline(
                        "",
                        "Event_Teilnahme_Temp",
                        part.id,
                        itemView.context
                    )
                }
            }
        }
    }

}

class GsonAdapterKommDialog(val item: List<Kommunikation>, val clickListener: (Kommunikation) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // numberOfItems
    override fun getItemCount(): Int {
        return item.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        // how do we even create a view
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.kommunikation_row, parent, false)
        return PartViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PartViewHolder).bind(item[position], clickListener)
    }

    class PartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(part: Kommunikation, clickListener: (Kommunikation) -> Unit) {
            itemView.komm_datum_dynamic?.text = part.datum
            itemView.komm_betreff_dynamic?.text = part.betreff
            if(part.gelesen == 1){
                itemView.komm_gelesen_dynamic?.text = "-"
            }else{
                itemView.komm_gelesen_dynamic?.text = "!"
                itemView.komm_gelesen_dynamic?.setTextColor(Color.RED)
            }

            itemView.setOnClickListener {clickListener(part) }
            }
        }
}



