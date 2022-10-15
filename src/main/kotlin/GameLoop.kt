import Behaviour.run
import screeps.api.*
import screeps.utils.lazyPerTick
import screeps.utils.toMap

object Context {
    val rooms by lazyPerTick { Game.rooms.toMap() }
    val creeps by lazyPerTick { Game.creeps.toMap() }
    val myStuctures by lazyPerTick { Game.structures.toMap() }
    val spawns by lazyPerTick { Game.spawns.toMap() }
}

fun gameLoop() {
    for ((spawn) in Context.spawns) {

    }
    for ((_, creep) in Context.creeps) {
        if (creep.spawning) continue
        creep.run()
    }
}