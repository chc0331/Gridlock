package com.android.gridpoc.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.android.gridpoc.model.ResizeCorner

private val HandleSizeDp = 16.dp

@Composable
fun ResizeHandle(
    corner: ResizeCorner,
    modifier: Modifier = Modifier,
    onDrag: (Offset, ResizeCorner) -> Unit,
    onDragEnd: (ResizeCorner) -> Unit
) {
    Box(
        modifier = modifier
            .size(HandleSizeDp)
            .background(androidx.compose.material3.MaterialTheme.colorScheme.primary)
            .pointerInput(corner) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onDrag(dragAmount, corner)
                    },
                    onDragEnd = { onDragEnd(corner) }
                )
            }
    )
}
