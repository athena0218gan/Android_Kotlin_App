package com.example.hunger2u

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import com.example.hunger2u.databinding.ActivityEditPasswordBinding
import com.example.hunger2u.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class EditPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        // AppBar
        supportActionBar?.title = Html.fromHtml("<font color=\"black\">" + "Edit Password" + "</font>");

        binding.bEPChangePassword.setOnClickListener{
            validatePassword()
        }
    }

    // Function
    fun validatePassword(){
        if(binding.etEPCurrentPassword.text.toString().isEmpty()){
            binding.etEPCurrentPassword.error = "Invalid Current Password"
            binding.etEPCurrentPassword.requestFocus()
            return
        }

        if(binding.etEPPassword.text.toString().isEmpty()){
            binding.etEPPassword.error = "Invalid Password"
            binding.etEPPassword.requestFocus()
            return
        }
        if(binding.etEPPassword.text.toString().length < 6){
            binding.etEPPassword.error = "Must more than 6 characters"
            binding.etEPPassword.requestFocus()
            return
        }
        if(binding.etEPConfirmPassword.text.toString() != binding.etEPPassword.text.toString()){
            binding.etEPConfirmPassword.error = "Must same with Password"
            binding.etEPConfirmPassword.requestFocus()
            return
        }

        auth.currentUser.let {
            val userCrendential = EmailAuthProvider.getCredential(it?.email!!, binding.etEPCurrentPassword.text.toString())
            it.reauthenticate(userCrendential).addOnCompleteListener() { task ->
                when{
                    task.isSuccessful -> {
                        auth.currentUser.let {
                            auth.currentUser?.updatePassword(binding.etEPPassword.text.toString())?.addOnCompleteListener{
                                if(it.isSuccessful){
                                    Toast.makeText(this, "Update Password Successfully", Toast.LENGTH_SHORT).show()
                                    auth.signOut()
                                    val i = Intent(applicationContext, MainActivity::class.java)
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    i.putExtra("EXIT", true)
                                    startActivity(i);
                                    finish()
                                }else{
                                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                    task.exception is FirebaseAuthInvalidCredentialsException -> {
                        binding.etEPCurrentPassword.error = "Wrong Current Password"
                        binding.etEPCurrentPassword.requestFocus()
                    }

                    else -> {
                        Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}