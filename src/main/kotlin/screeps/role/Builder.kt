package screeps.role

import screeps.api.*
import screeps.api.structures.StructureSpawn
import screeps.creep.collect
import screeps.room.getConstructionSite
import screeps.state
import screeps.workTarget

class Builder(name: String) : CreepExtension(name) {
    override val role: Role = Role.BUILDER
    override fun run() {
        when (creep?.memory?.state) {
            CreepState.COLLECT -> {
                creep!!.collect()
            }
            CreepState.WORK -> {
                val target =
                    Game.getObjectById<ConstructionSite>(creep!!.memory.workTarget) ?: creep!!.room.getConstructionSite()
                creep!!.memory.workTarget = target?.id ?: ""
                if (target != null) {
                    if (creep!!.build(target) == ERR_NOT_IN_RANGE) creep!!.moveTo(target)
                }
            }
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
       // console.log("try to spawn builder")
        spawn.spawnCreep(arrayOf(WORK, MOVE, CARRY, CARRY, CARRY), name)
    }
}