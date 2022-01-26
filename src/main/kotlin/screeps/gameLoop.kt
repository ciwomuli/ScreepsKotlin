package screeps

import screeps.api.Game
import screeps.api.component1
import screeps.api.component2
import screeps.api.iterator
import screeps.creep.run
import screeps.room.run

fun gameLoop() {
    if (Game.cpu.bucket == 10000) {
        Game.cpu.generatePixel()
    }
    for ((name, room) in Game.rooms) {
        room.run()
    }
    for ((name, creep) in Game.creeps) {
        creep.run()
    }
}