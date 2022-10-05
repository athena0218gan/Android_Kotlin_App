package com.example.hunger2u

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.example.hunger2u.databinding.ActivityEditProfileImageBinding
import com.example.hunger2u.databinding.ActivityNavBinding
import com.example.hunger2u.databinding.ActivityProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class NavActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // AppBar
        supportActionBar?.title = Html.fromHtml("<font color=\"black\">" + "Hunger2U" + "</font>");

        // OnClick
        binding.ivNBack.setOnClickListener{
            finish()
        }

        binding.tvNEditProfile.setOnClickListener{
            startActivity(Intent(this, EditProfileActivity::class.java));
        }

        binding.tvNLogout.setOnClickListener{
            Firebase.auth.signOut()
            val i = Intent(applicationContext, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.putExtra("EXIT", true)
            startActivity(i);
            finish()
        }
        val currentNightMode = Configuration.UI_MODE_NIGHT_MASK
        binding.btnDark.setOnClickListener{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
           when (currentNightMode) {
               Configuration.UI_MODE_NIGHT_NO -> {} // Night mode is not active, we're using the light theme
               Configuration.UI_MODE_NIGHT_YES -> {} // Night mode is active, we're using dark theme
           }
        }


    }
}