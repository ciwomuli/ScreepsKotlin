import role.CreepRole
import screeps.api.CreepMemory
import screeps.utils.memory.memory

var CreepMemory.role by memory(CreepRole.UNKNOWN)
var CreepMemory.state by memory(CreepState.IDLE)
var CreepMemory.fillType by memory(FillType.SOURCE)
var CreepMemory.fillId: String? by memory()
var CreepMemory.missionType by memory(MissionType.IDLE)
var CreepMemory.missionId: String? by memory()

enum class CreepState {
    IDLE, REFILL, WORK
}

enum class FillType {
    SOURCE, STORAGE
}