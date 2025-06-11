package com.bforbank.pokemon.ui.screens.pokemons

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize

@Composable
fun Modifier.hasLoading(
    isLoading: Boolean,
    shimmerSize: DpSize? = null,
    cornerRadius: Float = 25f, // Rounded corner radius
): Modifier = composed {
    var originalSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current

    // Shimmer animation
    val transition = rememberInfiniteTransition()
    val shimmerOffset by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    this.then(
        if (isLoading && originalSize != IntSize.Zero) {
            val finalSize = shimmerSize ?: with(density) {
                DpSize(originalSize.width.toDp(), originalSize.height.toDp())
            }

            Modifier
                .size(finalSize)
                .clip(RoundedCornerShape(cornerRadius)) // Apply rounded corners
                .drawWithContent {
                    // Draw shimmer effect
                    val shimmerColors = listOf(
                        Color.LightGray.copy(alpha = 0.6f),
                        Color.White.copy(alpha = 0.3f),
                        Color.LightGray.copy(alpha = 0.6f)
                    )

                    val brush = Brush.linearGradient(
                        colors = shimmerColors,
                        start = Offset(shimmerOffset * size.width, 0f),
                        end = Offset(shimmerOffset * size.width + size.width, size.height)
                    )

                    drawIntoCanvas {
                        withTransform({
                            // Clip to rounded corners
                            clipRect(0f, 0f, size.width, size.height)
                        }) {
                            // Draw shimmer gradient
                            drawRect(brush = brush)
                        }
                    }
                }
                .onGloballyPositioned { coordinates ->
                    originalSize = coordinates.size
                }
        } else {
            Modifier.onGloballyPositioned { coordinates ->
                originalSize = coordinates.size
            }
        }
    )
}
