package com.android.gridpoc.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.android.gridpoc.model.ResizeCorner
import com.android.gridpoc.state.EditorState
import com.android.gridpoc.state.GestureState
import kotlin.math.roundToInt

@Composable
fun GridEditorScreen(modifier: Modifier = Modifier) {
    val editorState = remember { EditorState() }
    val gestureState = remember { GestureState() }
    val cellSizePx = with(LocalDensity.current) { 120.dp.toPx().toInt() }

    Box(modifier = modifier.fillMaxSize()) {
        GridLayout(
            widgets = editorState.widgets,
            cellSizePx = cellSizePx,
            modifier = Modifier.fillMaxSize()
        ) {
            editorState.widgets.forEach { widget ->
                WidgetItem(
                    widget = widget,
                    isSelected = widget.id == editorState.resizingWidgetId,
                    onTap = {
                        editorState.startResize(widget.id)
                        editorState.getWidget(widget.id)?.let { w ->
                            gestureState.resizePreviewWidth.value = w.spanX * cellSizePx.toFloat()
                            gestureState.resizePreviewHeight.value = w.spanY * cellSizePx.toFloat()
                            gestureState.resizePreviewOffsetX.value = 0f
                            gestureState.resizePreviewOffsetY.value = 0f
                            gestureState.activeResizeCorner.value = null
                        }
                    },
                    onLongPress = {
                        editorState.startDrag(widget.id)
                        editorState.endResize()
                        gestureState.dragOffset.value = Offset.Zero
                    },
                    onDrag = { delta ->
                        gestureState.dragOffset.value += delta
                    },
                    onDragEnd = {
                        val id = editorState.draggingWidgetId ?: return@WidgetItem
                        val w = editorState.getWidget(id) ?: return@WidgetItem
                        val targetCol = ((w.col * cellSizePx + gestureState.dragOffset.value.x) / cellSizePx).roundToInt().coerceAtLeast(0)
                        val targetRow = ((w.row * cellSizePx + gestureState.dragOffset.value.y) / cellSizePx).roundToInt().coerceAtLeast(0)
                        editorState.moveWidget(id, targetRow, targetCol)
                        editorState.endDrag()
                        gestureState.dragOffset.value = Offset.Zero
                    }
                )
            }
        }

        DragOverlay(
            widget = editorState.draggingWidgetId?.let { editorState.getWidget(it) },
            cellSizePx = cellSizePx,
            gestureState = gestureState,
            modifier = Modifier.fillMaxSize()
        )

        ResizeOverlay(
            widget = editorState.resizingWidgetId?.let { editorState.getWidget(it) },
            cellSizePx = cellSizePx,
            gestureState = gestureState,
            onResizeHandleDrag = { delta, corner ->
                gestureState.activeResizeCorner.value = corner
                when (corner) {
                    ResizeCorner.BottomEnd -> {
                        gestureState.resizePreviewWidth.value += delta.x
                        gestureState.resizePreviewHeight.value += delta.y
                    }
                    ResizeCorner.TopStart -> {
                        gestureState.resizePreviewWidth.value -= delta.x
                        gestureState.resizePreviewHeight.value -= delta.y
                        gestureState.resizePreviewOffsetX.value += delta.x
                        gestureState.resizePreviewOffsetY.value += delta.y
                    }
                    ResizeCorner.TopEnd -> {
                        gestureState.resizePreviewWidth.value += delta.x
                        gestureState.resizePreviewHeight.value -= delta.y
                        gestureState.resizePreviewOffsetY.value += delta.y
                    }
                    ResizeCorner.BottomStart -> {
                        gestureState.resizePreviewWidth.value -= delta.x
                        gestureState.resizePreviewHeight.value += delta.y
                        gestureState.resizePreviewOffsetX.value += delta.x
                    }
                }
            },
            onResizeHandleDragEnd = end@ { corner ->
                editorState.resizingWidgetId?.let { id ->
                    val w = editorState.getWidget(id) ?: return@end
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
                    gestureState.activeResizeCorner.value = null
                    editorState.endResize()
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
