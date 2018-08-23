package me.jscott.festival.services

import me.jscott.festival.models.Plan
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Service
import java.io.File
import java.time.format.DateTimeFormatter

val dtformatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

@Service
class PlanCalendarExporter {
    fun export(file: File, plan: Plan) {
        val pw = file.printWriter()
        pw.write(export(plan))
        pw.flush()
    }


    fun export(plan: Plan): String {
        val str = StringBuilder("""
            BEGIN:VCALENDAR
            VERSION:2.0
            CALSCALE:GREGORIAN
            METHOD:PUBLISH

        """.trimIndent())

        plan.bookings.forEach {
            str.appendln("""
                BEGIN:VEVENT
                SUMMARY:${it.show.title}
                DTSTART:${it.showing.start.format(dtformatter)}
                DTEND:${it.showing.end.format(dtformatter)}
            """.trimIndent())

            if (it.show.description != null) {
                str.appendln("DESCRIPTION:${it.show.description}")
            }

            if (it.show.location != null) {
                str.appendln("LOCATION:${it.show.location}")
            }

            if (it.show.longitude != null && it.show.latitude != null) {
                str.appendln("GEO:${it.show.latitude};${it.show.longitude}")
            }

            if (it.show.location != null && it.show.longitude != null && it.show.latitude != null) {
                str.appendln("X-APPLE-STRUCTURED-LOCATION;VALUE=URI;X-ADDRESS=${it.show.location};X-APPLE-RADIUS=72;X-TITLE=${it.show.location}:geo:${it.show.latitude},${it.show.longitude}")
            }

            if (it.show.url != null) {
                str.appendln("URL:${it.show.url}")
            }

            str.appendln("END:VEVENT")
        }

        str.append("END:VCALENDAR")

        return str.toString()
    }
}