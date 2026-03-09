package com.android.gridpoc.grid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.android.gridpoc.grid.controller.DragController
import com.android.gridpoc.grid.controller.ResizeController
import com.android.gridpoc.grid.state.EditorState
import com.android.gridpoc.grid.state.GestureState

/**
 * State holder for the grid editor. Enables external control and state observation.
 * Use with [GridEditor] composable.
 */
@Stable
class GridEditorState {
    internal val editorState = EditorState()
    internal val gestureState = GestureState()
    internal val resizeController = ResizeController(editorState, gestureState)
    internal val dragController = DragController(editorState, gestureState)

    /** Read-only list of grid items. Order matches layout slot order. */
    val items: List<GridItem> get() = editorState.widgets
}

/**
 * Remembers a [GridEditorState]. Use to hoist state and control the grid externally.
 */
@Composable
fun rememberGridEditorState(): GridEditorState {
    return remember { GridEditorState() }
}
