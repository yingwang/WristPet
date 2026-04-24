package com.wristpet.tile

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.wristpet.data.model.PetState

object PetBitmapRenderer {

    private const val W = 0xFFFFFFFF.toInt()
    private const val B = 0xFF1A1A1A.toInt()
    private const val CHEEK = 0xFFFF8A80.toInt()

    fun render(state: PetState, sizePx: Int = 160): Bitmap {
        val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply { isAntiAlias = false; style = Paint.Style.FILL }

        val cx = sizePx / 2f
        val cy = sizePx / 2f
        val px = sizePx / 28f

        val bodyColor = when (state) {
            PetState.SICK -> 0xFF9E9E9E.toInt()
            PetState.ANGRY -> 0xFFE53935.toInt()
            PetState.SLEEPING -> 0xFF7986CB.toInt()
            else -> 0xFF4CAF50.toInt()
        }

        fun rect(color: Int, x: Float, y: Float, w: Float, h: Float) {
            paint.color = color
            canvas.drawRect(x, y, x + w, y + h, paint)
        }

        // Body
        for (row in -4..3) {
            val cols = when {
                row == -4 || row == 3 -> -3..2
                row == -3 || row == 2 -> -4..3
                else -> -5..4
            }
            for (col in cols) {
                rect(bodyColor, cx + col * px, cy + row * px, px, px)
            }
        }

        // Shadow
        for (col in -3..2) {
            rect(0x4D000000, cx + col * px, cy + 4 * px, px, px * 0.5f)
        }

        // Face
        when (state) {
            PetState.HAPPY -> {
                rect(W, cx - 3 * px, cy - 2 * px, 2 * px, 2 * px)
                rect(B, cx - 2.5f * px, cy - 1.5f * px, px, px)
                rect(W, cx + px, cy - 2 * px, 2 * px, 2 * px)
                rect(B, cx + 1.5f * px, cy - 1.5f * px, px, px)
                rect(CHEEK, cx - 4 * px, cy, 2 * px, px)
                rect(CHEEK, cx + 2 * px, cy, 2 * px, px)
                rect(B, cx - 2 * px, cy + px, px, px)
                rect(B, cx - px, cy + 1.5f * px, 2 * px, px)
                rect(B, cx + px, cy + px, px, px)
            }
            PetState.SLEEPING -> {
                rect(B, cx - 3 * px, cy - px, 2 * px, px)
                rect(B, cx + px, cy - px, 2 * px, px)
                rect(B, cx - 0.5f * px, cy + px, px, px)
                rect(0x99FFFFFF.toInt(), cx + 4 * px, cy - 4 * px, 2 * px, px)
                rect(0x66FFFFFF.toInt(), cx + 5 * px, cy - 6 * px, 1.5f * px, 0.8f * px)
                rect(0x33FFFFFF.toInt(), cx + 6 * px, cy - 8 * px, px, 0.6f * px)
            }
            PetState.BORED -> {
                rect(W, cx - 3 * px, cy - px, 2 * px, px)
                rect(B, cx - 2.5f * px, cy - 0.5f * px, px, px)
                rect(W, cx + px, cy - px, 2 * px, px)
                rect(B, cx + 1.5f * px, cy - 0.5f * px, px, px)
                rect(B, cx - px, cy + px, 2 * px, px)
            }
            PetState.ANGRY -> {
                rect(W, cx - 3 * px, cy - 2 * px, 2 * px, 2 * px)
                rect(B, cx - 2.5f * px, cy - px, px, px)
                rect(W, cx + px, cy - 2 * px, 2 * px, 2 * px)
                rect(B, cx + 1.5f * px, cy - px, px, px)
                rect(B, cx - 4 * px, cy - 3.5f * px, px, px)
                rect(B, cx - 3 * px, cy - 3 * px, px, px)
                rect(B, cx + 2 * px, cy - 3 * px, px, px)
                rect(B, cx + 3 * px, cy - 3.5f * px, px, px)
                rect(B, cx - px, cy + 1.5f * px, 2 * px, px)
                rect(B, cx - 2 * px, cy + px, px, px)
                rect(B, cx + px, cy + px, px, px)
            }
            PetState.SICK -> {
                rect(B, cx - 3 * px, cy - 2 * px, px, px)
                rect(B, cx - 2 * px, cy - 2 * px, px, px)
                rect(B, cx - 2 * px, cy - px, px, px)
                rect(B, cx - 3 * px, cy - px, px, px)
                rect(B, cx + px, cy - 2 * px, px, px)
                rect(B, cx + 2 * px, cy - 2 * px, px, px)
                rect(B, cx + 2 * px, cy - px, px, px)
                rect(B, cx + px, cy - px, px, px)
                rect(B, cx - 2 * px, cy + px, px, px)
                rect(B, cx - px, cy + 1.5f * px, px, px)
                rect(B, cx, cy + px, px, px)
                rect(B, cx + px, cy + 1.5f * px, px, px)
            }
        }

        return bitmap
    }
}
