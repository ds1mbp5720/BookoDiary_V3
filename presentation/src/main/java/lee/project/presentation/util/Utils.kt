package lee.project.presentation.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

// 좌우 위치에 따른 아이콘 대칭 변경 함수
@Composable
fun mirroringIcon(ltrIcon: ImageVector, rtlIcon: ImageVector): ImageVector =
    if(LocalLayoutDirection.current == LayoutDirection.Ltr) ltrIcon else rtlIcon

@Composable
fun mirroringBackIcon() = mirroringIcon(
    ltrIcon = Icons.Outlined.ArrowBack, rtlIcon = Icons.Outlined.ArrowForward
)
@SuppressLint("SimpleDateFormat")
fun millToDate(mills: Long): String {
    val pattern = "yyyy/MM/dd"
    val formatter = SimpleDateFormat(pattern)
    val date = formatter.format(Timestamp(mills))
    return date
}

// Record 화면의 책 제목 세로형 string 변경 함수
fun String.textChangeVertical(): String{
    var newString = "\n\n"
    this.forEach {
        if(it == '-'){
            newString = "$newString|\n"
        } else newString = newString + it +"\n"
    }
    return newString
}

// 금액 string , 와 원 추가 함수
fun String.addCommaWon(): String{
    return if(this.isNotBlank())
        DecimalFormat("#,###").apply { decimalFormatSymbols = DecimalFormatSymbols(Locale.KOREA) }.format(this.toLong()) + "원"
    else ""
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}