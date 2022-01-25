package screeps

import screeps.api.*
import screeps.role.CreepRole
import screeps.role.CreepState
import screeps.utils.memory.memory

//var RoomMemory.mainSpawnName : String by memory { "Spawn1" }
var RoomMemory.starting: Boolean by memory { true }
var CreepMemory.state by memory(CreepState.IDLE)
var CreepMemory.role by memory(CreepRole.UNASSIGNED)