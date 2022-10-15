package Mission

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import screeps.api.*
import screeps.utils.unsafe.delete

enum class MissionType {
    IDLE, TRANSFER, WORK
}

abstract class Mission(val missionType: MissionType, val missionId: String) {
    abstract fun update()
    open var complete = false
}

abstract class MissionMemory<T : Mission> {
    abstract val missionType: MissionType
    abstract val missionId: String
    abstract fun restoreMission(): T
    open fun isComplete(): Boolean = false
}

abstract class CreepMission {
    abstract fun run(creep: Creep)
}

@Serializable
data class ActiveMissionMemory(
    /*
    Rightnow there is no polymorphic serializer for kotlin-js so we have to resort to this
     */
    val workMissionMemory: MutableList<WorkMissionMemory> = mutableListOf()
)

object Missions {
    val missionMemory: ActiveMissionMemory
    val activeMissions: MutableMap<String, Mission> = mutableMapOf()

    init {
        missionMemory = Memory.activeMissionMemory ?: ActiveMissionMemory(mutableListOf())
    }

    fun load() {
        for (mission in missionMemory.workMissionMemory) {
            if (!activeMissions.containsKey(mission.missionId))
                activeMissions[mission.missionId] = mission.restoreMission()
        }
    }

    fun update() {
        for ((_, mission) in activeMissions) {
            if (!mission.complete)
                mission.update()
        }
    }

    fun save() {
        Memory.activeMissionMemory = missionMemory
    }

    private var Memory.activeMissionMemory: ActiveMissionMemory?
        get() {
            val internal = this.asDynamic()._missionMemory
            return if (internal == null) null else Json.decodeFromString(internal as String)
        }
        set(value) {
            val stringyfied = if (value == null) null else Json.encodeToString(value)
            this.asDynamic()._missionMemory = stringyfied
        }
}