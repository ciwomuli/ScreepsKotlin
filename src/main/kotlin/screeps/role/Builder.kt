package screeps.role

import screeps.api.*
import screeps.api.structures.StructureSpawn
import screeps.buildingTask
import screeps.creep.collectEnergy
import screeps.room.*
import screeps.state

class Builder(name: String) : CreepExtension(name) {
    override val role: Role = Role.BUILDER
    override fun run() {
        when (creep.memory.state) {
            CreepState.COLLECT -> {
                creep.collectEnergy()
            }
            CreepState.WORK -> {
                with(creep.memory.buildingTask) {
                    if (creep.memory.buildingTask.type == BuildingType.EMPTY) {
                        creep.memory.buildingTask = creep.room.extension.buildingTaskQueue.getTask()
                    }
                    if (type == BuildingType.EMPTY) return
                    val tar = Game.getObjectById<ConstructionSite>(target)
                    if (tar == null) {
                        creep.memory.buildingTask = emptyBuildingTask
                        return
                    }
                    if (creep.build(tar) == ERR_NOT_IN_RANGE) creep.moveTo(tar)

                }
            }
            CreepState.IDLE -> Unit
        }
    }

    override fun getNextState(): CreepState =
        when (creep.memory.state) {
            CreepState.IDLE -> CreepState.COLLECT
            CreepState.COLLECT -> if (creep.store.getFreeCapacity() == 0) CreepState.WORK else CreepState.COLLECT
            CreepState.WORK -> if (creep.store.getUsedCapacity() == 0) CreepState.COLLECT else CreepState.WORK
            else -> CreepState.IDLE
        }

    companion object {
        val bodyList = listOf(
            BodyParts(
                arrayOf(
                    WORK, WORK, WORK, WORK, WORK, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, CARRY,
                    CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY
                ),
                true
            ),
            BodyParts(
                arrayOf(WORK, WORK, WORK, MOVE, MOVE, MOVE, MOVE, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY),
                true
            ),
            BodyParts(arrayOf(WORK, WORK, MOVE, MOVE, MOVE, CARRY, CARRY, CARRY, CARRY), true),
            BodyParts(arrayOf(WORK, MOVE, CARRY, CARRY, CARRY), true)
        )
    }

    override fun spawn(spawn: StructureSpawn): ScreepsReturnCode {
        // console.log("try to spawn upgrader")
        return spawn.spawnCreep(chooseBody(bodyList, spawn.room).parts, name)
    }
}