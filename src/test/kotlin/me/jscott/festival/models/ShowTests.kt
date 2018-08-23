package me.jscott.festival.models

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDateTime

const val SHOW_NAME = "Show name"

class ShowTests {

    @Test
    fun testAddShowing() {
        val showing = Showing(LocalDateTime.now().minusHours(1), LocalDateTime.now())
        val show = Show(SHOW_NAME)
        assert(show.showings().isEmpty())

        show.addShowing(showing)
        assertEquals(showing, show.showings()[0])
    }
}