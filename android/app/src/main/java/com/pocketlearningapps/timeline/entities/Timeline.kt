package com.pocketlearningapps.timeline.entities

data class Timeline(
    val id: String,
    val title: String,
    val description: String?,
    val events: List<Event>,
    val categories: List<Category>?
)
