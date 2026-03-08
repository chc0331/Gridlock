package com.android.gridpoc.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
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
            onResizeHandleDrag = { delta ->
                gestureState.resizePreviewWidth.value += delta.x
                gestureState.resizePreviewHeight.value += delta.y
            },
            onResizeHandleDragEnd = {
                editorState.resizingWidgetId?.let { id ->
                    val newSpanX = (gestureState.resizePreviewWidth.value / cellSizePx).roundToInt().coerceAtLeast(1)
                    val newSpanY = (gestureState.resizePreviewHeight.value / cellSizePx).roundToInt().coerceAtLeast(1)
                    editorState.resizeWidget(id, newSpanX, newSpanY)
                    gestureState.resizePreviewWidth.value = 0f
                    gestureState.resizePreviewHeight.value = 0f
                    editorState.endResize()
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
