package screeps.room

import screeps.api.*
import screeps.sourceCount
import screeps.utils.lazyPerTick
import screeps.utils.mutableRecordOf

object RoomExtensions {
    private val rooms: MutableMap<String, RoomExtension> = mutableMapOf()
    operator fun invoke(name: String): RoomExtension =
        if (rooms[name] != null) rooms[name]!!
        else {
            rooms[name] = RoomExtension(name)
            rooms[name]!!
        }

}

val Room.extension
    get() =
        RoomExtensions(name)

fun Room.run() {
    if (Game.time % 3000 == 0) {
        memory.sourceCount = mutableRecordOf()
    }
    spawnCreep()
}

class RoomExtension(roomName: String) {
    private val room by lazyPerTick { Game.rooms[roomName]!! }
    val spawns by lazyPerTick { room.find(FIND_MY_SPAWNS) }
    val sources by lazy {
        room.find(FIND_SOURCES)
    }
    val constructionSites by lazyPerTick {
        room.find(FIND_CONSTRUCTION_SITES)
    }
}