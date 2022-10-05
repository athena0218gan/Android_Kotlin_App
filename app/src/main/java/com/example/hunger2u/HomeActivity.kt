package com.example.hunger2u

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.ImageView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // AppBar
        supportActionBar?.title = Html.fromHtml("<font color=\"black\">" + "Home" + "</font>");

        val ivSetting = findViewById<ImageView>(R.id.ivHSetting)
        val ivProfile = findViewById<ImageView>(R.id.ivHProfile)

        ivSetting.setOnClickListener{
            startActivity(Intent(this, NavActivity::class.java));
        }
        ivProfile.setOnClickListener{
            startActivity(Intent(this, ProfileActivity::class.java));
        }
    }
}