package screeps.room

import screeps.api.RESOURCE_ENERGY
import screeps.api.Room
import screeps.api.get
import screeps.roomSpawnLevelLastTime
import screeps.starting

fun Room.run() {
    spawnCreep()
    tower()
    transfer()
}