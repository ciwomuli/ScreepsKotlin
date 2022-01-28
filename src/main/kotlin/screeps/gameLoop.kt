package screeps

import screeps.api.*
import screeps.creep.run
import screeps.role.CreepExtensions
import screeps.role.Role
import screeps.room.extension
import screeps.room.run
import screeps.utils.memory.memory

var Memory.evalOnce by memory { false }

fun gameLoop() {
    if (!Memory.evalOnce) {
        CreepExtensions.addCreep("transfer_W4N9_2", Role.TRANSFER, true)
        Memory.evalOnce = true
    }
    if (Game.cpu.bucket == 10000) {
        Game.cpu.generatePixel()
    }
    for ((name, room) in Game.rooms) {
        room.run()
    }
    for ((name, creep) in Game.creeps) {
        creep.run()
    }
    for ((name, room) in Game.rooms) {
        room.extension.transferTaskQueue.save()
    }
}