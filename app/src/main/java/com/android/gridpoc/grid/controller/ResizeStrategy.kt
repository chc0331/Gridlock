package com.android.gridpoc.grid.controller

import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import com.android.gridpoc.grid.GridItem
import com.android.gridpoc.grid.state.GestureState
import kotlin.math.roundToInt

/**
 * Strategy for corner-specific resize logic.
 * Each corner (TopStart, TopEnd, BottomStart, BottomEnd) has different
 * behavior for preview delta application and position computation on commit.
 */
internal interface ResizeStrategy {
    /** Alignment for placing the resize handle in the overlay. */
    val alignment: Alignment

    /**
     * Applies drag delta to gesture state preview (width, height, offsetX, offsetY).
     */
    fun applyResizeDelta(
        gestureState: GestureState,
        delta: Offset,
        initW: Float,
        initH: Float,
        minSize: Float,
        maxOffsetX: Float,
        maxOffsetY: Float
    )

    /**
     * Computes new (row, col) from preview values when committing resize.
     */
    fun computeNewPosition(
        widget: GridItem,
        pw: Float,
        ph: Float,
        ox: Float,
        oy: Float,
        cellSizePx: Int
    ): Pair<Int, Int>

    companion object {
        /** All resize strategies for the four corners. */
        val All: List<ResizeStrategy> = listOf(
            TopStartResizeStrategy,
            TopEndResizeStrategy,
            BottomStartResizeStrategy,
            BottomEndResizeStrategy
        )
    }
}

/** BottomEnd: origin fixed, expand width/height. */
internal object BottomEndResizeStrategy : ResizeStrategy {
    override val alignment = Alignment.BottomEnd

    override fun applyResizeDelta(
        gestureState: GestureState,
        delta: Offset,
        initW: Float,
        initH: Float,
        minSize: Float,
        maxOffsetX: Float,
        maxOffsetY: Float
    ) {
        gestureState.resizePreviewWidth.value =
            (gestureState.resizePreviewWidth.value + delta.x).coerceAtLeast(minSize)
        gestureState.resizePreviewHeight.value =
            (gestureState.resizePreviewHeight.value + delta.y).coerceAtLeast(minSize)
    }

    override fun computeNewPosition(
        widget: GridItem,
        pw: Float,
        ph: Float,
        ox: Float,
        oy: Float,
        cellSizePx: Int
    ): Pair<Int, Int> = widget.row to widget.col
}

/** TopStart: origin moves; width/height shrink via offset. */
internal object TopStartResizeStrategy : ResizeStrategy {
    override val alignment = Alignment.TopStart

    override fun applyResizeDelta(
        gestureState: GestureState,
        delta: Offset,
        initW: Float,
        initH: Float,
        minSize: Float,
        maxOffsetX: Float,
        maxOffsetY: Float
    ) {
        gestureState.resizePreviewOffsetX.value =
            (gestureState.resizePreviewOffsetX.value + delta.x).coerceAtMost(maxOffsetX)
        gestureState.resizePreviewOffsetY.value =
            (gestureState.resizePreviewOffsetY.value + delta.y).coerceAtMost(maxOffsetY)
        gestureState.resizePreviewWidth.value = initW - gestureState.resizePreviewOffsetX.value
        gestureState.resizePreviewHeight.value = initH - gestureState.resizePreviewOffsetY.value
    }

    override fun computeNewPosition(
        widget: GridItem,
        pw: Float,
        ph: Float,
        ox: Float,
        oy: Float,
        cellSizePx: Int
    ): Pair<Int, Int> {
        val newRow = (widget.row + (oy / cellSizePx).roundToInt()).coerceAtLeast(0)
        val newCol = (widget.col + (ox / cellSizePx).roundToInt()).coerceAtLeast(0)
        return newRow to newCol
    }
}

/** TopEnd: origin moves vertically; width expands, height shrinks via offsetY. */
internal object TopEndResizeStrategy : ResizeStrategy {
    override val alignment = Alignment.TopEnd

    override fun applyResizeDelta(
        gestureState: GestureState,
        delta: Offset,
        initW: Float,
        initH: Float,
        minSize: Float,
        maxOffsetX: Float,
        maxOffsetY: Float
    ) {
        gestureState.resizePreviewWidth.value =
            (gestureState.resizePreviewWidth.value + delta.x).coerceAtLeast(minSize)
        gestureState.resizePreviewOffsetY.value =
            (gestureState.resizePreviewOffsetY.value + delta.y).coerceAtMost(maxOffsetY)
        gestureState.resizePreviewHeight.value = initH - gestureState.resizePreviewOffsetY.value
    }

    override fun computeNewPosition(
        widget: GridItem,
        pw: Float,
        ph: Float,
        ox: Float,
        oy: Float,
        cellSizePx: Int
    ): Pair<Int, Int> {
        val newRow = (widget.row + (oy / cellSizePx).roundToInt()).coerceAtLeast(0)
        return newRow to widget.col
    }
}

/** BottomStart: origin moves horizontally; width shrinks via offsetX, height expands. */
internal object BottomStartResizeStrategy : ResizeStrategy {
    override val alignment = Alignment.BottomStart

    override fun applyResizeDelta(
        gestureState: GestureState,
        delta: Offset,
        initW: Float,
        initH: Float,
        minSize: Float,
        maxOffsetX: Float,
        maxOffsetY: Float
    ) {
        gestureState.resizePreviewOffsetX.value =
            (gestureState.resizePreviewOffsetX.value + delta.x).coerceAtMost(maxOffsetX)
        gestureState.resizePreviewWidth.value = initW - gestureState.resizePreviewOffsetX.value
        gestureState.resizePreviewHeight.value =
            (gestureState.resizePreviewHeight.value + delta.y).coerceAtLeast(minSize)
    }

    override fun computeNewPosition(
        widget: GridItem,
        pw: Float,
        ph: Float,
        ox: Float,
        oy: Float,
        cellSizePx: Int
    ): Pair<Int, Int> {
        val newCol = (widget.col + (ox / cellSizePx).roundToInt()).coerceAtLeast(0)
        return widget.row to newCol
    }
}
