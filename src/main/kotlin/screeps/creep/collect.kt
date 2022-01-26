package screeps.creep

import screeps.api.Creep
import screeps.api.ERR_NOT_IN_RANGE
import screeps.api.Game
import screeps.api.Source
import screeps.collectTarget
import screeps.room.getFreeSource
import screeps.starting

fun Creep.collect() {
    if (room.memory.starting) {
        val source = Game.getObjectById<Source>(memory.collectTarget) ?: room.getFreeSource()
        memory.collectTarget = source.id
        if (harvest(source) == ERR_NOT_IN_RANGE) {
            moveTo(source)
        }
    }
}