package com.android.gridpoc.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import com.android.gridpoc.model.ResizeCorner

@Stable
class GestureState {
    val dragOffset = mutableStateOf(Offset.Zero)
    val resizePreviewWidth = mutableStateOf(0f)
    val resizePreviewHeight = mutableStateOf(0f)
    val resizePreviewOffsetX = mutableStateOf(0f)
    val resizePreviewOffsetY = mutableStateOf(0f)
    val activeResizeCorner = mutableStateOf<ResizeCorner?>(null)
    /** Initial width when resize started (for offset clamping). */
    val initialResizeWidth = mutableStateOf(0f)
    /** Initial height when resize started (for offset clamping). */
    val initialResizeHeight = mutableStateOf(0f)
}
