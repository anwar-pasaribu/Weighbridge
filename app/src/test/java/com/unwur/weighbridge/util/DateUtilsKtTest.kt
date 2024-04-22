package com.unwur.weighbridge.util

import com.google.common.truth.Truth.assertThat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import org.junit.Test

class DateUtilsKtTest {

    @Test
    fun testGetCurrentDateFormatted() {

        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val expectedDate = dateFormat.format(calendar.time)

        assertThat(expectedDate).isEqualTo(getCurrentDateFormatted())
    }
}