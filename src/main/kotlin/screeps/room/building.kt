package screeps.room

import screeps.api.ConstructionSite
import screeps.api.Room

fun Room.getConstructionSite(): ConstructionSite? {
    return this.extension.constructionSites[0]
}