package com.android.gridpoc.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import com.android.gridpoc.model.WidgetState
import com.android.gridpoc.state.GestureState

@Composable
fun DragOverlay(
    widget: WidgetState?,
    cellSizePx: Int,
    gestureState: GestureState,
    modifier: Modifier = Modifier
) {
    if (widget == null) return

    val dragOffset = gestureState.dragOffset.value
    val widthPx = widget.spanX * cellSizePx
    val heightPx = widget.spanY * cellSizePx
    val baseX = widget.col * cellSizePx
    val baseY = widget.row * cellSizePx

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset { IntOffset(baseX, baseY) }
                .graphicsLayer {
                    translationX = dragOffset.x
                    translationY = dragOffset.y
                    alpha = 0.8f
                }
                .then(
                    with(LocalDensity.current) {
                        Modifier.size(widthPx.toDp(), heightPx.toDp())
                    }
                )
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = widget.id,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
