package com.hvasoft.counterpro.presentation.home.adapter

import com.hvasoft.counterpro.domain.model.Counter

interface OnClickListener {
    fun onIncrementClick(counter: Counter)
    fun onDecrementClick(counter: Counter)
    fun onLongClick(counter: Counter)
}