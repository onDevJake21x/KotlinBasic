package com.example.jake21x.kotlinbasic

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.longToast
import org.json.JSONObject
import com.android.volley.toolbox.StringRequest
import io.realm.Realm
import io.realm.RealmConfiguration
import org.jetbrains.anko.alert
import java.util.*

class MainActivity : AppCompatActivity() {

    private val URL = "http://188.166.233.193/api/login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database_init();

        // get reference to button
        val btn_register = findViewById<TextView>(R.id.link_signup);
        // set on-click listener
        btn_register.setOnClickListener {
            // your code to perform when the user clicks on the button
            Toast.makeText(this,"Register" , Toast.LENGTH_SHORT).show();
        }

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
                    intent.putExtra("username", username.text.toString())
                    intent.putExtra("password", password.text.toString())
                    intent.putExtra("data", JSONObject(response).toString())
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    dismiss();
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

    fun database_init(){
        Realm.init(this);
        val config = RealmConfiguration.Builder().name("kotlinbasic").deleteRealmIfMigrationNeeded().build();
        val realm = Realm.getInstance(config);
    }
}