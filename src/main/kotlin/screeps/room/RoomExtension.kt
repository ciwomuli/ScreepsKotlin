package screeps.room

import screeps.api.*
import screeps.api.structures.StructureContainer
import screeps.api.structures.StructureTower
import screeps.harvesterContainers
import screeps.utils.lazyPerTick
import screeps.utils.memory.memory

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

class RoomExtension(roomName: String) {
    private val room by lazyPerTick { Game.rooms[roomName]!! }
    val spawns by lazyPerTick { room.find(FIND_MY_SPAWNS) }
    val sources by lazyPerTick {
        room.find(FIND_SOURCES)
    }
    val constructionSites by lazyPerTick {
        room.find(FIND_CONSTRUCTION_SITES)
    }
    val towerIds by lazy {
        room.find(FIND_MY_STRUCTURES, options { filter = { it.structureType == STRUCTURE_TOWER } }).map { it.id!! }
    }
    val towers: Array<StructureTower> by lazyPerTick {
        val tmp: MutableList<StructureTower> = mutableListOf()
        for (id in towerIds) {
            val t = Game.getObjectById<StructureTower>(id!!)
            if (t != null) {
                tmp.add(t)
            }
        }
        tmp.toTypedArray()
    }
    val harvesterContainers by lazyPerTick {
        val tmp: MutableList<StructureContainer> = mutableListOf()
        for (id in room.memory.harvesterContainers) {
            val t = Game.getObjectById<StructureContainer>(id)
            if (t != null) tmp.add(t)
        }
        tmp.toTypedArray()
    }
    val transferTaskQueue = TransferTaskQueue(room.name)
    val buildingTaskQueue = BuildingTaskQueue(room.name)
}
