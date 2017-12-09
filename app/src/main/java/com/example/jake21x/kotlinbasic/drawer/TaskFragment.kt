package com.example.jake21x.kotlinbasic.drawer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.*
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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.jake21x.kotlinbasic.utils.Db
import com.example.jake21x.kotlinbasic.R
import com.example.jake21x.kotlinbasic.drawer.activities.AddEditTaskActivity
import com.example.jake21x.kotlinbasic.model.Tasks
import com.example.jake21x.kotlinbasic.services.AppLogger
import org.jetbrains.anko.alert
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*




/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TaskFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null


    var user_code:Int = 1;
    var _view:View?=null;

    var liinear_no_user: LinearLayout?=null;

    var linearLayoutManager: LinearLayoutManager?=null;
    var recyclerView: RecyclerView?=null;
    var adapter: TaskCustomAdapter?=null;

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
        _view = inflater!!.inflate(R.layout.fragment_task, container, false)

        val fab = _view!!.find<FloatingActionButton>(R.id.fab)
        recyclerView = _view!!.find<RecyclerView>(R.id.task_recycler_list);

        liinear_no_user = _view!!.find<LinearLayout>(R.id.liinear_no_user);

        refreshList();

        fab.setOnClickListener{
            var intent = Intent(activity, AddEditTaskActivity::class.java);
            intent.putExtra("user_code",user_code)
            startActivityForResult(intent , user_code);
            // close this activity
            activity.overridePendingTransition(R.anim.push_down_from_up, R.anim.stay)
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

        return _view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == user_code){
            refreshList();
            //activity.toast("resultCode = " + resultCode.toString());
        }
    }



    fun refreshList() {

        linearLayoutManager = LinearLayoutManager(activity);

        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.hasFixedSize();

        val list = ArrayList(DbStore!!.getTasks(DbStore!!));

        if(list.size != 0 ){
            liinear_no_user!!.setVisibility(View.GONE);
        }else{
            liinear_no_user!!.setVisibility(View.VISIBLE);
        }

        adapter = TaskCustomAdapter(activity,list,DbStore!!);
        recyclerView!!.adapter = adapter

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
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TaskFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): TaskFragment {
            val fragment = TaskFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    class TaskCustomAdapter( val context:Context ,val TaskList : ArrayList<Tasks> , val DbStore: Db): RecyclerView.Adapter<TaskCustomAdapter.ViewHolder>()  {

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

            val item : Tasks = TaskList[position]
            val context : Context = context;

            val pattern = SimpleDateFormat("hh:mm a")

            holder!!.txt_client.text = item.client
            holder!!.txt_datetime.text = SimpleDateFormat("MMM dd").format(Date(item.date)).toString()+" at "+item.time
            holder!!.txt_start_end.text  =   "${getDateFromMsec(getDateDiffInMsec(  Date(item.starttime) , Date(item.endtime)))}"
//            holder!!.txt_start_end.text  =   pattern.format(Date(item.starttime)).toString() +" - "+ pattern.format(Date(item.endtime)).toString()
            holder!!.txt_startend_diff.text  =    ""

            if(item.long.toString() != "0.0"){
                holder!!.txt_location.text  = item.long.toString() +","+item.lat.toString()
            }else{
                holder!!.txt_location.text  = "No location stablished"
            }

            var generator = ColorGenerator.MATERIAL;

            var  drawable = TextDrawable.builder().buildRound( getInitials(item.client), generator.randomColor);
            holder!!.item_photoImageView.setImageDrawable(drawable);

            holder!!.btn_delete.onClick {

                context.alert {
                    title("Deleting Itinerary..")
                    message("Are you sure you want to delete Itinerary?")
                    negativeButton {
                        dismiss()
                    }
                    positiveButton {

                        TaskList.remove(item);
                        notifyDataSetChanged();


                        DbStore.use {

                            var res  = delete(Tasks.TABLE_NAME , "${Tasks.id} = ${item.id}");
                            //context.toast("res: "+res.toString())
                        }

                        dismiss()
                    }
                }.show()

            }

            holder!!.item_container.onClick {
                context.toast("item_container?")
            }
        }

        override fun getItemCount(): Int {
            return TaskList.size
        }

        fun getDateDiffInMsec(da: Date, db: Date): Long {
            var diffMSec: Long = 0
            diffMSec = db.time - da.time
            return diffMSec
        }

        // to convert Milliseconds into DD HH:MM:SS format.
        fun getDateFromMsec(diffMSec: Long): String {
            var left = 0
            var ss = 0
            var mm = 0
            var hh = 0
            var dd = 0
            left = (diffMSec / 1000).toInt()
            ss = left % 60
            left = left.toInt() / 60
            if (left > 0) {
                mm = left % 60
                left = left.toInt() / 60
                if (left > 0) {
                    hh = left % 24
                    left = left.toInt() / 24
                    if (left > 0) {
                        dd = left
                    }
                }
            }

            var show_time_laps = "";
            if(dd != 0){
                show_time_laps += " ${dd}day(s)"
            }
            if(hh != 0){
                show_time_laps += " ${hh}hr(s)"
            }
            if(mm != 0){
                show_time_laps += " ${mm}min(s)"
            }else if(ss != 0){
                show_time_laps += " ${ss}sec(s)"
            }else{
                show_time_laps += " ${1}sec(s)"
            }

            return show_time_laps
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
            val v = LayoutInflater.from(parent?.context).inflate(R.layout.task_item_onlist,parent,false);
            return ViewHolder(v);
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val txt_client = itemView.find<TextView>(R.id.txt_client);
            val txt_datetime = itemView.find<TextView>(R.id.txt_datetime);
            val txt_start_end = itemView.find<TextView>(R.id.txt_start_end);
            val txt_location = itemView.find<TextView>(R.id.txt_location);
            val txt_startend_diff = itemView.find<TextView>(R.id.txt_startend_diff);
            val btn_delete = itemView.find<ImageView>(R.id.btn_delete);
            val item_container = itemView.find<LinearLayout>(R.id.item_container);
            val item_photoImageView = itemView.find<ImageView>(R.id.tasks_item_photoImageView);
        }

        fun setScaledBitmap(item_photoImageView: ImageView, imageFilePath:String): Bitmap {

            return  transform(BitmapFactory.decodeFile(imageFilePath))

        }

        fun transform(source: Bitmap): Bitmap {

            val minEdge = Math.min(source.width, source.height)
            val dx = (source.width - minEdge) / 2
            val dy = (source.height - minEdge) / 2

            // Init shader
            val shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            val matrix = Matrix()
            matrix.setTranslate((-dx).toFloat(), (-dy).toFloat())   // Move the target area to center of the source bitmap
            shader.setLocalMatrix(matrix)

            // Init paint
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.shader = shader

            // Create and draw circle bitmap
            val output = Bitmap.createBitmap(minEdge, minEdge, source.config)
            val canvas = Canvas(output)
            canvas.drawOval(RectF(0f, 0f, minEdge.toFloat(), minEdge.toFloat()), paint)

            source.recycle()

            return output
        }

    }


}// Required empty public constructor
