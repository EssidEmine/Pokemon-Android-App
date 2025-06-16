package com.bforbank.pokemon.ui.screens.pokemons

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun Animated3DCard() {
    // State for rotation and scale
    var rotationX by remember { mutableStateOf(0f) }
    var rotationY by remember { mutableStateOf(0f) }
    val scale = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    // Update rotation based on touch
                    rotationX = (rotationX - pan.y).coerceIn(-30f, 30f)
                    rotationY = (rotationY + pan.x).coerceIn(-30f, 30f)

                    // Update scale based on pinch
                    coroutineScope.launch {
                        scale.snapTo((scale.value * zoom).coerceIn(0.8f, 1.2f))
                    }
                }
            }
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer(
                    rotationX = rotationX,
                    rotationY = rotationY,
                    scaleX = scale.value,
                    scaleY = scale.value,
                    cameraDistance = 12f // Adjust for 3D perspective
                )
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color.Blue, Color.Cyan, Color.Magenta),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Text(
                text = "Pokémon Card",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
            )
        }
    }
}