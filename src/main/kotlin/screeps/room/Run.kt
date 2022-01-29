package screeps.room

import screeps.api.Room

fun Room.run() {
    spawnCreep()
    tower()
    transfer()
    building()
}