package com.bforbank.pokemon.ui.screens.pokemons

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun BankCard3D() {
    // State for rotation and scale
    var rotationX by remember { mutableStateOf(0f) }
    var rotationY by remember { mutableStateOf(0f) }
    val scale = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope()

    // Shimmer effect state
    val shimmerTranslate = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        shimmerTranslate.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    // Jiggle animation state for the card
    val jiggleRotation = remember { Animatable(0f) }

    // Function to trigger jiggle animation for the card
    fun triggerJiggleAnimation() {
        coroutineScope.launch {
            jiggleRotation.animateTo(
                targetValue = 10f,
                animationSpec = keyframes {
                    durationMillis = 200
                    0f at 0
                    10f at 50
                    -10f at 100
                    10f at 150
                    -10f at 200
                    0f at 250
                }
            )
        }
    }


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
                        scale.snapTo((scale.value * zoom).coerceIn(0.9f, 1.1f))
                    }
                }
            }
    ) {
        // Bank Card
        Box(
            modifier = Modifier
                .size(300.dp, 180.dp)
                .graphicsLayer(
                    rotationX = rotationX,
                    rotationY = rotationY,
                    scaleX = scale.value,
                    scaleY = scale.value,
                    rotationZ = jiggleRotation.value, // Apply jiggle rotation to the card
                    cameraDistance = 12f // Adjust for 3D perspective
                )
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF1E3A8A), Color(0xFF1E40AF), Color(0xFF1E3A8A)),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            // Card Chip (with parallax effect)
            Box(
                modifier = Modifier
                    .size(40.dp, 30.dp)
                    .background(Color.Yellow, shape = RoundedCornerShape(4.dp))
                    .align(androidx.compose.ui.Alignment.TopStart)
                    .graphicsLayer {
                        translationX = 20.dp.toPx() + rotationY * 2 // Parallax effect
                        translationY = 20.dp.toPx() + rotationX * 2 // Parallax effect
                    }
            )

            // Card Number (with parallax effect)
            Text(
                text = "1234 5678 9012 3456",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(androidx.compose.ui.Alignment.BottomCenter)
                    .graphicsLayer {
                        translationY = (-40).dp.toPx() + rotationX * 1.5f // Parallax effect
                    }
            )

            // Expiry Date (with parallax effect)
            Text(
                text = "12/25",
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier
                    .align(androidx.compose.ui.Alignment.BottomEnd)
                    .graphicsLayer {
                        translationX = (-20).dp.toPx() + rotationY * 1.5f // Parallax effect
                        translationY = (-20).dp.toPx() + rotationX * 1.5f // Parallax effect
                    }
            )

            // Copy Icon
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "Copy Card Number",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .align(androidx.compose.ui.Alignment.TopEnd)
                    .graphicsLayer {
                        translationX = (-20).dp.toPx()
                        translationY = 20.dp.toPx()
                    }
                    .clickable {
                        // Trigger jiggle animation for the card
                        triggerJiggleAnimation()
                    }
            )

            // Shimmer Effect (Flair)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.3f),
                                Color.Transparent
                            ),
                            start = androidx.compose.ui.geometry.Offset(
                                x = shimmerTranslate.value * 1000,
                                y = shimmerTranslate.value * 1000
                            ),
                            end = androidx.compose.ui.geometry.Offset(
                                x = shimmerTranslate.value * 1000 + 500,
                                y = shimmerTranslate.value * 1000 + 500
                            )
                        )
                    )
            )
        }
    }
}