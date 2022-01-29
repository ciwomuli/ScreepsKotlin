package screeps.role

import screeps.api.*
import screeps.api.structures.StructureSpawn
import screeps.creep.collectEnergy
import screeps.room.BodyParts
import screeps.room.chooseBody
import screeps.state

class Center(name: String) : CreepExtension(name) {
    override val role: Role = Role.CENTER
    override fun run() {
        when (creep.memory.state) {
            CreepState.COLLECT -> {
                creep.collectEnergy()
            }
            CreepState.WORK -> {
                val target = creep.pos.findClosestByPath(FIND_MY_STRUCTURES, options {
                    filter = {
                        (it.structureType == STRUCTURE_EXTENSION || it.structureType == STRUCTURE_SPAWN || it.structureType == STRUCTURE_TOWER) && (it as StoreOwner).store.getFreeCapacity(
                            RESOURCE_ENERGY
                        ) != 0
                    }
                })
                if (target != null) {
                    if (creep.transfer(target as StoreOwner, RESOURCE_ENERGY) == ERR_NOT_IN_RANGE) creep.moveTo(
                        target
                    )
                } else {
                    if (creep.room.storage != null)
                        creep.moveTo(creep.room.storage!!)
                }
            }
            CreepState.IDLE -> Unit
        }
    }

    override fun getNextState(): CreepState = when (creep.memory.state) {
        CreepState.IDLE -> CreepState.COLLECT
        CreepState.COLLECT -> if (creep.store.getFreeCapacity() == 0) CreepState.WORK else CreepState.COLLECT
        CreepState.WORK -> if (creep.store.getUsedCapacity() == 0) CreepState.COLLECT else CreepState.WORK
        else -> CreepState.IDLE
    }

    companion object {
        val bodyList = listOf(
            BodyParts(
                arrayOf(
                    MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, CARRY, CARRY, CARRY,
                    CARRY, CARRY, CARRY, CARRY, CARRY
                ), false
            ),
            BodyParts(
                arrayOf(WORK, WORK, WORK, MOVE, MOVE, MOVE, MOVE, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY), true
            ),
            BodyParts(arrayOf(WORK, WORK, MOVE, MOVE, MOVE, CARRY, CARRY, CARRY, CARRY), true),
            BodyParts(arrayOf(WORK, MOVE, CARRY, CARRY, CARRY), true)
        )
    }

    override fun spawn(spawn: StructureSpawn): ScreepsReturnCode {
        // console.log("try to spawn upgrader")
        return spawn.spawnCreep(chooseBody(bodyList, spawn.room, spawn.room.energyAvailable).parts, name)
    }
}