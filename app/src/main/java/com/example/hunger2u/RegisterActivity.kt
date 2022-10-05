package com.example.hunger2u

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.hunger2u.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        //AppBar
        supportActionBar?.title = Html.fromHtml("<font color=\"black\">" + getString(R.string.app_name) + "</font>")

        val bSignUp = findViewById<Button>(R.id.bRSignUp) as Button
        val etEmail = findViewById<EditText>(R.id.etREmail) as EditText
        val etPassword = findViewById<EditText>(R.id.etRPassword) as EditText
        val etConfirmPassword = findViewById<EditText>(R.id.etRConfirmPassword) as EditText
        val etUsername = findViewById<EditText>(R.id.etRUsername) as EditText
        val etICName = findViewById<EditText>(R.id.etRICName) as EditText
        val etICNumber = findViewById<EditText>(R.id.etRICNumber) as EditText
        val tvText1 = findViewById<TextView>(R.id.tvRText1)
        val tvLogin = findViewById<TextView>(R.id.tvRLogin) as TextView

        // Design
        val tempString = "Login"
        val spanString = SpannableString(tempString)
        spanString.setSpan(UnderlineSpan(), 0, spanString.length, 0)
        spanString.setSpan(StyleSpan(Typeface.BOLD), 0, spanString.length, 0)
        tvLogin.text = spanString

        val tempString2 = "Already have an account ?"
        val spanString2 = SpannableString(tempString2)
        spanString2.setSpan(StyleSpan(Typeface.BOLD), 0, spanString2.length, 0)
        tvText1.text = spanString2

        // OnClick
        bSignUp.setOnClickListener{
            validate(etEmail, etPassword, etConfirmPassword, etUsername, etICName, etICNumber)
        }

        tvLogin.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun validate(email: EditText, password:EditText, confirmPassword: EditText, username: EditText, ICName: EditText, ICNumber: EditText){
        if(ICName.text.toString().isEmpty()){
            ICName.error = "Invalid Username"
            ICName.requestFocus()
            return
        }

        if(ICNumber.text.toString().isEmpty()){
            ICNumber.error = "Invalid IC Number"
            ICNumber.requestFocus()
            return
        }

        if(ICNumber.text.toString().length != 10){
            ICNumber.error = "IC Number must has 10 numbers"
            ICNumber.requestFocus()
            return
        }

        if(username.text.toString().isEmpty()){
            username.error = "Invalid Username"
            username.requestFocus()
            return
        }


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

        if(password.text.toString().length < 6){
            password.error = "Must more than 6 characters"
            password.requestFocus()
            return
        }

        if(confirmPassword.text.toString().isEmpty() || confirmPassword.text.toString() != password.text.toString()){
            confirmPassword.error = "Must same with Password"
            confirmPassword.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener(this){
                task -> if(task.isSuccessful) {
            auth.currentUser?.sendEmailVerification()
                ?.addOnSuccessListener {
                    val userL = User(username.text.toString(), ICName.text.toString().uppercase(), ICNumber.text.toString(), email.text.toString(), 0)
                    db.collection("User").document(email.text.toString()).set(userL).addOnSuccessListener {
                        Toast.makeText(this, "Register Successfully. Please check your email for verification.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java));
                        finish()
                    }

                }
                ?.addOnFailureListener(){
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }
        }
        else{
            Toast.makeText(this, "Failed to Sign Up", Toast.LENGTH_SHORT).show()
        }
        }
    }
}