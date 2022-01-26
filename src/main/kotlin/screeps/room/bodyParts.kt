package screeps.room

import screeps.api.ActiveBodyPartConstant
import screeps.api.Creep
import screeps.api.Room
import screeps.api.*
import screeps.roomSpawnLevel
import screeps.starting

data class BodyParts(val parts: Array<BodyPartConstant>, val availableForStart: Boolean)

fun calcEnergyForSpawn(parts: Array<BodyPartConstant>): Int {
    var res = 0
    for (part in parts) {
        res += when (part) {
            MOVE -> 50
            WORK -> 100
            CARRY -> 50
            ATTACK -> 80
            RANGED_ATTACK -> 150
            HEAL -> 250
            CLAIM -> 600
            TOUGH -> 10
            else -> 0
        }
    }
    return res
}

fun chooseBody(bodyList: List<BodyParts>, room: Room): BodyParts {
    for (bodyParts in bodyList) {
        if (bodyParts.availableForStart == room.memory.starting && calcEnergyForSpawn(bodyParts.parts) <= room.memory.roomSpawnLevel)
            return bodyParts
    }
    return bodyList.last()
}