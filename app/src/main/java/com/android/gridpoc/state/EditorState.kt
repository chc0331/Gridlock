package com.android.gridpoc.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.android.gridpoc.model.WidgetState

class EditorState {
    val widgets = mutableStateListOf<WidgetState>(
        WidgetState(id = "A", row = 0, col = 0, spanX = 2, spanY = 2),
        WidgetState(id = "B", row = 0, col = 2, spanX = 1, spanY = 1),
        WidgetState(id = "C", row = 3, col = 1, spanX = 2, spanY = 1),
        WidgetState(id = "D", row = 2, col = 0, spanX = 1, spanY = 2)
    )

    var draggingWidgetId: String? by mutableStateOf<String?>(null)
        private set

    var resizingWidgetId: String? by mutableStateOf<String?>(null)
        private set

    fun startDrag(id: String) {
        draggingWidgetId = id
    }

    fun endDrag() {
        draggingWidgetId = null
    }

    fun startResize(id: String) {
        resizingWidgetId = id
    }

    fun endResize() {
        resizingWidgetId = null
    }

    fun moveWidget(id: String, newRow: Int, newCol: Int) {
        val index = widgets.indexOfFirst { it.id == id }
        if (index >= 0) {
            val w = widgets[index]
            widgets[index] = w.copy(row = newRow, col = newCol)
        }
    }

    fun resizeWidget(id: String, newRow: Int, newCol: Int, newSpanX: Int, newSpanY: Int) {
        val index = widgets.indexOfFirst { it.id == id }
        if (index >= 0) {
            val w = widgets[index]
            val newWidget = w.copy(
                row = newRow.coerceAtLeast(0),
                col = newCol.coerceAtLeast(0),
                spanX = newSpanX.coerceAtLeast(1),
                spanY = newSpanY.coerceAtLeast(1)
            )
            widgets.set(index, newWidget)
        }
    }

    fun resizeWidget(id: String, newSpanX: Int, newSpanY: Int) {
        val w = getWidget(id) ?: return
        resizeWidget(id, w.row, w.col, newSpanX, newSpanY)
    }

    fun getWidget(id: String): WidgetState? = widgets.find { it.id == id }
}
