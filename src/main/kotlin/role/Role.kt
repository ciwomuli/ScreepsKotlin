package role

import screeps.api.Creep
import screeps.api.Game
import screeps.api.get
import screeps.utils.lazyPerTick

abstract class Role() {
    abstract fun updateState(creep: Creep)
    abstract fun run(creep: Creep)
}

enum class CreepRole {
    UNKNOWN, WORKER
}