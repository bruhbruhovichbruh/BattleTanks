package r.v.stoyanov.battletanks.models

import android.view.View
import r.v.stoyanov.battletanks.enums.Material

data class Element constructor(
    val viewId: Int = View.generateViewId(),
    val material: Material,
    var coordinate: Coordinate,
    val width: Int,
    val height: Int
)
