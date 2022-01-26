package screeps.role

import screeps.api.Creep
import screeps.api.Game
import screeps.api.get
import screeps.api.structures.StructureSpawn

abstract class CreepExtension(val name: String) {
    abstract val role: Role
    val creep
        get() = Game.creeps[name]

    abstract fun getNextState(): CreepState
    abstract fun run()
    abstract fun spawn(spawn: StructureSpawn)
}

val Creep.extension
    get() = CreepExtensions(name)

object CreepExtensions {
    val creeps: MutableMap<String, CreepExtension> = mutableMapOf(
        "center_W4N9_1" to Center("center_W4N9_1"),
        "upgrader_W4N9_1" to Upgrader("upgrader_W4N9_1"),
        "upgrader_W4N9_2" to Upgrader("upgrader_W4N9_2"),
        "upgrader_W4N9_3" to Upgrader("upgrader_W4N9_3"),
        "builder_W4N9_1" to Builder("builder_W4N9_1"),
        "builder_W4N9_1" to Builder("builder_W4N9_1")
    )

    operator fun invoke(name: String): CreepExtension = creeps[name]!!

}