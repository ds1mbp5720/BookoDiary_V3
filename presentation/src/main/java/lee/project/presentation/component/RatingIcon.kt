package lee.project.presentation.component

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import lee.project.presentation.R
import androidx.core.graphics.createBitmap

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    context: Context,
    rating: Float,
    totalCnt: Int,
    spaceBetween: Dp = 0.dp
) {
    val bitMapImage = getBitmapFromImage(context = context, drawable = R.drawable.baseline_star_outline_24)
    val bitMapImageFull = getBitmapFromImage(context = context, drawable = R.drawable.baseline_star_24)

    val height = LocalDensity.current.run { bitMapImage.height.toDp() }
    val width = LocalDensity.current.run { bitMapImage.width.toDp() }
    val space = LocalDensity.current.run { spaceBetween.toPx() }
    val totalWidth = width * totalCnt + spaceBetween * (totalCnt - 1)


    Box(
        modifier = modifier
            .width(totalWidth)
            .height(height)
            .drawBehind {
                drawRating(rating, bitMapImage, bitMapImageFull, space, totalCnt)
            })
}

private fun DrawScope.drawRating(
    rating: Float,
    image: Bitmap,
    imageFull: Bitmap,
    space: Float,
    totalCnt: Int,
) {

    val totalCount = 10

    val imageWidth = image.width.toFloat()
    val imageHeight = size.height

    val reminder = rating - rating.toInt()
    val ratingInt = (rating - reminder).toInt()

    for (i in 0 until totalCount) {

        val start = imageWidth * i + space * i

        drawImage(
            image = image.asImageBitmap(),
            topLeft = Offset(start, 0f)
        )
    }

    drawWithLayer {
        for (i in 0 until totalCount) {
            val start = imageWidth * i + space * i
            // Destination
            drawImage(
                image = imageFull.asImageBitmap(),
                topLeft = Offset(start, 0f)
            )
        }

        val end = imageWidth * totalCount + space * (totalCount - 1)
        val start = rating * imageWidth + ratingInt * space
        val size = end - start

        // Source
        drawRect(
            Color.Transparent,
            topLeft = Offset(start, 0f),
            size = Size(size, height = imageHeight),
            blendMode = BlendMode.SrcIn
        )
    }
}

private fun DrawScope.drawWithLayer(block: DrawScope.() -> Unit) {
    with(drawContext.canvas.nativeCanvas) {
        val checkPoint = saveLayer(null, null)
        block()
        restoreToCount(checkPoint)
    }

}

private fun getBitmapFromImage(context: Context, drawable: Int): Bitmap {
    val db = ContextCompat.getDrawable(context, drawable)
    val bit = createBitmap(db!!.intrinsicWidth, db.intrinsicHeight)
    val canvas = Canvas(bit)

    db.setBounds(0, 0, canvas.width, canvas.height)
    db.draw(canvas)

    return bit
}