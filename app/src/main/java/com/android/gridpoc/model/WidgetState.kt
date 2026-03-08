package com.android.gridpoc.model

data class WidgetState(
    val id: String,
    var row: Int,
    var col: Int,
    var spanX: Int,
    var spanY: Int
)
