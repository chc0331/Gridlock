package com.android.gridpoc.state

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset

class GestureState {
    val dragOffset = mutableStateOf(Offset.Zero)
    val resizePreviewWidth = mutableStateOf(0f)
    val resizePreviewHeight = mutableStateOf(0f)
}
