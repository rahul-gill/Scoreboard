package com.rahul.apps.scoreboard.util

import androidx.compose.runtime.*

fun <T> customMutableStateOf(value: T)
    = mutableStateOf(value, neverEqualPolicy())

fun <T> MutableState<T>.updateState(updater: T.() -> Unit){
    this.value.updater()
    this.value = this.value
}