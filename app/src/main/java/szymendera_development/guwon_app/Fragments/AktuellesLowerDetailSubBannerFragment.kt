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
import szymendera_development.guwon_app.R
import java.io.File


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AktuellesLowerDetailSubBannerFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AktuellesLowerDetailSubBannerFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AktuellesLowerDetailSubBannerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    var id: Int? = null
//    var content: String? = null
    var excerpt: String? = null
    var tv_value: TextView? = null
    var iv_subbanner1: ImageView? = null
    var iv_subbanner2: ImageView? = null
    var tv_subbanner1: TextView? = null
    var tv_subbanner2: TextView? = null

    var content_subbanner1: String? = null
    var content_subbanner2: String? = null
    var excerpt_subbanner1: String? = null
    var excerpt_subbanner2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            content_subbanner1 = getArguments()?.getString("content_subbanner1").toString()
            excerpt_subbanner1 = getArguments()?.getString("excerpt_subbanner1").toString()
            content_subbanner2 = getArguments()?.getString("content_subbanner2").toString()
            excerpt_subbanner2 = getArguments()?.getString("excerpt_subbanner2").toString()
            id = getArguments()?.getInt("id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v: View
        if (id == 1){
            v = inflater.inflate(R.layout.fragment_aktuelles_lower_detail_sub_banner, container, false)
        }else{
            v = inflater.inflate(R.layout.fragment_aktuelles_lower_detail_sub_banner2, container, false)
        }
        tv_value = v.findViewById(R.id.tv_subbanner_content)
        tv_subbanner1 = v.findViewById(R.id.tv_subbanner1)
        tv_subbanner2 = v.findViewById(R.id.tv_subbanner2)
        if(id == 1){
            tv_value!!.text = content_subbanner1
        }else{
            tv_value!!.text = content_subbanner2
        }
        tv_subbanner1!!.text = excerpt_subbanner1
        tv_subbanner2!!.text = excerpt_subbanner2
        iv_subbanner1 = v.findViewById(R.id.iv_subbanner1)
        iv_subbanner2 = v.findViewById(R.id.iv_subbanner2)
        iv_subbanner1!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val fr = AktuellesLowerDetailSubBannerFragment()
                val fc = activity as FragmentChangeListener
                fc.replaceFragment(fr,1)
            }
        })
        iv_subbanner2!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val fr = AktuellesLowerDetailSubBannerFragment()
                val fc = activity as FragmentChangeListener
                fc.replaceFragment(fr,2)
            }
        })
        setSubBanner1()
        setSubBanner2()
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
         * @return A new instance of fragment AktuellesLowerDetailSubBannerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AktuellesLowerDetailSubBannerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
