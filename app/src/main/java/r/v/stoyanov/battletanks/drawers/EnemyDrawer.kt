package r.v.stoyanov.battletanks.drawers

import android.widget.FrameLayout
import r.v.stoyanov.battletanks.activities.CELL_SIZE
import r.v.stoyanov.battletanks.GameCore
import r.v.stoyanov.battletanks.sounds.MainSoundPlayer
import r.v.stoyanov.battletanks.enums.CELLS_TANKS_SIZE
import r.v.stoyanov.battletanks.models.Coordinate
import r.v.stoyanov.battletanks.models.Element
import r.v.stoyanov.battletanks.utils.drawElement
import r.v.stoyanov.battletanks.enums.Direction.DOWN
import r.v.stoyanov.battletanks.enums.Material.ENEMY_TANK
import r.v.stoyanov.battletanks.models.Tank
import r.v.stoyanov.battletanks.utils.checkIfChanceBiggerThanRandom

private const val MAX_ENEMY_AMOUNT = 20

class EnemyDrawer(
    private val container: FrameLayout,
    private val elements: MutableList<Element>,
    private val mainSoundPlayer: MainSoundPlayer,
    private val gameCore: GameCore
) {
    private val respawnList: List<Coordinate>
    private var enemyAmount = 0
    private var currentCoordinate:Coordinate
    val tanks = mutableListOf<Tank>()
    lateinit var bulletDrawer: BulletDrawer
    private var gameStarted = false

    init {
        respawnList = getRespawnList();
        currentCoordinate = respawnList[0]
    }

    private fun getRespawnList(): List<Coordinate> {
        val respawnList = mutableListOf<Coordinate>()
        respawnList.add(Coordinate(0, 0))
        respawnList.add(
            Coordinate(
                0,
                ((container.width - container.width % CELL_SIZE) / CELL_SIZE -
                        (container.width - container.width % CELL_SIZE) / CELL_SIZE % 2) *
                        CELL_SIZE / 2 - CELL_SIZE * CELLS_TANKS_SIZE
            )
        )
        respawnList.add(
            Coordinate(
                0,
                (container.width - container.width % CELL_SIZE) - CELL_SIZE * CELLS_TANKS_SIZE
            )
        )
        return respawnList
    }

    private fun drawEnemy() {
        var index = respawnList.indexOf(currentCoordinate) + 1
        if (index == respawnList.size) {
            index = 0
        }
        currentCoordinate = respawnList[index]
        val enemyTank = Tank(
            Element(
                material = ENEMY_TANK,
                coordinate = currentCoordinate,
            ), DOWN,
            this
        )
        enemyTank.element.drawElement(container)
        tanks.add(enemyTank)
    }

    private fun moveEnemyTanks() {
        Thread(Runnable {
            while (true) {
                if (!gameCore.isPlaying()) {
                    continue
                }
                goThroughAllTanks()
                Thread.sleep(400)
            }
        }).start()
    }

    private fun goThroughAllTanks() {
        if (tanks.isNotEmpty()) {
            mainSoundPlayer.tankMove()
        } else {
            mainSoundPlayer.tankStop()
        }
        tanks.toList().forEach {
            it.move(it.direction, container, elements)
            if (checkIfChanceBiggerThanRandom(10)) {
                bulletDrawer.addNewBulletForTank(it)
            }
        }
    }

    fun startEnemyCreation() {
        if (gameStarted) {
            return
        }
        gameStarted = true
        Thread(Runnable {
            while (enemyAmount < MAX_ENEMY_AMOUNT) {
                if (!gameCore.isPlaying()) {
                    continue
                }
                drawEnemy()
                enemyAmount++
                Thread.sleep(3000)
            }
        }).start()
        moveEnemyTanks()
    }

    private fun isAllTanksDestroyed(): Boolean {
        return enemyAmount == MAX_ENEMY_AMOUNT && tanks.toList().isEmpty()
    }

    private fun getPlayerScore() = enemyAmount * 100

    fun removeTank(tankIndex: Int) {
        tanks.removeAt(tankIndex)
        if (isAllTanksDestroyed()) {
            gameCore.playerWon(getPlayerScore())
        }
    }
}