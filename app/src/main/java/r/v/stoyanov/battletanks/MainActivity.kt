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
import android.view.View.GONE
import r.v.stoyanov.battletanks.enums.Direction.UP
import r.v.stoyanov.battletanks.enums.Direction.DOWN
import r.v.stoyanov.battletanks.enums.Direction.LEFT
import r.v.stoyanov.battletanks.enums.Direction.RIGHT
import r.v.stoyanov.battletanks.databinding.ActivityMainBinding
import r.v.stoyanov.battletanks.drawers.BulletDrawer
import r.v.stoyanov.battletanks.drawers.ElementsDrawer
import r.v.stoyanov.battletanks.drawers.GridDrawer
import r.v.stoyanov.battletanks.drawers.TankDrawer
import r.v.stoyanov.battletanks.enums.Material

const val CELL_SIZE = 50

lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private var editMode = false
    private val gridDrawer by lazy {
        GridDrawer(binding.container)
    }

    private val elementsDrawer by lazy {
        ElementsDrawer(binding.container)
    }

    private val tankDrawer by lazy {
        TankDrawer(binding.container)
    }

    private val bulletDrawer by lazy {
        BulletDrawer(binding.container)
    }

    private val levelStorage by lazy {
        LevelStorage(this)
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
        binding.editorEnemyRespawn.setOnClickListener { elementsDrawer.currentMaterial = Material.ENEMY_TANK_RESPAWN }
        binding.editorPlayerRespawn.setOnClickListener { elementsDrawer.currentMaterial = Material.PLAYER_TANK_RESPAWN }
        binding.container.setOnTouchListener { _, event ->
            elementsDrawer.onTouchContainer(event.x, event.y)
            return@setOnTouchListener true
        }
        elementsDrawer.drawElementsList(levelStorage.loadLevel())
        hideSettings()
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
        elementsDrawer.changeElementsVisibility(true)
    }

    private fun hideSettings() {
        gridDrawer.removeGrid()
        binding.materialsContainer.visibility = GONE
        elementsDrawer.changeElementsVisibility(false)
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

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KEYCODE_DPAD_UP -> tankDrawer.move(binding.myTank, UP, elementsDrawer.elementsOnContainer)
            KEYCODE_DPAD_DOWN -> tankDrawer.move(binding.myTank, DOWN, elementsDrawer.elementsOnContainer)
            KEYCODE_DPAD_LEFT -> tankDrawer.move(binding.myTank, LEFT, elementsDrawer.elementsOnContainer)
            KEYCODE_DPAD_RIGHT -> tankDrawer.move(binding.myTank, RIGHT, elementsDrawer.elementsOnContainer)
            KEYCODE_SPACE -> bulletDrawer.makeBulletMove(binding.myTank, tankDrawer.currentDirection, elementsDrawer.elementsOnContainer)
        }
        return super.onKeyDown(keyCode, event)
    }
}