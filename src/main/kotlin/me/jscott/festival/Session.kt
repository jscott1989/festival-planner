package me.jscott.festival

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import me.jscott.festival.models.Show
import me.jscott.festival.models.Showing
import me.jscott.festival.services.ShowLocationEnricher
import org.springframework.stereotype.Service
import java.io.File
import java.net.URL
import java.time.LocalDateTime
import java.util.*

@Service
class Session(val locationEnricher: ShowLocationEnricher? = null, val shows: ArrayList<Show> = arrayListOf()) {

    companion object {

        data class ShowingJson(val start: String, val end: String) {

            companion object {
                fun fromShowing(showing: Showing) = ShowingJson(start=showing.start.toString(), end=showing.end.toString())
            }

            fun asJsonObject(): JsonObject {
                val obj = JsonObject()

                obj["start"] = start
                obj["end"] = end

                return obj
            }

            fun asShowing(): Showing = Showing(start=LocalDateTime.parse(start), end=LocalDateTime.parse(end))
        }

        data class ShowJson(val title: String, val description: String? = null, val url: String? = null, val location:String? = null, val showings: Array<ShowingJson> = arrayOf()) {

            companion object {
                fun fromShow(show: Show): ShowJson = ShowJson(show.title, show.description, show.url, show.location, show.showings().map { ShowingJson.fromShowing(it) }.toTypedArray())
            }

            fun asJsonObject(): JsonObject {
                val obj = JsonObject()

                obj["title"] = title
                if (description != null) {
                    obj["description"] = description
                }

                if (location != null) {
                    obj["location"] = location
                }

                if (url!= null) {
                    obj["url"] = url
                }

                obj["showings"] = JsonArray(showings.map { it.asJsonObject() })

                return obj
            }

            fun asShow(): Show
                = Show(title, description=description, location=location, url=url, showings=ArrayList(showings.map { it.asShowing() }))


            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as ShowJson

                if (title != other.title) return false
                if (description != other.description) return false
                if (!Arrays.equals(showings, other.showings)) return false

                return true
            }

            override fun hashCode(): Int {
                var result = title.hashCode()
                result = 31 * result + (description?.hashCode() ?: 0)
                result = 31 * result + Arrays.hashCode(showings)
                return result
            }
        }

        fun fromURL(url: URL, locationEnricher: ShowLocationEnricher? = null): Session {
            val session = Session(locationEnricher)

            val showsJson = Klaxon().parseArray<ShowJson>(url.readText())!!
            showsJson.forEach { session.addShow(it.asShow()) }

            return session
        }
    }

    fun addShow(show: Show) =
            shows.add(
                    if (locationEnricher != null) locationEnricher!!.enrichLocation(show)
                    else show
            )

    fun shows() = ArrayList(shows)

    fun writeToFile(file: File) {
        file.printWriter().use { out ->
            val showsJson = JsonArray(shows.map { ShowJson.fromShow(it).asJsonObject() })
            out.print(showsJson.toJsonString(true))
        }
    }

}