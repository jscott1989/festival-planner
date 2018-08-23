package me.jscott.festival.config

import me.jscott.festival.services.EdinburghFringeShowLocationEnricher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {

    @Bean
    fun showLocationEnricher() = EdinburghFringeShowLocationEnricher("/venues.json")

}