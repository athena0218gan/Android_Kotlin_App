package com.example.hunger2u

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hunger2u.databinding.ActivityEditProfileImageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class EditProfileImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileImageBinding
    lateinit var ImageUri : Uri
    private lateinit var auth: FirebaseAuth
    var ImageChange = "null"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        // AppBar
        supportActionBar?.title = Html.fromHtml("<font color=\"black\">" + "Edit Profile Image" + "</font>");

        // SetUp
        val email = auth.currentUser?.email.toString()
        val storageRef = FirebaseStorage.getInstance().reference.child("User/$email")
        val localFile = File.createTempFile("tempImage","jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.ivEPIImage.setImageBitmap(bitmap)
        }

        binding.bEPIUpload.setOnClickListener{
            uploadImage()
        }

        binding.bEPIRemove.setOnClickListener{
            ImageChange = "true"
            binding.ivEPIImage.setImageResource(R.drawable.ic_image);
        }

        binding.bEPISave.setOnClickListener{
            saveImage()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, EditProfileActivity::class.java));
        finish()
    }
    fun saveImage(){
        if(ImageChange == "false" ) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Uploading Image...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            val email = auth.currentUser?.email.toString()
            val storageRef = FirebaseStorage.getInstance().getReference("User/$email")
            storageRef.putFile(ImageUri)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully Uploaded", Toast.LENGTH_SHORT).show()
                    if (progressDialog.isShowing) progressDialog.dismiss()
                }
                .addOnFailureListener {
                    binding.ivEPIImage.setImageURI(null)
                    Toast.makeText(this, "Fail to Upload", Toast.LENGTH_SHORT).show()
                    if (progressDialog.isShowing) progressDialog.dismiss()
                }
        }else if(ImageChange == "true"){
            val email = auth.currentUser?.email.toString()
            val storageRef = FirebaseStorage.getInstance().getReference("User/$email")
            storageRef.delete().addOnSuccessListener {
                Toast.makeText(this, "Successfully Deleted", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "Nothing Change. Please upload/remove image", Toast.LENGTH_SHORT).show()
        }
    }

    fun uploadImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == RESULT_OK){
            ImageUri = data?.data!!
            binding.ivEPIImage.setImageURI(ImageUri)
            ImageChange = "false"
        }
    }
}