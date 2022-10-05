package com.example.hunger2u

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Patterns
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.hunger2u.data.Event
import com.example.hunger2u.databinding.ActivityAddEventBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.model.Values.isNumber
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class AddEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEventBinding
    private var eventMethod: String? = "Physical"
    private lateinit var db: FirebaseFirestore

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseFirestore.getInstance()

        // AppBar
        supportActionBar?.title = Html.fromHtml("<font color=\"black\">" + "Add New Event" + "</font>");

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        binding.bAEEventStartDate.setOnClickListener{
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view, mYear, mMonth, mDay ->
                binding.bAEEventStartDate.setText("" + mDay + "/" + (mMonth + 1) + "/" + mYear)
            }, year, month, day)
            dpd.show()
        }

        binding.bAEEventEndDate.setOnClickListener{
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view, mYear, mMonth, mDay ->
                binding.bAEEventEndDate.setText("" + mDay + "/" + (mMonth + 1) + "/" + mYear)
            }, year, month, day)
            dpd.show()
        }

        binding.rgAEEventMethod.setOnCheckedChangeListener{ radioGroup, i ->
            var rb = findViewById<RadioButton>(i)
            if(rb != null){
                eventMethod = rb.text.toString()
            }
            if(rb.text.toString() == "Online"){
                binding.etAEEventVenue.visibility = View.GONE
            }else{
                binding.etAEEventVenue.visibility = View.VISIBLE
            }
        }

        binding.bAEAdd.setOnClickListener{
            validateAdd()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun validateAdd(){
        val sdf = SimpleDateFormat("dd/MM/yyyy")

        if(binding.etAEEventName.text.toString().isEmpty()){
            binding.etAEEventName.error = "Invalid Event Name"
            binding.etAEEventName.requestFocus()
            return
        }

        if(binding.etAEEventDesc.text.toString().isEmpty()){
            binding.etAEEventDesc.error = "Invalid Event Description"
            binding.etAEEventDesc.requestFocus()
            return
        }

        if(binding.bAEEventStartDate.text.toString() == "Pick Date"){
            Toast.makeText(this, "Invalid Start Date", Toast.LENGTH_SHORT).show()
            return
        }

        if(binding.bAEEventEndDate.text.toString() == "Pick Date"){
            Toast.makeText(this, "Invalid End Date", Toast.LENGTH_SHORT).show()
            return
        }

        if(sdf.parse(binding.bAEEventStartDate.text.toString()).after(sdf.parse(binding.bAEEventEndDate.text.toString()))){
            Toast.makeText(this, "Start Date must before End Date", Toast.LENGTH_SHORT).show()
            return
        }

        if(sdf.parse(binding.bAEEventEndDate.text.toString()).before(sdf.parse(binding.bAEEventStartDate.text.toString()))){
            Toast.makeText(this, "End Date must after Start Date", Toast.LENGTH_SHORT).show()
            return
        }

        if(binding.rbAEPhysical.isChecked && binding.etAEEventVenue.text.toString().isEmpty()){
            binding.etAEEventVenue.error = "Invalid Event Venue"
            binding.etAEEventVenue.requestFocus()
            return
        }

        if(binding.etAETarget.text.toString().isEmpty() || !isNumber(binding.etAETarget.text.toString()) || binding.etAETarget.text.toString().toInt() < 0){
            binding.etAETarget.error = "Invalid Event Target Amount"
            binding.etAETarget.requestFocus()
            return
        }

        if(binding.etAEPICName.text.toString().isEmpty()){
            binding.etAEPICName.error = "Invalid PIC Name"
            binding.etAEPICName.requestFocus()
            return
        }

        if(binding.etAEPICPhone.text.toString().isEmpty()){
            binding.etAEPICPhone.error = "Invalid PIC Phone"
            binding.etAEPICPhone.requestFocus()
            return
        }

        if(binding.etAEPICEmail.text.toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(binding.etAEPICEmail.text.toString()).matches()){
            binding.etAEPICEmail.error = "Invalid PIC Email"
            binding.etAEPICEmail.requestFocus()
            return
        }

        if(binding.etAEPICPosition.text.toString().isEmpty()){
            binding.etAEPICPosition.error = "Invalid PIC Phone"
            binding.etAEPICPosition.requestFocus()
            return
        }

        if(binding.etAEPICCompany.text.toString().isEmpty()){
            binding.etAEPICCompany.error = "Invalid PIC Phone"
            binding.etAEPICCompany.requestFocus()
            return
        }

        if(binding.etAERequesterEmail.text.toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(binding.etAERequesterEmail.text.toString()).matches()){
            binding.etAERequesterEmail.error = "Invalid PIC Email"
            binding.etAERequesterEmail.requestFocus()
            return
        }

        val eventL = Event(binding.etAEEventName.text.toString(),binding.etAEEventDesc.text.toString(),binding.bAEEventStartDate.text.toString(),
            binding.bAEEventEndDate.text.toString(), eventMethod, binding.etAEEventVenue.text.toString(), binding.etAETarget.text.toString().toLong(),
            binding.etAEPICName.text.toString(), binding.etAEPICPhone.text.toString(),binding.etAEPICEmail.text.toString(), binding.etAEPICPosition.text.toString(),
            binding.etAEPICCompany.text.toString(), binding.etAERequesterEmail.text.toString())

        db.collection("Event").document().set(eventL).addOnSuccessListener {
            Toast.makeText(this, "Add Event Successfully", Toast.LENGTH_SHORT).show()
            finish()
        }


    }

    fun isNumber(s: String?): Boolean {
        return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) }
    }
}