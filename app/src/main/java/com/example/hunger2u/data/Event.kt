package com.example.hunger2u.data

data class Event(
    var eventName: String? = null,
    var eventDesc: String? = null,
    var eventStartDate: String? = null,
    var eventEndDate: String? = null,
    var eventMethod: String? = null,
    var eventVenue: String? = null,
    var eventTarget: Long? = null,
    var eventPICName: String? = null,
    var eventPICPhone: String? = null,
    var eventPICEmail: String? = null,
    var eventPICPosition: String? = null,
    var eventPICCompany: String? = null,
    var eventRequesterEmail: String? = null,
)
