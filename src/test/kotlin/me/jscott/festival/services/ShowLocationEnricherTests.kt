package me.jscott.festival.services

import junit.framework.TestCase.assertEquals
import me.jscott.festival.models.Show
import me.jscott.festival.models.Showing
import org.junit.Test
import java.time.LocalDateTime

class ShowLocationEnricherTests {

    object TestShowLocationEnricher : ShowLocationEnricher {
        override fun enrichLocation(location: String): GeoLocation? {
            if (location == "null") {
                return null
            }
            return GeoLocation(latitude="1", longitude="2")
        }

    }

    @Test
    fun testEnrichWithLatitudeAndLongitude() {
        val show = Show("title", description="description", url="url", latitude="1", longitude="2")
        assertEquals(show, TestShowLocationEnricher.enrichLocation(show))
    }

    @Test
    fun testEnrichWithoutLocation() {
        val show = Show("title", description="description", url="url")
        assertEquals(show, TestShowLocationEnricher.enrichLocation(show))
    }

    @Test
    fun testEnrichCannotLookup() {
        val show = Show("title", description="description", url="url", location="null")
        assertEquals(show, TestShowLocationEnricher.enrichLocation(show))
    }

    @Test
    fun testEnrich() {
        val show = Show("title", description="description", url="url", location="location")
        assertEquals(Show("title", description="description", url="url", location="location", latitude="1", longitude="2"), TestShowLocationEnricher.enrichLocation(show))
    }

    @Test
    fun testEnrichShowings() {
        val show = Show("title", location="location")
        show.addShowing(Showing(LocalDateTime.now().minusHours(1), LocalDateTime.now()))
        val enrichedShow = TestShowLocationEnricher.enrichLocation(show)

        assertEquals(show.showings(), enrichedShow.showings())
    }
}