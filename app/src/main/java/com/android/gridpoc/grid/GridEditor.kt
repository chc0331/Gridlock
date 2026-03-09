package com.android.gridpoc.grid

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.android.gridpoc.grid.ui.DragOverlay
import com.android.gridpoc.grid.ui.GridItemSlot
import com.android.gridpoc.grid.ui.GridLayout
import com.android.gridpoc.grid.ui.ResizeOverlay

/**
 * Grid editor composable with drag-and-drop and resize support.
 *
 * @param state Grid editor state. Use [rememberGridEditorState] to create.
 * @param modifier Modifier for the root layout
 * @param itemContent Composable slot for each grid item. Receives [GridItem] for customization.
 *   Default shows the item id as text.
 */
@Composable
fun GridEditor(
    state: GridEditorState = rememberGridEditorState(),
    modifier: Modifier = Modifier,
    itemContent: @Composable (GridItem) -> Unit = { item ->
        Text(
            text = item.id,
            style = MaterialTheme.typography.titleMedium
        )
    }
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
            editorState.widgets.forEach { item ->
                GridItemSlot(
                    item = item,
                    isSelected = item.id == editorState.resizingWidgetId,
                    onTap = { state.resizeController.startResize(item.id, cellSizePx) },
                    onLongPress = { state.dragController.startDrag(item.id) },
                    onDrag = { delta -> state.dragController.onDrag(delta) },
                    onDragEnd = { state.dragController.onDragEnd(cellSizePx) },
                    itemContent = itemContent
                )
            }
        }

        DragOverlay(
            widget = editorState.draggingWidgetId?.let { editorState.getWidget(it) },
            cellSizePx = cellSizePx,
            gestureState = gestureState,
            itemContent = itemContent,
            modifier = Modifier.fillMaxSize()
        )

        ResizeOverlay(
            widget = editorState.resizingWidgetId?.let { editorState.getWidget(it) },
            cellSizePx = cellSizePx,
            gestureState = gestureState,
            itemContent = itemContent,
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
