package hu.bme.aut.cameraxdemos

import android.graphics.RectF
import android.util.Size
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.toRectF
import com.google.mlkit.vision.text.Text
import kotlin.math.roundToInt

typealias BlockClickListener = (block: Text.TextBlock) -> Unit

data class Ratio(
    val widthRatio: Float,
    val heightRatio: Float
)

data class Coordinate(
    val x: Float,
    val y: Float
)

object BlocksRenderer {

    private const val IMAGE_ROTATION_UP = 0
    private const val IMAGE_ROTATION_DOWN = 180
    private const val IMAGE_ROTATION_LAND_RIGHT = 90
    private const val IMAGE_ROTATION_LAND_LEFT = 270

    fun drawBlocks(
        blocksViewGroup: ViewGroup,
        imageSize: Size,
        imageRotation: Int,
        textBlocks: MutableList<Text.TextBlock>,
        //textBlocks: MutableList<FirebaseVisionText.TextBlock>,
        clicks: BlockClickListener
    ) {
        val parentViewSize = Size(
            blocksViewGroup.width,
            blocksViewGroup.height
        )
        //because the image rotation is 90/270 we have to rotate the height and width
        //we also have to multiply them by the scale to match the size of the view to the taken image
        val ratio = getRatio(parentViewSize, imageSize, imageRotation)

        blocksViewGroup.removeAllViews()

        textBlocks.forEach {
            val image = AppCompatImageView(blocksViewGroup.context).apply {
                setImageResource(R.drawable.background_rectangle)
                setTag(R.id.foundTextTag, it.text)
                setTag(R.id.foundBlockTag, it)
                setOnClickListener {
                    clicks(getTag(R.id.foundBlockTag) as Text.TextBlock)
                }
            }

            val rect = it.boundingBox!!.toRectF()

            val coordinate = getViewCoordinate(rect, ratio, imageRotation)

            val viewSize = getViewSize(rect, ratio, imageRotation)

            image.x = coordinate.x
            image.y = coordinate.y

            blocksViewGroup.addView(
                image,
                viewSize.width,
                viewSize.height
            )
        }
    }

    private fun getRatio(parentSize: Size, imageSize: Size, imageRotation: Int): Ratio =
        if (imageRotation == IMAGE_ROTATION_UP || imageRotation == IMAGE_ROTATION_DOWN) {
            Ratio(
                parentSize.width.toFloat() / imageSize.width.toFloat(),
                parentSize.height.toFloat() / imageSize.height.toFloat()
            )
        } else {
            Ratio(
                parentSize.height.toFloat() / imageSize.width.toFloat(),
                parentSize.width.toFloat() / imageSize.height.toFloat()
            )
        }

    private fun getViewSize(boundingRect: RectF, ratio: Ratio, imageRotation: Int): Size =
        //90/270
        if (imageRotation == IMAGE_ROTATION_LAND_LEFT || imageRotation == IMAGE_ROTATION_LAND_RIGHT) {
            Size(
                (boundingRect.width() * ratio.heightRatio).roundToInt(),
                (boundingRect.height() * ratio.widthRatio).roundToInt()
            )
        } else {
            //0
            Size(
                (boundingRect.width() * ratio.widthRatio).roundToInt(),
                (boundingRect.height() * ratio.heightRatio).roundToInt()
            )
        }

    private fun getViewCoordinate(
        boundingRect: RectF,
        ratio: Ratio,
        imageRotation: Int
    ): Coordinate =
        if (imageRotation == IMAGE_ROTATION_LAND_LEFT || imageRotation == IMAGE_ROTATION_LAND_RIGHT) {
            Coordinate(
                x = boundingRect.left * ratio.heightRatio,
                y = boundingRect.top * ratio.widthRatio
            )
        } else {
            Coordinate(
                x = boundingRect.left * ratio.widthRatio,
                y = boundingRect.top * ratio.heightRatio
            )
        }
}