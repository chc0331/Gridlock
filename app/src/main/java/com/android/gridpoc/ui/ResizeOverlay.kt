package com.android.gridpoc.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.android.gridpoc.model.ResizeCorner
import com.android.gridpoc.model.WidgetState
import kotlin.math.roundToInt
import com.android.gridpoc.state.GestureState

@Composable
fun ResizeOverlay(
    widget: WidgetState?,
    cellSizePx: Int,
    gestureState: GestureState,
    onResizeHandleDrag: (Offset, ResizeCorner) -> Unit,
    onResizeHandleDragEnd: (ResizeCorner) -> Unit,
    modifier: Modifier = Modifier
) {
    if (widget == null) return

    val previewWidth = gestureState.resizePreviewWidth.value.coerceAtLeast(cellSizePx.toFloat())
    val previewHeight = gestureState.resizePreviewHeight.value.coerceAtLeast(cellSizePx.toFloat())
    val offsetX = gestureState.resizePreviewOffsetX.value
    val offsetY = gestureState.resizePreviewOffsetY.value
    val baseX = widget.col * cellSizePx
    val baseY = widget.row * cellSizePx
    val previewX = baseX + offsetX
    val previewY = baseY + offsetY

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset { IntOffset(previewX.roundToInt(), previewY.roundToInt()) }
                .then(
                    with(LocalDensity.current) {
                        Modifier.size(previewWidth.toDp(), previewHeight.toDp())
                    }
                )
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(2.dp, MaterialTheme.colorScheme.primary)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { })
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = widget.id,
                style = MaterialTheme.typography.titleMedium
            )
            listOf(
                ResizeCorner.TopStart to Alignment.TopStart,
                ResizeCorner.TopEnd to Alignment.TopEnd,
                ResizeCorner.BottomStart to Alignment.BottomStart,
                ResizeCorner.BottomEnd to Alignment.BottomEnd
            ).forEach { (corner, alignment) ->
                ResizeHandle(
                    corner = corner,
                    modifier = Modifier
                        .align(alignment)
                        .padding(4.dp),
                    onDrag = onResizeHandleDrag,
                    onDragEnd = onResizeHandleDragEnd
                )
            }
        }
    }
}
