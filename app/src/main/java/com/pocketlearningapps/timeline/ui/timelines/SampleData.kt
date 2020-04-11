package com.pocketlearningapps.timeline.ui.timelines

import com.pocketlearningapps.timeline.entities.Event
import com.pocketlearningapps.timeline.entities.Timeline
import java.time.LocalDate

val sampleTimeline = Timeline(
    id = "1",
    title = "World War 2",
    description = "Events of World War 2",
    events = listOf(
        Event("Germany invades Poland", LocalDate.of(1939, 9, 1)),
        Event("Britain goes to war", LocalDate.of(1939, 9, 3)),
        Event("Pearl Harbor", LocalDate.of(1941, 12, 7))
    )
)
