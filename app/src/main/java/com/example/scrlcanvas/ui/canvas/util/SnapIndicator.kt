package com.example.scrlcanvas.ui.canvas.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.scrlcanvas.ui.canvas.model.PlacedCanvasItem
import com.example.scrlcanvas.ui.canvas.model.SnapLine
import com.example.scrlcanvas.ui.canvas.model.SnapResult
import com.example.scrlcanvas.ui.canvas.model.enums.SnapEdge
import kotlin.math.abs

fun snapIndicator(
    position: Offset,
    canvasSize: Size,
    itemSize: Size,
    threshold: Float,
    allItems: List<PlacedCanvasItem>
): SnapResult {
    val snapLines = mutableListOf<SnapLine>()

    val snapToCanvasEdges = snapToCanvasEdges(position, threshold, canvasSize, itemSize, snapLines)
    val snappingFinalPosition = snapToOtherCanvasItems(
        snapToCanvasEdges,
        threshold,
        canvasSize,
        itemSize,
        allItems,
        snapLines
    )

    return SnapResult(snappingFinalPosition, snapLines)
}

private fun snapToCanvasEdges(
    position: Offset,
    threshold: Float,
    canvasSize: Size,
    itemSize: Size,
    snapLines: MutableList<SnapLine>
): Offset {
    var snappedX = position.x
    var snappedY = position.y

    val itemWidth = itemSize.width
    val itemHeight = itemSize.height
    snapToLeftEdge(position, threshold, canvasSize, {
        snappedX = 0f
    }, snapLines)

    snapToTopEdge(position, threshold, canvasSize, {
        snappedY = 0f
    }, snapLines)

    snapToRightEdge(position, itemWidth, threshold, canvasSize, {
        snappedX = canvasSize.width - itemWidth
    }, snapLines)

    snapToBottomEdge(position, itemHeight, threshold, canvasSize, {
        snappedY = canvasSize.height - itemHeight
    }, snapLines)

    snapToVerticalCenterGuideLine(position, itemWidth, threshold, canvasSize, {
        snappedX = it
    }, snapLines)

    snapToHorizontalCenterGuideLine(position, itemHeight, threshold, canvasSize, {
        snappedY = it
    }, snapLines)

    snapToVerticalCenterOfEachThird(position, itemWidth, threshold, canvasSize, {
        snappedX = it
    }, snapLines)

    return Offset(snappedX, snappedY)
}

private fun snapToOtherCanvasItems(
    position: Offset,
    threshold: Float,
    canvasSize: Size,
    itemSize: Size,
    allItems: List<PlacedCanvasItem>,
    snapLines: MutableList<SnapLine>
): Offset {
    var snappedX = position.x
    var snappedY = position.y

    snapToOtherItems(position, itemSize, allItems, threshold, canvasSize, {
        snappedX = it.x
        snappedY = it.y
    }, snapLines)

    return Offset(snappedX, snappedY)
}

private fun snapToLeftEdge(
    position: Offset,
    threshold: Float,
    canvasSize: Size,
    onSnap: () -> Unit,
    snapLines: MutableList<SnapLine>
) {
    if (abs(position.x) < threshold) {
        onSnap()
        snapLines.add(SnapLine(Offset(0f, 0f), Offset(0f, canvasSize.height)))
    }
}

private fun snapToTopEdge(
    position: Offset,
    threshold: Float,
    canvasSize: Size,
    onSnap: () -> Unit,
    snapLines: MutableList<SnapLine>
) {
    if (abs(position.y) < threshold) {
        onSnap()
        snapLines.add(SnapLine(Offset(0f, 0f), Offset(canvasSize.width, 0f)))
    }
}

private fun snapToRightEdge(
    position: Offset,
    itemWidth: Float,
    threshold: Float,
    canvasSize: Size,
    onSnap: () -> Unit,
    snapLines: MutableList<SnapLine>
) {
    if (abs((position.x + itemWidth) - canvasSize.width) < threshold) {
        onSnap()
        snapLines.add(
            SnapLine(
                Offset(canvasSize.width, 0f),
                Offset(canvasSize.width, canvasSize.height)
            )
        )
    }
}

private fun snapToBottomEdge(
    position: Offset,
    itemHeight: Float,
    threshold: Float,
    canvasSize: Size,
    onSnap: () -> Unit,
    snapLines: MutableList<SnapLine>
) {
    val itemBottom = position.y + itemHeight
    if (abs(itemBottom - canvasSize.height) < threshold) {
        onSnap()
        val y = canvasSize.height - 3f // adjust slightly inside
        snapLines.add(
            SnapLine(
                Offset(0f, y),
                Offset(canvasSize.width, y)
            )
        )
    }
}

