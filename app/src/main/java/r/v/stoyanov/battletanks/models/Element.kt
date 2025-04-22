package r.v.stoyanov.battletanks.models

import r.v.stoyanov.battletanks.enums.Material

data class Element(
    val viewId: Int,
    val material: Material,
    val coordinate: Coordinate
) {

}
