package szymendera_development.guwon_app.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import szymendera_development.guwon_app.R
import java.io.File


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ProfilLowerFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ProfilLowerFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ProfilLowerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    var tv_ranglistenplatz: TextView? = null
    var tv_gesamtpunkte: TextView? = null
    var tv_trainingspunkte: TextView? = null
    var tv_quizpunkte: TextView? = null
    var tv_eventpunkte: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v= inflater.inflate(R.layout.fragment_profil_lower, container, false)
        tv_ranglistenplatz = v.findViewById(R.id.tv_ranglistenplatz)
        tv_gesamtpunkte = v.findViewById(R.id.tv_gesamtpunkte)
        tv_trainingspunkte = v.findViewById(R.id.tv_trainingspunkte)
        tv_quizpunkte = v.findViewById(R.id.tv_quizpunkte)
        tv_eventpunkte = v.findViewById(R.id.tv_eventpunkte)
        return v
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun setRanglistenPlatz(value:Int){
        tv_ranglistenplatz?.text = value.toString() + "."
    }

    fun setGesamtPunkte(value:Int){
        tv_gesamtpunkte?.text = value.toString()
    }
    fun setTrainingsPunkte(value:Int){
        tv_trainingspunkte?.text = value.toString()
    }
    fun setQuizPunkte(value:Int){
        tv_quizpunkte?.text = value.toString()
    }
    fun setEventPunkte(value:Int){
        tv_eventpunkte?.text = value.toString()
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri){}
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfilLowerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfilLowerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
