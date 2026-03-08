package com.android.gridpoc.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.unit.Constraints
import com.android.gridpoc.model.WidgetState

@Composable
fun GridLayout(
    widgets: List<WidgetState>,
    cellSizePx: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val measurePolicy = MeasurePolicy { measurables, constraints ->
        if (widgets.size != measurables.size) {
            layout(constraints.minWidth, constraints.minHeight) {}
        } else {
            val placeables = measurables.mapIndexed { i, measurable ->
                val w = widgets[i]
                measurable.measure(
                    Constraints.fixed(
                        w.spanX * cellSizePx,
                        w.spanY * cellSizePx
                    )
                )
            }
            var width = 0
            var height = 0
            widgets.forEachIndexed { i, w ->
                width = maxOf(width, (w.col + w.spanX) * cellSizePx)
                height = maxOf(height, (w.row + w.spanY) * cellSizePx)
            }
            layout(width.coerceAtLeast(constraints.minWidth), height.coerceAtLeast(constraints.minHeight)) {
                placeables.forEachIndexed { i, placeable ->
                    val w = widgets[i]
                    placeable.place(
                        x = w.col * cellSizePx,
                        y = w.row * cellSizePx
                    )
                }
            }
        }
    }
    Layout(
        modifier = modifier,
        content = content,
        measurePolicy = measurePolicy
    )
}
