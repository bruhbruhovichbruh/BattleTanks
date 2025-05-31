package r.v.stoyanov.battletanks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_DPAD_DOWN
import android.view.KeyEvent.KEYCODE_DPAD_LEFT
import android.view.KeyEvent.KEYCODE_DPAD_RIGHT
import android.view.KeyEvent.KEYCODE_DPAD_UP
import android.view.KeyEvent.KEYCODE_SPACE
import android.view.Menu
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import r.v.stoyanov.battletanks.enums.Direction.UP
import r.v.stoyanov.battletanks.enums.Direction.DOWN
import r.v.stoyanov.battletanks.enums.Direction.LEFT
import r.v.stoyanov.battletanks.enums.Direction.RIGHT
import r.v.stoyanov.battletanks.databinding.ActivityMainBinding
import r.v.stoyanov.battletanks.drawers.BulletDrawer
import r.v.stoyanov.battletanks.drawers.ElementsDrawer
import r.v.stoyanov.battletanks.drawers.EnemyDrawer
import r.v.stoyanov.battletanks.drawers.GridDrawer
import r.v.stoyanov.battletanks.enums.Direction
import r.v.stoyanov.battletanks.enums.Material
import r.v.stoyanov.battletanks.enums.Material.PLAYER_TANK
import r.v.stoyanov.battletanks.models.Coordinate
import r.v.stoyanov.battletanks.models.Element
import r.v.stoyanov.battletanks.models.Tank

const val CELL_SIZE = 50

lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private var editMode = false
    private val playerTank = Tank(
        Element(
            R.id.myTank,
            PLAYER_TANK,
            Coordinate(0, 0),
            PLAYER_TANK.width,
            PLAYER_TANK.height
        ), UP
    )
    private val gridDrawer by lazy {
        GridDrawer(binding.container)
    }

    private val elementsDrawer by lazy {
        ElementsDrawer(binding.container)
    }

    private val bulletDrawer by lazy {
        BulletDrawer(binding.container)
    }

    private val levelStorage by lazy {
        LevelStorage(this)
    }

    private val enemyDrawer by lazy {
        EnemyDrawer(binding.container, elementsDrawer.elementsOnContainer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Menu"

        binding.editorClear.setOnClickListener { elementsDrawer.currentMaterial = Material.EMPTY }
        binding.editorBrick.setOnClickListener { elementsDrawer.currentMaterial = Material.BRICK }
        binding.editorConcrete.setOnClickListener { elementsDrawer.currentMaterial = Material.CONCRETE }
        binding.editorGrass.setOnClickListener { elementsDrawer.currentMaterial = Material.GRASS }
        binding.editorEagle.setOnClickListener { elementsDrawer.currentMaterial = Material.EAGLE }
        binding.container.setOnTouchListener { _, event ->
            elementsDrawer.onTouchContainer(event.x, event.y)
            return@setOnTouchListener true
        }
        elementsDrawer.drawElementsList(levelStorage.loadLevel())
        hideSettings()
        elementsDrawer.elementsOnContainer.add(playerTank.element)
    }

    private fun switchEditMode() {
        editMode = !editMode
        if  (editMode) {
            showSettings()
        } else {
            hideSettings()
        }
    }

    private fun showSettings() {
        gridDrawer.drawGrid()
        binding.materialsContainer.visibility = VISIBLE
    }

    private fun hideSettings() {
        gridDrawer.removeGrid()
        binding.materialsContainer.visibility = INVISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                switchEditMode()
                return true
            }

            R.id.menu_save -> {
                levelStorage.saveLevel(elementsDrawer.elementsOnContainer)
                return true
            }

            R.id.menu_play -> {
                startTheGame()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startTheGame() {
        if (editMode) {
            return
        }
        enemyDrawer.startEnemyCreation()
        enemyDrawer.moveEnemyTanks()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KEYCODE_DPAD_UP -> move(UP)
            KEYCODE_DPAD_DOWN -> move(DOWN)
            KEYCODE_DPAD_LEFT -> move(LEFT)
            KEYCODE_DPAD_RIGHT -> move(RIGHT)
            KEYCODE_SPACE -> bulletDrawer.makeBulletMove(
                binding.myTank, playerTank.direction, elementsDrawer.elementsOnContainer
            )
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun move(direction: Direction) {
        playerTank.move(direction, binding.container, elementsDrawer.elementsOnContainer)
    }
}