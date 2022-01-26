package screeps.creep
import screeps.api.Creep
import screeps.role.extension
import screeps.state

fun Creep.run(){
    //console.log("1$name")
    this.memory.state = this.extension.getNextState()
    this.extension.run()
}