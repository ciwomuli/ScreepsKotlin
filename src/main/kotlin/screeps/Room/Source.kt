package screeps.Room

import screeps.api.FIND_SOURCES
import screeps.api.Room
import screeps.api.Source
import screeps.utils.lazyPerTick

val Room.sources
        by lazyPerTick { this.find(FIND_SOURCES) }
fun Room.getFreeSource(): Source {
    return this.sources[0]
}