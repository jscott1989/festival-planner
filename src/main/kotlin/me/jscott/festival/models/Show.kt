package me.jscott.festival.models

import java.time.LocalDateTime

data class Show(val title: String, val description: String? = null, val url: String? = null, val location: String? = null, val longitude: String? = null, val latitude: String? = null, val showings: ArrayList<Showing> = arrayListOf()) {
    fun addShowing(showing: Showing) = showings.add(showing)
    fun showings() = ArrayList(showings)
}

data class Showing(val start: LocalDateTime, val end: LocalDateTime)