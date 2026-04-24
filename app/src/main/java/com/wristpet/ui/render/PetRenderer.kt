package com.wristpet.ui.render

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.wristpet.data.model.PetState
import kotlin.math.sin

private val BodyColor = Color(0xFF4CAF50)
private val EyeWhite = Color(0xFFFFFFFF)
private val EyeBlack = Color(0xFF1A1A1A)
private val Cheek = Color(0xFFFF8A80)
private val Angry = Color(0xFFE53935)
private val Sleepy = Color(0xFF7986CB)
private val Sick = Color(0xFF9E9E9E)

fun DrawScope.drawPet(state: PetState, animTick: Long) {
    val cx = size.width / 2
    val cy = size.height / 2
    val px = size.width / 28f

    val bobY = sin(animTick * 0.08).toFloat() * px * 0.5f

    val bodyColor = when (state) {
        PetState.SICK -> Sick
        PetState.ANGRY -> Angry
        PetState.SLEEPING -> Sleepy
        else -> BodyColor
    }

    // Body: rounded pixel blob
    for (row in -4..3) {
        val cols = when {
            row == -4 || row == 3 -> -3..2
            row == -3 || row == 2 -> -4..3
            else -> -5..4
        }
        for (col in cols) {
            drawRect(
                color = bodyColor,
                topLeft = Offset(cx + col * px, cy + row * px + bobY),
                size = Size(px, px)
            )
        }
    }

    // Shadow under body
    for (col in -3..2) {
        drawRect(
            color = Color.Black.copy(alpha = 0.3f),
            topLeft = Offset(cx + col * px, cy + 4 * px),
            size = Size(px, px * 0.5f)
        )
    }

    when (state) {
        PetState.HAPPY -> {
            // Eyes
            drawRect(EyeWhite, Offset(cx - 3 * px, cy - 2 * px + bobY), Size(px * 2, px * 2))
            drawRect(EyeBlack, Offset(cx - 2.5f * px, cy - 1.5f * px + bobY), Size(px, px))
            drawRect(EyeWhite, Offset(cx + 1 * px, cy - 2 * px + bobY), Size(px * 2, px * 2))
            drawRect(EyeBlack, Offset(cx + 1.5f * px, cy - 1.5f * px + bobY), Size(px, px))
            // Cheeks
            drawRect(Cheek, Offset(cx - 4 * px, cy + bobY), Size(px * 2, px))
            drawRect(Cheek, Offset(cx + 2 * px, cy + bobY), Size(px * 2, px))
            // Smile
            drawRect(EyeBlack, Offset(cx - 2 * px, cy + 1 * px + bobY), Size(px, px))
            drawRect(EyeBlack, Offset(cx - 1 * px, cy + 1.5f * px + bobY), Size(px * 2, px))
            drawRect(EyeBlack, Offset(cx + 1 * px, cy + 1 * px + bobY), Size(px, px))
        }

        PetState.SLEEPING -> {
            // Closed eyes
            drawRect(EyeBlack, Offset(cx - 3 * px, cy - 1 * px + bobY), Size(px * 2, px))
            drawRect(EyeBlack, Offset(cx + 1 * px, cy - 1 * px + bobY), Size(px * 2, px))
            // Mouth (small o)
            drawRect(EyeBlack, Offset(cx - 0.5f * px, cy + 1 * px + bobY), Size(px, px))
            // Floating Z's
            val zFloat = sin(animTick * 0.05).toFloat() * px
            drawRect(Color.White.copy(alpha = 0.6f), Offset(cx + 4 * px, cy - 4 * px + zFloat), Size(px * 2, px))
            drawRect(Color.White.copy(alpha = 0.4f), Offset(cx + 5 * px, cy - 6 * px + zFloat * 0.7f), Size(px * 1.5f, px * 0.8f))
            drawRect(Color.White.copy(alpha = 0.2f), Offset(cx + 6 * px, cy - 8 * px + zFloat * 0.5f), Size(px, px * 0.6f))
        }

        PetState.BORED -> {
            // Half-lidded eyes
            drawRect(EyeWhite, Offset(cx - 3 * px, cy - 1 * px + bobY), Size(px * 2, px))
            drawRect(EyeBlack, Offset(cx - 2.5f * px, cy - 0.5f * px + bobY), Size(px, px))
            drawRect(EyeWhite, Offset(cx + 1 * px, cy - 1 * px + bobY), Size(px * 2, px))
            drawRect(EyeBlack, Offset(cx + 1.5f * px, cy - 0.5f * px + bobY), Size(px, px))
            // Flat mouth
            drawRect(EyeBlack, Offset(cx - 1 * px, cy + 1 * px + bobY), Size(px * 2, px))
        }

        PetState.ANGRY -> {
            // Eyes
            drawRect(EyeWhite, Offset(cx - 3 * px, cy - 2 * px + bobY), Size(px * 2, px * 2))
            drawRect(EyeBlack, Offset(cx - 2.5f * px, cy - 1 * px + bobY), Size(px, px))
            drawRect(EyeWhite, Offset(cx + 1 * px, cy - 2 * px + bobY), Size(px * 2, px * 2))
            drawRect(EyeBlack, Offset(cx + 1.5f * px, cy - 1 * px + bobY), Size(px, px))
            // Angry brows (\ /)
            drawRect(EyeBlack, Offset(cx - 4 * px, cy - 3.5f * px + bobY), Size(px, px))
            drawRect(EyeBlack, Offset(cx - 3 * px, cy - 3 * px + bobY), Size(px, px))
            drawRect(EyeBlack, Offset(cx + 2 * px, cy - 3 * px + bobY), Size(px, px))
            drawRect(EyeBlack, Offset(cx + 3 * px, cy - 3.5f * px + bobY), Size(px, px))
            // Frown
            drawRect(EyeBlack, Offset(cx - 1 * px, cy + 1.5f * px + bobY), Size(px * 2, px))
            drawRect(EyeBlack, Offset(cx - 2 * px, cy + 1 * px + bobY), Size(px, px))
            drawRect(EyeBlack, Offset(cx + 1 * px, cy + 1 * px + bobY), Size(px, px))
        }

        PetState.SICK -> {
            // Spiral/dizzy eyes
            drawRect(EyeBlack, Offset(cx - 3 * px, cy - 2 * px + bobY), Size(px, px))
            drawRect(EyeBlack, Offset(cx - 2 * px, cy - 2 * px + bobY), Size(px, px))
            drawRect(EyeBlack, Offset(cx - 2 * px, cy - 1 * px + bobY), Size(px, px))
            drawRect(EyeBlack, Offset(cx - 3 * px, cy - 1 * px + bobY), Size(px, px))
            drawRect(EyeBlack, Offset(cx + 1 * px, cy - 2 * px + bobY), Size(px, px))
            drawRect(EyeBlack, Offset(cx + 2 * px, cy - 2 * px + bobY), Size(px, px))
            drawRect(EyeBlack, Offset(cx + 2 * px, cy - 1 * px + bobY), Size(px, px))
            drawRect(EyeBlack, Offset(cx + 1 * px, cy - 1 * px + bobY), Size(px, px))
            // Wavy mouth
            drawRect(EyeBlack, Offset(cx - 2 * px, cy + 1 * px + bobY), Size(px, px))
            drawRect(EyeBlack, Offset(cx - 1 * px, cy + 1.5f * px + bobY), Size(px, px))
            drawRect(EyeBlack, Offset(cx, cy + 1 * px + bobY), Size(px, px))
            drawRect(EyeBlack, Offset(cx + 1 * px, cy + 1.5f * px + bobY), Size(px, px))
        }
    }
}
