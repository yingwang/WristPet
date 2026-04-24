package com.wristpet.complication

import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PhotoImageComplicationData
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.SmallImage
import androidx.wear.watchface.complications.data.SmallImageComplicationData
import androidx.wear.watchface.complications.data.SmallImageType
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import com.wristpet.data.model.Pet
import com.wristpet.data.model.PetState
import com.wristpet.data.repository.PetRepository
import com.wristpet.tile.PetBitmapRenderer
import com.wristpet.ui.WristPetActivity

class PetComplicationService : SuspendingComplicationDataSourceService() {

    private fun launchIntent(): PendingIntent {
        val intent = Intent(this, WristPetActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        val pet = Pet()
        return when (type) {
            ComplicationType.SHORT_TEXT -> buildShortText(pet, null)
            ComplicationType.SMALL_IMAGE -> buildSmallImage(pet, null)
            ComplicationType.PHOTO_IMAGE -> buildPhotoImage(pet, null)
            else -> null
        }
    }

    override suspend fun onComplicationRequest(
        request: ComplicationRequest
    ): ComplicationData? {
        val pet = PetRepository.get()
        val tap = launchIntent()
        return when (request.complicationType) {
            ComplicationType.SHORT_TEXT -> buildShortText(pet, tap)
            ComplicationType.SMALL_IMAGE -> buildSmallImage(pet, tap)
            ComplicationType.PHOTO_IMAGE -> buildPhotoImage(pet, tap)
            else -> null
        }
    }

    private fun buildShortText(pet: Pet, tapAction: PendingIntent?): ComplicationData {
        val stateLabel = when (pet.state) {
            PetState.HAPPY -> "Happy"
            PetState.BORED -> "Bored"
            PetState.ANGRY -> "Angry"
            PetState.SLEEPING -> "Zzz"
            PetState.SICK -> "Sick"
        }
        val petBitmap = PetBitmapRenderer.render(pet.state, 64)
        val builder = ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder(stateLabel).build(),
            contentDescription = PlainComplicationText.Builder("Pet status").build()
        )
            .setTitle(PlainComplicationText.Builder("${pet.happiness}%").build())
            .setSmallImage(
                SmallImage.Builder(
                    image = Icon.createWithBitmap(petBitmap),
                    type = SmallImageType.PHOTO
                ).build()
            )
        tapAction?.let { builder.setTapAction(it) }
        return builder.build()
    }

    private fun buildSmallImage(pet: Pet, tapAction: PendingIntent?): ComplicationData {
        val bitmap = PetBitmapRenderer.render(pet.state, 64)
        val builder = SmallImageComplicationData.Builder(
            smallImage = SmallImage.Builder(
                image = Icon.createWithBitmap(bitmap),
                type = SmallImageType.PHOTO
            ).build(),
            contentDescription = PlainComplicationText.Builder("Pet").build()
        )
        tapAction?.let { builder.setTapAction(it) }
        return builder.build()
    }

    private fun buildPhotoImage(pet: Pet, tapAction: PendingIntent?): ComplicationData {
        val bitmap = PetBitmapRenderer.render(pet.state, 160)
        val builder = PhotoImageComplicationData.Builder(
            photoImage = Icon.createWithBitmap(bitmap),
            contentDescription = PlainComplicationText.Builder("Pet").build()
        )
        tapAction?.let { builder.setTapAction(it) }
        return builder.build()
    }
}
