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

private val HandleSizeDp = 16.dp

@Composable
fun ResizeHandle(
    modifier: Modifier = Modifier,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit
) {
    Box(
        modifier = modifier
            .size(HandleSizeDp)
            .background(androidx.compose.material3.MaterialTheme.colorScheme.primary)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onDrag(dragAmount)
                    },
                    onDragEnd = { onDragEnd() }
                )
            }
    )
}
