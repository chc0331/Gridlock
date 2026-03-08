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
import com.android.gridpoc.model.WidgetState
import com.android.gridpoc.state.GestureState

@Composable
fun ResizeOverlay(
    widget: WidgetState?,
    cellSizePx: Int,
    gestureState: GestureState,
    onResizeHandleDrag: (Offset) -> Unit,
    onResizeHandleDragEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (widget == null) return

    val previewWidth = gestureState.resizePreviewWidth.value.coerceAtLeast(cellSizePx.toFloat())
    val previewHeight = gestureState.resizePreviewHeight.value.coerceAtLeast(cellSizePx.toFloat())
    val baseX = widget.col * cellSizePx
    val baseY = widget.row * cellSizePx

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset { IntOffset(baseX, baseY) }
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
            ResizeHandle(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp),
                onDrag = onResizeHandleDrag,
                onDragEnd = onResizeHandleDragEnd
            )
        }
    }
}
