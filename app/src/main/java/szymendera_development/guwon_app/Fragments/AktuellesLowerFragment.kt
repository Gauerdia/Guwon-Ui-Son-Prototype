package szymendera_development.guwon_app.Fragments

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
 * [AktuellesLowerFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AktuellesLowerFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AktuellesLowerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    var tv_subbanner1: TextView? = null
    var tv_subbanner2: TextView? = null
    var iv_subbanner1: ImageView? = null
    var iv_subbanner2: ImageView? = null

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
        var v = inflater.inflate(R.layout.fragment_aktuelles_lower, container, false)
        tv_subbanner1 = v.findViewById(R.id.tv_subbanner1)
        tv_subbanner2 = v.findViewById(R.id.tv_subbanner2)
        iv_subbanner1 = v.findViewById(R.id.iv_subbanner1)
        iv_subbanner2 = v.findViewById(R.id.iv_subbanner2)
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
    fun setBannerText1(value:String){
        tv_subbanner1?.text = value
    }
    fun setBannerText2(value:String){
        tv_subbanner2?.text = value
    }

    fun setSubBanner1(){
//        Glide.with(this)
//            .load(activity?.getFilesDir().toString()
//                    + File.separator + name + ".jpg")
//            .into(iv_subbanner1!!)
        val imgFile = File(activity?.getFilesDir().toString()
                + File.separator + "subbanner1" + ".jpg")

        if (imgFile.exists()) {

            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

            iv_subbanner1?.setImageBitmap(myBitmap)
            iv_subbanner1?.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    val fr = AktuellesLowerDetailSubBannerFragment()
                    val fc = activity as FragmentChangeListener
                    fc.replaceFragment(fr,1)
                }
            })
        }
    }

    fun setSubBanner2(){
//        Glide.with(this)
//            .load(activity?.getFilesDir().toString()
//                    + File.separator + name + ".jpg")
//            .into(iv_subbanner2!!)
        val imgFile = File(activity?.getFilesDir().toString()
                + File.separator + "subbanner2" + ".jpg")

        if (imgFile.exists()) {

            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

            iv_subbanner2?.setImageBitmap(myBitmap)
            iv_subbanner2?.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    val fr = AktuellesLowerDetailSubBannerFragment()
                    val fc = activity as FragmentChangeListener
                    fc.replaceFragment(fr,2)
                }
            })
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

    interface FragmentChangeListener {
        fun replaceFragment(fragment: Fragment, id:Int)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AktuellesLowerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AktuellesLowerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
