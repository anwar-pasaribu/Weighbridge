package com.unwur.weighbridge.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getCurrentDateFormatted(): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return formatter.format(Date())
}