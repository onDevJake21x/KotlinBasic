package com.example.jake21x.kotlinbasic.drawer

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.jake21x.kotlinbasic.utils.Db
import com.example.jake21x.kotlinbasic.R
import org.jetbrains.anko.alert
import org.jetbrains.anko.find
import android.app.AlarmManager
import android.app.PendingIntent
import android.view.KeyEvent
import android.content.Intent
import com.example.jake21x.kotlinbasic.services.AppLogger


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


    var DbStore: Db? = null

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            data = arguments.getString(ARG_PARAM)
            //activity.longToast(data.toString());
        }

        DbStore = Db.Instance(activity);
        DbStore!!.writableDatabase



    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _view= inflater!!.inflate(R.layout.fragment_home, container, false)
        txt_api_res = _view!!.find<TextView>(R.id.txt_api_res);


            txt_api_res!!.text =  ""+
                    "Token recieve :  NO TOKEN  "+"\n" +
                    "id : ${  DbStore!!.getSession(DbStore!!)[0].user_id.toString()  } \n" +
                    "user_level : ${  DbStore!!.getSession(DbStore!!)[0].user_level.toString()   } \n" +
                    "name : ${  DbStore!!.getSession(DbStore!!)[0].name.toString()   } \n" +
                    "email : ${  DbStore!!.getSession(DbStore!!)[0].email.toString()   } \n"

        _view!!.setFocusableInTouchMode(true)
        _view!!.requestFocus()
        _view!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    activity.alert(title = "Logout",message = "Are you sure you want to Logout?") {
                        positiveButton("Yes") {

                            val alarm = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager;

                            val intentForService = Intent(activity, AppLogger::class.java)

                            val pendIntent = PendingIntent.getService(activity ,0 , intentForService , PendingIntent.FLAG_UPDATE_CURRENT);

                            alarm.cancel(pendIntent);

                            activity.finish()
                        }
                        negativeButton("No") {  }
                    }.show();

                    return@OnKeyListener true
                }
            }
            false
        })

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
