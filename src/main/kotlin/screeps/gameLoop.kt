package screeps

import screeps.api.*
import screeps.creep.run
import screeps.role.CreepExtensions
import screeps.role.Role
import screeps.room.TransferTask
import screeps.room.extension
import screeps.room.run
import screeps.utils.memory.memory

var Memory.evalOnce by memory { false }

fun gameLoop() {
    if (!Memory.evalOnce) {
        // CreepExtensions.addCreep("upgrader_W4N9_3", Role.UPGRADER, true)
        //CreepExtensions.deleteCreep("center_W4N9_2")
       /* Game.rooms["W4N9"]!!.extension.transferTaskQueue.addTask(
            TransferTask(
                1,
                "test",
                "test",
                "test",
                RESOURCE_ALLOY as String,
                0
            )
        )*/
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
        room.extension.buildingTaskQueue.save()
    }
}