package me.jscott.festival.services

import junit.framework.TestCase.assertEquals
import me.jscott.festival.models.Booking
import me.jscott.festival.models.Plan
import me.jscott.festival.models.Show
import me.jscott.festival.models.Showing
import org.junit.Test
import java.time.LocalDateTime

val showTitle1 = "Test Show"
val booking1start = LocalDateTime.of(2018, 1, 2, 3, 4)
val booking1end = LocalDateTime.of(2018, 1, 2, 4, 4)
val booking1 = Booking(Show(showTitle1), Showing(booking1start, booking1end))

val showTitle2 = "Test Show 2"
val booking2start = LocalDateTime.of(2019, 1, 2, 3, 4)
val booking2end = LocalDateTime.of(2019, 1, 2, 4, 4)
val booking2 = Booking(Show(showTitle2), Showing(booking2start, booking2end))

val url = "http://events.test"
val description = "A test description"
val location = "A location, Somewhere"
val latitude = "51.510160"
val longitude = "-0.130530"

class PlanCalendarExporterTests {

    val exporter = PlanCalendarExporter()

    @Test
    fun testEmptyCalendar() {
        val EMPTY_ICAL = """
            BEGIN:VCALENDAR
            VERSION:2.0
            CALSCALE:GREGORIAN
            METHOD:PUBLISH
            END:VCALENDAR
        """.trimIndent()
        assertEquals(EMPTY_ICAL, exporter.export(Plan(ArrayList())))
    }

    @Test
    fun testSingleEvent() {
        val SINGLE_EVENT_ICAL = """
            BEGIN:VCALENDAR
            VERSION:2.0
            CALSCALE:GREGORIAN
            METHOD:PUBLISH
            BEGIN:VEVENT
            SUMMARY:Test Show
            DTSTART:20180102T030400
            DTEND:20180102T040400
            END:VEVENT
            END:VCALENDAR
        """.trimIndent()
        assertEquals(SINGLE_EVENT_ICAL, exporter.export(Plan(listOf(booking1))))
    }

    @Test
    fun testMultipleEvents() {
        val MULTIPLE_EVENTS_ICAL = """
            BEGIN:VCALENDAR
            VERSION:2.0
            CALSCALE:GREGORIAN
            METHOD:PUBLISH
            BEGIN:VEVENT
            SUMMARY:Test Show
            DTSTART:20180102T030400
            DTEND:20180102T040400
            END:VEVENT
            BEGIN:VEVENT
            SUMMARY:Test Show 2
            DTSTART:20190102T030400
            DTEND:20190102T040400
            END:VEVENT
            END:VCALENDAR
        """.trimIndent()
        assertEquals(MULTIPLE_EVENTS_ICAL, exporter.export(Plan(listOf(booking1, booking2))))
    }

    @Test
    fun testWithURL() {
        val bookingWithUrl = Booking(Show(showTitle1, url=url), Showing(booking1start, booking1end))

        val URL_ICAL = """
            BEGIN:VCALENDAR
            VERSION:2.0
            CALSCALE:GREGORIAN
            METHOD:PUBLISH
            BEGIN:VEVENT
            SUMMARY:Test Show
            DTSTART:20180102T030400
            DTEND:20180102T040400
            URL:http://events.test
            END:VEVENT
            END:VCALENDAR
        """.trimIndent()
        assertEquals(URL_ICAL, exporter.export(Plan(listOf(bookingWithUrl))))
    }

    @Test
    fun testWithDescription() {
        val bookingWithDescription = Booking(Show(showTitle1, description=description), Showing(booking1start, booking1end))

        val DESCRIPTION_ICAL = """
            BEGIN:VCALENDAR
            VERSION:2.0
            CALSCALE:GREGORIAN
            METHOD:PUBLISH
            BEGIN:VEVENT
            SUMMARY:Test Show
            DTSTART:20180102T030400
            DTEND:20180102T040400
            DESCRIPTION:A test description
            END:VEVENT
            END:VCALENDAR
        """.trimIndent()
        assertEquals(DESCRIPTION_ICAL, exporter.export(Plan(listOf(bookingWithDescription))))
    }

    @Test
    fun testWithLocation() {
        val bookingWithLocation = Booking(Show(showTitle1, location=location), Showing(booking1start, booking1end))

        val LOCATION_ICAL = """
            BEGIN:VCALENDAR
            VERSION:2.0
            CALSCALE:GREGORIAN
            METHOD:PUBLISH
            BEGIN:VEVENT
            SUMMARY:Test Show
            DTSTART:20180102T030400
            DTEND:20180102T040400
            LOCATION:A location, Somewhere
            END:VEVENT
            END:VCALENDAR
        """.trimIndent()
        assertEquals(LOCATION_ICAL, exporter.export(Plan(listOf(bookingWithLocation))))
    }

    @Test
    fun testWithGeo() {
        val bookingWithGeo = Booking(Show(showTitle1, latitude=latitude, longitude=longitude), Showing(booking1start, booking1end))

        val GEO_ICAL = """
            BEGIN:VCALENDAR
            VERSION:2.0
            CALSCALE:GREGORIAN
            METHOD:PUBLISH
            BEGIN:VEVENT
            SUMMARY:Test Show
            DTSTART:20180102T030400
            DTEND:20180102T040400
            GEO:51.510160;-0.130530
            END:VEVENT
            END:VCALENDAR
        """.trimIndent()
        assertEquals(GEO_ICAL, exporter.export(Plan(listOf(bookingWithGeo))))
    }

    @Test
    fun testWithLocationAndGeo() {
        val bookingWithLocationAndGeo = Booking(Show(showTitle1, location=location, latitude=latitude, longitude=longitude), Showing(booking1start, booking1end))

        val LOCATION_GEO_ICAL = """
            BEGIN:VCALENDAR
            VERSION:2.0
            CALSCALE:GREGORIAN
            METHOD:PUBLISH
            BEGIN:VEVENT
            SUMMARY:Test Show
            DTSTART:20180102T030400
            DTEND:20180102T040400
            LOCATION:A location, Somewhere
            GEO:51.510160;-0.130530
            X-APPLE-STRUCTURED-LOCATION;VALUE=URI;X-ADDRESS=A location, Somewhere;X-APPLE-RADIUS=72;X-TITLE=A location, Somewhere:geo:51.510160,-0.130530
            END:VEVENT
            END:VCALENDAR
        """.trimIndent()
        assertEquals(LOCATION_GEO_ICAL, exporter.export(Plan(listOf(bookingWithLocationAndGeo))))
    }

    @Test
    fun testComplete() {
        val booking = Booking(
                Show(showTitle1,
                        description= description,
                        url=url,
                        location=location,
                        latitude=latitude,
                        longitude=longitude), Showing(booking1start, booking1end))

        val ICAL = """
            BEGIN:VCALENDAR
            VERSION:2.0
            CALSCALE:GREGORIAN
            METHOD:PUBLISH
            BEGIN:VEVENT
            SUMMARY:Test Show
            DTSTART:20180102T030400
            DTEND:20180102T040400
            DESCRIPTION:A test description
            LOCATION:A location, Somewhere
            GEO:51.510160;-0.130530
            X-APPLE-STRUCTURED-LOCATION;VALUE=URI;X-ADDRESS=A location, Somewhere;X-APPLE-RADIUS=72;X-TITLE=A location, Somewhere:geo:51.510160,-0.130530
            URL:http://events.test
            END:VEVENT
            END:VCALENDAR
        """.trimIndent()
        assertEquals(ICAL, exporter.export(Plan(listOf(booking))))
    }
}