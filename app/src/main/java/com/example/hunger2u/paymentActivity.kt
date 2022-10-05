package com.example.hunger2u

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast

class paymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val paybtn = findViewById<Button>(R.id.paybtn)
        paybtn.setOnClickListener {
            Toast.makeText(this, "Payment Successfully", Toast.LENGTH_SHORT).show()


        }

        val editTextDate  = findViewById<EditText>(R.id.editTextDate)
        val editTextTextPersonNameUtf8 = findViewById<EditText>(R.id.editTextTextPersonName8)
        val editTextNumber2 = findViewById<EditText>(R.id.editTextNumber2)
        val editTextTextPersonName10 = findViewById<EditText>(R.id.editTextTextPersonName10)
        val btnReset = findViewById<Button>(R.id.btnReset)
        btnReset.setOnClickListener {
            editTextDate.text.clear()
            editTextNumber2.text.clear()
            editTextTextPersonName10.text.clear()
            editTextTextPersonNameUtf8.text.clear()
        }

    }




}



