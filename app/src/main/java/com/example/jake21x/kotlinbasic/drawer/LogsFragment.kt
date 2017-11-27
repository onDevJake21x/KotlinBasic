package com.example.jake21x.kotlinbasic.drawer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import com.example.jake21x.kotlinbasic.R
import com.example.jake21x.kotlinbasic.drawer.activities.AddEditUserActivity
import com.example.jake21x.kotlinbasic.realm.Users
import io.realm.Realm
import io.realm.RealmConfiguration
import org.jetbrains.anko.alert
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast
import java.util.*
import io.realm.RealmResults
import android.graphics.*
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.jake21x.kotlinbasic.realm.Logs


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

    var realm:Realm?=null;
    var config:RealmConfiguration?=null;

    var linearLayoutManager: LinearLayoutManager?=null;
    var recyclerView: RecyclerView?=null;
    var adapter: CustomAdapter?=null;

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

        recyclerView = _view!!.find<RecyclerView>(R.id.logs_recycler_list);


        refreshList();
        return _view
    }





    fun getUsers(realm:Realm) : RealmResults<Logs>{
        return realm.where(Logs::class.java).findAll()
    }


    fun refreshList() {

        linearLayoutManager = LinearLayoutManager(activity);

        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.hasFixedSize();

//        val divider = DividerItemDecoration(recyclerView!!.getContext(), DividerItemDecoration.VERTICAL)
//        divider.setDrawable(ContextCompat.getDrawable(activity, R.drawable.divider))
//        divider.set
//        recyclerView!!.addItemDecoration(divider)

        val list = ArrayList(getUsers(realm!!));

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
            val context : Context = context;
            val config = RealmConfiguration.Builder().name("kotlinbasic").deleteRealmIfMigrationNeeded().build();
            val realm = Realm.getInstance(config);

            holder!!.txt_name.text = item.id
            holder!!.txt_email.text = item.user


        }

        override fun getItemCount(): Int {
            return LogsList.size
        }

        fun getInitials(name: String?): String {
            val initials = StringBuilder()
            var addNext = true
            if (name != null) {
                for (i in 0 until name.length) {
                    val c = name[i]
                    if (c == ' ' || c == '-' || c == '.') {
                        addNext = true
                    } else if (addNext) {
                        initials.append(c)
                        addNext = false
                    }
                }
            }
            return initials.toString()
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent?.context).inflate(R.layout.user_item_onlist,parent,false);
            return ViewHolder(v);
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val txt_name = itemView.find<TextView>(R.id.txt_name);
            val txt_email = itemView.find<TextView>(R.id.txt_email);
        }
    }

}// Required empty public constructor






