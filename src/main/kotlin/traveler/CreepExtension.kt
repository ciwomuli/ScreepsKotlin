package traveler

import screeps.api.Creep
import screeps.api.NavigationTarget
import screeps.api.RoomPosition
import screeps.api.ScreepsReturnCode
import screeps.api.structures.Structure

/**
 * Use Traveler to travel to target destination.
 */
fun Creep.travelTo(target: NavigationTarget, travelToOptions: TravelToOptions? = null): ScreepsReturnCode {
    return if (travelToOptions == null) {
        (this.unsafeCast<TravelerCreep>()).travelTo(target)
    } else {
        (this.unsafeCast<TravelerCreep>()).travelTo(target, travelToOptions)
    }
}