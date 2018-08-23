package me.jscott.festival.tools

import com.beust.klaxon.Klaxon
import me.jscott.festival.Session
import me.jscott.festival.models.Show
import me.jscott.festival.models.Showing
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class EdinburghFringeSessionShowingTime(val start_time: String, val end_time:String)

data class EdinburghFringeSessionShowing(val start_date: String, val end_date:String, val times: Array<EdinburghFringeSessionShowingTime>) {
    fun asShowings(): ArrayList<Showing> {
        val showings = arrayListOf<Showing>()

        val start_date = LocalDate.parse(start_date)
        val end_date = LocalDate.parse(end_date)

        if (start_date.isAfter(end_date)) {
            throw IllegalArgumentException("Start date cannot be after end date")
        }

        var date = start_date
        while (date <= end_date) {
            times.forEach {
                val time_end_date = if (LocalTime.parse(it.end_time).isBefore(LocalTime.parse(it.start_time))) date.plusDays(1) else date
                showings.add(Showing(LocalDateTime.parse("${date}T${it.start_time}"), LocalDateTime.parse("${time_end_date}T${it.end_time}"))) }
            date = date.plusDays(1)
        }

        return showings
    }
}

data class EdinburghFringeSessionShow(val title: String, val description: String? = null, val url: String? = null, val location: String? = null, val longitude: String? = null, val latitude: String? = null, val showings: ArrayList<EdinburghFringeSessionShowing> = arrayListOf()) {
    fun asShow(): Show = Show(title, description=description, location=location, url=url, longitude=longitude, latitude=latitude, showings=ArrayList(showings.flatMap { it.asShowings() }))
}

/**
 * Take a simplified edfringe style session file and produce a complete session file
 * args[0] should be an input filename
 * args[1] should be an output filename
 */
fun main(args: Array<String>) {
    val inputFilename = args[0]
    val outputFilename = args[1]

    val session = Session(shows =
        ArrayList(Klaxon().parseArray<EdinburghFringeSessionShow>(File(inputFilename).toURL().readText())!!.map { it.asShow() }))

    session.writeToFile(File(outputFilename))
}