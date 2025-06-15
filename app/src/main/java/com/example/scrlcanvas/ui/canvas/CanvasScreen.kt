package com.example.scrlcanvas.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.scrlcanvas.ui.canvas.model.PlacedCanvasItem
import com.example.scrlcanvas.ui.canvas.sheets.StickersSheet
import com.example.scrlcanvas.ui.canvas.state.CanvasUiState

@Composable
fun CanvasScreen(state: CanvasUiState, onEvent: (CanvasUiEvent) -> Unit) {
    val canvasWidth = 800.dp
    val canvasHeight = 300.dp
    val scrollState = rememberScrollState()
    val initialHorizontalPadding = 24.dp
    val hasScrolled = scrollState.value > 0


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(shape = CircleShape, containerColor = Color.White, onClick = {
                onEvent(CanvasUiEvent.OnToggleSheet)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        containerColor = Color.Black,
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .padding(horizontal = if (hasScrolled) 0.dp else initialHorizontalPadding)
                    .height(canvasHeight)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                onEvent(CanvasUiEvent.OnDeselectCanvasOverlays)
                            }
                        )
                    }
            ) {
                Box(
                    modifier = Modifier
                        .width(canvasWidth)
                        .height(canvasHeight)

                ) {
                    Canvas(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.White)
                    ) {
                        val sectionWidth = size.width / 3

                        drawLine(
                            color = Color.Black,
                            start = Offset(sectionWidth, 0f),
                            end = Offset(sectionWidth, size.height),
                            strokeWidth = 4f
                        )
                        drawLine(
                            color = Color.Black,
                            start = Offset(sectionWidth * 2, 0f),
                            end = Offset(sectionWidth * 2, size.height),
                            strokeWidth = 4f
                        )

                        state.snapLines.forEach { line ->
                            drawLine(
                                color = Color.Yellow,
                                start = line.start,
                                end = line.end,
                                strokeWidth = 4f
                            )
                        }
                    }


                    Overlays(state, canvasWidth, canvasHeight, onEvent)
                }
            }

            StickersSheet(state, onEvent)
        }
    }
}


@Composable
fun Overlays(
    state: CanvasUiState,
    canvasWidth: Dp,
    canvasHeight: Dp,
    onEvent: (CanvasUiEvent) -> Unit
) {
    state.selectedOverlays.forEach { placedItem ->
        DraggableOverlayItem(placedItem, canvasWidth, canvasHeight, onEvent)
    }
}


@Composable
private fun DraggableOverlayItem(
    placedItem: PlacedCanvasItem,
    canvasWidth: Dp,
    canvasHeight: Dp,
    onEvent: (CanvasUiEvent) -> Unit
) {
    val density = LocalDensity.current
    val canvasSizePx = with(density) {
        Size(canvasWidth.toPx(), canvasHeight.toPx())
    }

    var imageSize by remember { mutableStateOf<Size?>(null) }
    val offset = placedItem.position
    val currentOnEvent by rememberUpdatedState(onEvent)

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(placedItem.overlay.source_url)
            .crossfade(true)
            .build(),
        contentDescription = placedItem.overlay.overlay_name,
        contentScale = ContentScale.Fit,
        onSuccess = { success ->
            val width = success.result.drawable.intrinsicWidth
            val height = success.result.drawable.intrinsicHeight
            imageSize = with(density) { Size(width.toDp().toPx(), height.toDp().toPx()) }
        },
        modifier = Modifier
            .then(
                if (imageSize != null) {
                    Modifier.size(
                        with(density) { imageSize!!.width.toDp() },
                        with(density) { imageSize!!.height.toDp() }
                    )
                } else {
                    Modifier.size(50.dp) // fallback size while loading
                }
            )
            .graphicsLayer {
                translationX = offset.x
                translationY = offset.y
            }
            .pointerInput(placedItem.overlay.id to placedItem.isSelected) {
                if (placedItem.isSelected) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        imageSize?.let {
                            currentOnEvent(
                                CanvasUiEvent.OnCanvasOverlayPositionChange(
                                    placedItem.overlay.id,
                                    dragAmount,
                                    canvasSizePx,
                                    it
                                )
                            )
                        }
                    }
                }
            }
            .pointerInput(placedItem.overlay.id) {
                detectTapGestures {
                    currentOnEvent(CanvasUiEvent.OnCanvasOverlayTapped(placedItem.overlay.id))
                }
            }
            .border(
                width = if (placedItem.isSelected) 2.dp else 0.dp,
                color = if (placedItem.isSelected) Color.Green else Color.Transparent
            )
    )
}



@Preview(showBackground = true)
@Composable
private fun CanvasScreenPreview() {
    CanvasScreen(state = CanvasUiState()) {}
}