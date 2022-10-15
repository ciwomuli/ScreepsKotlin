package Behaviour

import screeps.api.Creep
import state

fun Creep.run() {
    when (this.memory.state) {
        CreepState.IDLE -> IdleBehaviour.run(this)
        CreepState.WORK -> WorkBehaviour.run(this)
        CreepState.REFILL -> ReFillBehaviour.run(this)

    }
}

abstract class Behaviour {
    abstract fun run(creep: Creep)
}

object IdleBehaviour : Behaviour() {
    override fun run(creep: Creep) {

    }
}

object ReFillBehaviour : Behaviour() {
    override fun run(creep: Creep) {

    }
}

object WorkBehaviour : Behaviour() {
    override fun run(creep: Creep) {

    }
}