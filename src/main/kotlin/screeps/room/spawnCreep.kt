package screeps.room

import screeps.api.Room
import screeps.role.CreepExtensions

fun Room.spawnCreep() {
    for ((name, extension) in CreepExtensions.creeps) {
        if (extension.creep == null && name.contains(this.name)) {
            extension.spawn(this.extension.spawns[0])
            break
        }
    }
}