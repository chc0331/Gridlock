package com.android.gridpoc.grid

/**
 * Public model for a grid item. Represents position and span in cell coordinates.
 *
 * @param id Unique identifier for the item
 * @param row Top row (0-based)
 * @param col Left column (0-based)
 * @param spanX Number of columns spanned
 * @param spanY Number of rows spanned
 */
data class GridItem(
    val id: String,
    var row: Int,
    var col: Int,
    var spanX: Int,
    var spanY: Int
)
