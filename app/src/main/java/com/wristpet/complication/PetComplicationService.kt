package com.wristpet.complication

import android.graphics.drawable.Icon
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.SmallImage
import androidx.wear.watchface.complications.data.SmallImageComplicationData
import androidx.wear.watchface.complications.data.SmallImageType
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import com.wristpet.R
import com.wristpet.data.model.Pet
import com.wristpet.data.model.PetState
import com.wristpet.data.repository.PetRepository
import com.wristpet.tile.PetBitmapRenderer

class PetComplicationService : SuspendingComplicationDataSourceService() {

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        val pet = Pet()
        return when (type) {
            ComplicationType.SHORT_TEXT -> buildShortText(pet)
            ComplicationType.SMALL_IMAGE -> buildSmallImage(pet)
            else -> null
        }
    }

    override suspend fun onComplicationRequest(
        request: ComplicationRequest
    ): ComplicationData? {
        val pet = PetRepository.get()
        return when (request.complicationType) {
            ComplicationType.SHORT_TEXT -> buildShortText(pet)
            ComplicationType.SMALL_IMAGE -> buildSmallImage(pet)
            else -> null
        }
    }

    private fun buildShortText(pet: Pet): ComplicationData {
        val stateLabel = when (pet.state) {
            PetState.HAPPY -> "Happy"
            PetState.BORED -> "Bored"
            PetState.ANGRY -> "Angry"
            PetState.SLEEPING -> "Zzz"
            PetState.SICK -> "Sick"
        }
        val petBitmap = PetBitmapRenderer.render(pet.state, 64)
        return ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder(stateLabel).build(),
            contentDescription = PlainComplicationText.Builder("Pet status").build()
        )
            .setTitle(PlainComplicationText.Builder("${pet.happiness}%").build())
            .setMonochromaticImage(
                MonochromaticImage.Builder(
                    Icon.createWithBitmap(petBitmap)
                ).build()
            )
            .build()
    }

    private fun buildSmallImage(pet: Pet): ComplicationData {
        val bitmap = PetBitmapRenderer.render(pet.state, 64)
        return SmallImageComplicationData.Builder(
            smallImage = SmallImage.Builder(
                image = Icon.createWithBitmap(bitmap),
                type = SmallImageType.PHOTO
            ).build(),
            contentDescription = PlainComplicationText.Builder("宠物").build()
        ).build()
    }
}
