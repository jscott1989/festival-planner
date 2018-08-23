package me.jscott.festival.planners

import me.jscott.festival.Session
import me.jscott.festival.models.Booking
import me.jscott.festival.models.Plan
import me.jscott.festival.models.Showing
import me.jscott.festival.services.EdinburghFringeShowLocationEnricher
import me.jscott.festival.services.PlanCalendarExporter
import org.apache.tomcat.jni.Local
import java.io.File
import java.time.LocalDateTime
import java.util.*

const val START_PADDING_MINUTES = 5L
const val END_PADDING_MINUTES = 5L

fun timesOverlap(s1: LocalDateTime, e1: LocalDateTime, s2: LocalDateTime, e2: LocalDateTime) =
        e1 >= s2 && e2 >= s1

abstract class GreedyPlanner : Planner {
    override fun plan(session: Session): Plan {
        var bookings = ArrayList<Booking>()
        val queue = PriorityQueue<Booking> { b1: Booking, b2: Booking -> compare(b1, b2)}

        session.shows.forEach {show ->
            show.showings.forEach { showing ->
                queue.add(Booking(show, showing))
            }
        }

        while (queue.isNotEmpty()) {
            val nextBooking = queue.poll()
            bookings.add(nextBooking)
            queue.removeIf { it.show == nextBooking.show || timesOverlap(it.showing.start.minusMinutes(START_PADDING_MINUTES), it.showing.end.plusMinutes(END_PADDING_MINUTES), nextBooking.showing.start.minusMinutes(START_PADDING_MINUTES), nextBooking.showing.end.plusMinutes(END_PADDING_MINUTES)) }
        }

        return Plan(bookings)
    }

    abstract fun compare(b1: Booking, b2: Booking): Int
}

class GreedyTimeBasedPlanner : GreedyPlanner() {
    override fun compare(b1: Booking, b2: Booking): Int =
        b1.showing.end.compareTo(b2.showing.end)

}

/**
 * Create a random plan and output the calendar
 *
 * args[0] should be an input session filename
 * args[1] should be an output calendar filename
 */
fun main(args: Array<String>) {
    val inputFilename = args[0]
    val outputFilename = args[1]
    val session = Session.fromURL(File(inputFilename).toURL(), EdinburghFringeShowLocationEnricher("/venues.json"))
    val plan = GreedyTimeBasedPlanner().plan(session)
    PlanCalendarExporter().export(File(outputFilename), plan)
}