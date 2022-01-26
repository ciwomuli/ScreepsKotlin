package screeps

import screeps.api.*
import screeps.role.Role
import screeps.role.CreepState
import screeps.utils.memory.memory

//var RoomMemory.mainSpawnName : String by memory { "Spawn1" }
var RoomMemory.starting: Boolean by memory { true }
var CreepMemory.state by memory(CreepState.IDLE)
var CreepMemory.collectTarget: String by memory { "" }
var CreepMemory.workTarget by memory { "" }