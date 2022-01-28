package screeps.room

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import screeps.api.*
import screeps.inTransferQueue
import screeps.structures
import screeps.utils.lazyPerTick

@Serializable
class TransferTask(
    val priority: Int,
    val from: String,
    val to: String,
    val sender: String,
    val type: ResourceConstant,
    var amount: Int
)

val emptyTransferTask = TransferTask(0, "", "", "", RESOURCE_ENERGY, 0)

@Serializable
class TransferTaskList(val queue: List<TransferTask>)

var RoomMemory.transferTaskQueue: TransferTaskList?
    get() {
        val internal = this.asDynamic().transferTaskQueue
        return if (internal == null) null else Json.decodeFromString(internal as String)
    }
    set(value) {
        val stringyfied = if (value == null) null else Json.encodeToString(value)
        this.asDynamic().transferTaskQueue = stringyfied
    }

class TransferTaskQueue(roomName: String) {
    val room: Room by lazyPerTick { Game.rooms[roomName]!! }

    @Serializable
    private val queue: MutableList<TransferTask> =
        room.memory.transferTaskQueue?.queue?.toMutableList() ?: mutableListOf()
    var modified: Boolean = false
    fun addTask(task: TransferTask) {
        queue.add(task)
        queue.sortByDescending { it.priority }
        modified = true
    }

    fun getTask(): TransferTask {
        return if (queue.isEmpty()) emptyTransferTask
        else {
            val ret = queue.first()
            queue.removeAt(0)
            modified = true
            ret
        }
    }

    fun save() {
        if (modified) room.memory.transferTaskQueue = TransferTaskList(queue.toList())
        modified = false
    }
}

fun Room.transfer() {
    for (container in extension.harvesterContainers) {
        if (Memory.structures[container.id]?.inTransferQueue == false && container.store[RESOURCE_ENERGY] >= 1000) {
            extension.transferTaskQueue.addTask(
                TransferTask(
                    5, container.id, storage?.id ?: "", container.id,
                    RESOURCE_ENERGY, 1000
                )
            )
            Memory.structures[container.id]?.inTransferQueue = true
        }
    }
}