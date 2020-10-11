package szymendera_development.guwon_app.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import szymendera_development.guwon_app.R
import java.io.File
import java.lang.Exception


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SchuelerProfilUpperFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SchuelerProfilUpperFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SchuelerProfilUpperFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    var vornameTV : TextView? = null
    var nachnameTV : TextView? = null
    var rangTV : TextView? = null
    var sportartTV: TextView? = null

    var iv_photo: ImageView? = null

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
        var v = inflater.inflate(R.layout.fragment_schueler_profil_upper, container, false)
        vornameTV = v.findViewById(R.id.schueler_profil_frag_vorname)
        nachnameTV = v.findViewById(R.id.schueler_profil_frag_nachname)
        rangTV = v.findViewById(R.id.schueler_profil_frag_rang)
        sportartTV = v.findViewById(R.id.schueler_profil_frag_sportart)
        iv_photo = v.findViewById(R.id.schueler_profil_frag_photo)
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

    fun setVorname(value: String){
        vornameTV?.text = value
    }
    fun setNachname(value: String){
        nachnameTV?.text = value
    }
    fun setRang(value: String){
        rangTV?.text = value
    }
    fun setSportart(value: String){
        sportartTV?.text = value
    }

    fun updatePhoto(name: String){
        try {
            Glide.with(this)
//                .load(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + File.separator + ".guwon_app/" + name + ".jpg")
                .load(activity?.getFilesDir().toString()
                        + File.separator + name + ".jpg")
                .into(iv_photo!!)
        }catch(e: Exception){
            Log.d("updatePhoto", e.toString())
        }
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
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SchuelerProfilUpperFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SchuelerProfilUpperFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
