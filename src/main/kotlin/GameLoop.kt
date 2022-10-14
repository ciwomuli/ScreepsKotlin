import role.CreepRole
import role.Role
import role.Worker
import screeps.api.*
import screeps.api.structures.Structure
import screeps.api.structures.StructureSpawn
import screeps.api.structures.StructureTower
import screeps.utils.lazyPerTick
import screeps.utils.toMap

object Context {
    val roles: Map<CreepRole, Role> by lazy {
        mapOf(CreepRole.WORKER to Worker())
    }
    val rooms by lazyPerTick { Game.rooms.toMap() }
    val creeps by lazyPerTick { Game.creeps.toMap() }
    val myStuctures by lazyPerTick { Game.structures.toMap() }
    val spawns by lazyPerTick { Game.spawns.toMap() }
}

fun gameLoop() {
    for ((spawn) in Context.spawns) {

    }
    for ((creepName, creep) in Context.creeps) {
        if (creep.spawning) continue
        Context.roles[creep.memory.role]?.run(creep)
    }
}