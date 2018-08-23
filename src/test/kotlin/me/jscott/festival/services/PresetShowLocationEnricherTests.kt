package me.jscott.festival.services

import junit.framework.Assert.assertEquals
import me.jscott.festival.models.Venue
import org.junit.Test

class PresetShowLocationEnricherTests {
    val venue = Venue("venue1", latitude="1", longitude="2")
    val venues = mapOf(venue.name to venue)
    val enricher = PresetShowLocationEnricher(venues)

    @Test
    fun testEnrichUnknown() {
        assertEquals(null, enricher.enrichLocation("Test"))
    }

    @Test
    fun testEnrichKnown() {
        assertEquals(GeoLocation(latitude=venue.latitude, longitude=venue.longitude), enricher.enrichLocation(venue.name))
    }
}