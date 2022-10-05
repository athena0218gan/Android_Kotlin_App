package com.example.hunger2u

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.hunger2u.data.User
import com.example.hunger2u.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

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
            binding.ivEPImage.setImageBitmap(bitmap)
        }

        val docUser = db.collection("User").whereEqualTo("userEmail", auth.currentUser?.email.toString())
        docUser.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    binding.tvEPText1.text = document.getString("userName")
                    if (progressDialog.isShowing) progressDialog.dismiss()
                }
            }

        // AppBar
        supportActionBar?.title = Html.fromHtml("<font color=\"black\">" + "Hunger2U" + "</font>");

        // OnClick
        binding.ivEPBack.setOnClickListener{
            finish()
        }
        binding.tvEPImage.setOnClickListener{
            startActivity(Intent(this, EditProfileImageActivity::class.java));
            finish()
        }

        binding.tvEPUsername.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.layout_edit_text, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.etLETText)

            with(builder){
                setTitle("Enter New Username")
                setPositiveButton("Confirm"){ dialog, which ->
                    validateUsername(editText)
                }
                setNegativeButton("Cancel"){dialog, which ->

                }
                setView(dialogLayout)
                show()
            }
        }

        binding.tvEPPassword.setOnClickListener{
            startActivity(Intent(this, EditPasswordActivity::class.java));
        }
    }

    // Function
    fun validateUsername(editText: EditText){
        if(editText.text.toString().isEmpty()){
            Toast.makeText(this, "Invalid Username", Toast.LENGTH_SHORT).show()
            return
        }

        val docUser = db.collection("User").whereEqualTo("userEmail", auth.currentUser?.email.toString())
        docUser.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val email = document.getString("userEmail")
                    val ICName = document.getString("userICName")
                    val ICNumber = document.getString("userICNumber")
                    val Money = document.getLong("userMoney")

                    val userL = User(editText.text.toString(), ICName, ICNumber, email, Money)
                    db.collection("User").document(auth.currentUser?.email.toString()).set(userL).addOnSuccessListener {
                        binding.tvEPText1.text = editText.text.toString()
                        Toast.makeText(this, "Username Updated Successfully", Toast.LENGTH_SHORT).show()
                    }
                }
            }

    }

}