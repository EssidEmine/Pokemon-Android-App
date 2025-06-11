package com.bforbank.pokemon.ui.screens.pokemons

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun DrawableBankCard(
    modifier: Modifier = Modifier,
    cardNumber: String = "•••• •••• •••• 4242",
    cardHolder: String = "JOHN DOE",
    expiryDate: String = "12/25"
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner()
    val coroutineScope = rememberCoroutineScope()
    
    // Drawing state
    val paths = remember { mutableStateListOf<Pair<Path, Color>>() }
    val currentPath = remember { mutableStateOf<Path?>(null) }
    val currentPathColor = remember { mutableStateOf(Color.White) }
    
    // Sensor values for gyroscope
    var rotationX by remember { mutableStateOf(0f) }
    var rotationY by remember { mutableStateOf(0f) }
    
    // Last sensor update timestamp to detect if gyro is moving
    var lastSensorUpdateTime by remember { mutableStateOf(0L) }
    var autoShineActive by remember { mutableStateOf(true) }
    
    // Auto-shine animation state
    val autoShinePosition = remember { Animatable(initialValue = -1f) }
    
    // Color selection for drawing
    var currentColor by remember { mutableStateOf(Color.White) }
    val availableColors = listOf(
        Color.White, Color.Yellow, Color.Red, Color.Green, Color.Blue, Color.Cyan, Color.Magenta
    )
    
    // Drawing mode state
    var isDrawingMode by remember { mutableStateOf(false) }
    
    // Initial shine animation trigger
    LaunchedEffect(Unit) {
        // Trigger initial shine animation
        triggerShineAnimation(autoShinePosition, coroutineScope)
        
        // Set up periodic auto-shine when gyro is inactive
        while (true) {
            delay(5000) // Check every 5 seconds
            val currentTime = System.currentTimeMillis()
            // If no gyro movement for 3 seconds, trigger auto shine
            if (currentTime - lastSensorUpdateTime > 3000 && !autoShineActive) {
                autoShineActive = true
                triggerShineAnimation(autoShinePosition, coroutineScope)
            }
        }
    }
    
    // Smooth animations for rotation changes
    val animatedRotationX = animateFloatAsState(targetValue = rotationX, label = "rotationX")
    val animatedRotationY = animateFloatAsState(targetValue = rotationY, label = "rotationY")
    
    // Sensor manager setup
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val rotationSensor = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) }
    
    // Register for sensor updates
    DisposableEffect(lifecycleOwner) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                    val currentTime = System.currentTimeMillis()
                    val previousX = rotationX
                    val previousY = rotationY
                    
                    // Update rotation values
                    rotationY = event.values[0] * 2 // left-right tilt
                    rotationX = event.values[1] * 2 // front-back tilt
                    
                    // Check if there was meaningful movement
                    val movementDelta = sqrt((rotationX - previousX).pow(2) + (rotationY - previousY).pow(2))
                    if (movementDelta > 0.02f) {
                        lastSensorUpdateTime = currentTime
                        if (autoShineActive) {
                            autoShineActive = false
                        }
                    }
                }
            }
            
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // Not needed for this implementation
            }
        }
        
        // Lifecycle observer to register/unregister sensor
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    sensorManager.registerListener(
                        listener,
                        rotationSensor,
                        SensorManager.SENSOR_DELAY_GAME
                    )
                }
                Lifecycle.Event.ON_PAUSE -> {
                    sensorManager.unregisterListener(listener)
                }
                else -> {}
            }
        }
        
        // Register the observer
        lifecycleOwner.lifecycle.addObserver(observer)
        
        // Cleanup
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            sensorManager.unregisterListener(listener)
        }
    }
    
    // Calculate normalized position for shine effect based on gyro or animation
    val normalizedX = if (autoShineActive) {
        val position = autoShinePosition.value
        if (position < 0f || position > 1f) 0.5f else position
    } else {
        (animatedRotationY.value + 1) / 2
    }
    
    val normalizedY = if (autoShineActive) {
        val position = autoShinePosition.value
        if (position < 0f || position > 1f) 0.5f else 1f - position
    } else {
        (animatedRotationX.value + 1) / 2
    }
    
    // Calculate angle for gradient rotation
    val angle by remember(normalizedX, normalizedY) {
        derivedStateOf {
            val dx = normalizedX - 0.5f
            val dy = normalizedY - 0.5f
            val degrees = atan2(dy, dx) * (180 / Math.PI).toFloat()
            degrees
        }
    }
    
    // Calculate intensity based on tilt amount or animation
    val intensity by remember(normalizedX, normalizedY, autoShineActive) {
        derivedStateOf {
            if (autoShineActive) {
                val position = autoShinePosition.value
                if (position in 0f..1f) 0.7f else 0.2f
            } else {
                val dx = normalizedX - 0.5f
                val dy = normalizedY - 0.5f
                val distance = sqrt(dx.pow(2) + dy.pow(2)) * 2
                (0.2f + distance * 0.3f).coerceIn(0.2f, 0.8f)
            }
        }
    }
    
    Column(modifier = modifier) {
        // Main card with drawing capability
        Box(
            modifier = Modifier
                .width(320.dp)
                .height(200.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(isDrawingMode) {
                        if (isDrawingMode) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    currentPath.value = Path().apply {
                                        moveTo(offset.x, offset.y)
                                    }
                                    currentPathColor.value = currentColor
                                },
                                onDrag = { change, _ ->
                                    currentPath.value?.lineTo(change.position.x, change.position.y)
                                },
                                onDragEnd = {
                                    currentPath.value?.let { path ->
                                        paths.add(Pair(path, currentPathColor.value))
                                    }
                                    currentPath.value = null
                                }
                            )
                        }
                    },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                // Holographic background with gradient and shine effects
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawWithCache {
                            // Base gradient
                            val baseGradient = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF1A237E), // Deep blue
                                    Color(0xFF7B1FA2)  // Purple
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(size.width, size.height)
                            )
                            
                            // Holographic shine gradient
                            val shineGradient = Brush.linearGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = intensity),
                                    Color.Transparent
                                ),
                                start = Offset(
                                    size.width * (1 - normalizedX),
                                    size.height * (1 - normalizedY)
                                ),
                                end = Offset(
                                    size.width * normalizedX,
                                    size.height * normalizedY
                                )
                            )
                            
                            onDrawBehind {
                                // Draw base gradient
                                drawRect(baseGradient)
                                
                                // Draw holographic pattern
                                for (i in 0..size.width.toInt() step 20) {
                                    drawLine(
                                        Color.White.copy(alpha = 0.1f),
                                        start = Offset(i.toFloat(), 0f),
                                        end = Offset(i.toFloat(), size.height),
                                        strokeWidth = 1f
                                    )
                                }
                                
                                for (i in 0..size.height.toInt() step 20) {
                                    drawLine(
                                        Color.White.copy(alpha = 0.1f),
                                        start = Offset(0f, i.toFloat()),
                                        end = Offset(size.width, i.toFloat()),
                                        strokeWidth = 1f
                                    )
                                }
                                
                                // Rotate and draw shine effect
                                rotate(angle) {
                                    drawRect(shineGradient)
                                }
                            }
                        }
                ) {
                    // Draw saved paths
                    Canvas(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        paths.forEach { (path, color) ->
                            drawPath(
                                path = path,
                                color = color,
                                style = Stroke(width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                            )
                        }
                        
                        // Draw current path
                        currentPath.value?.let { path ->
                            drawPath(
                                path = path,
                                color = currentPathColor.value,
                                style = Stroke(width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                            )
                        }
                    }
                    
                    // Card content
                    if (!isDrawingMode) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Top section - Bank name and chip
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "BANK CARD",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 14.sp,
                                    color = Color.White,
                                    letterSpacing = 2.sp
                                )
                                
                                // Chip
                                Box(
                                    modifier = Modifier
                                        .size(width = 40.dp, height = 30.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFFFFD700).copy(alpha = 0.9f))
                                )
                            }
                            
                            // Middle section - Card number
                            Text(
                                text = cardNumber,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 18.sp,
                                color = Color.White,
                                letterSpacing = 2.sp,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                            
                            // Bottom section - Card holder and expiry
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Column {
                                    Text(
                                        text = "CARD HOLDER",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        color = Color.White.copy(alpha = 0.8f),
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = cardHolder,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 14.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium,
                                        letterSpacing = 1.sp
                                    )
                                }
                                
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "EXPIRES",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        color = Color.White.copy(alpha = 0.8f),
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = expiryDate,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 14.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.End,
                                        letterSpacing = 1.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Drawing controls
        Column(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            // Drawing mode toggle
            Button(
                onClick = { isDrawingMode = !isDrawingMode },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isDrawingMode) "Exit Drawing Mode" else "Enter Drawing Mode")
            }
            
            // Only show these controls in drawing mode
            if (isDrawingMode) {
                Spacer(modifier = Modifier.height(8.dp))
                
                // Color picker
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    items(availableColors.size) { index ->
                        val color = availableColors[index]
                        ColorButton(
                            color = color,
                            isSelected = color == currentColor,
                            onClick = { currentColor = color }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Clear button
                Button(
                    onClick = { paths.clear() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Clear Drawing")
                }
            }
        }
    }
}

@Composable
fun ColorButton(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(androidx.compose.foundation.shape.CircleShape)
            .background(color)
            .padding(2.dp)
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 3.dp,
                        color = Color.White,
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onClick)
    )
}

// Helper function to trigger the shine animation
private fun triggerShineAnimation(
    autoShinePosition: Animatable<Float, AnimationVector1D>,
    coroutineScope: CoroutineScope
) {
    coroutineScope.launch {
        // Reset position to start off-screen
        autoShinePosition.snapTo(-0.2f)
        
        // Animate shine across the card
        autoShinePosition.animateTo(
            targetValue = 1.2f,
            animationSpec = tween(
                durationMillis = 1500,
                easing = LinearEasing
            )
        )
    }
}

// Helper function to get the lifecycle owner in Compose
@Composable
fun LocalLifecycleOwner(): LifecycleOwner {
    val lifecycleOwnerAmbient = androidx.compose.ui.platform.LocalLifecycleOwner.current
    return lifecycleOwnerAmbient
}



// LazyRowScope definition for content
interface LazyRowScope {
    fun items(
        count: Int,
        itemContent: @Composable (index: Int) -> Unit
    )
}