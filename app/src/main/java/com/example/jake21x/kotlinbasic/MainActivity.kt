package com.example.jake21x.kotlinbasic

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
import com.example.jake21x.kotlinbasic.model.Session
import com.example.jake21x.kotlinbasic.services.AppBroadCast
import java.text.SimpleDateFormat
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {

    //private val URL = "http://188.166.233.193/api/login"
    private val URL = "http://128.199.125.45/api/login"

    var appdb: Db? = null
    var db: SQLiteDatabase? = null
    var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get reference to button
        val btn_register = findViewById<TextView>(R.id.link_signup);
        // set on-click listener
        btn_register.setOnClickListener {
            // your code to perform when the user clicks on the button
            Toast.makeText(this,"Register" , Toast.LENGTH_SHORT).show();
        }

        appdb = Db(this)
    }

    fun btn_login(view: View){

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

                          val intent = Intent(this@MainActivity , AppBroadCast::class.java);
                          intent.putExtra("msg" , "Logger fire!");
                          intent.action = "com.example.jake21x.kotlinbasic";
                          val pendIntent = PendingIntent.getBroadcast(this@MainActivity ,0 , intent , PendingIntent.FLAG_UPDATE_CURRENT);

                          alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, 1000 , pendIntent);

//                        val intent = Intent(getContext().getApplicationContext(), AppLogger::class.java);
//                        val pintent = PendingIntent.getService(getContext().getApplicationContext(), 0, intent, 0);
//
//                        val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager;
//                         // Start every 30 seconds
//                        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + (1*1000), 1*1000, pintent)
//                        //webView.getContext().startService(new Intent(webView.getContext().getApplicationContext(), Logger.class));

                    }else{
                        alert {
                            title("Login Failed!");
                            message(response?.toString())
                            positiveButton("OK") { dismiss() }
                            cancellable(false)
                        }.show();
                    }

                }else{
                    alert {
                        title("Login Failed!");
                        message("Please check your account & data connection.")
                        positiveButton("OK") { dismiss() }
                        cancellable(false)
                    }.show();
                }

            },
                    Response.ErrorListener {
                        // error
                        dismiss()
                        alert {
                            title("Login Failed!");
                            message("Please check data connection.")
                            positiveButton("OK") { dismiss() }
                            cancellable(false)
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
        alert {
           title("Exit Application.");
           message("Are you sure you want to Exit?");
           positiveButton("Yes") {
                finish();
           }
           negativeButton("No") {  }
           cancellable(false);
        }.show();
    }

}