private fun snapToVerticalCenterGuideLine(
    position: Offset,
    itemWidth: Float,
    threshold: Float,
    canvasSize: Size,
    onSnap: (Float) -> Unit,
    snapLines: MutableList<SnapLine>
) {
    val leftEdge = position.x
    val rightEdge = position.x + itemWidth
    val guideLines = listOf(canvasSize.width / 3, canvasSize.width * 2 / 3)

    for (lineX in guideLines) {
        when {
            abs(leftEdge - lineX) < threshold -> {
                onSnap(lineX)
                snapLines.add(SnapLine(Offset(lineX, 0f), Offset(lineX, canvasSize.height)))
                return
            }

            abs(rightEdge - lineX) < threshold -> {
                onSnap(lineX - itemWidth)
                snapLines.add(SnapLine(Offset(lineX, 0f), Offset(lineX, canvasSize.height)))
                return
            }
        }
    }
}

private fun snapToHorizontalCenterGuideLine(
    position: Offset,
    itemHeight: Float,
    threshold: Float,
    canvasSize: Size,
    onSnap: (Float) -> Unit,
    snapLines: MutableList<SnapLine>
) {
    val itemCenterY = position.y + itemHeight / 2
    val canvasCenterY = canvasSize.height / 2

    if (abs(itemCenterY - canvasCenterY) < threshold) {
        val snappedY = canvasCenterY - itemHeight / 2
        onSnap(snappedY)
        snapLines.add(
            SnapLine(
                Offset(0f, canvasCenterY),
                Offset(canvasSize.width, canvasCenterY)
            )
        )
    }
}


private fun snapToVerticalCenterOfEachThird(
    position: Offset,
    itemWidth: Float,
    threshold: Float,
    canvasSize: Size,
    onSnap: (Float) -> Unit,
    snapLines: MutableList<SnapLine>
) {
    val itemCenterX = position.x + itemWidth / 2
    val thirdWidth = canvasSize.width / 3

    val guideLines = listOf(
        thirdWidth / 2,                      // Center of first box
        thirdWidth + thirdWidth / 2,         // Center of second box
        2 * thirdWidth + thirdWidth / 2      // Center of third box
    )

    for (lineX in guideLines) {
        if (abs(itemCenterX - lineX) < threshold) {
            val snappedX = lineX - itemWidth / 2
            onSnap(snappedX)
            snapLines.add(
                SnapLine(
                    Offset(lineX, 0f),
                    Offset(lineX, canvasSize.height)
                )
            )
            return
        }
    }
}

private fun snapToOtherItems(
    position: Offset,
    itemSize: Size,
    allItems: List<PlacedCanvasItem>,
    threshold: Float,
    canvasSize: Size,
    onSnap: (Offset) -> Unit,
    snapLines: MutableList<SnapLine>
) {
    val itemLeft = position.x
    val itemRight = position.x + itemSize.width
    val itemTop = position.y
    val itemBottom = position.y + itemSize.height

    for (other in allItems) {
        val otherLeft = other.position.x
        val otherRight = other.position.x + itemSize.width
        val otherTop = other.position.y
        val otherBottom = other.position.y + itemSize.height

        val snapDirection = when {
            abs(itemLeft - otherRight) < threshold -> SnapEdge.LEFT_TO_RIGHT
            abs(itemRight - otherLeft) < threshold -> SnapEdge.RIGHT_TO_LEFT
            abs(itemTop - otherBottom) < threshold -> SnapEdge.TOP_TO_BOTTOM
            abs(itemBottom - otherTop) < threshold -> SnapEdge.BOTTOM_TO_TOP
            else -> null
        }

        when (snapDirection) {
            SnapEdge.LEFT_TO_RIGHT -> {
                onSnap(Offset(otherRight, position.y))
                snapLines.add(
                    SnapLine(
                        Offset(otherRight, 0f),
                        Offset(otherRight, canvasSize.height)
                    )
                )
                return
            }

            SnapEdge.RIGHT_TO_LEFT -> {
                onSnap(Offset(otherLeft - itemSize.width, position.y))
                snapLines.add(SnapLine(Offset(otherLeft, 0f), Offset(otherLeft, canvasSize.height)))
                return
            }

            SnapEdge.TOP_TO_BOTTOM -> {
                onSnap(Offset(position.x, otherBottom))
                snapLines.add(
                    SnapLine(
                        Offset(0f, otherBottom),
                        Offset(canvasSize.width, otherBottom)
                    )
                )
                return
            }

            SnapEdge.BOTTOM_TO_TOP -> {
                onSnap(Offset(position.x, otherTop - itemSize.height))
                snapLines.add(SnapLine(Offset(0f, otherTop), Offset(canvasSize.width, otherTop)))
                return
            }

            null -> {}
        }
    }

}