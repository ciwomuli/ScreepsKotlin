package screeps.creep

import screeps.api.*
import screeps.collectTarget
import screeps.room.getFreeSource
import screeps.starting

fun Creep.collectEnergy() {
    if (room.memory.starting || room.storage == null) {
        val source = Game.getObjectById<Source>(memory.collectTarget) ?: room.getFreeSource()
        memory.collectTarget = source.id
        if (harvest(source) == ERR_NOT_IN_RANGE) {
            moveTo(source)
        }
    } else {
        if (room.storage != null) {
            if (withdraw(room.storage!!, RESOURCE_ENERGY) == ERR_NOT_IN_RANGE) moveTo(room.storage!!)
        }
    }
}