package me.jscott.festival


import com.nhaarman.mockito_kotlin.mock
import junit.framework.TestCase.assertEquals
import me.jscott.festival.models.SHOW_NAME
import me.jscott.festival.models.Show
import me.jscott.festival.models.Showing
import org.junit.Test
import java.time.LocalDateTime

class SessionTests {

    @Test
    fun testAddShow() {
        val show = Show(SHOW_NAME)
        show.addShowing(Showing(LocalDateTime.now().minusHours(1), LocalDateTime.now()))

        val session = Session(mock())

        assert(session.shows().isEmpty())

        session.addShow(show)
        assertEquals(show, session.shows()[0])
    }
}