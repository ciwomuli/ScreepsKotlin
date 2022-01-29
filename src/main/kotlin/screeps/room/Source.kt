package screeps.room

import screeps.api.Room
import screeps.api.Source

fun Room.getFreeSource(): Source = extension.sources[0]
