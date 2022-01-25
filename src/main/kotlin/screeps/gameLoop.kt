package screeps

import screeps.api.*
import screeps.role.CreepRole
import screeps.utils.*
import screeps.creep.run
import screeps.role.Upgrader
import screeps.utils.unsafe.jsObject

fun gameLoop() {
    val mainSpawn = (Game.spawns["Spawn1"])!!
    for ((name, creep) in Game.creeps) {
        when (creep.memory.role) {
            CreepRole.UPGRADER -> creep.run(Upgrader)
            else -> creep.memory.role = CreepRole.UPGRADER
        }
    }
    if (mainSpawn.spawning == null) {
        mainSpawn.spawnCreep(
            arrayOf(WORK, MOVE, CARRY, CARRY, CARRY),
            "upgrader_${Game.time}",
            options {
                memory = jsObject<CreepMemory> {
                    this.role = CreepRole.UPGRADER
                }
            })
    }
}