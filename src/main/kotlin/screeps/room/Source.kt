package screeps.room

import screeps.api.Room
import screeps.api.Source

fun Room.getFreeSource(): Source {
    return this.extension.sources[1]
}