package com.wristpet.tile

import android.graphics.Bitmap
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ColorBuilders.argb
import androidx.wear.protolayout.DimensionBuilders.dp
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.DimensionBuilders.sp
import androidx.wear.protolayout.DimensionBuilders.wrap
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.Box
import androidx.wear.protolayout.LayoutElementBuilders.Column
import androidx.wear.protolayout.LayoutElementBuilders.FontStyle
import androidx.wear.protolayout.LayoutElementBuilders.Image
import androidx.wear.protolayout.LayoutElementBuilders.Layout
import androidx.wear.protolayout.LayoutElementBuilders.Row
import androidx.wear.protolayout.LayoutElementBuilders.Spacer
import androidx.wear.protolayout.LayoutElementBuilders.Text
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.ResourceBuilders.InlineImageResource
import androidx.wear.protolayout.ResourceBuilders.Resources
import androidx.wear.protolayout.TimelineBuilders.Timeline
import androidx.wear.protolayout.TimelineBuilders.TimelineEntry
import androidx.wear.tiles.RequestBuilders.ResourcesRequest
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.TileService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.wristpet.data.model.Pet
import com.wristpet.data.model.PetState
import com.wristpet.data.repository.PetRepository
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream

class PetTileService : TileService() {

    companion object {
        private const val RES_PET = "pet_image"
    }

    private var cachedPet: Pet = Pet()

    override fun onTileRequest(request: TileRequest): ListenableFuture<Tile> {
        cachedPet = runBlocking { PetRepository.get() }

        val stateLabel = when (cachedPet.state) {
            PetState.HAPPY -> "开心"
            PetState.BORED -> "无聊"
            PetState.ANGRY -> "生气"
            PetState.SLEEPING -> "睡觉中"
            PetState.SICK -> "生病了"
        }

        val clickable = ModifiersBuilders.Clickable.Builder()
            .setId("open_app")
            .setOnClick(
                ActionBuilders.LaunchAction.Builder()
                    .setAndroidActivity(
                        ActionBuilders.AndroidActivity.Builder()
                            .setPackageName("com.wristpet")
                            .setClassName("com.wristpet.ui.WristPetActivity")
                            .build()
                    )
                    .build()
            )
            .build()

        val petImage = Image.Builder()
            .setResourceId(RES_PET)
            .setWidth(dp(80f))
            .setHeight(dp(80f))
            .build()

        val stateText = Text.Builder()
            .setText(stateLabel)
            .setFontStyle(
                FontStyle.Builder()
                    .setSize(sp(14f))
                    .setColor(argb(0xDDFFFFFF.toInt()))
                    .build()
            )
            .build()

        // Happiness bar background
        val barBg = Box.Builder()
            .setWidth(dp(60f))
            .setHeight(dp(4f))
            .setHorizontalAlignment(LayoutElementBuilders.HORIZONTAL_ALIGN_START)
            .setModifiers(
                ModifiersBuilders.Modifiers.Builder()
                    .setBackground(
                        ModifiersBuilders.Background.Builder()
                            .setColor(argb(0xFF333333.toInt()))
                            .setCorner(
                                ModifiersBuilders.Corner.Builder()
                                    .setRadius(dp(2f))
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .addContent(
                // Happiness bar fill
                Box.Builder()
                    .setWidth(dp(60f * cachedPet.happiness / 100f))
                    .setHeight(dp(4f))
                    .setModifiers(
                        ModifiersBuilders.Modifiers.Builder()
                            .setBackground(
                                ModifiersBuilders.Background.Builder()
                                    .setColor(argb(0xFF66BB6A.toInt()))
                                    .setCorner(
                                        ModifiersBuilders.Corner.Builder()
                                            .setRadius(dp(2f))
                                            .build()
                                    )
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()

        val stepsText = Text.Builder()
            .setText("${cachedPet.totalStepsToday} 步")
            .setFontStyle(
                FontStyle.Builder()
                    .setSize(sp(12f))
                    .setColor(argb(0x99FFFFFF.toInt()))
                    .build()
            )
            .build()

        val column = Column.Builder()
            .setHorizontalAlignment(LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER)
            .addContent(petImage)
            .addContent(Spacer.Builder().setHeight(dp(4f)).build())
            .addContent(stateText)
            .addContent(Spacer.Builder().setHeight(dp(4f)).build())
            .addContent(barBg)
            .addContent(Spacer.Builder().setHeight(dp(4f)).build())
            .addContent(stepsText)
            .build()

        val root = Box.Builder()
            .setWidth(expand())
            .setHeight(expand())
            .setHorizontalAlignment(LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER)
            .setVerticalAlignment(LayoutElementBuilders.VERTICAL_ALIGN_CENTER)
            .setModifiers(
                ModifiersBuilders.Modifiers.Builder()
                    .setClickable(clickable)
                    .setBackground(
                        ModifiersBuilders.Background.Builder()
                            .setColor(argb(0xFF0D0D1A.toInt()))
                            .build()
                    )
                    .build()
            )
            .addContent(column)
            .build()

        val entry = TimelineEntry.Builder()
            .setLayout(Layout.Builder().setRoot(root).build())
            .build()

        val tile = Tile.Builder()
            .setResourcesVersion("${cachedPet.state}_${cachedPet.happiness}_${cachedPet.totalStepsToday}")
            .setTileTimeline(Timeline.Builder().addTimelineEntry(entry).build())
            .setFreshnessIntervalMillis(1_800_000) // 30 minutes
            .build()

        return Futures.immediateFuture(tile)
    }

    override fun onTileResourcesRequest(request: ResourcesRequest): ListenableFuture<Resources> {
        val bitmap = PetBitmapRenderer.render(cachedPet.state)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val bytes = stream.toByteArray()

        val imageResource = ResourceBuilders.ImageResource.Builder()
            .setInlineResource(
                InlineImageResource.Builder()
                    .setData(bytes)
                    .setWidthPx(bitmap.width)
                    .setHeightPx(bitmap.height)
                    .setFormat(ResourceBuilders.IMAGE_FORMAT_UNDEFINED)
                    .build()
            )
            .build()

        bitmap.recycle()

        return Futures.immediateFuture(
            Resources.Builder()
                .setVersion(request.version)
                .addIdToImageMapping(RES_PET, imageResource)
                .build()
        )
    }
}
