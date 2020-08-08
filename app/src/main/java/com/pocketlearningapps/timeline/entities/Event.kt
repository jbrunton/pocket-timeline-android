package com.pocketlearningapps.timeline.entities

import java.time.LocalDate

data class Event(
    val id: String,
    val title: String,
    val date: LocalDate
)
