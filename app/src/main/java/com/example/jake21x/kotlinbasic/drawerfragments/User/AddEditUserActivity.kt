package com.example.jake21x.kotlinbasic.drawerfragments.User

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.TextInputLayout
import android.support.v4.content.FileProvider
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.example.jake21x.kotlinbasic.R
import com.example.jake21x.kotlinbasic.realm.Users
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_add_edit_user.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.longToast
import org.jetbrains.anko.onClick
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddEditUserActivity : AppCompatActivity() {


    lateinit var input_name:EditText
    lateinit var input_email:EditText
    lateinit var input_contact:EditText
    lateinit var input_address:EditText
    lateinit var input_birthday:EditText
    lateinit var realm:Realm;

    val CAMERA_REQUEST_CODE = 0
    var imageFilePath: String?=null;
    var captured: File?=null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_user)

       input_name = findViewById<EditText>(R.id.input_name);
       input_email = findViewById<EditText>(R.id.input_email);
       input_contact = findViewById<EditText>(R.id.input_contact);
       input_address = findViewById<EditText>(R.id.input_address);
       input_birthday = findViewById<EditText>(R.id.input_birthday);
       val input_cover_birthday = findViewById<TextInputLayout>(R.id.input_cover_birthday);

        val config = RealmConfiguration.Builder().name("kotlinbasic").deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(config);

        setupToolBar();

        input_birthday.onClick {
            val c = Calendar.getInstance()
            val day = c.get(Calendar.DAY_OF_MONTH)
            val month = c.get(Calendar.MONTH)
            val year = c.get(Calendar.YEAR)

            val dpd = DatePickerDialog(this, android.R.style.Theme_Material_Dialog, DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                input_birthday.setText("${monthOfYear + 1}/${dayOfMonth}/${year}");
            }, year, month, day)

            //show datepicker
            dpd.show()
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
        toolbar.title = "Create User"
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


                realm.beginTransaction();
                val db = realm.createObject(Users::class.java, pk)

                db.id  = pk.toString();
                db.username  = input_name.text.toString();
                db.address  = input_address.text.toString();
                db.contact  = input_contact.text.toString();
                db.email  = input_email.text.toString();
                db.position  = "HomeBase Programmer";
                db.birthday  = input_birthday.text.toString();

                if(imageFilePath != null){
                    db.photo  = captured!!.absolutePath.toString() ;
                }else{
                    db.photo  = null;
                }

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
