package me.jscott.festival.tools

import junit.framework.TestCase.assertEquals
import me.jscott.festival.Session
import me.jscott.festival.models.Show
import me.jscott.festival.models.Showing
import org.junit.Test
import java.time.LocalDateTime

class DatetimeFilterTests {

    val showFull = Show("show name")

    init {
        showFull.addShowing(Showing(LocalDateTime.of(2018, 1, 1, 15, 30), LocalDateTime.of(2018, 1, 1, 16, 30)))
        showFull.addShowing(Showing(LocalDateTime.of(2018, 1, 2, 15, 30), LocalDateTime.of(2018, 1, 2, 16, 30)))
        showFull.addShowing(Showing(LocalDateTime.of(2018, 1, 3, 15, 30), LocalDateTime.of(2018, 1, 3, 16, 30)))
        showFull.addShowing(Showing(LocalDateTime.of(2018, 1, 4, 15, 30), LocalDateTime.of(2018, 1, 4, 16, 30)))
        showFull.addShowing(Showing(LocalDateTime.of(2018, 1, 5, 15, 30), LocalDateTime.of(2018, 1, 5, 16, 30)))
    }

    @Test
    fun testFilter() {
        val session = Session()

        session.addShow(showFull)

        val earliestStart = LocalDateTime.of(2018, 1, 1, 0, 0)
        val latestEnd = LocalDateTime.of(2018, 1, 3, 0, 0)

        val showFiltered = Show("show name")

        showFiltered.addShowing(Showing(LocalDateTime.of(2018, 1, 1, 15, 30), LocalDateTime.of(2018, 1, 1, 16, 30)))
        showFiltered.addShowing(Showing(LocalDateTime.of(2018, 1, 2, 15, 30), LocalDateTime.of(2018, 1, 2, 16, 30)))

        val new_session = filter(session, earliestStart, latestEnd)
        assertEquals(showFiltered, new_session.shows()[0])
    }

    @Test
    fun testFilterBoundaries() {
        val session = Session()

        session.addShow(showFull)

        val earliestStart = LocalDateTime.of(2018, 1, 1, 15, 30)
        val latestEnd = LocalDateTime.of(2018, 1, 2, 16, 30)

        val showFiltered = Show("show name")

        showFiltered.addShowing(Showing(LocalDateTime.of(2018, 1, 1, 15, 30), LocalDateTime.of(2018, 1, 1, 16, 30)))
        showFiltered.addShowing(Showing(LocalDateTime.of(2018, 1, 2, 15, 30), LocalDateTime.of(2018, 1, 2, 16, 30)))

        val new_session = filter(session, earliestStart, latestEnd)
        assertEquals(showFiltered, new_session.shows()[0])
    }
}