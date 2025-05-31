package r.v.stoyanov.battletanks.drawers

import android.widget.FrameLayout
import r.v.stoyanov.battletanks.CELL_SIZE
import r.v.stoyanov.battletanks.models.Coordinate

class EnemyDrawer(private val container: FrameLayout) {
    private val respawnList: List<Coordinate>

    init {
        respawnList = getRespawnList();
    }

    private fun getRespawnList(): List<Coordinate> {
        val respawnList = mutableListOf<Coordinate>()
        respawnList.add(Coordinate(0, 0))
        respawnList.add(Coordinate(0, container.width / 2 - CELL_SIZE))
        respawnList.add(Coordinate(0, container.width - CELL_SIZE))
        return respawnList
    }
}