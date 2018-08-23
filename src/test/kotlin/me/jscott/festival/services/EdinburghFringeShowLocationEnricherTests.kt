package me.jscott.festival.services

import junit.framework.TestCase.assertEquals
import org.junit.Test

class EdinburghFringeShowLocationEnricherTests {

    val enricher = EdinburghFringeShowLocationEnricher("/test_venues.json")

    @Test
    fun testEnrichLocation() {
        assertEquals(GeoLocation(latitude="55.946766", longitude="-3.198368"), enricher.enrichLocation("52 Canoes (Grassmarket)"))
    }
}