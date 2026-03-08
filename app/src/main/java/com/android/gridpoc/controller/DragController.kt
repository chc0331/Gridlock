package com.android.gridpoc.controller

import androidx.compose.ui.geometry.Offset
import com.android.gridpoc.state.EditorState
import com.android.gridpoc.state.GestureState
import kotlin.math.roundToInt

/**
 * Handles drag gesture logic: offset updates and commit to editor state.
 */
class DragController(
    private val editorState: EditorState,
    private val gestureState: GestureState
) {
    fun startDrag(widgetId: String) {
        editorState.startDrag(widgetId)
        editorState.endResize()
        gestureState.dragOffset.value = Offset.Zero
    }

    fun onDrag(delta: Offset) {
        gestureState.dragOffset.value += delta
    }

    fun onDragEnd(cellSizePx: Int) {
        val id = editorState.draggingWidgetId ?: return
        val w = editorState.getWidget(id) ?: return
        val targetCol = ((w.col * cellSizePx + gestureState.dragOffset.value.x) / cellSizePx).roundToInt().coerceAtLeast(0)
        val targetRow = ((w.row * cellSizePx + gestureState.dragOffset.value.y) / cellSizePx).roundToInt().coerceAtLeast(0)
        editorState.moveWidget(id, targetRow, targetCol)
        editorState.endDrag()
        gestureState.dragOffset.value = Offset.Zero
    }
}
