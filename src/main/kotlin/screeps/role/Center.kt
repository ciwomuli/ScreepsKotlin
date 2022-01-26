package screeps.role

import screeps.api.*
import screeps.api.structures.StructureSpawn
import screeps.creep.collect
import screeps.state

class Center(name: String) : CreepExtension(name) {
    override val role: Role = Role.CENTER
    override fun run() {
        when (creep?.memory?.state) {
            CreepState.COLLECT -> {
                creep!!.collect()
            }
            CreepState.WORK -> {
                val target =
                    creep!!.pos.findClosestByPath(FIND_MY_STRUCTURES, options {
                        filter = {
                            (it.structureType == STRUCTURE_EXTENSION || it.structureType == STRUCTURE_SPAWN) && (it as StoreOwner).store.getFreeCapacity() != 0
                        }
                    })
                if (target != null) {
                    if (creep!!.transfer(target as StoreOwner, RESOURCE_ENERGY) == ERR_NOT_IN_RANGE) creep!!.moveTo(
                        target
                    )
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
        //console.log("try to spawn center")
        spawn.spawnCreep(arrayOf(WORK, MOVE, CARRY, CARRY, CARRY), name)
    }
}