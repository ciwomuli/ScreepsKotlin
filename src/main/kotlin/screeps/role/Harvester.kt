package screeps.role

import screeps.api.*
import screeps.api.structures.StructureSpawn
import screeps.collectTarget
import screeps.room.BodyParts
import screeps.room.chooseBody
import screeps.state
import screeps.workTarget

class Harvester(name: String) : CreepExtension(name) {
    override val role: Role = Role.HARVESTER
    override fun getNextState(): CreepState =
        when (creep.memory.state) {
            CreepState.COLLECT -> if (creep.store.getFreeCapacity() == 0) CreepState.WORK else CreepState.COLLECT
            CreepState.WORK -> if (creep.store.getUsedCapacity() == 0) CreepState.COLLECT else CreepState.WORK
            else -> CreepState.COLLECT
        }

    override fun run() {
        when (creep.memory.state) {
            CreepState.COLLECT -> {
                val target = Game.getObjectById<Source>(creep.memory.collectTarget)
                if (target != null) {
                    if (creep.harvest(target) == ERR_NOT_IN_RANGE) creep.moveTo(target)
                }
            }
            CreepState.WORK -> {
                if (Game.getObjectById<StoreOwner>(creep.memory.workTarget) == null) {
                    creep.memory.workTarget = creep.pos.findClosestByRange(
                        FIND_MY_STRUCTURES,
                        options {
                            filter =
                                { it.structureType == STRUCTURE_CONTAINER || it.structureType == STRUCTURE_LINK || it.structureType == STRUCTURE_STORAGE }
                        })?.id ?: ""
                }
                val target = Game.getObjectById<StoreOwner>(creep.memory.workTarget)
                if (target != null && creep.transfer(target, RESOURCE_ENERGY) == ERR_NOT_IN_RANGE) {
                    creep.moveTo(target)
                }
            }

            else -> Unit
        }
    }

    companion object {
        val bodyList = listOf(
            BodyParts(
                arrayOf(WORK, WORK, WORK, WORK, WORK, WORK, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, CARRY, CARRY),
                false
            ),
            BodyParts(
                arrayOf(WORK, WORK, WORK, WORK, WORK, MOVE, MOVE, MOVE, MOVE, MOVE, CARRY, CARRY, CARRY, CARRY, CARRY),
                true
            ),
            BodyParts(
                arrayOf(WORK, WORK, WORK, MOVE, MOVE, MOVE, MOVE, CARRY, CARRY, CARRY, CARRY, CARRY),
                true
            ),
            BodyParts(arrayOf(WORK, WORK, MOVE, MOVE, MOVE, CARRY, CARRY, CARRY, CARRY), true),
            BodyParts(arrayOf(WORK, MOVE, CARRY, CARRY, CARRY), true)
        )
    }

    override fun spawn(spawn: StructureSpawn): ScreepsReturnCode {
        return spawn.spawnCreep(chooseBody(bodyList, spawn.room).parts, name)
    }

}