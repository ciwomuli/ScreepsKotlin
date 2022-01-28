package screeps.role

import screeps.api.CARRY
import screeps.api.MOVE
import screeps.api.ScreepsReturnCode
import screeps.api.WORK
import screeps.api.structures.StructureSpawn
import screeps.creep.collectEnergy
import screeps.room.BodyParts
import screeps.room.chooseBody
import screeps.state

class Upgrader(name: String) : CreepExtension(name) {
    override val role: Role = Role.UPGRADER
    override fun run() {
        when (creep.memory.state) {
            CreepState.COLLECT -> {
                creep.collectEnergy()
            }
            CreepState.WORK -> if (!creep.pos.inRangeTo(creep.room.controller!!.pos, 1)) creep.moveTo(
                creep.room.controller!!
            ) else {
                creep.upgradeController(creep.room.controller!!)
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