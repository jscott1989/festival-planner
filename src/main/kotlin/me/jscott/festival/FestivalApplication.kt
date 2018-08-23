package me.jscott.festival

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FestivalApplication

fun main(args: Array<String>) {
    runApplication<FestivalApplication>(*args)
}
