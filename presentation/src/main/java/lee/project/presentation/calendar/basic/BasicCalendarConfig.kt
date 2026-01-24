package lee.project.presentation.calendar.basic

import java.util.Locale

data class BasicCalendarConfig (
    val yearRange: IntRange = IntRange(1970, 2100),
    val locale: Locale = Locale.KOREAN
)