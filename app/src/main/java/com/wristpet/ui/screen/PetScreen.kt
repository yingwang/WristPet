package com.wristpet.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wristpet.data.health.StepCountReader
import com.wristpet.data.model.PetState
import com.wristpet.ui.render.drawPet
import kotlinx.coroutines.delay

@Composable
fun PetScreen(viewModel: PetViewModel = viewModel()) {
    val pet by viewModel.pet.collectAsState()
    val steps by StepCountReader.dailySteps.collectAsState()
    val textMeasurer = rememberTextMeasurer()
    var animTick by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(50L)
            animTick++
        }
    }

    LaunchedEffect(steps) {
        viewModel.onStepsUpdated(steps)
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                viewModel.onPetTapped()
            }
    ) {
        val w = size.width
        val h = size.height

        // Background
        drawRect(Color(0xFF0D0D1A))

        // Draw pixel pet
        drawPet(pet?.state ?: PetState.HAPPY, animTick)

        // Pet name at top
        val nameLayout = textMeasurer.measure(
            pet?.name ?: "Pixel",
            style = TextStyle(
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        )
        drawText(
            nameLayout,
            topLeft = Offset(
                (w - nameLayout.size.width) / 2,
                h * 0.08f
            )
        )

        // Happiness bar
        val barWidth = w * 0.4f
        val barHeight = h * 0.02f
        val barX = (w - barWidth) / 2
        val barY = h * 0.15f
        val happinessRatio = (pet?.happiness ?: 100) / 100f
        drawRect(
            Color.DarkGray,
            topLeft = Offset(barX, barY),
            size = Size(barWidth, barHeight)
        )
        drawRect(
            Color(0xFF66BB6A),
            topLeft = Offset(barX, barY),
            size = Size(barWidth * happinessRatio, barHeight)
        )

        // Step counter at bottom
        val stepsText = "$steps steps"
        val stepsLayout = textMeasurer.measure(
            stepsText,
            style = TextStyle(
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        )
        drawText(
            stepsLayout,
            topLeft = Offset(
                (w - stepsLayout.size.width) / 2,
                h * 0.85f
            )
        )

        // State label
        val stateText = when (pet?.state) {
            PetState.HAPPY -> "Happy!"
            PetState.BORED -> "Bored..."
            PetState.ANGRY -> "Angry!"
            PetState.SLEEPING -> "Zzz..."
            PetState.SICK -> "Sick..."
            null -> ""
        }
        val stateLayout = textMeasurer.measure(
            stateText,
            style = TextStyle(
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
        )
        drawText(
            stateLayout,
            topLeft = Offset(
                (w - stateLayout.size.width) / 2,
                h * 0.78f
            )
        )
    }
}
