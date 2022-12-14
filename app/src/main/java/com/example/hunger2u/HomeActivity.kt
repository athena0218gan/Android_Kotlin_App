package com.example.hunger2u

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        // AppBar
        supportActionBar?.title = Html.fromHtml("<font color=\"black\">" + "Home" + "</font>");

        val ivSetting = findViewById<ImageView>(R.id.ivHSetting)
        val ivProfile = findViewById<ImageView>(R.id.ivHProfile)
        val donatebtn = findViewById<Button>(R.id.donatebtn)
        val donatebtn2 = findViewById<Button>(R.id.donatebtn2)
        donatebtn.setOnClickListener(){
            val intent = Intent(this, paymentActivity::class.java)
            startActivity(intent)
        }
        donatebtn2.setOnClickListener {
            val intent = Intent(this, paymentActivity::class.java)
            startActivity(intent)
        }
        ivSetting.setOnClickListener{
            startActivity(Intent(this, NavActivity::class.java));
        }
        ivProfile.setOnClickListener{
            startActivity(Intent(this, ProfileActivity::class.java));
        }


    }
}