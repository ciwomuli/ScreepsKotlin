package screeps

import screeps.api.*
import screeps.role.CreepState
import screeps.role.Role
import screeps.room.BuildingTask
import screeps.room.TransferTask
import screeps.room.emptyBuildingTask
import screeps.room.emptyTransferTask
import screeps.utils.memory.memory
import screeps.utils.mutableRecordOf
import screeps.utils.recordOf

//var RoomMemory.mainSpawnName : String by memory { "Spawn1" }
var RoomMemory.starting: Boolean by memory { true }
var CreepMemory.state by memory(CreepState.IDLE)
var CreepMemory.collectTarget: String by memory { "" }
var CreepMemory.workTarget by memory { "" }
var CreepMemory.role by memory(Role.UNASSIGNED)
var CreepMemory.needToSpawn by memory { true }
var CreepMemory.transferTask: TransferTask by memory { emptyTransferTask }
var CreepMemory.buildingTask: BuildingTask by memory { emptyBuildingTask }
var CreepMemory.roomName by memory { "  " }
var RoomMemory.roomSpawnLevel by memory { 300 }
var RoomMemory.roomSpawnLevelLastTime by memory { 0 }
var RoomMemory.ticksLowEnergy: Int by memory { 0 }
var RoomMemory.harvesterContainers: Array<String> by memory { arrayOf("") }

external interface StructureMemory : MemoryMarker

var Memory.structures: Record<String, StructureMemory> by memory { recordOf() }
var Memory.testing by memory { false }
var StructureMemory.inTransferQueue: Boolean by memory { false }
var StructureMemory.inBuildingQueue: Boolean by memory { false }