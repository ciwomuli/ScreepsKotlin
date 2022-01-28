package screeps.role

import screeps.api.*
import screeps.api.Game.creeps
import screeps.api.structures.StructureSpawn
import screeps.needToSpawn
import screeps.role
import screeps.room.extension
import screeps.roomName
import screeps.transferTask
import screeps.utils.lazyPerTick

abstract class CreepExtension(val name: String) {
    abstract val role: Role
    val creep
            by lazyPerTick { creeps[name]!! }

    abstract fun getNextState(): CreepState
    abstract fun run()
    abstract fun spawn(spawn: StructureSpawn): ScreepsReturnCode
}

val Creep.extension
    get() = CreepExtensions(name)

object CreepExtensions {
    val creeps: MutableMap<String, CreepExtension> = mutableMapOf();
    fun getCreepExtention(role: Enum<Role>, name: String) = when (role) {
        Role.UPGRADER -> Upgrader(name)
        Role.BUILDER -> Builder(name)
        Role.CENTER -> Center(name)
        Role.HARVESTER -> Harvester(name)
        Role.TRANSFER -> Transfer(name)
        else -> {
            console.log("unknown role ${role}")
            Center(name)
        }
    }

    init {
        for ((name, memory) in Memory.creeps) {
           // console.log(name)
            if (Game.creeps[name] == null && !memory.needToSpawn) {
               // console.log("1${name}")
                if (Memory.creeps[name]!!.transferTask.amount != 0) {
                    Game.rooms[memory.roomName]!!.extension.transferTaskQueue.addTask(Game.creeps[name]!!.memory.transferTask)
                    Memory.creeps[name]!!.transferTask.amount = 0
                }
                js("delete Memory.creeps[name];")
            } else {
              //  console.log("2${name}")
                creeps[name] = getCreepExtention(memory.role, name)
            }
        }
    }

    fun addCreep(name: String, role: Role, needToSpawn: Boolean) {
        js("Memory.creeps[name] = {};")
        Memory.creeps[name]!!.role = role
        Memory.creeps[name]!!.needToSpawn = needToSpawn
        creeps[name] = getCreepExtention(role, name)
    }

    fun deleteCreep(name: String) {
        Memory.creeps[name]!!.needToSpawn = false
    }

    operator fun invoke(name: String): CreepExtension = creeps[name]!!

}