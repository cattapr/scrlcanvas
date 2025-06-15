package com.example.scrlcanvas.ui.canvas.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.scrlcanvas.data.model.OverlayItem
import com.example.scrlcanvas.ui.canvas.CanvasUiEvent
import com.example.scrlcanvas.ui.canvas.state.CanvasUiState
import com.example.scrlcanvas.ui.components.LoadingAnimation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StickersSheet(state: CanvasUiState, onEvent: (CanvasUiEvent) -> Unit) {
    TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (state.showBottomSheet) {
        ModalBottomSheet(
            containerColor = Color.White,
            contentColor = Color.Black,
            onDismissRequest = { onEvent(CanvasUiEvent.OnToggleSheet) },
            sheetState = sheetState
        ) {
            LoadingAnimation(isLoading = state.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        state.overlays?.forEach { category ->
                            item {
                                Text(
                                    text = category.title,
                                    modifier = Modifier
                                        .background(
                                            if (category == state.selectedCategory) MaterialTheme.colorScheme.primary
                                            else Color.LightGray,
                                            shape = MaterialTheme.shapes.small
                                        )
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                        .clickable {
                                            onEvent(
                                                CanvasUiEvent.OnSetOverlayCategory(
                                                    category
                                                )
                                            )
                                        },
                                    color = if (category == state.selectedCategory) Color.White else Color.Black
                                )
                            }
                        }
                    }

                    state.selectedCategory?.let { overlay ->
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            overlayItems(overlay.items, onEvent)
                        }
                    }
                }
            }
        }
    }
}


private fun LazyGridScope.overlayItems(overlayItems: List<OverlayItem>, onEvent: (CanvasUiEvent) -> Unit) {
    items(overlayItems) { item ->
        AsyncImage(
            model = item.source_url,
            contentDescription = item.overlay_name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.LightGray)
                .clickable {
                    onEvent(CanvasUiEvent.OnOverlaySelected(item))
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StickersSheetPreview() {
    Surface(
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        StickersSheet(CanvasUiState()) {}
    }
}