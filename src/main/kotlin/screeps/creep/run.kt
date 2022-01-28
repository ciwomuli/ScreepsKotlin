package screeps.creep

import screeps.api.Creep
import screeps.role.CreepExtensions
import screeps.role.extension
import screeps.roomName
import screeps.state

fun Creep.run() {
    //console.log("1$name")
    if (spawning) return
    if (CreepExtensions.creeps[name] == null) {
        suicide()
        return
    }
    this.memory.roomName = this.room.name
    this.memory.state = this.extension.getNextState()
    //memory.role = extension.role
    this.extension.run()
}