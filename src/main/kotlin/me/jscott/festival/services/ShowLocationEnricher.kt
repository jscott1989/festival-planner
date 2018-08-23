package me.jscott.festival.services

import com.beust.klaxon.Klaxon
import me.jscott.festival.models.Show
import me.jscott.festival.models.Venue
import org.springframework.stereotype.Service
import java.net.URL

data class GeoLocation(val latitude: String, val longitude: String)

interface ShowLocationEnricher {

    fun enrichLocation(location: String): GeoLocation?

    fun enrichLocation(show: Show): Show {
        if ((show.latitude != null && show.longitude != null) || show.location == null) {
            return show
        }

        val geoLocation = enrichLocation(show.location) ?: return show

        val newShow = Show(show.title,
                description = show.description,
                url = show.url,
                location = show.location,
                latitude = geoLocation.latitude,
                longitude = geoLocation.longitude)

        show.showings().forEach { newShow.addShowing(it) }
        return newShow
    }
}

open class PresetShowLocationEnricher(private val venues: Map<String, Venue>) : ShowLocationEnricher {
    override fun enrichLocation(location: String): GeoLocation? {
        if (!venues.containsKey(location)) {
            return null
        }

        val venue = venues[location]!!
        return GeoLocation(venue.latitude, venue.longitude)
    }
}

class EdinburghFringeShowLocationEnricher(url: URL) : PresetShowLocationEnricher(Klaxon()
        .parseArray<EdinburghFringeVenue>(url.readText())!!
        .associateBy({it.name}, {Venue(name=it.name, longitude=it.lng.toString(), latitude=it.lat.toString())})) {

    constructor(filename: String): this(EdinburghFringeShowLocationEnricher::class.java.getResource(filename))

    data class EdinburghFringeVenue(val name: String, val slug: String, val number: String, val address: String, val postcode: String, val lat: Double, val lng: Double)
}