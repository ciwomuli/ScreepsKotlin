package screeps.creep

import screeps.api.Creep
import screeps.role.CreepExtensions
import screeps.role.extension
import screeps.state

fun Creep.run() {
    //console.log("1$name")
    if (CreepExtensions.creeps[name] == null) suicide()
    this.memory.state = this.extension.getNextState()
    this.extension.run()
}