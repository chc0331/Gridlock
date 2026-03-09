package com.android.gridpoc.controller

import androidx.compose.ui.geometry.Offset
import com.android.gridpoc.controller.ResizeStrategy
import com.android.gridpoc.state.EditorState
import com.android.gridpoc.state.GestureState
import kotlin.math.roundToInt

/**
 * Handles resize gesture logic: preview updates and commit to editor state.
 * Uses [ResizeStrategy] per corner (TopStart, TopEnd, BottomStart, BottomEnd).
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
            gestureState.activeResizeStrategy.value = null
        }
    }

    fun applyResizeDelta(delta: Offset, strategy: ResizeStrategy, cellSizePx: Int) {
        gestureState.activeResizeStrategy.value = strategy
        val initW = gestureState.initialResizeWidth.value
        val initH = gestureState.initialResizeHeight.value
        val minSize = cellSizePx.toFloat()
        val maxOffsetX = (initW - minSize).coerceAtLeast(0f)
        val maxOffsetY = (initH - minSize).coerceAtLeast(0f)
        strategy.applyResizeDelta(
            gestureState = gestureState,
            delta = delta,
            initW = initW,
            initH = initH,
            minSize = minSize,
            maxOffsetX = maxOffsetX,
            maxOffsetY = maxOffsetY
        )
    }

    fun commitResize(strategy: ResizeStrategy, cellSizePx: Int) {
        val id = editorState.resizingWidgetId ?: return
        val w = editorState.getWidget(id) ?: return
        val pw = gestureState.resizePreviewWidth.value
        val ph = gestureState.resizePreviewHeight.value
        val ox = gestureState.resizePreviewOffsetX.value
        val oy = gestureState.resizePreviewOffsetY.value
        val newSpanX = (pw / cellSizePx).roundToInt().coerceAtLeast(1)
        val newSpanY = (ph / cellSizePx).roundToInt().coerceAtLeast(1)
        val (newRow, newCol) = strategy.computeNewPosition(w, pw, ph, ox, oy, cellSizePx)
        editorState.resizeWidget(id, newRow, newCol, newSpanX, newSpanY)
        gestureState.resizePreviewWidth.value = 0f
        gestureState.resizePreviewHeight.value = 0f
        gestureState.resizePreviewOffsetX.value = 0f
        gestureState.resizePreviewOffsetY.value = 0f
        gestureState.initialResizeWidth.value = 0f
        gestureState.initialResizeHeight.value = 0f
        gestureState.activeResizeStrategy.value = null
        editorState.endResize()
    }
}
