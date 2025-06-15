package com.example.scrlcanvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.scrlcanvas.ui.canvas.CanvasView
import com.example.scrlcanvas.ui.canvas.state.CanvasUiState
import com.example.scrlcanvas.ui.canvas.viewmodel.CanvasViewModel
import com.example.scrlcanvas.ui.theme.ScrlcanvasTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val canvasViewModel: CanvasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var canvasUiState: CanvasUiState by mutableStateOf(CanvasUiState())

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                canvasViewModel.state.collectLatest { canvasUiState = it }
            }
        }

        enableEdgeToEdge()
        setContent {
            ScrlcanvasTheme {
                CanvasView(
                    canvasUiState,
                    canvasViewModel::onEvent
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ScrlcanvasTheme {
        CanvasView(CanvasUiState()) {}
    }
}