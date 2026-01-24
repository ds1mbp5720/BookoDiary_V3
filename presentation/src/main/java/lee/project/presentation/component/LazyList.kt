package lee.project.presentation.component

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.offset
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FixedThreshold
import androidx.compose.material.ResistanceConfig
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.ThresholdConfig
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToDismissVertical(
    state: DismissState,
    modifier: Modifier = Modifier,
    // 수직 방향에 맞춰 방향 설정
    directions: Set<DismissDirection> = setOf(DismissDirection.EndToStart, DismissDirection.StartToEnd),
    dismissThresholds: (DismissDirection) -> ThresholdConfig = { FixedThreshold(56.dp) },
    background: @Composable RowScope.() -> Unit,
    dismissContent: @Composable RowScope.() -> Unit
) {
    BoxWithConstraints(modifier) {
        // constraints.maxHeight를 직접 사용하여 BoxWithConstraintsScope 사용을 명확히 함
        val height = constraints.maxHeight.toFloat()

        val anchors = mutableMapOf(0f to DismissValue.Default)
        if (DismissDirection.StartToEnd in directions) {
            anchors += height to DismissValue.DismissedToEnd
        }
        if (DismissDirection.EndToStart in directions) {
            anchors += -height to DismissValue.DismissedToStart
        }

        Box(
            modifier = Modifier.swipeable(
                state = state,
                anchors = anchors,
                thresholds = { from, to ->
                    val direction = when {
                        to == DismissValue.DismissedToEnd -> DismissDirection.StartToEnd
                        to == DismissValue.DismissedToStart -> DismissDirection.EndToStart
                        else -> if (from == DismissValue.DismissedToEnd) DismissDirection.StartToEnd else DismissDirection.EndToStart
                    }
                    dismissThresholds(direction)
                },
                orientation = Orientation.Vertical,
                enabled = state.currentValue == DismissValue.Default,
                resistance = ResistanceConfig(
                    basis = height,
                    factorAtMin = SwipeableDefaults.StiffResistanceFactor,
                    factorAtMax = SwipeableDefaults.StiffResistanceFactor
                )
            )
        ) {
            Row(
                modifier = Modifier.matchParentSize()
            ) {
                background()
            }
            Row(
                modifier = Modifier.offset {
                    IntOffset(0, state.offset.value.roundToInt())
                }
            ) {
                dismissContent()
            }
        }
    }
}
