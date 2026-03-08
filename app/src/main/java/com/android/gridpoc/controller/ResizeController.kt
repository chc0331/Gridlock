package com.android.gridpoc.controller

import androidx.compose.ui.geometry.Offset
import com.android.gridpoc.model.ResizeCorner
import com.android.gridpoc.state.EditorState
import com.android.gridpoc.state.GestureState
import kotlin.math.roundToInt

/**
 * Handles resize gesture logic: preview updates and commit to editor state.
 */
class ResizeController(
    private val editorState: EditorState,
    private val gestureState: GestureState
) {
    fun startResize(widgetId: String, cellSizePx: Int) {
        editorState.startResize(widgetId)
        editorState.getWidget(widgetId)?.let { w ->
            val initW = w.spanX * cellSizePx.toFloat()
            val initH = w.spanY * cellSizePx.toFloat()
            gestureState.resizePreviewWidth.value = initW
            gestureState.resizePreviewHeight.value = initH
            gestureState.resizePreviewOffsetX.value = 0f
            gestureState.resizePreviewOffsetY.value = 0f
            gestureState.initialResizeWidth.value = initW
            gestureState.initialResizeHeight.value = initH
            gestureState.activeResizeCorner.value = null
        }
    }

    fun applyResizeDelta(delta: Offset, corner: ResizeCorner, cellSizePx: Int) {
        gestureState.activeResizeCorner.value = corner
        val initW = gestureState.initialResizeWidth.value
        val initH = gestureState.initialResizeHeight.value
        val minSize = cellSizePx.toFloat()
        val maxOffsetX = (initW - minSize).coerceAtLeast(0f)
        val maxOffsetY = (initH - minSize).coerceAtLeast(0f)
        when (corner) {
            ResizeCorner.BottomEnd -> {
                gestureState.resizePreviewWidth.value = (gestureState.resizePreviewWidth.value + delta.x).coerceAtLeast(minSize)
                gestureState.resizePreviewHeight.value = (gestureState.resizePreviewHeight.value + delta.y).coerceAtLeast(minSize)
            }
            ResizeCorner.TopStart -> {
                gestureState.resizePreviewOffsetX.value = (gestureState.resizePreviewOffsetX.value + delta.x).coerceAtMost(maxOffsetX)
                gestureState.resizePreviewOffsetY.value = (gestureState.resizePreviewOffsetY.value + delta.y).coerceAtMost(maxOffsetY)
                gestureState.resizePreviewWidth.value = initW - gestureState.resizePreviewOffsetX.value
                gestureState.resizePreviewHeight.value = initH - gestureState.resizePreviewOffsetY.value
            }
            ResizeCorner.TopEnd -> {
                gestureState.resizePreviewWidth.value = (gestureState.resizePreviewWidth.value + delta.x).coerceAtLeast(minSize)
                gestureState.resizePreviewOffsetY.value = (gestureState.resizePreviewOffsetY.value + delta.y).coerceAtMost(maxOffsetY)
                gestureState.resizePreviewHeight.value = initH - gestureState.resizePreviewOffsetY.value
            }
            ResizeCorner.BottomStart -> {
                gestureState.resizePreviewOffsetX.value = (gestureState.resizePreviewOffsetX.value + delta.x).coerceAtMost(maxOffsetX)
                gestureState.resizePreviewWidth.value = initW - gestureState.resizePreviewOffsetX.value
                gestureState.resizePreviewHeight.value = (gestureState.resizePreviewHeight.value + delta.y).coerceAtLeast(minSize)
            }
        }
    }

    fun commitResize(corner: ResizeCorner, cellSizePx: Int) {
        val id = editorState.resizingWidgetId ?: return
        val w = editorState.getWidget(id) ?: return
        val pw = gestureState.resizePreviewWidth.value
        val ph = gestureState.resizePreviewHeight.value
        val ox = gestureState.resizePreviewOffsetX.value
        val oy = gestureState.resizePreviewOffsetY.value
        val newSpanX = (pw / cellSizePx).roundToInt().coerceAtLeast(1)
        val newSpanY = (ph / cellSizePx).roundToInt().coerceAtLeast(1)
        val (newRow, newCol) = when (corner) {
            ResizeCorner.BottomEnd -> w.row to w.col
            ResizeCorner.TopStart -> (w.row + (oy / cellSizePx).roundToInt()).coerceAtLeast(0) to (w.col + (ox / cellSizePx).roundToInt()).coerceAtLeast(0)
            ResizeCorner.TopEnd -> (w.row + (oy / cellSizePx).roundToInt()).coerceAtLeast(0) to w.col
            ResizeCorner.BottomStart -> w.row to (w.col + (ox / cellSizePx).roundToInt()).coerceAtLeast(0)
        }
        editorState.resizeWidget(id, newRow, newCol, newSpanX, newSpanY)
        gestureState.resizePreviewWidth.value = 0f
        gestureState.resizePreviewHeight.value = 0f
        gestureState.resizePreviewOffsetX.value = 0f
        gestureState.resizePreviewOffsetY.value = 0f
        gestureState.initialResizeWidth.value = 0f
        gestureState.initialResizeHeight.value = 0f
        gestureState.activeResizeCorner.value = null
        editorState.endResize()
    }
}
