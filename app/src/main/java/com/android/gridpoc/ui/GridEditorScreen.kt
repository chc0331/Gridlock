package com.android.gridpoc.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.android.gridpoc.state.GridEditorState
import com.android.gridpoc.state.rememberGridEditorState

@Composable
fun GridEditorScreen(
    state: GridEditorState = rememberGridEditorState(),
    modifier: Modifier = Modifier
) {
    val editorState = state.editorState
    val gestureState = state.gestureState
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
                    onTap = { state.resizeController.startResize(widget.id, cellSizePx) },
                    onLongPress = { state.dragController.startDrag(widget.id) },
                    onDrag = { delta -> state.dragController.onDrag(delta) },
                    onDragEnd = { state.dragController.onDragEnd(cellSizePx) }
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
            onResizeHandleDrag = { delta, strategy ->
                state.resizeController.applyResizeDelta(delta, strategy, cellSizePx)
            },
            onResizeHandleDragEnd = { strategy ->
                state.resizeController.commitResize(strategy, cellSizePx)
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
