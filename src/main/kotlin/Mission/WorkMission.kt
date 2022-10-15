package Mission

import getCarry
import getWork
import kotlinx.serialization.Serializable
import missionId
import screeps.api.Creep
import kotlin.math.abs

enum class WorkType {
    BUILD, REPAIR, UPGRADE
}

class WorkMission(missionType: MissionType, missionId: String, targetId: String, val workType: WorkType) :
    Mission(missionType, missionId) {
    override fun update() {
        var workers = Context.creeps.values.filter { it.memory.missionId == this.missionId }
        when (workType) {
            WorkType.BUILD, WorkType.REPAIR -> {
                if (workers.isEmpty()) {
                    val avilWorkers = Context.creeps.values.filter { it.getWork() >= 1 }
                        .sortedBy { abs(1 - 1.0 * it.getWork() / it.getCarry()) }
                    if (avilWorkers.isNotEmpty()) {
                        avilWorkers[0].memory.missionId = this.missionId
                    }
                }
            }
            WorkType.UPGRADE -> {

            }
        }
    }
}

@Serializable
class WorkMissionMemory(val targetId: String, val workType: WorkType) : MissionMemory<WorkMission>() {
    override val missionId: String
        get() = "work_$targetId"
    override val missionType: MissionType
        get() = MissionType.WORK

    override fun restoreMission(): WorkMission {
        return WorkMission(missionType, missionId, targetId, workType)
    }
}

class WorkCreepMission : CreepMission() {
    override fun run(creep: Creep) {
        TODO("Not yet implemented")
    }
}