package com.example.jake21x.kotlinbasic.drawerfragments.User

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import com.example.jake21x.kotlinbasic.R
import com.example.jake21x.kotlinbasic.realm.Users
import io.realm.Realm
import io.realm.RealmConfiguration
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast

class AddEditUserActivity : AppCompatActivity() {


    lateinit var input_name:EditText
    lateinit var input_email:EditText
    lateinit var input_contact:EditText
    lateinit var input_address:EditText
    lateinit var realm:Realm;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_user)

       input_name = findViewById<EditText>(R.id.input_name);
       input_email = findViewById<EditText>(R.id.input_email);
       input_contact = findViewById<EditText>(R.id.input_contact);
       input_address = findViewById<EditText>(R.id.input_address);

        val config = RealmConfiguration.Builder().name("kotlinbasic").deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(config);

        setupToolBar();
    }

    private fun setupToolBar() {
        // Set up the toolbar.
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab?.setHomeAsUpIndicator(R.drawable.ic_close)
        ab?.setDisplayHomeAsUpEnabled(true)
        toolbar.title = "Create User"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.users, menu)
        return true
    }

    override fun onBackPressed() {
        finish();
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_save -> {

                if( !input_name.text.toString().equals("") &&
                    !input_address.text.toString().equals("") &&
                    !input_contact.text.toString().equals("") &&
                    !input_email.text.toString().equals("")  ){

                    store_user(realm);

                }else{
                    alert {
                        title("Not Valid Input");
                        message("Sorry please enter a valid inputs only.");
                        positiveButton("OK") { dismiss() }
                    }.show();
                }


                return true
            }
            android.R.id.home -> {
                finish();
                return true
            }
            else -> return false
        }

    }


    fun store_user(realm:Realm){

        alert {

            title("Saving User..");
            message("Are you usre you want to save user?");
            positiveButton("Yes") {
                var pk: Long = 1
                if (realm.where(Users::class.java).max("pk") != null) {
                    pk = realm.where(Users::class.java).max("pk") as Long + 1
                }
                toast(pk.toString());

                realm.beginTransaction();
                val db = realm.createObject(Users::class.java, pk)

                db.id  = pk.toString();
                db.username  = input_name.text.toString();
                db.address  = input_address.text.toString();
                db.contact  = input_contact.text.toString();
                db.email  = input_email.text.toString();
                db.position  = "HomeBase Programmer";
                db.birthday  = "01/21/1995";
                db.photo  = "sample.png";
                realm.commitTransaction();
                inputClear();
                dismiss();
                setResult(1);
                overridePendingTransition(R.anim.stay,R.anim.push_down_from_up);
                finish();
            }
            negativeButton("No") {
                dismiss();
            }
            cancellable(false)
        }.show()


    }


    fun inputClear(){
        input_name.text.clear();
        input_email.text.clear();
        input_contact.text.clear();
        input_address.text.clear();
    }
}
