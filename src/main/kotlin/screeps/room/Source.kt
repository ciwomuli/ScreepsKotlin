package screeps.room

import screeps.api.*
import screeps.collectTarget
import screeps.sourceCount
import screeps.utils.contains
import screeps.utils.mutableRecordOf

fun Room.getFreeSource(): Source {
    var mn = 1000
    var ret: Source = extension.sources[0]!!
    for (source in extension.sources) {
        if (source.id !in memory.sourceCount) {
            memory.sourceCount[source.id] = 0;
        }
        if (memory.sourceCount[source.id]!! < mn) {
            mn = memory.sourceCount[source.id]!!
            ret = source
        }
    }
    memory.sourceCount[ret.id] = memory.sourceCount[ret.id]!! + 1
    return ret
}