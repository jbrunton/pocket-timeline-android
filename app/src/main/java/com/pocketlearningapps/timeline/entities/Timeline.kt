package com.pocketlearningapps.timeline.entities

data class Timeline(
    val title: String,
    val description: String?,
    val events: List<Event>
)
