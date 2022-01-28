package screeps.room

import screeps.*
import screeps.api.*
import screeps.role.CreepExtensions

fun Room.spawnCreep() {
    if (memory.roomSpawnLevelLastTime == 0) {
        memory.roomSpawnLevel = energyCapacityAvailable
    } else {
        memory.roomSpawnLevelLastTime--
    }
    if (memory.ticksLowEnergy > 1500) {
        memory.starting = true
        memory.roomSpawnLevel = 300
        memory.roomSpawnLevelLastTime = 4000
    }
    var spawned = false
    for ((name, extension) in CreepExtensions.creeps) {
        if (Game.creeps[name] == null && name.contains(this.name) && Memory.creeps[name]?.needToSpawn == true) {
            spawned = true
            val res = extension.spawn(this.extension.spawns[0])
            if (res == ERR_NOT_ENOUGH_ENERGY) memory.ticksLowEnergy++
            else memory.ticksLowEnergy = 0
            break
        }
    }
    if (!spawned) memory.ticksLowEnergy = 0
}