package com.example.voiceintent.feature.record.presentation.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.voiceintent.R

@Composable
fun RecordButton(isRecording: Boolean, onClick: () -> Unit) {
    val animatedColor by animateColorAsState(
        targetValue = if (isRecording) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.primary
        },
        label = "button_color"
    )

    Button(
        onClick = onClick,
        modifier = Modifier.size(56.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = animatedColor),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            painter = painterResource(if (isRecording) R.drawable.ic_baseline_stop_24 else R.drawable.ic_rounded_mic_24),
            contentDescription = if (isRecording) "Остановить запись" else "Начать запись",
            tint = Color.White,
            modifier = Modifier.size(22.dp)
        )
    }
}