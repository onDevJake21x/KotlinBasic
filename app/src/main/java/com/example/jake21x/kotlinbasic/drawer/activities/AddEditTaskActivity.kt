package com.example.jake21x.kotlinbasic.drawer.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.*
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.jake21x.kotlinbasic.Db
import com.example.jake21x.kotlinbasic.R
import com.example.jake21x.kotlinbasic.model.Tasks
import com.example.jake21x.kotlinbasic.services.GPSTracker
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.activity_add_edit_task.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddEditTaskActivity  : AppCompatActivity() {


    lateinit var input_client:EditText
    lateinit var input_date:EditText
    lateinit var input_time:EditText
    lateinit var input_remarks:EditText
    lateinit var layout_second_option:LinearLayout

    lateinit var txt_location:TextView

    val CAMERA_REQUEST_CODE = 0
    var imageFilePath: String?=null;
    var captured: File?=null;
    var set_starttime: String?=null;
    var set_endtime: String?=null;

    var DbStore: Db? = null
    var gps:GPSTracker?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_task)

        input_client = findViewById<EditText>(R.id.input_client);
        input_date = findViewById<EditText>(R.id.input_date);
        input_time = findViewById<EditText>(R.id.input_time);
        input_remarks = findViewById<EditText>(R.id.input_remarks);

        layout_second_option = findViewById<LinearLayout>(R.id.layout_second_option);
        layout_second_option.visibility = View.GONE

        txt_location = findViewById<TextView>(R.id.txt_location);

        DbStore = Db.Instance(this);
        DbStore!!.writableDatabase
        gps = GPSTracker(this);



        setupToolBar();

        input_date.setText(SimpleDateFormat("MM/dd/yyyy").format(Date()).toString());
        input_time.setText(SimpleDateFormat("hh:mm a").format(Date()).toString());


        //TODO this part is need to check and get first the values for editing ..
        if(set_starttime == null){
            // new
            set_starttime = SimpleDateFormat("yyyy/MM/dd HH:mm").format(Date()).toString();
            set_endtime = "";
        }else{
            // edit starttime and endtime must preserve
             //for now if edit ..
            // we need to call set_endtime = value from db .
            set_endtime = SimpleDateFormat("yyyy/MM/dd HH:mm").format(Date()).toString();
        }

        input_date.onClick {
            val c = Calendar.getInstance()
            val day = c.get(Calendar.DAY_OF_MONTH)
            val month = c.get(Calendar.MONTH)
            val year = c.get(Calendar.YEAR)

            val dpd = DatePickerDialog(this, android.R.style.Theme_Material_Dialog, DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                input_date.setText("${monthOfYear + 1}/${dayOfMonth}/${year}");
            }, year, month, day)

            //show datepicker
            dpd.show()

        }


        input_time.onClick {
            val hh = SimpleDateFormat("hh")
            val mm = SimpleDateFormat("mm")

            val dpt = TimePickerDialog(this, android.R.style.Theme_Material_Dialog, TimePickerDialog.OnTimeSetListener{ timePicker, hh, mm ->


                val time_now = "${hh}:${mm}"
                val time = time_now
                val sdf = SimpleDateFormat("HH:mm")
                val dateObj = sdf.parse(time)
                System.out.println(dateObj)
                println(SimpleDateFormat("hh:mm a").format(dateObj))

                input_time.setText(SimpleDateFormat("hh:mm a").format(dateObj));

            },hh.format(Date()).toInt(),mm.format(Date()).toInt(), false)
            dpt.show();
        }


        retryButton.setOnClickListener {
            try {
                val imageFile = createImageFile()
                val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if(callCameraIntent.resolveActivity(packageManager) != null) {
                    val authorities = packageName + ".fileprovider"
                    val imageUri = FileProvider.getUriForFile(this, authorities, imageFile)

                    callCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

                    startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
                }
            } catch (e: IOException) {
                Toast.makeText(this, "Could not create file!", Toast.LENGTH_SHORT).show()
            }
        }



        stablis_location()

        retryButton.onClick {
            stablis_location()
            toast("Retrying ...")
        }


    }


    fun stablis_location(){
        if(gps!!.canGetLocation){
            txt_location.setText("Location: ${gps!!.longitude},${gps!!.latitude}")
            linear_hasNoLocation.visibility = View.GONE;
            cameraButton.visibility = View.GONE;

        }else{
            gps!!.showSettingsAlert();
            txt_location.setText("Cannot stablish location!");
            linear_hasNoLocation.visibility = View.VISIBLE;
            cameraButton.visibility = View.VISIBLE;
        }
    }


    // Callbacks for Nearby Connection
    private inner class ConnectionCallbacks : GoogleApiClient.ConnectionCallbacks {
        override fun onConnected(p0: Bundle?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onConnectionSuspended(p0: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    private fun setupToolBar() {
        // Set up the toolbar.
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab?.setHomeAsUpIndicator(R.drawable.ic_close)
        ab?.setDisplayHomeAsUpEnabled(true)
        toolbar.title = "Create Itinerary"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                /*if(resultCode == Activity.RESULT_OK && data != null) {
                    photoImageView.setImageBitmap(data.extras.get("data") as Bitmap)
                }*/
                if (resultCode == Activity.RESULT_OK) {
                    tasks_item_photoImageView.setImageBitmap(setScaledBitmap())
                }
            }
            else -> {
                Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName: String = "JPEG_" + timeStamp + "_"
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if(!storageDir.exists()) storageDir.mkdirs()
        val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
        imageFilePath = imageFile.absolutePath
        captured = imageFile
        return imageFile
    }

    fun setScaledBitmap(): Bitmap {
        val imageViewWidth = tasks_item_photoImageView.width
        val imageViewHeight = tasks_item_photoImageView.height

        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFilePath, bmOptions)
        val bitmapWidth = bmOptions.outWidth
        val bitmapHeight = bmOptions.outHeight

        val scaleFactor = Math.min(bitmapWidth/imageViewWidth, bitmapHeight/imageViewHeight)

        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor;
        bmOptions

        return  BitmapFactory.decodeFile(imageFilePath, bmOptions)

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

                if( !input_client.text.toString().equals("") &&
                    !input_remarks.text.toString().equals("")  ){

                    store_user();
                    toast( input_client.text.toString() + " , "+input_remarks.text.toString() )

                }else{
                    alert(title = "Not Valid Input" , message = "Sorry please enter a valid inputs only.") {
                        positiveButton("OK") {
                          dismiss()
                        }
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


    fun store_user(){

        alert {

            title("Saving User..");
            message("Are you usre you want to save Itinerary?");
            positiveButton("Yes") {

                // save now
                val timeFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
                var use_end_time = "";
                if(set_endtime == ""){
                    use_end_time  = timeFormat.format(Date()).toString();
                }else{
                    use_end_time  = set_endtime!!.toString();
                }

//                longToast(
//                        DbStore!!.getSession(DbStore!!)[0].user_id.toString() + ","+
//                                input_remarks.text.toString() + ","+
//                                "0.0" + ","+
//                                "0.0" + ","+
//                                input_date.text.toString() + ","+
//                                input_time.text.toString() + ","+
//                                set_starttime.toString() + ","+
//                                use_end_time.toString() + ","+
//                                input_client.text.toString()
//                )



                DbStore!!.use {
                  insert(Tasks.TABLE_NAME,
                          Tasks.user_id to DbStore!!.getSession(DbStore!!)[0].user_id.toString(),
                          Tasks.remarks to input_remarks.text.toString(),
                          Tasks.long to "0.0",
                          Tasks.lat to "0.0",
                          Tasks.date to input_date.text.toString(),
                          Tasks.time to input_time.text.toString(),
                          Tasks.starttime to set_starttime.toString(),
                          Tasks.endtime to use_end_time.toString(),
                          Tasks.client to input_client.text.toString()
                          )
                }

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
        input_client.text.clear();
        input_remarks.text.clear();
    }



    companion object {
        var getLocation:Location?=null;
    }
}
