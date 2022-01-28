package screeps.role

import screeps.api.*
import screeps.api.structures.StructureSpawn
import screeps.inTransferQueue
import screeps.room.BodyParts
import screeps.room.chooseBody
import screeps.room.emptyTransferTask
import screeps.room.extension
import screeps.state
import screeps.structures
import screeps.transferTask

class Transfer(name: String) : CreepExtension(name) {
    fun finishTask() {
        Memory.structures[creep.memory.transferTask.sender]?.inTransferQueue = false
        creep.memory.transferTask = emptyTransferTask
    }

    override fun run() {
        if (creep.memory.transferTask.amount == 0) creep.memory.transferTask =
            creep.room.extension.transferTaskQueue.getTask()
        if (creep.memory.transferTask.amount == 0) return
        with(creep.memory.transferTask) {
            when (creep.memory.state) {
                CreepState.COLLECT -> {
                    val target = Game.getObjectById<StoreOwner>(from)
                    if (target == null) {
                        finishTask()
                        return
                    }
                    val amunt =
                        minOf(amount, target.store[type] ?: 0, creep.store.getFreeCapacity())
                    val code = creep.withdraw(target, type, amunt)
                    if (code == ERR_NOT_IN_RANGE) creep.moveTo(target)
                    else if (code == OK) {
                        creep.memory.state = CreepState.WORK
                        target.store[type] = target.store[type]!! - amunt
                    } else console.log("${creep.name} try to withdraw ${target.id} err code : ${code}")
                }
                CreepState.WORK -> {
                    val target = Game.getObjectById<StoreOwner>(to)
                    if (target == null) {
                        finishTask()
                        return
                    }
                    val amunt =
                        minOf(amount, target.store.getFreeCapacity(), creep.store[type] ?: 0)
                    val code = creep.transfer(target, type, amunt)
                    if (code == ERR_NOT_IN_RANGE) creep.moveTo(target)
                    else if (code == OK) {
                        creep.memory.state = CreepState.COLLECT
                        target.store[type] = target.store[type]!! + amunt
                        creep.memory.transferTask.amount -= amunt
                        if (creep.memory.transferTask.amount <= 0) {
                            finishTask()
                        } else {
                        }
                    } else console.log("${creep.name} try to transfer ${target.id} err code : ${code}")
                }
                else -> Unit
            }
        }
    }

    override val role: Role = Role.TRANSFER

    override fun getNextState(): CreepState = when (creep.memory.state) {
        CreepState.IDLE -> CreepState.COLLECT
        CreepState.COLLECT -> CreepState.COLLECT
        CreepState.WORK -> CreepState.WORK
        else -> CreepState.COLLECT
    }

    companion object {
        val bodyList = listOf(
            BodyParts(
                arrayOf(
                    MOVE, MOVE, MOVE, MOVE, MOVE,
                    CARRY, CARRY, CARRY, CARRY, CARRY,
                    CARRY, CARRY, CARRY, CARRY, CARRY
                ), false
            ),
            BodyParts(
                arrayOf(
                    MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, CARRY, CARRY, CARRY,
                    CARRY, CARRY, CARRY, CARRY, CARRY
                ), false
            )
        )
    }

    override fun spawn(spawn: StructureSpawn): ScreepsReturnCode {
        return spawn.spawnCreep(chooseBody(bodyList, spawn.room).parts, name)
    }

}

private operator fun Store.set(type: ResourceConstant, value: Int) {
    this.asDynamic()[type] = value
}
