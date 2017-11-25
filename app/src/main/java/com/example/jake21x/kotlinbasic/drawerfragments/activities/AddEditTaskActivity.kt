package com.example.jake21x.kotlinbasic.drawerfragments.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import com.example.jake21x.kotlinbasic.R
import com.example.jake21x.kotlinbasic.realm.Tasks
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_add_edit_task.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.onClick
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddEditTaskActivity : AppCompatActivity() {


    lateinit var input_client:EditText
    lateinit var input_date:EditText
    lateinit var input_time:EditText
    lateinit var input_remarks:EditText
    lateinit var realm:Realm;

    val CAMERA_REQUEST_CODE = 0
    var imageFilePath: String?=null;
    var captured: File?=null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_task)

       input_client = findViewById<EditText>(R.id.input_client);
       input_date = findViewById<EditText>(R.id.input_date);
       input_time = findViewById<EditText>(R.id.input_time);
        input_remarks = findViewById<EditText>(R.id.input_remarks);


        val config = RealmConfiguration.Builder().name("kotlinbasic").deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(config);

        setupToolBar();


        val date = Date()
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        input_date.setText(dateFormat.format(date).toString());


        val timeFormat = SimpleDateFormat("hh:mm a")
        input_time.setText(timeFormat.format(date).toString());

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
                input_time.setText(""+hh+":"+mm);
            },hh.format(date).toInt(),mm.format(date).toInt(), false)
            dpt.show();
        }


        cameraButton.setOnClickListener {
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
/*                if(resultCode == Activity.RESULT_OK && data != null) {
                    photoImageView.setImageBitmap(data.extras.get("data") as Bitmap)
                }*/
                if (resultCode == Activity.RESULT_OK) {

                    item_photoImageView.setImageBitmap(setScaledBitmap())
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
        val imageViewWidth = item_photoImageView.width
        val imageViewHeight = item_photoImageView.height

        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFilePath, bmOptions)
        val bitmapWidth = bmOptions.outWidth
        val bitmapHeight = bmOptions.outHeight

        val scaleFactor = Math.min(bitmapWidth/imageViewWidth, bitmapHeight/imageViewHeight)

        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor;
        bmOptions

        return  transform(BitmapFactory.decodeFile(imageFilePath, bmOptions))

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
            message("Are you usre you want to save Itinerary?");
            positiveButton("Yes") {
                var pk: Long = 1
                if (realm.where(Tasks::class.java).max("pk") != null) {
                    pk = realm.where(Tasks::class.java).max("pk") as Long + 1
                }


                realm.beginTransaction();
                val db = realm.createObject(Tasks::class.java, pk)

                db.id  = pk.toString();

                db.client  = input_client.text.toString();
                db.remarks  = input_remarks.text.toString();
                db.date  = input_date.text.toString();
                db.time  = input_time.text.toString();

                db.tasktype  = "none";

                val timeFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
                db.starttime  = timeFormat.format(Date()).toString();
                db.endtime  = timeFormat.format(Date()).toString();

                db.long  = "000";
                db.lat  = "000";

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
        input_client.text.clear();
        input_remarks.text.clear();
    }
}
