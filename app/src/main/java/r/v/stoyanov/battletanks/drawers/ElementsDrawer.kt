package r.v.stoyanov.battletanks.drawers

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.DrawableRes
import r.v.stoyanov.battletanks.CELL_SIZE
import r.v.stoyanov.battletanks.R
import r.v.stoyanov.battletanks.enums.Material
import r.v.stoyanov.battletanks.models.Coordinate
import r.v.stoyanov.battletanks.models.Element
import r.v.stoyanov.battletanks.utils.getElementByCoordinates


class ElementsDrawer(val container: FrameLayout) {
    var currentMaterial = Material.EMPTY
    val elementsOnContainer = mutableListOf<Element>()

    fun onTouchContainer(x: Float, y: Float) {
        val topMargin = y.toInt() - (y.toInt() % CELL_SIZE)
        val leftMargin = x.toInt() - (x.toInt() % CELL_SIZE)
        val coordinate = Coordinate(topMargin, leftMargin)
        selectMaterial(coordinate)
        if (currentMaterial == Material.EMPTY) {
            eraseView(coordinate)
        } else {
            drawOrReplaceView(coordinate);
        }
    }

    private fun drawOrReplaceView(coordinate: Coordinate) {
        val viewOnCoordinate = getElementByCoordinates(coordinate, elementsOnContainer)
        if (viewOnCoordinate == null) {
            selectMaterial(coordinate)
            return
        }
        if (viewOnCoordinate.material != currentMaterial) {
            replaceView(coordinate)
        }
    }

    private fun replaceView(coordinate: Coordinate) {
        eraseView(coordinate);
        selectMaterial(coordinate);
    }

    private fun eraseView(coordinate: Coordinate) {
        val elementOnCoordinate = getElementByCoordinates(coordinate, elementsOnContainer)
        if (elementOnCoordinate != null) {
            val erasingView = container.findViewById<View>(elementOnCoordinate.viewId)
            container.removeView(erasingView)
            elementsOnContainer.remove(elementOnCoordinate)
        }
    }

    fun selectMaterial(coordinate: Coordinate) {
        when (currentMaterial) {
            Material.BRICK -> drawView(R.drawable.brick, coordinate)
            Material.CONCRETE -> drawView(R.drawable.concrete, coordinate)
            Material.GRASS -> drawView(R.drawable.grass, coordinate)
            Material.EAGLE -> {
                removeExistingEagle()
                drawView(R.drawable.eagle, coordinate, 4, 3)
            }
            Material.EMPTY -> {}
        }
    }

    private fun removeExistingEagle() {
        elementsOnContainer.firstOrNull { it.material == Material.EAGLE }?.coordinate?.let {
            eraseView(it)
        }
    }

    private fun drawView(
        @DrawableRes image: Int,
        coordinate: Coordinate,
        width: Int = 1,
        height: Int = 1
    ) {
        val view = ImageView(container.context)
        val layoutParams = FrameLayout.LayoutParams(width * CELL_SIZE, height * CELL_SIZE)
        view.setImageResource(image)
        layoutParams.topMargin = coordinate.top
        layoutParams.leftMargin = coordinate.left
        val viewId = View.generateViewId()
        view.id = viewId
        view.layoutParams = layoutParams
        container.addView(view)
        elementsOnContainer.add(Element(viewId, currentMaterial, coordinate, width, height))

    }
}