package r.v.stoyanov.battletanks.utils

import android.view.View
import r.v.stoyanov.battletanks.binding
import r.v.stoyanov.battletanks.models.Coordinate

fun View.checkViewCanMoveThroughBorder(coordinate: Coordinate): Boolean {
    return coordinate.top >= 0 &&
            coordinate.top + this.height <= binding.container.height &&
            coordinate.left >= 0 &&
            coordinate.left + this.width <= binding.container.width
}