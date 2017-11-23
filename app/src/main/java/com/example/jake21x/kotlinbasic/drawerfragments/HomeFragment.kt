package com.example.jake21x.kotlinbasic.drawerfragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.jake21x.kotlinbasic.R
import kotlinx.android.synthetic.main.nav_header_home.*
import org.jetbrains.anko.find
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var data: String? = null
    var user_code:Int = 1;
    var _view:View?=null;
    var txt_api_res:TextView?=null;

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            data = arguments.getString(ARG_PARAM)
            //activity.longToast(data.toString());
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _view= inflater!!.inflate(R.layout.fragment_home, container, false)
        txt_api_res = _view!!.find<TextView>(R.id.txt_api_res);

        if(data!=null){
            txt_api_res!!.text =  ""+
                    "Token recieve : ${JSONObject(data.toString())["token"].toString().substring(0,45)} ..."+"\n" +
                    "Name : ${ JSONObject(JSONArray(JSONObject(data.toString())["credentials"].toString()).get(0).toString())["name"] } \n" +
                    "Username : ${ JSONObject(JSONArray(JSONObject(data.toString())["credentials"].toString()).get(0).toString())["email"] } \n" +
                    "Password : ${ JSONObject(JSONArray(JSONObject(data.toString())["credentials"].toString()).get(0).toString())["set_password"] } \n"

        }else{
            txt_api_res!!.text =  "";
        }


        return _view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            mListener = context
//        } else {
//            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
//        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        private val ARG_PARAM = "data"

        fun newInstance(param: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString(ARG_PARAM, param)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
