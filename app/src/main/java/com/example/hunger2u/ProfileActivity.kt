package com.example.hunger2u

import android.app.ProgressDialog
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import com.example.hunger2u.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // AppBar
        supportActionBar?.title = Html.fromHtml("<font color=\"black\">" + "Profile" + "</font>");

        // SetUp
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val email = auth.currentUser?.email.toString()
        val storageRef = FirebaseStorage.getInstance().reference.child("User/$email")
        val localFile = File.createTempFile("tempImage","jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.ivPImage.setImageBitmap(bitmap)
        }

        val docUser = db.collection("User").whereEqualTo("userEmail", auth.currentUser?.email.toString())
        docUser.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    binding.tvPEmail.text = document.getString("userEmail")
                    binding.tvPUsername.text = document.getString("userName")
                    binding.tvPICName.text = document.getString("userICName")
                    binding.tvPICNumber.text = document.getString("userICNumber")
                    binding.tvPMoney.text = "Hunger2U E-Wallet: RM" + document.getLong("userMoney").toString()
                }
                if (progressDialog.isShowing) progressDialog.dismiss()
            }
            .addOnFailureListener{
                if (progressDialog.isShowing) progressDialog.dismiss()
            }
    }
}