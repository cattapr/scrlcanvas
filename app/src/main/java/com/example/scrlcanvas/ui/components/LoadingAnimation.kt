package com.example.scrlcanvas.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingAnimation(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    enterTransition: EnterTransition = fadeIn() + slideInVertically { 500 },
    content: @Composable () -> Unit
) {
    Box {
        Box(
            Modifier
                .padding(top = 20.dp)
                .then(modifier)
        ) {
            AnimatedVisibility(visible = isLoading, exit = fadeOut(), enter = fadeIn()) {
                CustomLoader()
            }
        }
        Box {
            AnimatedVisibility(
                visible = !isLoading,
                exit = fadeOut(),
                enter = enterTransition
            ) {
                content.invoke()
            }
        }
    }
}

@Composable
private fun CustomLoader() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(color = ProgressIndicatorDefaults.circularColor)
    }
}