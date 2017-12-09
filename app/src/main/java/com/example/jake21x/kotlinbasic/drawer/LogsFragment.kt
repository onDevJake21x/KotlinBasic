package com.example.jake21x.kotlinbasic.drawer

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.jake21x.kotlinbasic.utils.Db
import com.example.jake21x.kotlinbasic.R
import org.jetbrains.anko.find
import java.util.*
import com.example.jake21x.kotlinbasic.model.Logs
import com.example.jake21x.kotlinbasic.services.AppLogger
import org.jetbrains.anko.alert


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LogsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LogsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogsFragment : Fragment() {

    private var mParam1: String? = null
    private var mParam2: String? = null

    var _view:View?=null;

    var linearLayoutManager: LinearLayoutManager?=null;
    var recyclerView: RecyclerView?=null;
    var adapter: CustomAdapter?=null;

    var DbStore: Db? = null

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }

        DbStore = Db.Instance(activity);
        DbStore!!.writableDatabase

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _view = inflater!!.inflate(R.layout.fragment_logs, container, false)

        val txt_session_id = _view!!.find<TextView>(R.id.txt_session_id);
        txt_session_id.setText("Id: "+DbStore!!.getSession(DbStore!!)[0].user_id)
        val txt_session_name = _view!!.find<TextView>(R.id.txt_session_name);
        txt_session_name.setText("Name: "+DbStore!!.getSession(DbStore!!)[0].name)
        val txt_session_email = _view!!.find<TextView>(R.id.txt_session_email);
        txt_session_email.setText("Email: "+DbStore!!.getSession(DbStore!!)[0].email)
        val txt_session_password = _view!!.find<TextView>(R.id.txt_session_password);
        txt_session_password.setText("Password: "+DbStore!!.getSession(DbStore!!)[0].password)
        val txt_session_token = _view!!.find<TextView>(R.id.txt_session_token);
        txt_session_token.setText("Token: "+DbStore!!.getSession(DbStore!!)[0].token);

        val fab = _view!!.find<FloatingActionButton>(R.id.fab);
        fab.setOnClickListener{
            refreshList();
        }

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

        recyclerView = _view!!.find<RecyclerView>(R.id.logs_recycler_list);

        refreshList();
        return _view
    }



    @SuppressLint("NewApi")
    fun refreshList() {

        linearLayoutManager = LinearLayoutManager(activity);

        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.hasFixedSize();

//        val divider = DividerItemDecoration(recyclerView!!.getContext(), DividerItemDecoration.VERTICAL)
//        divider.setDrawable(ContextCompat.getDrawable(activity, R.drawable.divider))
//        divider.set
//        recyclerView!!.addItemDecoration(divider)

        val list = ArrayList(DbStore!!.getLogs(DbStore!!));
        Collections.reverse(list)

        adapter = CustomAdapter(activity,list)
        recyclerView!!.adapter = adapter

    }



    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
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

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): LogsFragment {
            val fragment = LogsFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }

    }

    class CustomAdapter( val context:Context ,val LogsList : ArrayList<Logs>):RecyclerView.Adapter<CustomAdapter.ViewHolder>()  {

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

            val item :Logs = LogsList[position]

            holder!!.txt_log_id.text = item.id.toString();
            holder!!.txt_log_user_id.text = item.user_id;
            holder!!.txt_log_date.text = item.created_date;
            holder!!.txt_log_time.text = item.created_time;
            holder!!.txt_log_battery.text = item.battery;
            holder!!.txt_log_status.text = item.status;

        }

        override fun getItemCount(): Int {
            return LogsList.size
        }


        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent?.context).inflate(R.layout.logs_item_onlist,parent,false);
            return ViewHolder(v);
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val txt_log_id = itemView.find<TextView>(R.id.txt_log_id);
            val txt_log_user_id = itemView.find<TextView>(R.id.txt_log_user_id);
            val txt_log_date = itemView.find<TextView>(R.id.txt_log_date);
            val txt_log_time = itemView.find<TextView>(R.id.txt_log_time);
            val txt_log_battery = itemView.find<TextView>(R.id.txt_log_battery);
            val txt_log_status = itemView.find<TextView>(R.id.txt_log_status);
        }
    }

}// Required empty public constructor






