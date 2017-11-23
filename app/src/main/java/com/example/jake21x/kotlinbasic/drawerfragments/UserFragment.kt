package com.example.jake21x.kotlinbasic.drawerfragments

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
import com.example.jake21x.kotlinbasic.drawerfragments.User.AddEditUserActivity
import com.example.jake21x.kotlinbasic.realm.Users
import io.realm.Realm
import io.realm.RealmConfiguration
import org.jetbrains.anko.alert
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast
import java.util.*
import io.realm.RealmObject.deleteFromRealm
import io.realm.RealmResults
import android.R.attr.fragment



/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [UserFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {

    private var mParam1: String? = null
    private var mParam2: String? = null

    var user_code:Int = 1;
    var _view:View?=null;

    var realm:Realm?=null;
    var config:RealmConfiguration?=null;

    var liinear_no_user: LinearLayout?=null;

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
        _view = inflater!!.inflate(R.layout.fragment_user, container, false)

        val fab = _view!!.find<FloatingActionButton>(R.id.fab)
        recyclerView = _view!!.find<RecyclerView>(R.id.recycler_list);

        liinear_no_user = _view!!.find<LinearLayout>(R.id.liinear_no_user);

        refreshList();

        fab.setOnClickListener{
            var intent = Intent(activity,AddEditUserActivity::class.java);
            intent.putExtra("user_code",user_code)
            startActivityForResult(intent , user_code);
            // close this activity
            activity.overridePendingTransition(R.anim.push_down_from_up, R.anim.stay)
        }
        return _view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == user_code){
            refreshList();
            //activity.toast("resultCode = " + resultCode.toString());
        }
    }



    fun getUsers(realm:Realm) : RealmResults<Users>{
        return realm.where(Users::class.java).findAll()
    }


    fun refreshList() {

        linearLayoutManager = LinearLayoutManager(activity);

        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.hasFixedSize();

        val list = ArrayList(getUsers(realm!!));

        if(list.size != 0 ){
           liinear_no_user!!.setVisibility(GONE);
        }else{
            liinear_no_user!!.setVisibility(VISIBLE);
        }

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

        fun newInstance(param1: String, param2: String): UserFragment {
            val fragment = UserFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }

    }

    class CustomAdapter( val context:Context ,val UsersList : ArrayList<Users>):RecyclerView.Adapter<CustomAdapter.ViewHolder>()  {

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            val item :Users = UsersList[position]
            val context : Context = context;
            val config = RealmConfiguration.Builder().name("kotlinbasic").deleteRealmIfMigrationNeeded().build();
            val realm = Realm.getInstance(config);

            holder!!.txt_name.text = item.username
            holder!!.txt_email.text = item.email
            holder!!.txt_contact.text  = item.contact
            holder!!.btn_delete.onClick {

                context.alert {
                    title("Deleting User..")
                    message("Are you sure you want to delete user?")
                    negativeButton {
                        dismiss()
                    }
                    positiveButton {

                        UsersList.remove(item);
                        notifyDataSetChanged();

                        realm.beginTransaction()
                        val messageobj = realm.where(Users::class.java).contains("id", item.id.toString()).findAll()
                        messageobj.deleteAllFromRealm()
                        realm.commitTransaction();

                        dismiss()
                    }
                }.show()

            }
            holder!!.item_container.onClick {
                context.toast("item_container?")
            }
        }

        override fun getItemCount(): Int {
            return UsersList.size
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent?.context).inflate(R.layout.user_item_onlist,parent,false);
            return ViewHolder(v);
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val txt_name = itemView.find<TextView>(R.id.txt_name);
            val txt_email = itemView.find<TextView>(R.id.txt_email);
            val txt_contact = itemView.find<TextView>(R.id.txt_contact);
            val btn_delete = itemView.find<ImageView>(R.id.btn_delete);
            val item_container = itemView.find<LinearLayout>(R.id.item_container);
        }
    }

}// Required empty public constructor






