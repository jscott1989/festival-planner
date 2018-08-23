package me.jscott.festival.tools

import me.jscott.festival.Session
import me.jscott.festival.models.Show
import java.io.File
import java.time.LocalDateTime

fun filter(session: Session, earliestStart: LocalDateTime, latestEnd: LocalDateTime): Session {
    val new_session = Session()
    session.shows().forEach {show ->
        val showings = show.showings().filter { !earliestStart.isAfter(it.start) && !latestEnd.isBefore(it.end) }
        if (!showings.isEmpty()) {
            val new_show = Show(
                    title=show.title,
                    description=show.description,
                    location=show.location,
                    url=show.url,
                    longitude=show.longitude,
                    latitude=show.latitude)
            showings.forEach { new_show.addShowing(it) }
            new_session.addShow(new_show)
        }
    }
    return new_session
}

/**
 * Take a complete session file and produce a new one only including showings within the specified
 * range.
 *
 * args[0] should be an input filename
 * args[1] should be an output filename
 * args[2] should be the earliest valid start time
 * args[3] should be the latest valid end time
 */
fun main(args: Array<String>) {
    val inputFilename = args[0]
    val outputFilename = args[1]
    val earliestStart = LocalDateTime.parse(args[2])
    val latestEnd = LocalDateTime.parse(args[3])

    val session = Session.fromURL(File(inputFilename).toURL())

    filter(session, earliestStart, latestEnd).writeToFile(File(outputFilename))
}