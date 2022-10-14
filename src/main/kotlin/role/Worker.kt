package role

import fillId
import fillType
import getFreeSource
import screeps.api.Creep
import screeps.api.ERR_NOT_IN_RANGE
import screeps.api.Game
import screeps.api.Source
import state
import traveler.travelTo

class Worker : Role() {
    override fun run(creep: Creep) {
        when (creep.memory.state) {
            CreepState.REFILL -> {
                when (creep.memory.fillType) {
                    FillType.SOURCE -> {
                        var target = Game.getObjectById<Source>(creep.memory.fillId)
                        if (target == null) {
                            target = creep.room.getFreeSource()
                            creep.memory.fillId = target.id;
                        }
                        if (target != null && creep.harvest(target) == ERR_NOT_IN_RANGE) {
                            creep.travelTo(target)

                        }
                    }
                }
            }
            CreepState.WORK -> {

            }
        }
    }

    override fun updateState(creep: Creep) {
        TODO("Not yet implemented")
    }
}