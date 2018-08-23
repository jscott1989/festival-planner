package me.jscott.festival.planners

import me.jscott.festival.Session
import me.jscott.festival.models.Plan

interface Planner {
    fun plan(session: Session): Plan
}