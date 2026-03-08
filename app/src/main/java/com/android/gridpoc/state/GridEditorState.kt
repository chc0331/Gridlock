package com.android.gridpoc.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember

/**
 * State holder for the grid editor layout.
 * Enables external control (e.g. programmatic item movement, state observation).
 */
@Stable
class GridEditorState {
    val editorState = EditorState()
    val gestureState = GestureState()
}

/**
 * Remembers a [GridEditorState]. Use this to hoist state and control the layout externally.
 */
@Composable
fun rememberGridEditorState(): GridEditorState {
    return remember { GridEditorState() }
}
