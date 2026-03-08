package com.android.gridpoc.state

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import com.android.gridpoc.model.ResizeCorner

class GestureState {
    val dragOffset = mutableStateOf(Offset.Zero)
    val resizePreviewWidth = mutableStateOf(0f)
    val resizePreviewHeight = mutableStateOf(0f)
    val resizePreviewOffsetX = mutableStateOf(0f)
    val resizePreviewOffsetY = mutableStateOf(0f)
    val activeResizeCorner = mutableStateOf<ResizeCorner?>(null)
}
