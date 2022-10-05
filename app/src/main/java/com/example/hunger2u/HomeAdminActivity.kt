package com.example.hunger2u

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hunger2u.adapter.EventAdapter
import com.example.hunger2u.data.Event
import com.example.hunger2u.databinding.ActivityHomeAdminBinding
import com.example.hunger2u.databinding.ActivityProfileBinding
import com.google.firebase.firestore.*

class HomeAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeAdminBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventArrayList : ArrayList<Event>
    private lateinit var myAdapter: EventAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // AppBar
        supportActionBar?.title = Html.fromHtml("<font color=\"black\">" + "Home Admin" + "</font>");

        recyclerView = findViewById(R.id.rvHAEvent)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        eventArrayList = arrayListOf()
        myAdapter = EventAdapter(eventArrayList)
        recyclerView.adapter = myAdapter
        EventChangeListener()

        binding.ivHAAdd.setOnClickListener{
            startActivity(Intent(this, AddEventActivity::class.java))
        }
    }

    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
            db.collection("Event")
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if (error != null) {
                            return
                        }

                        for (dc: DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                eventArrayList.add(dc.document.toObject(Event::class.java))
                            }
                        }

                        myAdapter.notifyDataSetChanged()
                    }
                })
    }
}