package screeps.role

import screeps.Room.getFreeSource
import screeps.api.Creep
import screeps.api.ERR_NOT_IN_RANGE
import screeps.starting
import screeps.state

object Upgrader : Role {
    override fun run(creep: Creep) {
        when (creep.memory.state) {
            CreepState.COLLECT -> {
                if (creep.room.memory.starting) {
                    val source = creep.room.getFreeSource()
                    if (creep.harvest(source) == ERR_NOT_IN_RANGE) {
                        creep.moveTo(source)
                    }
                }
            }
            CreepState.WORK -> if (creep.upgradeController(creep.room.controller!!) == ERR_NOT_IN_RANGE) creep.moveTo(
                creep.room.controller!!
            )
            CreepState.IDLE -> Unit
        }
    }

    override fun getNextState(creep: Creep): CreepState =
        when (creep.memory.state) {
            CreepState.IDLE -> CreepState.COLLECT
            CreepState.COLLECT -> if (creep.store.getFreeCapacity() == 0) CreepState.WORK else CreepState.COLLECT
            CreepState.WORK -> if (creep.store.getUsedCapacity() == 0) CreepState.COLLECT else CreepState.WORK
            else -> CreepState.IDLE
        }
}