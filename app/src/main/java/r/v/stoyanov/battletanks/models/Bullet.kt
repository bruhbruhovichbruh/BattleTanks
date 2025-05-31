package r.v.stoyanov.battletanks.models

import android.view.View
import r.v.stoyanov.battletanks.enums.Direction

data class Bullet (
    val view: View,
    val direction: Direction,
    val tank: Tank,
    var canMoveFurther: Boolean = true
)