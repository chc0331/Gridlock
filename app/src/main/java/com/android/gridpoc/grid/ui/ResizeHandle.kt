package com.android.gridpoc.grid.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.android.gridpoc.grid.controller.ResizeStrategy

private val HandleSizeDp = 16.dp

@Composable
internal fun ResizeHandle(
    strategy: ResizeStrategy,
    modifier: Modifier = Modifier,
    onDrag: (Offset, ResizeStrategy) -> Unit,
    onDragEnd: (ResizeStrategy) -> Unit
) {
    Box(
        modifier = modifier
            .size(HandleSizeDp)
            .background(MaterialTheme.colorScheme.primary)
            .pointerInput(strategy) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onDrag(dragAmount, strategy)
                    },
                    onDragEnd = { onDragEnd(strategy) }
                )
            }
    )
}
