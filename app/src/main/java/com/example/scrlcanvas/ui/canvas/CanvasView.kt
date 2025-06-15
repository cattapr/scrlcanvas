package com.example.scrlcanvas.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.scrlcanvas.ui.canvas.model.PlacedCanvasItem
import com.example.scrlcanvas.ui.canvas.sheets.StickersSheet
import com.example.scrlcanvas.ui.canvas.state.CanvasUiState

@Composable
fun CanvasView(state: CanvasUiState, onEvent: (CanvasUiEvent) -> Unit) {
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
                        //TODO: Draw vertical dividers and snapping lines
                    }


                    Overlays(state)
                }
            }

            StickersSheet(state, onEvent)
        }
    }
}


@Composable
fun Overlays(
    state: CanvasUiState
) {
    state.selectedOverlays.forEach { placedItem ->
        DraggableOverlayItem(placedItem)
    }
}

@Composable
private fun DraggableOverlayItem(
    placedItem: PlacedCanvasItem
) {
    val imageSize = 100.dp

    AsyncImage(
        model = placedItem.overlay.source_url,
        contentDescription = placedItem.overlay.overlay_name,
        modifier = Modifier
            .size(imageSize)
    )
}

@Preview(showBackground = true)
@Composable
private fun CanvasViewPreview() {
    CanvasView(state = CanvasUiState()) {}
}