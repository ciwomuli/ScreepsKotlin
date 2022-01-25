package screeps.role
import screeps.api.Creep
interface Role {
    fun getNextState(creep:Creep):CreepState
    fun run(creep:Creep)
}
enum class CreepRole{
    UNASSIGNED,
    UPGRADER,
    BUILDER,
    HARVESTER
}