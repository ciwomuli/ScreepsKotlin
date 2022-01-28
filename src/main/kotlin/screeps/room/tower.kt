package screeps.room

import screeps.api.*

fun Room.tower() {
    val hostileCreeps = find(FIND_HOSTILE_CREEPS)
    val structuresNeedToRepair =
        find(
            FIND_STRUCTURES,
            options {
                filter =
                    { it.structureType != STRUCTURE_RAMPART && it.structureType != STRUCTURE_WALL && it.hits <= it.hitsMax - 1000 }
            })
    if (hostileCreeps.isNotEmpty()) {
        for (tower in extension.towers) {
            tower.attack(hostileCreeps[0])
        }
    } else if (structuresNeedToRepair.isNotEmpty()) {
        for (tower in extension.towers) {
            tower.repair(structuresNeedToRepair[0])
        }
    }
}