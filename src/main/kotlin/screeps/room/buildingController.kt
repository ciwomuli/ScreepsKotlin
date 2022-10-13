package screeps.room

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import screeps.api.*
import screeps.inBuildingQueue
import screeps.structures
import screeps.testing
import screeps.utils.lazyPerTick

enum class BuildingType {
    BUILD,
    REPAIR,
    EMPTY
}

@Serializable
data class BuildingTask(val priority: Int, val target: String, val type: String, val repairTarget: Int)

@Serializable
data class BuildingTaskList(val queue: List<BuildingTask>)

val emptyBuildingTask = BuildingTask(-1, "", BuildingType.EMPTY.name, 0)
var RoomMemory.buildingTaskQueue: BuildingTaskList?
    get() {
        val internal = this.asDynamic().buildingTaskQueue
        return if (internal == null) null else Json.decodeFromString(internal as String)
    }
    set(value) {
        val stringyfied = if (value == null) null else Json.encodeToString(value)
        this.asDynamic().buildingTaskQueue = stringyfied
    }

class BuildingTaskQueue(roomName: String) {
    val room: Room by lazyPerTick { Game.rooms[roomName]!! }

    @Serializable
    private val queue: MutableList<BuildingTask> =
        room.memory.buildingTaskQueue?.queue?.toMutableList() ?: mutableListOf()
    var modified: Boolean = false
    fun addTask(task: BuildingTask) {
        queue.add(task)
        queue.sortByDescending { it.priority }
        modified = true
    }

    fun getTask(): BuildingTask {
        return if (queue.isEmpty()) emptyBuildingTask
        else {
            val ret = queue.first()
            queue.removeAt(0)
            modified = true
            ret
        }
    }

    fun save() {
        if (modified) room.memory.buildingTaskQueue = BuildingTaskList(queue.toList())
        modified = false
    }
}

fun Room.building() {
    if (Game.time % 10000 == 0 || Memory.testing) {
        for ((id, _) in Memory.structures) {
            if (Game.getObjectById<ConstructionSite>(id) == null)
                js("delete Memory.structures[id]")
        }
    }
    for (constructionSite in extension.constructionSites) {
        if (Memory.structures[constructionSite.id] == null) {
            js("Memory.structures[constructionSite.id] = {};")
        }
        if (Memory.structures[constructionSite.id]?.inBuildingQueue == false) {
            extension.buildingTaskQueue.addTask(
                BuildingTask(
                    when (constructionSite.structureType) {
                        STRUCTURE_EXTENSION -> 5
                        else -> 4
                    }, constructionSite.id, BuildingType.BUILD.name, 0
                )
            )
            Memory.structures[constructionSite.id]!!.inBuildingQueue = true
        }
    }
}
