package com.example.jake21x.kotlinbasic

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import org.jetbrains.anko.indeterminateProgressDialog
import org.json.JSONObject
import com.android.volley.toolbox.StringRequest
import org.jetbrains.anko.alert
import java.util.*
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.example.jake21x.kotlinbasic.model.Session
import java.text.SimpleDateFormat
import com.example.jake21x.kotlinbasic.services.AppLogger
import com.example.jake21x.kotlinbasic.utils.Db
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert


class MainActivity : AppCompatActivity() {

    //private val URL = "http://188.166.233.193/api/login"
    private val URL = "http://128.199.125.45/api/login"


    var mContext: Context? = null;
    var DbStore: Db? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DbStore = Db.Instance(this);
        DbStore!!.writableDatabase

        // add permission if android version is greater then 23
        if (Build.VERSION.SDK_INT < 23) {
            //Do not need to check the permission

        } else {
            if (checkAndRequestPermissions()) {
                //If you have already permitted the permission

            } else {

            }
        }
    }


    private fun checkAndRequestPermissions(): Boolean {
        val permissionCAMERA = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        val listPermissionsNeeded = ArrayList<String>()
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toTypedArray(), 33)
            return false
        }
        return true
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            33 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission Granted Successfully. Write working code here.

            } else {

            }
        }
    }

    fun btn_login(view: View){

        var username = findViewById<EditText>(R.id.input_email)
        var password = findViewById<EditText>(R.id.input_password)

        if(DbStore!!.getSession(DbStore!!).size == 0){
            httpLogin();
        }else{
            if(DbStore!!.getSession(DbStore!!)[0].email.equals(username.text.toString() , true) &&
                    DbStore!!.getSession(DbStore!!)[0].password.equals(password.text.toString()) ){

                val intent =  Intent(this,HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }else{
                httpLogin();
            }
        }
    }

    fun httpLogin(){
        val intent =  Intent(this,HomeActivity::class.java)
        var username = findViewById<EditText>(R.id.input_email)
        var password = findViewById<EditText>(R.id.input_password)
        indeterminateProgressDialog("logging-in..."){

            val queue = Volley.newRequestQueue(this@MainActivity)

            val response: String? = null

            val finalResponse = response

            val postRequest = object : StringRequest(Request.Method.POST, URL,Response.Listener<String>
            {

                // Getting Response from Server
                response ->
                if(response?.toString() != null){

                    if(response?.toString() != "Credentials Mismatch"){

                        if(DbStore!!.getSession(DbStore!!).size == 0){
                            DbStore!!.use {
                                insert(Session.TABLE_NAME,
                                        Session.user_id to JSONObject(response.toString())["id"].toString() ,
                                        Session.name to JSONObject(response.toString())["name"].toString(),
                                        Session.email to JSONObject(response.toString())["email"].toString(),
                                        Session.user_level to JSONObject(response.toString())["user_level"].toString(),
                                        Session.password to password.text.toString(),
                                        Session.onsession to "loggedIn",
                                        Session.token to "none"
                                )
                            }

                            //toast("is Empty true . just insert")
                        }else{
                            DbStore!!.use {
                                delete(Session.TABLE_NAME);

                                insert(Session.TABLE_NAME,
                                        Session.user_id to JSONObject(response.toString())["id"].toString() ,
                                        Session.name to JSONObject(response.toString())["name"].toString(),
                                        Session.email to JSONObject(response.toString())["email"].toString(),
                                        Session.user_level to JSONObject(response.toString())["user_level"].toString(),
                                        Session.password to password.text.toString(),
                                        Session.onsession to "loggedIn",
                                        Session.token to "none"
                                )
                            };

                            //toast("is Empty false,. clear table first")
                        }



                        intent.putExtra("password", password.text.toString())
                        intent.putExtra("data", JSONObject(response).toString())
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        dismiss();


                        //TODO : Hour is not cleared if we need to use 12hr format or 24
                        val cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY, SimpleDateFormat("HH").format(Date()).toInt());
                        cal.set(Calendar.MINUTE,SimpleDateFormat("mm").format(Date()).toInt());
                        cal.set(Calendar.SECOND,0)
                        //cal.set(Calendar.SECOND,SimpleDateFormat("ss").format(Date()).toInt())

                        val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager;

                        val intent = Intent(this@MainActivity , AppLogger::class.java);
                        //intent.putExtra("msg" , "Logger fire!");
                        //intent.action = "com.example.jake21x.kotlinbasic";
                        val pendIntent = PendingIntent.getService(this@MainActivity ,0 , intent , PendingIntent.FLAG_UPDATE_CURRENT);

                        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, 500 , pendIntent);

//                        val intent = Intent(getContext().getApplicationContext(), AppLogger::class.java);
//                        val pintent = PendingIntent.getService(getContext().getApplicationContext(), 0, intent, 0);
//
//                        val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager;
//                         // Start every 30 seconds
//                        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + (1*1000), 1*1000, pintent)
//                        //webView.getContext().startService(new Intent(webView.getContext().getApplicationContext(), Logger.class));

                    }else{
                        alert(title = "Login Failed!" ,  message = response?.toString() ) {
                            positiveButton("OK") { dismiss() }
                            setCancelable(false)
                        }.show();
                    }

                }else{
                    alert(title = "Login Failed!",  message = "Please check your account & data connection.") {
                        positiveButton("OK") { dismiss() }
                        setCancelable(false)
                    }.show();
                }

            },
                    Response.ErrorListener {
                        // error
                        dismiss()
                        alert(title = "Login Failed!" , message= "Please check data connection.") {
                            positiveButton("OK") { dismiss() }
                            setCancelable(false)
                        }.show();
                        Log.d("ErrorResponse", response.toString())
                    }
            ) {
                override fun getParams(): Map<String, String> {
                    //Creating HashMap
                    val params = HashMap<String, String>()
                    params.put("email", username?.text.toString())
                    params.put("password", password?.text.toString())
                    return params
                }

                override fun getHeaders(): Map<String, String> {
                    //Creating HashMap
                    val header = HashMap<String, String>()
                    header.put("Content-Type", "application/x-www-form-urlencoded")
                    return header
                }
            }

            postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

            queue.add(postRequest)

            setCancelable(false);

        }.show();
    }

    override fun onBackPressed() {
        alert(title = "Exit Application.",message = "Are you sure you want to Exit?") {
           positiveButton("Yes") {
                finish();
           }
           negativeButton("No") {  }
        }.show();
    }

}