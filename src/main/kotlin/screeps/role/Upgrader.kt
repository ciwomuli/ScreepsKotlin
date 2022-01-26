package screeps.role

import screeps.api.CARRY
import screeps.api.ERR_NOT_IN_RANGE
import screeps.api.MOVE
import screeps.api.WORK
import screeps.api.structures.StructureSpawn
import screeps.creep.collect
import screeps.state

class Upgrader(name: String) : CreepExtension(name) {
    override val role: Role = Role.UPGRADER
    override fun run() {
        when (creep?.memory?.state) {
            CreepState.COLLECT -> {
                creep!!.collect()
            }
            CreepState.WORK -> if (creep!!.upgradeController(creep!!.room.controller!!) == ERR_NOT_IN_RANGE) creep!!.moveTo(
                creep!!.room.controller!!
            )
            CreepState.IDLE -> Unit
        }
    }

    override fun getNextState(): CreepState =
        when (creep?.memory?.state) {
            CreepState.IDLE -> CreepState.COLLECT
            CreepState.COLLECT -> if (creep!!.store.getFreeCapacity() == 0) CreepState.WORK else CreepState.COLLECT
            CreepState.WORK -> if (creep!!.store.getUsedCapacity() == 0) CreepState.COLLECT else CreepState.WORK
            else -> CreepState.IDLE
        }

    override fun spawn(spawn: StructureSpawn) {
       // console.log("try to spawn upgrader")
        spawn.spawnCreep(arrayOf(WORK, MOVE, CARRY, CARRY, CARRY), name)
    }
}