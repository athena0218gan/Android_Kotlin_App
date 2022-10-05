package com.example.hunger2u

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Patterns
import android.util.Patterns.EMAIL_ADDRESS
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.util.PatternsCompat.EMAIL_ADDRESS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        // AppBar
        supportActionBar?.title = Html.fromHtml("<font color=\"black\">" + getString(R.string.app_name) + "</font>");

        // Variable
        val etEmail = findViewById<EditText>(R.id.etMEmail) as EditText
        val etPassword = findViewById<EditText>(R.id.etMPassword) as EditText
        val tvForgetPassword = findViewById<TextView>(R.id.tvMForgetPassword) as TextView
        val tvText1 = findViewById<TextView>(R.id.tvMText1)
        val tvSignUp = findViewById<TextView>(R.id.tvMSignUp) as TextView
        val bLogin = findViewById<Button>(R.id.bMLogin) as Button

        // Design
        val tempString = "Register"
        val spanString = SpannableString(tempString)
        spanString.setSpan(UnderlineSpan(), 0, spanString.length, 0)
        spanString.setSpan(StyleSpan(Typeface.BOLD), 0, spanString.length, 0)
        tvSignUp.text = spanString

        val tempString2 = "Already have an account ?"
        val spanString2 = SpannableString(tempString2)
        spanString2.setSpan(StyleSpan(Typeface.BOLD), 0, spanString2.length, 0)
        tvText1.text = spanString2

        // OnClick
        tvForgetPassword.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.layout_edit_text, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.etLETText)

            with(builder){
                setTitle("Enter Your Email")
                setPositiveButton("Submit"){ dialog, which ->
                    sendForgetPasswordLink(editText)
                }
                setNegativeButton("Cancel"){dialog, which ->

                }
                setView(dialogLayout)
                show()
            }
        }

        tvSignUp.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        bLogin.setOnClickListener{
            if(etEmail.text.toString() == "admin" &&  etPassword.text.toString() == "123"){
                startActivity(Intent(this, HomeAdminActivity::class.java))
                finish()
            }else {
                validate(etEmail, etPassword)
            }
        }

    }

    // Function
    fun sendForgetPasswordLink(editText: EditText){
        if(editText.text.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(editText.text.toString()).matches()){
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()
            return
        }
        auth.sendPasswordResetEmail(editText.text.toString()).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(this, "Please check your email to reset password", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun validate(email: EditText, password: EditText){
        if(email.text.toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            email.error = "Invalid Email"
            email.requestFocus()
            return
        }

        if(password.text.toString().isEmpty()){
            password.error = "Invalid Password"
            password.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    updateUI(null)
                }
            }
            .addOnFailureListener{
                Toast.makeText(baseContext, "Authentication failed",
                    Toast.LENGTH_SHORT).show()
            }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?){
        if(currentUser != null){
            if(currentUser.isEmailVerified) {
                startActivity(Intent(this, HomeActivity::class.java))
            }else{
                Toast.makeText(baseContext, "Please check your email verification.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

}