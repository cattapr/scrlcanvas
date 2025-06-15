package com.example.scrlcanvas.ui.canvas.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.scrlcanvas.ui.canvas.model.SnapLine
import com.example.scrlcanvas.ui.canvas.model.SnapResult
import kotlin.math.abs

fun snapIndicator(
    position: Offset,
    canvasSize: Size,
    itemSize: Size,
    threshold: Float
): SnapResult {
    var snappedX = position.x
    var snappedY = position.y
    val snapLines = mutableListOf<SnapLine>()

    val itemWidth = itemSize.width
    val itemHeight = itemSize.height

    // Snap to canvas edges
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

    return SnapResult(Offset(snappedX, snappedY), snapLines)
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