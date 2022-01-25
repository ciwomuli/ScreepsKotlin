package screeps.creep
import screeps.api.Creep
import screeps.role.Role
import screeps.state

fun Creep.run(role: Role):Unit{
    this.memory.state = role.getNextState(this)
    role.run(this)
}