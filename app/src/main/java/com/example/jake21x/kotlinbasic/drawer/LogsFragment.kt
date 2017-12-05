package com.example.jake21x.kotlinbasic.drawer

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.jake21x.kotlinbasic.R
import org.jetbrains.anko.find
import java.util.*
import com.example.jake21x.kotlinbasic.realm.Logs
import com.example.jake21x.kotlinbasic.realm.Session
import io.realm.Realm
import io.realm.RealmConfiguration


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

    var realm: Realm?=null;
    var config: RealmConfiguration?=null;

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }

        config = RealmConfiguration.Builder().name("kotlinbasic").deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(config);

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _view = inflater!!.inflate(R.layout.fragment_logs, container, false)

        var onsession =  realm!!.where(Session::class.java).findAll();
        val txt_session_id = _view!!.find<TextView>(R.id.txt_session_id);
        txt_session_id.setText("Id: "+onsession.get(0)!!.id)
        val txt_session_name = _view!!.find<TextView>(R.id.txt_session_name);
        txt_session_name.setText("Name: "+onsession.get(0)!!.name)
        val txt_session_email = _view!!.find<TextView>(R.id.txt_session_email);
        txt_session_email.setText("Email: "+onsession.get(0)!!.email)
        val txt_session_password = _view!!.find<TextView>(R.id.txt_session_password);
        txt_session_password.setText("Password: "+onsession.get(0)!!.password)
        val txt_session_token = _view!!.find<TextView>(R.id.txt_session_token);
        txt_session_token.setText("Token: "+onsession.get(0)!!.token);


        val fab = _view!!.find<FloatingActionButton>(R.id.fab);
        fab.setOnClickListener{
            refreshList();
        }

        recyclerView = _view!!.find<RecyclerView>(R.id.logs_recycler_list);

        refreshList();
        return _view
    }



    fun refreshList() {

//        linearLayoutManager = LinearLayoutManager(activity);
//
//        recyclerView!!.layoutManager = linearLayoutManager
//        recyclerView!!.hasFixedSize();
//
////        val divider = DividerItemDecoration(recyclerView!!.getContext(), DividerItemDecoration.VERTICAL)
////        divider.setDrawable(ContextCompat.getDrawable(activity, R.drawable.divider))
////        divider.set
////        recyclerView!!.addItemDecoration(divider)
//
//        val list = ArrayList(getAll(realm!!));
//
//        adapter = CustomAdapter(activity,list)
//        recyclerView!!.adapter = adapter

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

            val item : Logs = LogsList[position]

            holder!!.txt_log_id.text = item.id;
            holder!!.txt_log_user_id.text = item.user;
            holder!!.txt_log_date.text = item.date;
            holder!!.txt_log_time.text = item.time;
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
            val txt_log_id = itemView.find<TextView>(R.id.txt_id);
            val txt_log_user_id = itemView.find<TextView>(R.id.txt_log_user_id);
            val txt_log_date = itemView.find<TextView>(R.id.txt_log_date);
            val txt_log_time = itemView.find<TextView>(R.id.txt_log_time);
            val txt_log_battery = itemView.find<TextView>(R.id.txt_log_battery);
            val txt_log_status = itemView.find<TextView>(R.id.txt_log_status);
        }
    }

}// Required empty public constructor
