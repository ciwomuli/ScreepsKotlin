package screeps.room

import screeps.api.Room
import screeps.role.CreepExtensions
import screeps.roomSpawnLevel
import screeps.roomSpawnLevelLastTime

fun Room.spawnCreep() {
    if (memory.roomSpawnLevelLastTime == 0) {
        memory.roomSpawnLevel = energyCapacityAvailable
    } else {
        memory.roomSpawnLevelLastTime--
    }
    for ((name, extension) in CreepExtensions.creeps) {
        if (extension.creep == null && name.contains(this.name)) {
            extension.spawn(this.extension.spawns[0])
            break
        }
    }
}