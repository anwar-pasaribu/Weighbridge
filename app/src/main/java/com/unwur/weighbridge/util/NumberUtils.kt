package com.unwur.weighbridge.util

fun Double?.formatDoubleAsNeeded(): String {
    if (this == null) return ""
    return if (this % 1.0 == 0.0) {
        String.format("%.0f", this)
    } else {
        String.format("%.2f", this)
    }
}