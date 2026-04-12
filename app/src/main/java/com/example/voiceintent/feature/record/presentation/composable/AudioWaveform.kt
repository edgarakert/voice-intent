package com.example.voiceintent.feature.record.presentation.composable

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset

private const val BAR_COUNT = 32


@Composable
fun AudioWaveform(
    amplitudeLevel: Float,
    modifier: Modifier = Modifier
) {
    val animatedAmplitude by animateFloatAsState(
        targetValue = amplitudeLevel,
        animationSpec = tween(durationMillis = 100),
        label = "amplitude"
    )

    val color = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier) {
        val barWidth = size.width / (BAR_COUNT * 2f)
        val centerY = size.height / 2f

        for (i in 0 until BAR_COUNT) {
            val x = i * barWidth * 2f + barWidth
            val scale = if (i % 2 == 0) animatedAmplitude else animatedAmplitude * 0.6f
            val barHeight = (size.height * 0.15f) + (size.height * 0.7f * scale)

            drawLine(
                color = color,
                start = Offset(x, centerY - barHeight / 2f),
                end = Offset(x, centerY + barHeight / 2f),
                strokeWidth = barWidth * 0.7f
            )
        }
    }
}