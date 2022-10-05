package com.example.hunger2u.data

import com.google.firebase.firestore.Exclude

data class User(
    var userName: String? = null,
    var userICName: String? = null,
    var userICNumber: String? = null,
    var userEmail: String? = null,
    var userMoney: Long? = null,

    //@get:Exclude var docId: String?= null,
)
