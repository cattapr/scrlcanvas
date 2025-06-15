package com.example.scrlcanvas.ui.canvas.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
                    StickersCategoryRow(state, onEvent)
                    state.selectedCategory?.let {
                        StickersGrid(it.items, onEvent)
                    }
                }
            }
        }
    }
}

@Composable
private fun StickersCategoryRow(state: CanvasUiState, onEvent: (CanvasUiEvent) -> Unit) {
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
                            onEvent(CanvasUiEvent.OnSetOverlayCategory(category))
                        },
                    color = if (category == state.selectedCategory) Color.White else Color.Black
                )
            }
        }
    }
}

@Composable
private fun StickersGrid(items: List<OverlayItem>, onEvent: (CanvasUiEvent) -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.wrapContentWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                OverlayGridItem(item, onEvent)
            }
        }
    }

}


@Composable
private fun OverlayGridItem(
    item: OverlayItem,
    onEvent: (CanvasUiEvent) -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth()
            .background(Color.LightGray)
            .clickable {
                onEvent(CanvasUiEvent.OnOverlaySelected(item))
            },
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(item.source_url)
                .crossfade(true)
                .build(),
            contentDescription = item.overlay_name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize(0.8f)
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