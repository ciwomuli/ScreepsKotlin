import screeps.api.CARRY
import screeps.api.Creep
import screeps.api.WORK

fun Creep.getWork(): Int {
    return this.body.count { it.type == WORK }
}

fun Creep.getCarry(): Int {
    return this.body.count { it.type == CARRY }
}