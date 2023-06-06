package com.hvasoft.counterpro.domain.model

data class Counter(
    val id: String,
    val title: String,
    var count: Int,
    var isSelected: Boolean = false,
    var isDeleted: Boolean = false
)
